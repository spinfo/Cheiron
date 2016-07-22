package Cheiron;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Prometheus {

	private String api = "http://prometheus.uni-koeln.de/pandora/api/xml/search/search";
	private String usr = null;
	private String key = null;

	public Prometheus(String api, String usr, String key) {
		this.api = api;
		this.usr = usr;
		this.key = key;
	}

	public List<Map<String, String>> getRecords(String field, String param, String limit) throws Exception {
		List<Map<String, String>> records = new ArrayList<Map<String, String>>();

		String clearAuth = usr + ":" + key;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encodeToString(clearAuth.getBytes()));

		String f = "?field=" + URLEncoder.encode(field, "UTF-8");
		String p = "&term=" + URLEncoder.encode(param, "UTF-8");
		String l = "&per_page=" + URLEncoder.encode(limit, "UTF-8");

		URL url = new URL(api + f + p + l);
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("authorization", basicAuth);

		System.out.println("Prometheus.getRecords(): " + url.toString());

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(connection.getInputStream());
		NodeList results = doc.getElementsByTagName("result");

		String key;
		String value;
		NodeList result;
		Map<String, String> record;

		for (int i = 0; i < results.getLength(); i++) {
			result = results.item(i).getChildNodes();
			record = new HashMap<String, String>();

			for (int j = 0; j < result.getLength(); j++) {
				key = result.item(j).getNodeName();
				value = result.item(j).getTextContent();

				record.put(key, value);
			}

			records.add(record);
		}

		return records;
	}
}
