package Cheiron.Processor.Custom;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import Cheiron.Processor.Processor;
import de.uk.spinfo.types.EntityFrequency;
import de.uk.spinfo.types.EntityTFIDF;
import de.uk.spinfo.types.LemmaFrequency;
import de.uk.spinfo.types.LemmaTFIDF;
import de.uk.spinfo.types.TokenFrequency;
import de.uk.spinfo.types.TokenTFIDF;

public class Inverter extends Processor {

	public JCas metacas;
	public List<String> pids;

	public Map<String, Map<String, Double>> tokenFreq;
	public Map<String, Map<String, Double>> lemmaFreq;
	public Map<String, Map<String, Double>> entityFreq;

	public Map<String, Double> invertedToken;
	public Map<String, Double> invertedLemma;
	public Map<String, Double> invertedEntity;

	private void aggregateToken(JCas view, JCas cas) throws Exception {
		Map<String, Entry<Double, Double>> inverse = new LinkedHashMap<String, Entry<Double, Double>>();

		for (TokenFrequency t : JCasUtil.select(view, TokenFrequency.class))
			inverse.put(t.getValue(), new SimpleEntry<Double, Double>(t.getRelativeFreq(),
					t.getRelativeFreq() * invertedToken.get(t.getValue())));

		inverse = inverse.entrySet().stream()
				.sorted((x, y) -> Double.compare(y.getValue().getValue(), x.getValue().getValue()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (x, y) -> {
					return null;
				}, LinkedHashMap::new));

		for (Entry<String, Entry<Double, Double>> e : inverse.entrySet()) {
			TokenTFIDF tfidf = new TokenTFIDF(cas);
			tfidf.setValue(e.getKey());
			tfidf.setTfWeight(e.getValue().getKey());
			tfidf.setTfidfWeight(e.getValue().getValue());
			tfidf.setComponentId("Custom.Inverter");
			tfidf.addToIndexes();

			System.out.println("Custom.Inverter(Token): [" + cas.getViewName() + "] " + e.getKey() + "/"
					+ e.getValue().getKey() + "/" + e.getValue().getValue());
		}
	}

	private void aggregateLemma(JCas view, JCas cas) throws Exception {
		Map<String, Entry<Double, Double>> inverse = new LinkedHashMap<String, Entry<Double, Double>>();

		for (LemmaFrequency l : JCasUtil.select(view, LemmaFrequency.class))
			inverse.put(l.getValue(), new SimpleEntry<Double, Double>(l.getRelativeFreq(),
					l.getRelativeFreq() * invertedLemma.get(l.getValue())));

		inverse = inverse.entrySet().stream()
				.sorted((x, y) -> Double.compare(y.getValue().getValue(), x.getValue().getValue()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (x, y) -> {
					return null;
				}, LinkedHashMap::new));

		for (Entry<String, Entry<Double, Double>> e : inverse.entrySet()) {
			LemmaTFIDF tfidf = new LemmaTFIDF(cas);
			tfidf.setValue(e.getKey());
			tfidf.setTfWeight(e.getValue().getKey());
			tfidf.setTfidfWeight(e.getValue().getValue());
			tfidf.setComponentId("Custom.Inverter");
			tfidf.addToIndexes();

			System.out.println("Custom.Inverter(Lemma): [" + cas.getViewName() + "] " + e.getKey() + "/"
					+ e.getValue().getKey() + "/" + e.getValue().getValue());
		}
	}

	private void aggregateEntity(JCas view, JCas cas) throws Exception {
		Map<String, Entry<Double, Double>> inverse = new LinkedHashMap<String, Entry<Double, Double>>();

		for (EntityFrequency e : JCasUtil.select(view, EntityFrequency.class))
			inverse.put(e.getValue(), new SimpleEntry<Double, Double>(e.getRelativeFreq(),
					e.getRelativeFreq() * invertedEntity.get(e.getValue())));

		inverse = inverse.entrySet().stream()
				.sorted((x, y) -> Double.compare(y.getValue().getValue(), x.getValue().getValue()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (x, y) -> {
					return null;
				}, LinkedHashMap::new));

		for (Entry<String, Entry<Double, Double>> e : inverse.entrySet()) {
			EntityTFIDF tfidf = new EntityTFIDF(cas);
			tfidf.setValue(e.getKey());
			tfidf.setTfWeight(e.getValue().getKey());
			tfidf.setTfidfWeight(e.getValue().getValue());
			tfidf.setComponentId("Custom.Inverter");
			tfidf.addToIndexes();

			System.out.println("Custom.Inverter(Entity): [" + cas.getViewName() + "] " + e.getKey() + "/"
					+ e.getValue().getKey() + "/" + e.getValue().getValue());
		}
	}

	@Override
	protected void processView(JCas view) throws Exception {
		String pid = view.getView("_InitialView").getSofaDataURI();

		aggregateToken(view, JCasUtil.getView(metacas, pid, true));
		aggregateLemma(view, JCasUtil.getView(metacas, pid, true));
		aggregateEntity(view, JCasUtil.getView(metacas, pid, true));
	}

	@Override
	protected void loadDetector(String lang) throws Exception {
		return;
	}

}
