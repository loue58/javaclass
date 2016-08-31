package lab2;

import java.util.Random ;

public class Dna3mers {

	public static void main(String[] args) {
		/* 
		 * print to the console 1,000 randomly generated 
	     * DNA 3 mers (e.g. “ACA”, “TCG” )
	     * where the frequency of A,C,G and T is 25% and is uniformly sampled.
		 */
		int aaacnt = 0 ;	
		int aaacnttot = 0; 
		Random random = new Random() ;
		char[] cagt = {'A','C','G','T'} ;
		char[] kmer = new char[3] ;
		for (int i=0; i<1000;i++){
			for (int j=0; j<3; j++) {
				kmer[j] = cagt[random.nextInt(4)] ;
				if (kmer[j] == 'A') {
					aaacnt++ ;
				}
			}
		System.out.println(kmer) ;	
		if (aaacnt == 3) {
			aaacnttot ++ ;
		}
		aaacnt = 0 ;
		}
		System.out.format("Percent of times AAA found: %.4f ", aaacnttot*100/1000f);
		/* Expected percentage is .25 * .25 * .25 
		 * 1.5625%
		 */
	}

}
