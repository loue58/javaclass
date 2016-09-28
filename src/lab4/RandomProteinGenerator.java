package lab4;

import java.util.Random;

public class RandomProteinGenerator {
	/*
	 * constructor
	 * 
	 * if useUniformFrequencies == true, the random proteins have an equal
	 * probability of all 20 residues.
	 * 
	 * if useUniformFrequencies == false, the 20 residues defined by { 'A', 'C',
	 * 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
	 * 'T', 'V', 'W', 'Y' }
	 * 
	 * have a distribution of
	 * 
	 * {0.072658f, 0.024692f, 0.050007f, 0.061087f, 0.041774f, 0.071589f,
	 * 0.023392f, 0.052691f, 0.063923f, 0.089093f, 0.023150f, 0.042931f,
	 * 0.052228f, 0.039871f, 0.052012f, 0.073087f, 0.055606f, 0.063321f,
	 * 0.012720f, 0.032955f}
	 * 
	 */
	private static char AA_ID[] = { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
			'V', 'W', 'Y' };
	private static float AA_NON_UNIF_FREQ[] = { 0.072658f, 0.024692f, 0.050007f, 0.061087f, 0.041774f, 0.071589f,
			0.023392f, 0.052691f, 0.063923f, 0.089093f, 0.023150f, 0.042931f, 0.052228f, 0.039871f, 0.052012f,
			0.073087f, 0.055606f, 0.063321f, 0.012720f, 0.032955f };
	private float[] aaFreq = new float[AA_ID.length];
	private float[] cumfreq = new float[aaFreq.length + 1];
	private Random random = new Random();

	public RandomProteinGenerator(boolean useUniformFrequencies) throws Exception {
		if (useUniformFrequencies == true) {
			for (int i = 0; i < AA_ID.length; i++) {
				this.aaFreq[i] = 1f / AA_ID.length;
			}
		} else {
			if (AA_NON_UNIF_FREQ.length != AA_ID.length)
				throw new Exception("AA_ID array length not equal AA_NON_UNIF_FREQ array length");
			this.aaFreq = AA_NON_UNIF_FREQ;
		}
		// initialize cumulative frequency array
		cumfreq[0] = 0;
		for (int i = 1; i < cumfreq.length; i++) {
			cumfreq[i] = cumfreq[i - 1] + aaFreq[i - 1];
		}
	}

	/*
	 * Returns a randomly generated protein of length length.
	 */
	public String getRandomProtein(int length) {
		
		char[] protein = new char[length];
		for (int i = 0; i < length; i++) {
			float f = random.nextFloat();
			for (int j = 0; j < cumfreq.length - 1; j++) {
				if (f >= cumfreq[j] && f < cumfreq[j + 1]) {
					protein[i] = AA_ID[j];
				}
				if (f >= 1.0) {
					protein[i] = AA_ID[AA_ID.length - 1];
				}
			}

		}
		// System.out.println(new String(protein));
		return new String(protein);
	}

	/*
	 * Returns the probability of seeing the given sequence given the underlying
	 * residue frequencies represented by this class. For example, if
	 * useUniformFrequencies==false in constructor, the probability of "AC"
	 * would be 0.072658 * 0.024692
	 */
	public double getExpectedFrequency(String protSeq) {
		double expectedFreq = 1;
		for (int i = 0; i < protSeq.length(); i++) {
			for (int j = 0; j < AA_ID.length; j++)
				if (protSeq.charAt(i) == AA_ID[j]) {
					expectedFreq = expectedFreq * aaFreq[j];
				}
		}
		return expectedFreq;
	}

	/*
	 * calls getRandomProtein() numIterations times generating a protein with
	 * length equal to protSeq.length(). Returns the number of time protSeq was
	 * observed / numIterations
	 */
	public double getFrequencyFromSimulation(String protSeq, int numIterations) {

		int observedFreq = 0;
		for (int i = 0; i < numIterations; i++) {
			if (getRandomProtein(protSeq.length()).equals(protSeq)) {
				observedFreq++;
			}
		}

		return observedFreq / (double) numIterations;
	}

	public static void main(String[] args) throws Exception {
		RandomProteinGenerator uniformGen = new RandomProteinGenerator(true);
		String testProtein = "ACD";
		int numIterations = 10000000;
		System.out.println(uniformGen.getExpectedFrequency(testProtein)); // should
																			// be
																			// 0.05^3
																			// =
																			// 0.000125
		System.out.println(uniformGen.getFrequencyFromSimulation(testProtein, numIterations)); // should
																								// be
																								// close

		RandomProteinGenerator realisticGen = new RandomProteinGenerator(false);

		// should be 0.072658 * 0.024692 * 0.050007 == 8.97161E-05
		System.out.println(realisticGen.getExpectedFrequency(testProtein));
		System.out.println(realisticGen.getFrequencyFromSimulation(testProtein, numIterations)); // should
																									// be
																									// close

	}
}
