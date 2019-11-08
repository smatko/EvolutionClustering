package sk.tuke.sanitize;

import gate.Document;
import gate.Factory;
import gate.creole.ResourceInstantiationException;

public class Sanitize {
	public static Document sanitize(String text) {
		Document document = null;
		// text na gate dokument
		try {
			document = Factory.newDocument(text);
		} catch (ResourceInstantiationException e1) {
			e1.printStackTrace();
		}
		return document;
	}
}