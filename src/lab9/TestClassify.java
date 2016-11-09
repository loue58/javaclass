package lab9;
/* 
 * 
 * 
 * Use the weka program (a java classification suite) to run the 
 * random forest generator on a colorectal adenomas case-control set.
 * Run 100 permuations: Once with 1 thread and once with 4 threads
 * For each run:
 * What is the average % correct observed?
 * How many CPUs on your system?
 * What speedup did you see? 
 * 
 * */
//
// 100 on 1 threads took 54 seconds.
// On 4 available processors
// Average percent correct is 61.19718309859153 

// 25 on 4 threads took 27 seconds.
// On 4 available processors
// Average percent correct is 61.52112676056335

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import net.jcip.annotations.*;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TestClassify {

	private final Object myLock = new Object();
	@GuardedBy(value="myLock")
	private static List<Double> pctCorrectListAll = new ArrayList<Double>();
	
	private Random random = new Random();
	private final File file = new File(
			"C:\\Users\\Lisa\\git\\javaclass\\src\\lab9\\pivoted_genusLogNormalWithMetadata.arff");
	
	private static final int numThreads = 1;
	private static final CountDownLatch cdl = new CountDownLatch(numThreads);
	private static final int NUM_PERMUTATIONS = 100;

	public static void main(String[] args) throws Exception {

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < numThreads; i++) {
			TestClassify tc = new TestClassify();
			tc.start();
		}
		cdl.await();
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

	private void start() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				List<Double> pctCorrectList;
				try {
					pctCorrectList = getPercentCorrectForOneFile(file, NUM_PERMUTATIONS, random);
					synchronized (myLock) {
					pctCorrectListAll.addAll(pctCorrectList); 
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cdl.countDown();
			}
		};
		new Thread(r).start();

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
