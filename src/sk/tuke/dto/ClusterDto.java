package sk.tuke.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, Double> centroid;
	private final List<String> documentId;

	public ClusterDto(Map<String, Double> words) {
		centroid = words;
		documentId = new ArrayList<>();
	}

	public ClusterDto() {
		centroid = new HashMap<>();
		documentId = new ArrayList<>();
	}

	public Map<String, Double> getCentroid() {
		return centroid;
	}

	public void setCentroid(Map<String, Double> centroid) {
		this.centroid = centroid;
	}

	public List<String> getDocumentId() {
		return documentId;
	}

	public void addDocumentId(String documentId) {
		this.documentId.add(documentId);
	}

	public void addDocumentIdAll(List<String> documentId) {
		this.documentId.addAll(documentId);
	}

	public void clearDocumentId() {
		this.documentId.clear();
	}

	// TODO pridavanie dokumentov a centroidov
}
