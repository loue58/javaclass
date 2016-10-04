package lab6;

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
		return getHeader();
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

	public static void writeUnique(File inFile, File outFile) throws Exception

	/*
	 * Write each unique sequence to the output file with the # of times each
	 * sequence was seen in the input file as the header (with the sequence seen
	 * the fewest times the first)
	 */
	{
		// create list of FastaSequences
		List<FastaSequence> fastaList = FastaSequence.readFastaFile(inFile.getAbsolutePath());
		// accumulate totals in a HashMap
		Map<String, Integer> seqMap = new HashMap<String, Integer>();
		for (FastaSequence fs : fastaList) {
			Integer count = seqMap.get(fs.getSequence());
			if (count == null) // first one
				count = 0;
			count++;
			seqMap.put(fs.getSequence(), count);
		}
		// sort map by value - first convert to List
		List<Map.Entry<String, Integer>> seq = new ArrayList<Map.Entry<String, Integer>>(seqMap.entrySet());

		Comparator<Map.Entry<String, Integer>> sortValue = new Comparator<Map.Entry<String, Integer>>() {
			// sort by Map value
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		};
		Collections.sort(seq, sortValue);

		// write the output file
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		for (Map.Entry<String, Integer> c : seq) {
			writer.write(">" + c.getValue());
			writer.newLine();
			writer.write(c.getKey());
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	public static void main(String[] args) throws Exception {
		File inputFile = new File("C:\\Users\\Lisa\\Documents\\UNCC\\java\\lab6test1in.fasta");
		File outputFile = new File("C:\\Users\\Lisa\\Documents\\UNCC\\java\\lab6test1out.fasta");

		FastaSequence.writeUnique(inputFile, outputFile);

	}
}
