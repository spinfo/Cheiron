package Cheiron.Processor.Matetools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSTypeConstraint;
import org.apache.uima.jcas.JCas;

import de.julielab.jcore.types.Lemma;
import de.julielab.jcore.types.Sentence;
import de.julielab.jcore.types.Token;
import is2.data.SentenceData09;

public class Lemmatizer extends JCasAnnotator_ImplBase {

	private static Map<String, is2.lemmatizer2.Lemmatizer> detectors = new HashMap<String, is2.lemmatizer2.Lemmatizer>();

	private void processView(JCas view) {
		Token token = null;
		String[] tokens = null;
		ArrayList<Token> tokenList = null;
		Lemma lemma = null;
		SentenceData09 data = null;
		Sentence sentence = null;

		FSIterator sentence_it = view.getAnnotationIndex().iterator();
		FSTypeConstraint sentence_con = view.getConstraintFactory().createTypeConstraint();
		sentence_con.add(Sentence.class.getName());
		sentence_it = view.createFilteredIterator(sentence_it, sentence_con);

		FSIterator token_it = view.getAnnotationIndex().iterator();
		FSTypeConstraint token_con = view.getConstraintFactory().createTypeConstraint();
		token_con.add(Token.class.getName());
		token_it = view.createFilteredIterator(token_it, token_con);

		while (sentence_it.isValid()) {
			sentence = (Sentence) sentence_it.get();
			tokenList = new ArrayList<Token>();
			data = new SentenceData09();

			while (token_it.isValid()) {
				token = (Token) token_it.get();

				if (sentence.getBegin() <= token.getBegin() && sentence.getEnd() >= token.getEnd())
					tokenList.add((Token) token_it.get());

				token_it.moveToNext();
			}

			tokens = new String[tokenList.size() + 1];
			tokens[0] = "<root>";
			for (int i = 1; i < tokens.length; i++)
				tokens[i] = tokenList.get(i - 1).getCoveredText();

			data.init(tokens);
			data = detectors.get(view.getDocumentLanguage()).apply(data);

			for (int i = 0; i < data.length(); i++) {
				token = (Token) tokenList.get(i);

				lemma = new Lemma(view);
				lemma.setComponentId("Matetools.Lemmatizer");
				lemma.setBegin(token.getBegin());
				lemma.setEnd(token.getEnd());
				lemma.setValue(data.plemmas[i]);
				lemma.addToIndexes();
				token.setLemma(lemma);

				System.out
						.println("Matetools.Lemmatizer: " + token.getCoveredText() + "/" + lemma.getValue());
			}

			token_it.moveToFirst();
			sentence_it.moveToNext();
		}
	}

	private void loadDetector(String lang) {
		if (detectors.containsKey(lang))
			return;

		File model = new File("resources/matetools/" + lang + "-lemma.mdl");

		if (!model.exists()) {
			System.out.println("Matetools.Lemmatizer: No model for language '" + lang + "' found, aborting!");
			return;
		}

		try {
			detectors.put(lang, new is2.lemmatizer2.Lemmatizer(model.getPath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		JCas view = null;
		Iterator<JCas> it = null;
		try {
			it = cas.getViewIterator();
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (it.hasNext()) {
			view = it.next();
			if (view.getViewName().equals("_InitialView"))
				continue;

			loadDetector(view.getDocumentLanguage());
			processView(view);
		}
	}
}
