package sk.tuke.evaluation;

import java.util.List;
import java.util.Map;

import sk.tuke.dto.ClusterDto;
import sk.tuke.dto.DocumentDto;
import sk.tuke.evolution.FitnessCalculation;

public class Evaluation {

	public static void evaluation(List<ClusterDto> clusters,
			Map<String, DocumentDto> documents, int numClusters) {
		Double fitness = FitnessCalculation.fitness(documents, clusters);
		System.out.println("fitness: " + fitness);
		entrophy(clusters, documents, numClusters);
	}

	private static void entrophy(List<ClusterDto> clusters,
			Map<String, DocumentDto> documents, int numClusters) {

		double N = documents.size();
		double Entrophy = 0D;
		for (int i = 0; i < numClusters; i++) {
			ClusterDto cluster = clusters.get(i);
			if (cluster.getDocumentId().size() > 0) {
				Entrophy += (cluster.getDocumentId().size() / N)
						* Sr(cluster, documents, numClusters);
			}
		}
		System.out.println("Entrophy: " + Entrophy);
	}

	private static Double Sr(ClusterDto cluster,
			Map<String, DocumentDto> documents, int numClusters) {
		double first = -(1 / Math.log(numClusters));
		double second = 0;
		for (int i = 1; i <= numClusters; i++) {
			String topic = "T" + i;
			double Nri = 0;
			for (String docKey : cluster.getDocumentId()) {
				if (documents.get(docKey).getTopic().equals(topic)) {
					Nri++;
				}
			}
			if (Nri != 0) {
				second += (Nri / cluster.getDocumentId().size())
						* Math.log(Nri
								/ cluster.getDocumentId().size());
			} else {
				second += (Nri / cluster.getDocumentId().size());
			}
		}

		return first * second;

	}
}
