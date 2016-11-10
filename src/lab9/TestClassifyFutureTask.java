package lab9;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

// 100 on 1 threads took 54 seconds.
// On 4 available processors
// Average percent correct is 61.33802816901405
// 25 on 4 threads took 27 seconds.
// On 4 available processors
// Average percent correct is 61.85915492957743

public class TestClassifyFutureTask {

	private static List<Double> pctCorrectListAll = new ArrayList<Double>();
	
	private Random random = new Random();
	private final File file = new File(
			"C:\\Users\\Lisa\\git\\javaclass\\src\\lab9\\pivoted_genusLogNormalWithMetadata.arff");
	
	private static final int numThreads = 4;
	private static final int NUM_PERMUTATIONS = 25;

	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();
		List<TestClassifyFutureTask> taskList = new ArrayList<TestClassifyFutureTask>();
		List<FutureTask<List<Double>>> threadList = new ArrayList<FutureTask<List<Double>>>();
		for (int i = 0; i < numThreads; i++) {
			TestClassifyFutureTask task = new TestClassifyFutureTask() ;
			taskList.add(task);
			FutureTask<List<Double>> thread = task.getPercentCorrectForOneFileFuture();
			threadList.add(thread);
			new Thread(threadList.get(i)).start();
		}
		for (int i = 0; i < numThreads; i++) {
			FutureTask<List<Double>> thread = threadList.get(i);
			pctCorrectListAll.addAll(thread.get()); 
		}
		
		long currentTime = System.currentTimeMillis();
		System.out.println(NUM_PERMUTATIONS + " on " + numThreads + " threads took " + (currentTime - startTime) / 1000
				+ " seconds.");
		System.out.println("On " + Runtime.getRuntime().availableProcessors() + " available processors");
		System.out.println("Average percent correct is " + calcAvgDouble(pctCorrectListAll));

	}

	private static double calcAvgDouble(List<Double> entry) {
		double sum = 0;
		if (!entry.isEmpty()) {
			for (Double d : entry) {
				sum += d.doubleValue();
			}
			return sum / entry.size();
		}
		return sum;
	}

	private FutureTask<List<Double>> getPercentCorrectForOneFileFuture() {
		return new FutureTask<List<Double>> (new Callable<List<Double>>() {
			@Override
			public List<Double> call() throws Exception {
				return getPercentCorrectForOneFile(file, NUM_PERMUTATIONS, random);
			}
		});
	}

	private List<Double> getPercentCorrectForOneFile(File inFile, int NUM_PERMUTATIONS, Random random)
			throws Exception {
		List<Double> percentCorrect = new ArrayList<Double>();

		for (int x = 0; x < NUM_PERMUTATIONS; x++) {
			/*
			 * All methods that change a set of instances are safe, ie. a change
			 * of a set of instances does not affect any other sets of
			 * instances. All methods that change a datasets's attribute
			 * information clone the dataset before it is changed.
			 */
			Instances data = DataSource.read(inFile.getAbsolutePath());
			data.setClassIndex(data.numAttributes() - 1);
			Evaluation ev = new Evaluation(data);
			AbstractClassifier rf = new RandomForest();

			// rf.buildClassifier(data);
			ev.crossValidateModel(rf, data, 10, random);
			// System.out.println(ev.toSummaryString("\nResults\n\n", false));
			// System.out.println(x + " " + ev.areaUnderROC(0) + " " +
			// ev.pctCorrect());
			percentCorrect.add(ev.pctCorrect());
		}

		return percentCorrect;

	}
	
	}

