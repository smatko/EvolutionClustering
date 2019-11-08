package sk.tuke.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sk.tuke.dto.ChromosomeDto;
import sk.tuke.dto.ClusterDto;
import sk.tuke.dto.DocumentDto;

public class Crossover {
	private static final Double PROBABILITY_CROSS = 0.8;
	private static Random m_rand;
	private static int ind1 = 0, ind2 = 0;

	public Crossover() {
		m_rand = new Random();
	}

	public List<ChromosomeDto> crossover(List<ChromosomeDto> parents,
			int numClusters, Map<String, DocumentDto> documents) {
		List<ChromosomeDto> cross = new ArrayList<>();
		ind1 = 0;
		ind2 = 0;
		while (ind1 == ind2) {
			getIndexes(parents.size() - 1);
		}
		Double prob = m_rand.nextDouble();
		if (prob > PROBABILITY_CROSS) {
			cross.add(Recomputing.copyChrom(parents.get(ind2)));
			cross.add(Recomputing.copyChrom(parents.get(ind1)));
			return cross;
		}

		int seknutie = m_rand.nextInt((numClusters - 2) + 1) + 1;
		List<ClusterDto> first = new ArrayList<>();
		List<ClusterDto> second = new ArrayList<>();

		first.addAll(parents.get(ind1).getClusters().subList(0, seknutie));
		first.addAll(parents.get(ind2).getClusters()
				.subList(seknutie, numClusters));
		Recomputing.recomp(documents, first);
		second.addAll(parents.get(ind2).getClusters().subList(0, seknutie));
		second.addAll(parents.get(ind1).getClusters()
				.subList(seknutie, numClusters));
		Recomputing.recomp(documents, second);
		cross.add(new ChromosomeDto(first, FitnessCalculation.fitness(
				documents, first)));
		cross.add(new ChromosomeDto(second, FitnessCalculation.fitness(
				documents, second)));
		return cross;
	}

	private void getIndexes(int size) {
		ind1 = m_rand.nextInt(size + 1);
		ind2 = m_rand.nextInt(size + 1);
	}
}
