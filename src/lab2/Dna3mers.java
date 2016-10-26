package lab2;

import java.util.Random ;

public class Dna3mers {

	public static void main(String[] args) {
		/* 
		 * print to the console 1,000 randomly generated 
		 * DNA 3 mers (e.g. "ACA", "TCG" )
		 * where the frequency of A,C,G and T is 25% and is uniformly sampled.
		 * 
		 */

		int NUM_TO_GEN = 1000 ;

		int aaacnttot = 0; 
		Random random = new Random() ;
		char[] cagt = {'A','C','G','T'} ;
		char[] kmer = new char[3] ;
		
		for (int i=0; i<NUM_TO_GEN; i++){
			for (int j=0; j<3; j++) {
				kmer[j] = cagt[random.nextInt(4)] ;
			}
			System.out.println(kmer) ;	
			if ("AAA".equals(new String(kmer))) {
				aaacnttot ++ ;
			}

		}
		System.out.format("Percent of times AAA found: %.4f ", aaacnttot*100f/NUM_TO_GEN);
		/* Expected percentage is .25 * .25 * .25 
		 * 1.5625%
		 */
	}

}
