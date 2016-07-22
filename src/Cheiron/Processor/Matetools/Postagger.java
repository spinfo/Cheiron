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
import org.apache.uima.jcas.cas.FSArray;

import de.julielab.jcore.types.POSTag;
import de.julielab.jcore.types.Sentence;
import de.julielab.jcore.types.Token;
import is2.data.SentenceData09;
import is2.lemmatizer2.Lemmatizer;
import is2.transitionS2a.Parser;

public class Postagger extends JCasAnnotator_ImplBase {

	private static Map<String, Lemmatizer> ldetectors = new HashMap<String, Lemmatizer>();
	private static Map<String, Parser> pdetectors = new HashMap<String, Parser>();

	private void processView(JCas view) {
		if (ldetectors.containsKey(view.getDocumentLanguage()) || !pdetectors.containsKey(view.getDocumentLanguage()))
			return;

		Token token = null;
		String[] tokens = null;
		ArrayList<Token> tokenList = null;
		POSTag postag = null;
		SentenceData09 data = null;
		Sentence sentence = null;
		FSArray tokenArray = null;

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
			data = ldetectors.get(view.getDocumentLanguage()).apply(data);
			data = pdetectors.get(view.getDocumentLanguage()).apply(data);

			for (int i = 0; i < data.length(); i++) {
				token = (Token) tokenList.get(i);

				postag = new POSTag(view);
				postag.setComponentId("Matetools.Postagger");
				postag.setBegin(token.getBegin());
				postag.setEnd(token.getEnd());
				postag.setValue(data.plabels[i]);
				postag.addToIndexes();

				if (token.getPosTag() == null) {
					tokenArray = new FSArray(view, 1);
					tokenArray.set(0, postag);
				} else {
					tokenArray = new FSArray(view, token.getPosTag().size() + 1);
					tokenArray.copyFromArray(token.getPosTag().toArray(), 0, 0, token.getPosTag().size());
					tokenArray.set(token.getPosTag().size(), postag);
				}
				token.setPosTag(tokenArray);

				System.out.println("Matetools.Postagger: " + token.getCoveredText() + "/" + postag.getValue());
			}

			token_it.moveToFirst();
			sentence_it.moveToNext();
		}
	}

	private void loadDetector(String lang) {
		if (ldetectors.containsKey(lang) && pdetectors.containsKey(lang))
			return;

		File lmodel = new File("resources/matetools/" + lang + "-lemma.mdl");
		File pmodel = new File("resources/matetools/" + lang + "-tagging.mdl");

		if (!lmodel.exists() || !pmodel.exists()) {
			System.out.println("Matetools.Postagger: No model for language '" + lang + "' found, aborting!");
			return;
		}

		try {
			ldetectors.put(lang, new Lemmatizer(lmodel.getPath()));
			pdetectors.put(lang, new Parser(pmodel.getPath()));
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
