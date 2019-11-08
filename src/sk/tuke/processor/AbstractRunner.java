package sk.tuke.processor;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import sk.tuke.dto.ChromosomeDto;
import sk.tuke.dto.ClusterDto;
import sk.tuke.dto.DocumentDto;
import sk.tuke.gate.InitGate;
import sk.tuke.preprocessor.Preprocessor;

public abstract class AbstractRunner {
	private static Map<String, DocumentDto> documents;
	private static HashSet<String> wordSet;

	public AbstractRunner() {
		InitGate gate = new InitGate();
		Preprocessor preprocessor = new Preprocessor(gate.getController());
		documents = preprocessor.getDocuments();
		wordSet = preprocessor.getWordSet();
	}

	public static Map<String, DocumentDto> getDocuments() {
		return documents;
	}

	public static void setDocuments(Map<String, DocumentDto> documents) {
		AbstractRunner.documents = documents;
	}

	public abstract List<ClusterDto> process();

	public static HashSet<String> getWordSet() {
		return wordSet;
	}

	public static void setWordSet(HashSet<String> wordSet) {
		AbstractRunner.wordSet = wordSet;
	}

	public class ChromosomeComparator implements Comparator<ChromosomeDto> {
		@Override
		public int compare(ChromosomeDto o1, ChromosomeDto o2) {
			return o2.getFitness().compareTo(o1.getFitness());
		}
	}

}
