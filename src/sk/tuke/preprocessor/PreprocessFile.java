package sk.tuke.preprocessor;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import sk.tuke.dto.DocumentDto;
import sk.tuke.dto.FileDto;
import sk.tuke.sanitize.Sanitize;
import sk.tuke.utils.Constants;

public class PreprocessFile {
	private static HashSet<String> wordSet;
	private static Map<String, Double> wordsOccurenceGlobal;
	private static Map<String, Word> wordsCooccurenceGlobal;

	public PreprocessFile() {
		wordSet = new HashSet<>();
		wordsCooccurenceGlobal = new HashMap<>();
		wordsOccurenceGlobal = new HashMap<>();
	}

	public static DocumentDto preprocessFile(
			SerialAnalyserController controller, FileDto file,
			StopWordsEnglish stopEnglish) {

		Map<String, Double> wordsOccurence = new HashMap<>();
		preprocess(controller, file, wordsOccurence, stopEnglish);

		// keywordExtraction(wordsCooccurence, wordsOccurence, words);
		return new DocumentDto(file.getName(), file.getTopic(), wordsOccurence);
	}

	private static void preprocess(SerialAnalyserController controller,
			FileDto file, Map<String, Double> wordsOccurence,
			StopWordsEnglish stopEnglish) {

		// comparator na zoradenie anotácii
		Comparator<Annotation> comparator = new Comparator<Annotation>() {

			@Override
			public int compare(Annotation annot1, Annotation annot2) {
				return (int) (annot1.getStartNode().getOffset() - annot2
						.getStartNode().getOffset());
			}

		};

		Document document = Sanitize.sanitize(file.getText());
		// vytvorenie korpusu
		Corpus corpus = null;
		try {
			corpus = Factory.newCorpus("Luwak Corpus");
			corpus.add(document);
			controller.setCorpus(corpus);
		} catch (ResourceInstantiationException e) {
			e.printStackTrace();
		}

		// predspracovanie korpusu
		try {
			controller.execute();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// anotácie viet
		AnnotationSet sentenceSet = document.getAnnotations("Tokens").get(
				"Sentence");
		// anotácie tokenov
		AnnotationSet tokenSet = document.getAnnotations("Tokens").get("Token");

		// prechádanie všetkých viet v dokumente
		Iterator<Annotation> sentenceIterator = sentenceSet.iterator();
		while (sentenceIterator.hasNext()) {
			Annotation sentence = sentenceIterator.next();
			AnnotationSet sentenceTokens = tokenSet.get(sentence.getStartNode()
					.getOffset(), sentence.getEndNode().getOffset());

			Iterator<Annotation> tokensIterator = sentenceTokens.iterator();
			SortedSet<Annotation> sorted = new TreeSet<Annotation>(comparator);

			while (tokensIterator.hasNext()) {
				Annotation annot = tokensIterator.next();
				sorted.add(annot);
			}

			// prechádzanie všetkých slov v jednotlivých vetách
			List<String> sentenceWords = new ArrayList<>();
			for (Annotation token : sorted) {
				if (token.getFeatures().containsKey("kind")
						&& token.getFeatures().get("kind").equals("word")) {
					String fullWord = token.getFeatures().get("string")
							.toString();
					if (!isStopWord(fullWord.toLowerCase(), stopEnglish)
							&& !(fullWord.length() <= Constants.LENGHT_STOP)) {
						String stem = token.getFeatures().get("stem")
								.toString();
						wordSet.add(stem);
						sentenceWords.add(stem);
						if (wordsOccurence.containsKey(stem)) {
							Double value = wordsOccurence.get(stem);
							wordsOccurence.put(stem, value + 1);
						} else {
							wordsOccurence.put(stem, 1D);
						}
						if (wordsOccurenceGlobal.containsKey(stem)) {
							Double value = wordsOccurenceGlobal.get(stem);
							wordsOccurenceGlobal.put(stem, value + 1);
						} else {
							wordsOccurenceGlobal.put(stem, 1D);
						}
					}

				}
			}

			for (String sentenceWord : sentenceWords) {
				Word cooccurence;
				if (wordsCooccurenceGlobal.containsKey(sentenceWord)) {
					cooccurence = wordsCooccurenceGlobal.get(sentenceWord);
				} else {
					cooccurence = new Word(sentenceWord);
				}
				for (String sentenceOther : sentenceWords) {
					if (!sentenceOther.equals(sentenceWord)) {
						cooccurence.addCooccurrence(sentenceOther, 1D);
					}
				}
				wordsCooccurenceGlobal.put(sentenceWord, cooccurence);
			}

		}

		cleanCorpus(corpus);
	}

	// uvolnenie korpusu z pamete
	private static void cleanCorpus(Corpus corp) {
		if (!corp.isEmpty()) {
			for (int i = 0; i < corp.size(); i++) {
				Document doc1 = corp.remove(i);
				corp.unloadDocument(doc1);
				Factory.deleteResource(corp);
				Factory.deleteResource(doc1);
			}
		}
	}

	private static boolean isStopWord(String token, StopWordsEnglish stopEnglish) {
		return stopEnglish.isStopWord(token);
	}

	public static HashSet<String> getWordSet() {
		return wordSet;
	}

	public static Map<String, Double> getWordsOccurenceGlobal() {
		return wordsOccurenceGlobal;
	}

	public static Map<String, Word> getWordsCooccurenceGlobal() {
		return wordsCooccurenceGlobal;
	}

	public static void extraction(Map<String, DocumentDto> documents,
			HashSet<String> wordSetExtracted) {
		HashSet<String> wordsSet = new HashSet<>();
		HashSet<String> toDelete = new HashSet<>();
		keywordExtraction(wordsCooccurenceGlobal, wordsOccurenceGlobal,
				wordsSet, toDelete);

		wordSetExtracted = wordsSet;

		for (String key : documents.keySet()) {
			documents.get(key).removeWord(toDelete);
		}

	}

	private static void keywordExtraction(Map<String, Word> wordsCooccurence,
			Map<String, Double> wordsOccurence, HashSet<String> words,
			HashSet<String> toDelete) {

		Double totalOccurrence = 0D;
		for (Double occurence : wordsOccurence.values()) {
			totalOccurrence += occurence;
		}

		double normCoef = 0;
		List<WordInformativeness> wordLocalInfo = new ArrayList<>();
		for (Word word : wordsCooccurence.values()) {
			Double value = 0D;
			for (String g : word.getCooccurrence().keySet()) {
				double pg = wordsOccurence.get(g) / totalOccurrence;
				double citatel = word.getCooccurrence().get(g)
						- (wordsOccurence.get(word.getWord()) * pg);
				double menovatel = wordsOccurence.get(word.getWord()) * pg;
				value += Math.pow(citatel, 2) / menovatel;

			}
			normCoef = Math.pow(value, 2);
			wordLocalInfo.add(new WordInformativeness(word.getWord(), value));
		}
		normCoef = Math.sqrt(normCoef);

		List<WordInformativeness> wordLocalResult = new ArrayList<>();
		for (WordInformativeness word : wordLocalInfo) {
			word.setInformativeness(word.getInformativeness() / normCoef);
			wordLocalResult.add(new WordInformativeness(word.getWord(), word
					.getInformativeness()));
		}

		Collections.sort(wordLocalResult,
				new Comparator<WordInformativeness>() {
					@Override
					public int compare(WordInformativeness o1,
							WordInformativeness o2) {
						return o2.getInformativeness().compareTo(
								o1.getInformativeness());
					}
				});

		int end = wordLocalResult.size() / 2;
		for (int i = 0; i < end; i++) {
			words.add(wordLocalResult.get(i).getWord());
		}
		for (int i = end; i < wordLocalResult.size(); i++) {
			toDelete.add(wordLocalResult.get(i).getWord());
		}
		// List<WordInformativeness> resultSub = new ArrayList<>();
		// if (wordLocalResult.size() >= 100) {
		// resultSub = wordLocalResult.subList(0, 100);
		// } else {
		// resultSub = wordLocalResult;
		// }
		//
		// for (WordInformativeness word : resultSub) {
		// words.put(word.getWord(), word.getInformativeness());
		// }

	}

}
