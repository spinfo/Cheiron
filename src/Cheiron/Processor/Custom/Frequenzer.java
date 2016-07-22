package Cheiron.Processor.Custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import com.google.common.collect.Lists;

import Cheiron.Processor.Processor_ImplBase;
import de.julielab.jcore.types.Lemma;
import de.julielab.jcore.types.Token;
import de.uk.spinfo.types.LemmaFrequency;
import de.uk.spinfo.types.TokenFrequency;

public class Frequenzer extends Processor_ImplBase {

	@Override
	public void processView(JCas view) {
		int length = 0;

		List<Token> tokens = null;
		FSArray tokenArray = null;
		TokenFrequency tokenFreq = null;
		Set<String> tokenHash = new HashSet<String>();
		List<Token> tokenList = new ArrayList<Token>();
		List<String> tokenStrings = new ArrayList<String>();

		List<Lemma> lemmas = null;
		FSArray lemmaArray = null;
		LemmaFrequency lemmaFreq = null;
		Set<String> lemmaHash = new HashSet<String>();
		ArrayList<Lemma> lemmaList = new ArrayList<Lemma>();
		ArrayList<String> lemmaStrings = new ArrayList<String>();

		for (Token t : JCasUtil.select(view, Token.class)) {
			tokenList.add(t);
			tokenStrings.add(t.getCoveredText());
		}

		for (Lemma l : JCasUtil.select(view, Lemma.class)) {
			lemmaList.add(l);
			lemmaStrings.add(l.getCoveredText());
		}

		tokenHash.addAll(tokenStrings);
		lemmaHash.addAll(lemmaStrings);

		for (String s : tokenHash) {
			tokenFreq = new TokenFrequency(view);
			tokenFreq.setBegin(0);
			tokenFreq.setEnd(view.getDocumentText().length());

			length = Collections.frequency(tokenStrings, s);
			tokenFreq.setFrequency(length);

			System.out.println("Custom.Frequenzer(Token): " + s + "/" + length);

			tokenArray = new FSArray(view, length);
			tokens = tokenList.stream().filter(t -> t.getCoveredText().equals(s)).collect(Collectors.toList());
			tokens = Lists.reverse(tokens);

			for (Token t : tokens)
				tokenArray.set(--length, t);

			tokenFreq.setValue(s);
			tokenFreq.setToken(tokenArray);
			tokenFreq.addToIndexes();
		}

		for (String s : lemmaHash) {
			lemmaFreq = new LemmaFrequency(view);
			lemmaFreq.setBegin(0);
			lemmaFreq.setEnd(view.getDocumentText().length());

			length = Collections.frequency(lemmaStrings, s);
			lemmaFreq.setFrequency(length);
			lemmaFreq.setValue(s);

			System.out.println("Custom.Frequenzer(Lemma): " + s + "/" + length);

			lemmaArray = new FSArray(view, length);
			lemmas = lemmaList.stream().filter(l -> l.getCoveredText().equals(s)).collect(Collectors.toList());
			lemmas = Lists.reverse(lemmas);

			for (Lemma l : lemmas)
				lemmaArray.set(--length, l);

			lemmaFreq.setValue(lemmas.get(0).getValue());
			lemmaFreq.setLemma(lemmaArray);
			lemmaFreq.addToIndexes();
		}
	}

	@Override
	public void loadDetector(String lang) {
		return;
	}

}
