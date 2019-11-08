package sk.tuke.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;

import sk.tuke.utils.Constants;

public class InitGate {
	private static SerialAnalyserController controller;

	public InitGate() {
		System.setProperty("gate.home", Constants.GATE_HOME);
		try {
			Gate.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		File gateHome = Gate.getGateHome();
		File pluginsHome = new File(gateHome, "plugins");

		try {
			Gate.getCreoleRegister().registerDirectories(
					new File(pluginsHome, "ANNIE").toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (GateException e) {
			e.printStackTrace();
		}
		System.out.println("...GATE initialised");
		System.out.println("inicializacia Gate uspesna");

		initAnnie();

	}

	private static void initAnnie() {
		System.out.println("Initialising ANNIE...");
		String[][] processingResources = {
				{ "gate.creole.tokeniser.DefaultTokeniser", null, "Tokens",
						null },
				{ "gate.creole.splitter.RegexSentenceSplitter", "Tokens",
						"Tokens", null },
				{ "stemmer.SnowballStemmer", null, "Tokens", "english" } };

		try {
			controller = (SerialAnalyserController) Factory
					.createResource("gate.creole.SerialAnalyserController");
		} catch (ResourceInstantiationException e) {
			e.printStackTrace();
		}

		for (String[] processingResource : processingResources) {
			FeatureMap fm = Factory.newFeatureMap();
			if (processingResource[1] == null) {
				fm.put("annotationSetName", processingResource[2]);
			} else {
				if (processingResource[2] != null) {
					if (!processingResource[0]
							.equals("gate.creole.splitter.RegexSentenceSplitter")) {
						fm.put("inputASName", processingResource[1]);
					}
					fm.put("outputASName", processingResource[2]);
				}
			}
			if (processingResource[3] != null) {
				fm.put("language", processingResource[3]);
			}
			ProcessingResource langAnalyser = null;
			try {
				langAnalyser = (LanguageAnalyser) (Factory.createResource(
						processingResource[0], fm));
			} catch (ResourceInstantiationException e) {
				e.printStackTrace();
			}
			controller.add(langAnalyser);
		}
		System.out.println("...ANNIE loaded");
	}

	public static SerialAnalyserController getController() {
		return controller;
	}

}
