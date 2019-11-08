package sk.tuke.preprocessor;

import gate.creole.SerialAnalyserController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import sk.tuke.dto.DocumentDto;
import sk.tuke.dto.FileDto;

public class Preprocessor {
	private final Map<String, DocumentDto> documents;
	private static HashSet<String> wordSet;

	public Preprocessor(SerialAnalyserController controller) {
		documents = new HashMap<String, DocumentDto>();
		wordSet = new HashSet<>();
		List<FileDto> files = new ArrayList<>();

		try {
			Preprocessor.getTexts(files, "Data/cicling48.col");
		} catch (IOException e) {
			e.printStackTrace();
		}

		StopWordsEnglish stopEnglish = new StopWordsEnglish();

		PreprocessFile preprocessFile = new PreprocessFile();
		for (FileDto file : files) {
			DocumentDto doc = preprocessFile.preprocessFile(controller, file,
					stopEnglish);
			documents.put(doc.getName(), doc);
		}
		preprocessFile.extraction(documents,wordSet);

	}

	public Map<String, DocumentDto> getDocuments() {
		return documents;
	}

	private static void loadCategories(Map<String, List<String>> categories,
			String filePath) throws IOException {
		final File fileEntry = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(fileEntry));
		String line;
		int i = 1;
		while ((line = br.readLine()) != null) {
			String topic = "T" + i;
			String[] docs = line.split(" ");
			if (docs.length > 0) {
				categories.put(topic,
						new ArrayList<String>(Arrays.asList(docs)));
			}
			i++;
		}
		br.close();
	}

	private static void getTexts(List<FileDto> files, String filePath)
			throws IOException {

		Map<String, List<String>> categories = new HashMap<String, List<String>>();
		try {
			Preprocessor.loadCategories(categories, "Data/cicling48.goldstd");
		} catch (IOException e) {
			e.printStackTrace();
		}

		final File fileEntry = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(fileEntry));
		String line;
		int i = 1;
		while ((line = br.readLine()) != null) {
			String[] docs = line.split(" ", 2);
			for (String topic : categories.keySet()) {
				List<String> values = categories.get(topic);
				if (values.contains(docs[0])) {
					files.add(new FileDto(docs[0], topic, docs[1]));
				}
			}

		}
		br.close();
	}

	public static HashSet<String> getWordSet() {
		return wordSet;
	}

}
