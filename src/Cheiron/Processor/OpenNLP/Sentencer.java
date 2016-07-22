package Cheiron.Processor.OpenNLP;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.jcas.JCas;

import Cheiron.Processor.Processor_ImplBase;
import de.julielab.jcore.types.Sentence;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

public class Sentencer extends Processor_ImplBase {

	private static Map<String, SentenceDetectorME> detectors = new HashMap<String, SentenceDetectorME>();

	@Override
	public void processView(JCas view) {
		Sentence sentence = null;

		for (Span span : detectors.get(view.getDocumentLanguage()).sentPosDetect(view.getDocumentText())) {
			sentence = new Sentence(view);
			sentence.setComponentId("OpenNLP.Sentencer");
			sentence.setBegin(span.getStart());
			sentence.setEnd(span.getEnd());
			sentence.addToIndexes();

			System.out.println("OpenNLP.Sentencer: " + sentence.getCoveredText());
		}
	}

	@Override
	public void loadDetector(String lang) {
		if (detectors.containsKey(lang))
			return;

		File model = new File("resources/opennlp/" + lang + "-sent.bin");

		if (!model.exists()) {
			System.out.println("OpenNLP.Sentencer: No model for language '" + lang + "' found, aborting!");
			return;
		}

		try {
			detectors.put(lang, new SentenceDetectorME(new SentenceModel(model)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
