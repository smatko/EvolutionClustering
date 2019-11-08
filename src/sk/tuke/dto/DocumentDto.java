package sk.tuke.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;

public class DocumentDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final String topic;
	private final Map<String, Double> words;

	public DocumentDto(String name, String topic, Map<String, Double> words) {
		this.name = name;
		this.topic = topic;
		this.words = words;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public String getTopic() {
		return topic;
	}

	public Map<String, Double> getWords() {
		return words;
	}

	public void removeWord(HashSet<String> toDelete) {
		for (String delete : toDelete) {
			if (words.containsKey(delete)) {
				words.remove(delete);
			}
		}
	}

}
