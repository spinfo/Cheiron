package Cheiron.Processor.HeidelTime;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import Cheiron.Processor.Processor;
import de.julielab.jcore.types.Timex2Mention;
import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;
import de.unihd.dbs.uima.types.heideltime.Timex3;

public class Timetagger extends Processor {

	private Map<String, HeidelTimeStandalone> detectors = new HashMap<String, HeidelTimeStandalone>();

	@Override
	protected void loadDetector(String lang) throws Exception {

		Language language = null;
		switch (lang) {
		case "de":
			language = Language.GERMAN;
			break;
		case "en":
			language = Language.ENGLISH;
			break;
		}

		detectors.put(lang, new HeidelTimeStandalone(language, DocumentType.SCIENTIFIC, null,
				"src/Cheiron/Processor/HeidelTime/heideltime.props", POSTagger.NO));
	}

	@Override
	protected void processView(JCas view) throws Exception {

		ResultBasket formatter = new ResultBasket();
		detectors.get(view.getDocumentLanguage()).process(view.getDocumentText(), formatter);
		JCas output = formatter.getCas();

		for (Timex3 t : JCasUtil.select(output, Timex3.class)) {
			Timex2Mention timex = new Timex2Mention(view);
			timex.setBegin(t.getBegin());
			timex.setEnd(t.getEnd());
			timex.setTextualRepresentation(t.getTimexValue());
			timex.setSpecificType(t.getTimexType());
			timex.setComponentId("Heideltime.Timetagger");
			timex.addToIndexes();
		}

	}

}
