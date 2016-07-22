package Cheiron.Processor.Stanford;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.jcas.JCas;

import Cheiron.Processor.Processor_ImplBase;
import de.julielab.jcore.types.EntityMention;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;

public class Entityrecognizer extends Processor_ImplBase {

	private static Map<String, AbstractSequenceClassifier<CoreLabel>> detectors = new HashMap<String, AbstractSequenceClassifier<CoreLabel>>();

	@Override
	public void processView(JCas view) {
		if (!detectors.containsKey(view.getDocumentLanguage()))
			return;

		AbstractSequenceClassifier<CoreLabel> classifier = detectors.get(view.getDocumentLanguage());

		for (Triple<String, Integer, Integer> item : classifier.classifyToCharacterOffsets(view.getDocumentText())) {
			//System.out.println(item.first() + ": " + view.getDocumentText().substring(item.second(), item.third()));
			EntityMention entity = new EntityMention(view);
			entity.setBegin(item.second());
			entity.setEnd(item.third());
			entity.setComponentId("Stanford.Entityrecognizer");
			entity.setTextualRepresentation(item.first());
			entity.addToIndexes();
		}
		/*
		 * List<List<CoreLabel>> out = classifier.classify(fileContents); for
		 * (List<CoreLabel> sentence : out) { for (CoreLabel word : sentence) {
		 * System.out.print(word.word() + '/' +
		 * word.get(CoreAnnotations.AnswerAnnotation.class) + ' '); }
		 * System.out.println(); }
		 */
	}

	@Override
	public void loadDetector(String lang) {
		if (detectors.containsKey(lang))
			return;

		File model = new File("resources/stanford/" + lang + "-ner.crf.ser.gz");

		if (!model.exists()) {
			System.out.println("Stanford.Entityrecognizr: No model for language '" + lang + "' found, aborting!");
			return;
		}

		try {
			detectors.put(lang, CRFClassifier.getClassifier(model));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
