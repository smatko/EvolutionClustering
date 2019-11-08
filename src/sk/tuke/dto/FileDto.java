package sk.tuke.dto;

import java.io.Serializable;

public class FileDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String topic;
	private String text;

	public FileDto(String name, String topic, String text) {
		this.name = name;
		this.topic = topic;
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
