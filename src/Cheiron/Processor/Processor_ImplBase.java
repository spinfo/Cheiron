package Cheiron.Processor;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

public abstract class Processor_ImplBase extends JCasAnnotator_ImplBase {

	abstract public void processView(JCas view);

	abstract public void loadDetector(String lang);

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
