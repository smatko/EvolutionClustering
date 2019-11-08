package sk.tuke.preprocessor;

import java.util.HashMap;
import java.util.Map;

public class Word {
	private final String word;
	private final Map<String, Double> cooccurrence;

	public Word(String word) {
		this.word = word;
		this.cooccurrence = new HashMap<>();
	}

	public void addCooccurrence(String word, Double value) {
		if (cooccurrence.containsKey(word)) {
			cooccurrence.put(word, cooccurrence.get(word) + value);
		} else {
			cooccurrence.put(word, value);
		}
	}

	public String getWord() {
		return word;
	}

	public Map<String, Double> getCooccurrence() {
		return cooccurrence;
	}


}
