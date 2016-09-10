package lab3;

import java.util.Random ;

public class Aaquizx {

	public static String[] SHORT_NAMES = 
		{ "A","R", "N", "D", "C", "Q", "E", 
				"G",  "H", "I", "L", "K", "M", "F", 
				"P", "S", "T", "W", "Y", "V" };

	public static String[] FULL_NAMES = 
		{
				"alanine","arginine", "asparagine", 
				"aspartic acid", "cysteine",
				"glutamine",  "glutamic acid",
				"glycine" ,"histidine","isoleucine",
				"leucine",  "lysine", "methionine", 
				"phenylalanine", "proline", 
				"serine","threonine","tryptophan", 
				"tyrosine", "valine"};

	public static void main(String[] args) throws Exception {
		/** Quiz to drill on amino acids
		 * The quiz is n seconds long - n being the first and only argument.
		 * The program displays the full name
		 * of an amino acid (like “alanine” ) 
		 * and asks the user to type in the one character code (like a)
		 * The quiz ignores case in the answer.
		 * The total score is the number of correct answers…
		 * A report at the end tells the quiz taker the number right and wrong
		 * for each amino acid
		 * @author Lisa Ouellette 
		 */
		int quizSecs = 30 ;
		if (args.length > 1) {
			throw new Exception("Only one argument allowed - integer number of seconds for quiz");
		}
		else if (args.length == 1)
		{ quizSecs = Integer.parseInt(args[0]); }

		System.out.println("Type the one letter abbreviation for each amino acid");
		System.out.println("Continue for " + quizSecs + " seconds.");

		Random random = new Random() ;
		long startTime = System.currentTimeMillis();
		long currentTime = System.currentTimeMillis();
		int aaIndex = 0;
		int correctAnswers = 0 ;
		int[] aaCorrect = new int[FULL_NAMES.length] ;
		int[] aaWrong = new int[FULL_NAMES.length] ; 


		while (currentTime - startTime < quizSecs*1000) {
			aaIndex = random.nextInt(20) ;
			System.out.println(FULL_NAMES[aaIndex]);
			String aString = System.console().readLine().toUpperCase();
			if (aString.length() == 0) {
				System.out.println("Please enter an amino acid one letter abbreviation");
			}
			else {
				String aChar = "" + aString.charAt(0);
				System.out.println("You entered " + aChar) ;
				if (aChar.equals(SHORT_NAMES[aaIndex])) { 
					System.out.println("Correct!");
					aaCorrect[aaIndex]++ ;
					correctAnswers++ ;
				}
				else {
					aaWrong[aaIndex]++ ;
					System.out.println("WRONG!  The correct answer is " + SHORT_NAMES[aaIndex]);
				}}
			currentTime = System.currentTimeMillis();
			System.out.println("You have " + (quizSecs-(currentTime - startTime)/1000) + " seconds left.");

		}

		System.out.println("Your score is " + correctAnswers);
		for (int i = 0 ; i < FULL_NAMES.length ; i++){
			System.out.println(FULL_NAMES[i]+ ":"
					+ " Correct: " + aaCorrect[i]
							+ " Wrong: " + aaWrong[i] 
					);

		}
	}

}
