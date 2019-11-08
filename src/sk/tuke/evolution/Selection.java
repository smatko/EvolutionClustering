package sk.tuke.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import sk.tuke.dto.ChromosomeDto;

public class Selection {
	private static Random m_rand = new Random();

	public static List<ChromosomeDto> randomSelection(
			List<ChromosomeDto> chromosomes, int numParents) {

		List<ChromosomeDto> parents = new ArrayList<>();
		for (int i = 0; i < numParents; i++) {
			List<ChromosomeDto> chromosomesUnsort = new ArrayList<>();
			chromosomesUnsort.addAll(chromosomes);
			Collections.shuffle(chromosomesUnsort);
			parents.add(chromosomesUnsort.get(0));
		}

		return parents;

	}

	public static List<ChromosomeDto> roulette(List<ChromosomeDto> chromosomes,
			int numParents) {
		List<ChromosomeDto> parents = new ArrayList<>();

		double totalFitness = 0D;
		for (ChromosomeDto chromosomeDto : chromosomes) {
			totalFitness += chromosomeDto.getFitness();
		}

		for (int i = 0; i < numParents; i++) {
			parents.add(rouletteWheelSelection(chromosomes, totalFitness));
		}

		return parents;
	}

	private static ChromosomeDto rouletteWheelSelection(
			List<ChromosomeDto> chromosomes, Double totalFitness) {
		double randNum = m_rand.nextDouble() * totalFitness;
		int idx;
		for (idx = 0; idx < chromosomes.size() && randNum > 0; ++idx) {
			randNum -= chromosomes.get(idx).getFitness();
		}
		return Recomputing.copyChrom(chromosomes.get(idx - 1));
	}
}
