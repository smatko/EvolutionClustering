package sk.tuke.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.tuke.dto.ChromosomeDto;
import sk.tuke.dto.ClusterDto;
import sk.tuke.evolution.Crossover;
import sk.tuke.evolution.Initialization;
import sk.tuke.evolution.Mutation;
import sk.tuke.evolution.Selection;

public class Runner extends AbstractRunner {

	public static final int NUM_CLUSTERS = 4;
	private static final int POPULATION = 200;
	private static final int PARENTS = 50;

	@Override
	public List<ClusterDto> process() {

		List<ChromosomeDto> chromosomes = new ArrayList<>();
		// inicializacia + evaluacia + zoradenie
		for (int i = 0; i < POPULATION; i++) {
			chromosomes.add(Initialization.initialization(getDocuments(),
					NUM_CLUSTERS));
		}

		int iterr = 0;
		while (iterr < getDocuments().size()) {
			System.out.println(iterr);
			Collections.sort(chromosomes, new ChromosomeComparator());

			System.out.println("fit:" + chromosomes.get(0).getFitness());

			// selekcia
			List<ChromosomeDto> parents = Selection.roulette(chromosomes,
					PARENTS);

			// krizenie
			List<ChromosomeDto> crossed = new ArrayList<>();
			Crossover crossovers = new Crossover();
			do {
				crossed.addAll(crossovers.crossover(parents, NUM_CLUSTERS,
						getDocuments()));
			} while (crossed.size() < parents.size());

			// mutacia
			List<ChromosomeDto> mutated = new ArrayList<>();
			for (ChromosomeDto cross : crossed) {
				mutated.add(Mutation.mutation(cross, getDocuments()));
			}

			chromosomes = chromosomes.subList(0, chromosomes.size() - PARENTS);
			chromosomes.addAll(mutated);
			iterr++;
		}

		Collections.sort(chromosomes, new ChromosomeComparator());

		return chromosomes.get(0).getClusters();
	}



}
