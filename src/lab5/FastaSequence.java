package lab5;

import java.util.*;
import java.io.*;

public class FastaSequence {

	private final String sequence;
	private final String header;

	public FastaSequence(String header, String sequence) {
		this.header = header;
		this.sequence = sequence;
	}

	// returns the header of this sequence without the “>”
	public String getHeader() {
		return header;
	}

	@Override
	public String toString() {
		return getHeader() ;
	}
	// returns the DNA sequence of this FastaSequence
	public String getSequence() {
		return sequence;
	}

	// returns the number of G’s and C’s divided by the length of this sequence
	public float getGCRatio() {
		int GCCount = 0;
		{
			for (int i = 0; i < sequence.length(); i++)
				if (sequence.charAt(i) == 'C' || sequence.charAt(i) == 'G') {
					GCCount++;
				}
		}
		return (float) GCCount / sequence.length();
	}

	public static List<FastaSequence> readFastaFile(String filepath) throws Exception {

		List<FastaSequence> fileSequences = new ArrayList<FastaSequence>();
		boolean newFastaSequence = true;
		String header = new String();
		StringBuffer sequence = new StringBuffer(); // to accumulate sequence
		long seqCount = 0;
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));

		for (String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine()) {
			if (nextLine.charAt(0) == '>') {
				if (seqCount > 0) {
					// create FastaSequence object for previous
					fileSequences.add(new FastaSequence(header, sequence.toString()));
				}
				seqCount++;
				header = nextLine.substring(1); // omit leading >
				newFastaSequence = true;
			} else {
				if (newFastaSequence) {
					sequence.setLength(0); // clear buffer for next sequence
					newFastaSequence = false;
				}
				sequence.append(nextLine);
			}
		}
		reader.close();
		// write last one
		if (seqCount > 0) {
			fileSequences.add(new FastaSequence(header, sequence.toString()));
		}
		// check for empty file
		else {
			throw new Exception("No fasta sequences found in input file");
		}
		return fileSequences;

	}

	public static void main(String[] args) throws Exception {
		List<FastaSequence> fastaList = FastaSequence
				.readFastaFile("C:\\Users\\Lisa\\Documents\\UNCC\\java\\test.fasta");

		for (FastaSequence fs : fastaList) {
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println(fs.getGCRatio());
		//	System.out.println(fs);
		}
	}

}