package sk.tuke.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sk.tuke.distances.Distance;
import sk.tuke.dto.ChromosomeDto;
import sk.tuke.dto.ClusterDto;
import sk.tuke.dto.DocumentDto;

public class Recomputing {
	public static void recomp(Map<String, DocumentDto> documents,
			List<ClusterDto> clusters) {
		List<DocumentDto> documentsList = new ArrayList(documents.values());

		for (ClusterDto cl : clusters) {
			cl.clearDocumentId();
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

	}

	public static ChromosomeDto copyChrom(ChromosomeDto chromosome) {
		List<ClusterDto> clusters = new ArrayList<>();
		for (ClusterDto cl : chromosome.getClusters()) {
			ClusterDto c = new ClusterDto();
			c.setCentroid(cl.getCentroid());
			c.addDocumentIdAll(cl.getDocumentId());
			clusters.add(c);
		}
		ChromosomeDto newCh = new ChromosomeDto(clusters,
				chromosome.getFitness());

		return newCh;
	}
}
