import java.util.List;

import sk.tuke.dto.ClusterDto;
import sk.tuke.evaluation.Evaluation;
import sk.tuke.processor.Runner;

public class Main {

	public static void main(String[] args) {

		long startTime;
		long endTime;
		long duration;

		startTime = System.currentTimeMillis();
		Runner runner3 = new Runner();
		List<ClusterDto> genetics = runner3.process();
		Evaluation.evaluation(genetics, runner3.getDocuments(),
				runner3.NUM_CLUSTERS);
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("time: " + duration);

	}

}
