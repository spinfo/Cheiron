package Cheiron;

import java.io.File;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.XMLInputSource;

public class Pipeline {

	public static AnalysisEngine engine = null;

	public Pipeline() throws Exception {
		XMLInputSource desc = new XMLInputSource(new File("desc/pipeline.xml"));
		ResourceSpecifier spec = UIMAFramework.getXMLParser().parseResourceSpecifier(desc);
		engine = UIMAFramework.produceAnalysisEngine(spec);
	}

	public JCas process(Map<String, String> sofas) throws Exception {
		JCas cas = engine.newJCas();

		for (Map.Entry<String, String> e : sofas.entrySet())
			cas.createView(e.getKey()).setDocumentText(e.getValue());

		engine.process(cas);
		return cas;
	}
}
