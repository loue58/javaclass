package lab3;

import java.util.Random ;

public class Aaquiz {

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

	public static void main(String[] args) {
		/** Quiz to drill on amino acids
		 * It is 30 seconds long.
		 * The quiz ends after 30 seconds
		 * or when there is a single incorrect answer.
		 * The program displays the full name
		 * of an amino acid (like "alanine" ) 
		 * and asks the user to type in the one character code (like a)
		 * The quiz ignores case in the answer.
		 * The total score is the number of correct answers
		 * @author Lisa Ouellette 
		 */

		int QUIZ_SECS = 30 ;

		System.out.println("Type the one letter abbreviation for each amino acid");
		System.out.println("You continue for " + QUIZ_SECS + " or one wrong answer - whichever comes first");

		boolean answer = true;
		Random random = new Random() ;
		long startTime = System.currentTimeMillis();
		long currentTime = System.currentTimeMillis();
		int aaIndex = 0;
		int correctAnswers = 0 ;

		while (currentTime - startTime < QUIZ_SECS*1000 && answer == true) {
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
					correctAnswers ++ ;
					System.out.println("So far " + correctAnswers + " correct.");
				}
				else {
					answer = false;
					System.out.println("WRONG!  The correct answer is " + SHORT_NAMES[aaIndex]);
				}}
			currentTime = System.currentTimeMillis();
			System.out.println("You have " + (QUIZ_SECS-(currentTime - startTime)/1000) + " seconds left.");

		}

		System.out.println("Your score is " + correctAnswers);

	}

}
