package Cheiron.Pipeline;

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

		inverter.set("metacas", metacas);
		inverter.set("pids", aggregator.get("pids"));
		inverter.set("tokenFreq", aggregator.get("tokenFreq"));
		inverter.set("lemmaFreq", aggregator.get("lemmaFreq"));
		inverter.set("entityFreq", aggregator.get("entityFreq"));

		for (Entry<String, JCas> entry : cases.entrySet())
			inverter.process(entry.getValue());

		cases.put("metacas-" + hash.hashCode(), metacas);

		return cases;
	}

}
