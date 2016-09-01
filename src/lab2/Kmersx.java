package lab2;

import java.util.Random ;

public class Kmersx {

	public static char[] getkmer (int kmerlength, char[] val, float[] freq){
		/* generate a random sequence of length kmerlength
		 * from val with frequency passed in freq
		 */
		// should check length of val is = length of freq
		// and freqs total 1

		char[] kmer = new char[kmerlength] ;
		float[] cumfreq = new float[freq.length+1] ;

		// initialize cumulative freq array 
		cumfreq[0] =  0 ;
		for (int i=1 ; i < cumfreq.length ; i++ ) {
			cumfreq[i] = cumfreq[i-1]+freq[i-1] ;
		}
		// last one should be 1

		for (int i=0; i<kmerlength; i++){
			Random random = new Random() ;
			float f = random.nextFloat() ;
			for (int j=0 ; j < cumfreq.length-1 ; j++) {
				if (f >= cumfreq[j] && f < cumfreq[j+1]) {
					kmer[i] = val[j];
				}
				if (f == 1.0) {
					kmer[i] = val[val.length-1] ;
				}
			}

		}
		return kmer;

	}


	public static void main(String[] args) {
		/* 
		 * print to the console 1,000 randomly generated 
		 * DNA 3 mers (e.g. “ACA”, “TCG” )
		 * where the frequency of A,C,G and T is 
		 * p(A) = 0.12
	 	 *  p(C) = 0.38
		 *  p(G) = 0.39
		 *  p(T) = 0.11
		 */
		int aaacnt = 0 ;	
		int aaacnttot = 0; 

		char[] cagt = {'A','C','G','T'} ;
		char[] kmer = new char[3] ;
		float[] freqs = {0.12f, 0.38f, 0.39f, 0.11f} ;
		for (int i=0; i<1000; i++){
			kmer = getkmer( 3, cagt, freqs) ;
			System.out.println(kmer) ;	
			for (int j=0 ; j < 3; j++ ) {
				if (kmer[j] == 'A') {
					aaacnt ++ ;
				}
			}
			if (aaacnt == 3) {
				aaacnttot ++ ;
			}

			aaacnt = 0 ;
		}
		System.out.format("Percent of times AAA found: %.4f ", aaacnttot*100/1000f);
		/* Expected percentage is .12 * .12 * .12 
		 * 0.1728%
		 */
	}

}
