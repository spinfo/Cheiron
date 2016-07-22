package Cheiron;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.jcas.JCas;

public class Application {

	private static Class<?> className = null;

	private static String prometheusApi = "http://prometheus.uni-koeln.de/pandora/api/xml/search/search";
	private static String prometheusUsr = "";
	private static String prometheusKey = "";

	private static String searchField = "all";
	private static String searchParam = "";
	private static String searchLimit = "5";

	private static String saveCAS = "false";
	private static String savePath = "data/";

	public static void main(String[] args) throws Exception {
		className = MethodHandles.lookup().lookupClass();
		Field[] classFields = className.getDeclaredFields();
		List<String> validFields = new ArrayList<String>();

		for (Field f : classFields)
			validFields.add(f.toString().substring(f.toString().lastIndexOf(".") + 1));

		for (int i = 0; i < args.length; i++) {
			if (!args[i].contains("="))
				continue;

			String[] arg = args[i].split("=");

			if (!validFields.contains(arg[0]))
				continue;

			className.getDeclaredField(arg[0]).set(className, arg[1]);
		}

		JCas cas = null;
		XmiCasSerializer writer = null;
		OutputStream stream = null;
		Pipeline pipe = new Pipeline();
		Prometheus prom = new Prometheus(prometheusApi, prometheusUsr, prometheusKey);

		String field = null;
		String[] split = null;
		Map<String, String> sofas = null;

		for (Map<String, String> r : prom.getRecords(searchField, searchParam, searchLimit)) {
			sofas = new HashMap<String, String>();
			field = r.get("description");
			split = field.split("\\|");

			for (int i = 0; i < split.length; i++)
				if (!split[i].trim().isEmpty())
					sofas.put("description_" + i, split[i]);

			if (sofas.size() < 1)
				continue;

			cas = pipe.process(sofas);

			if (!saveCAS.equals(new String("true")))
				continue;

			writer = new XmiCasSerializer(cas.getTypeSystem());
			stream = new FileOutputStream(new File(savePath + r.get("pid") + ".cas"));
			writer.serialize(cas.getCas(), stream);
		}
	}

	public static String getSavePath() {
		return savePath;
	}
}
