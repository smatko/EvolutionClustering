package sk.tuke.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import sk.tuke.distances.Distance;
import sk.tuke.dto.ChromosomeDto;
import sk.tuke.dto.ClusterDto;
import sk.tuke.dto.DocumentDto;

public class Initialization {
	public static ChromosomeDto initialization(
			Map<String, DocumentDto> documents, int nUM_CLUSTERS) {

		List<DocumentDto> documentsList = new ArrayList(documents.values());
		Collections.shuffle(documentsList);

		List<ClusterDto> clusters = new ArrayList<>();
		for (int i = 0; i < nUM_CLUSTERS; i++) {
			clusters.add(new ClusterDto(documentsList.get(i).getWords()));
		}

		for (int i = 0; i < documentsList.size(); i++) {
			double value = -1D;
			int index = -1;
			for (int j = 0; j < clusters.size(); j++) {
				double clustValue = Distance.cosineDistance(documentsList
						.get(i).getWords(), clusters.get(j).getCentroid());
				if (clustValue > value) {
					value = clustValue;
					index = j;

				}
			}
			clusters.get(index).addDocumentId(documentsList.get(i).getName());
		}

		for (int j = 0; j < clusters.size(); j++) {
			clusters.get(j).setCentroid(
					Distance.updateDistance(documents, clusters.get(j)
							.getDocumentId()));
		}

		//evaluation
		return new ChromosomeDto(clusters, FitnessCalculation.fitness(
				documents, clusters));
	}
}
