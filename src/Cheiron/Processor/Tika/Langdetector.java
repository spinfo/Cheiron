package Cheiron.Processor.Tika;

import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.uima.jcas.JCas;

import Cheiron.Processor.Processor_ImplBase;

public class Langdetector extends Processor_ImplBase {

	private static LanguageDetector detector = null;

	@Override
	public void processView(JCas view) {
		String lang = detector.detect(view.getDocumentText()).getLanguage();
		view.setDocumentLanguage(lang);

		System.out.println("Tika.Langdetector: " + view.getDocumentText() + "/" + lang);
	}

	@Override
	public void loadDetector(String lang) {
		if (detector != null)
			return;

		try {
			detector = new OptimaizeLangDetector().loadModels();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
