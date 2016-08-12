package Cheiron.Pipeline;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.uima.jcas.JCas;

import Cheiron.Processor.Custom.Aggregator;
import Cheiron.Processor.Custom.Inverter;

public class Metascope extends Pipeline {

	protected String pos = null;

	@Override
	public Map<String, JCas> process(Map<String, JCas> cases) throws Exception {
		JCas metacas = engine.newJCas();
		Aggregator aggregator = new Aggregator();
		Inverter inverter = new Inverter();
		String hash = cases.entrySet().stream().map(e -> e.getKey()).collect(Collectors.joining("/"));

		for (Entry<String, JCas> entry : cases.entrySet())
			aggregator.process(entry.getValue());

		List<String> pids = (ArrayList<String>) aggregator.get("pids");
		Map<String, Map<String, Double>> tf = (HashMap<String, Map<String, Double>>) aggregator.get("tokenFreq");
		Map<String, Map<String, Double>> lf = (HashMap<String, Map<String, Double>>) aggregator.get("lemmaFreq");
		Map<String, Map<String, Double>> ef = (HashMap<String, Map<String, Double>>) aggregator.get("entityFreq");
		Map<String, Double> invertedToken = new HashMap<String, Double>();
		Map<String, Double> invertedLemma = new HashMap<String, Double>();
		Map<String, Double> invertedEntity = new HashMap<String, Double>();

		for (Entry<String, Map<String, Double>> t : tf.entrySet())
			invertedToken.put(t.getKey(), (double) Math.log((double) pids.size() / t.getValue().size()));

		for (Entry<String, Map<String, Double>> t : lf.entrySet())
			invertedLemma.put(t.getKey(), (double) Math.log((double) pids.size() / t.getValue().size()));

		for (Entry<String, Map<String, Double>> t : ef.entrySet())
			invertedEntity.put(t.getKey(), (double) Math.log((double) pids.size() / t.getValue().size()));

		inverter.set("metacas", metacas);
		inverter.set("pids", aggregator.get("pids"));
		inverter.set("tokenFreq", aggregator.get("tokenFreq"));
		inverter.set("lemmaFreq", aggregator.get("lemmaFreq"));
		inverter.set("entityFreq", aggregator.get("entityFreq"));
		inverter.set("invertedToken", invertedToken);
		inverter.set("invertedLemma", invertedLemma);
		inverter.set("invertedEntity", invertedEntity);

		for (Entry<String, JCas> entry : cases.entrySet())
			inverter.process(entry.getValue());

		cases.put("metacas_" + hash.hashCode(), metacas);

		return cases;
	}

}
