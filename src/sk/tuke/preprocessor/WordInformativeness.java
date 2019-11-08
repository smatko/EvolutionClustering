package sk.tuke.preprocessor;

public class WordInformativeness {
	private String word;
	private Double informativeness;

	public WordInformativeness(String word, Double tfIdf) {
		this.word = word;
		this.informativeness = tfIdf;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Double getInformativeness() {
		return informativeness;
	}

	public void setInformativeness(Double informativeness) {
		this.informativeness = informativeness;
	}

}
