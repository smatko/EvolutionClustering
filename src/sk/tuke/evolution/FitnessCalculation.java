package sk.tuke.evolution;

import java.util.List;
import java.util.Map;

import sk.tuke.distances.Distance;
import sk.tuke.dto.ClusterDto;
import sk.tuke.dto.DocumentDto;

public class FitnessCalculation {

	public static double fitness(Map<String, DocumentDto> documents,
			List<ClusterDto> clusters) {
		double fitness = 0D;
		for (int i = 0; i < clusters.size(); i++) {
			ClusterDto cluster = clusters.get(i);
			double pom = 0;
			for (String docId : cluster.getDocumentId()) {
				DocumentDto doc = documents.get(docId);

				pom += Distance.cosineDistance(cluster.getCentroid(),
						doc.getWords());
			}
			fitness += pom;
		}
		return fitness;

	}

}
