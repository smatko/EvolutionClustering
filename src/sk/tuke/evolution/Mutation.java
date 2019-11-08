package sk.tuke.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sk.tuke.dto.ChromosomeDto;
import sk.tuke.dto.ClusterDto;
import sk.tuke.dto.DocumentDto;

public class Mutation {
	private static final Double PROBABILITY_MUT = 0.1;
	private static Random m_rand = new Random();
	private static Double[] rise = { 1D, 1D, 1D, 1D, -1D, -1D, -1D, -1D, 2D,
			2D, -2D, -2D, 3D, -3D };

	public static ChromosomeDto mutation(ChromosomeDto parent,
			Map<String, DocumentDto> documents) {
		List<ClusterDto> clusters = new ArrayList<>();
		for (ClusterDto cluster : parent.getClusters()) {
			ClusterDto c = new ClusterDto();
			if (m_rand.nextDouble() < PROBABILITY_MUT) {
				Map<String, Double> centroid = new HashMap<String, Double>();
				for (String key : cluster.getCentroid().keySet()) {
					Double value = cluster.getCentroid().get(key);
					if (m_rand.nextDouble() < PROBABILITY_MUT) {
						value = value +   rise[m_rand.nextInt(rise.length)];
						if (value > 0) {
							centroid.put(key, value);
						}
					} else {
						centroid.put(key, value);
					}

				}
				c.setCentroid(centroid);
			} else {
				c.setCentroid(cluster.getCentroid());
				c.addDocumentIdAll(cluster.getDocumentId());
			}
			clusters.add(c);
		}
		Recomputing.recomp(documents, clusters);

		ChromosomeDto mutated = new ChromosomeDto(clusters,
				FitnessCalculation.fitness(documents, clusters));
		return mutated;

	}

}
