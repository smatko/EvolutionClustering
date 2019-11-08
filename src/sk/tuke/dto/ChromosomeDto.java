package sk.tuke.dto;

import java.io.Serializable;
import java.util.List;

public class ChromosomeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<ClusterDto> clusters;
	private Double fitness;

	public ChromosomeDto(List<ClusterDto> clusters, Double fitness) {
		this.clusters = clusters;
		this.fitness = fitness;
	}

	public List<ClusterDto> getClusters() {
		return clusters;
	}

	public void setClusters(List<ClusterDto> clusters) {
		this.clusters = clusters;
	}

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

}
