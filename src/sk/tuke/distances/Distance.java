package sk.tuke.distances;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import sk.tuke.dto.DocumentDto;

public class Distance {
	public static double cosineDistance(Map<String, Double> centroid1,
			Map<String, Double> centroid2) {
		HashSet<String> tokens = new HashSet<>();
		tokens.addAll(centroid1.keySet());
		tokens.addAll(centroid2.keySet());

		double pqSum = 0D;
		double pCoef = 0D;
		double qCoef = 0D;
		for (String token : tokens) {
			double p = 0D, q = 0D;
			if (centroid1.containsKey(token)) {
				p = centroid1.get(token);
			}
			if (centroid2.containsKey(token)) {
				q = centroid2.get(token);
			}
			pqSum += p * q;
			if (p != 0) {
				pCoef += Math.pow(p, 2);
			}
			if (q != 0) {
				qCoef += Math.pow(q, 2);
			}
		}
		double menovatel = Math.sqrt(pCoef) * Math.sqrt(qCoef);
		if (menovatel == 0) {
			return 0D;
		}
		return pqSum/menovatel;
	}

	public static double euclideDistance(Map<String, Double> centroid1,
			Map<String, Double> centroid2) {
		HashSet<String> tokens = new HashSet<>();
		tokens.addAll(centroid1.keySet());
		tokens.addAll(centroid2.keySet());

		double pqSum = 0D;
		for (String token : tokens) {
			double p = 0D, q = 0D;
			if (centroid1.containsKey(token)) {
				p = centroid1.get(token);
			}
			if (centroid2.containsKey(token)) {
				q = centroid2.get(token);
			}
			pqSum = Math.sqrt(Math.pow(p - q, 2));
		}

		return pqSum;
	}

	public static Map<String, Double> updateDistance(
			Map<String, DocumentDto> documents, List<String> documentsId) {
		Map<String, Double> centroid = new HashMap<String, Double>();

		for (String key : documentsId) {
			Map<String, Double> words = documents.get(key).getWords();
			HashSet<String> tokens = new HashSet<>();
			tokens.addAll(centroid.keySet());
			tokens.addAll(words.keySet());
			for (String token : tokens) {
				Double value = 0D;
				if (centroid.containsKey(token)) {
					value += centroid.get(token);
				}
				if (words.containsKey(token)) {
					value += words.get(token);
				}
				centroid.put(token, value / 2D);
			}
		}

		return centroid;
	}
}
