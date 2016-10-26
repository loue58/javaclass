package lab8;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

public class WriteClusterFile {

	private static Random random = new Random();
	private static long msecsToWait;

	public static void main(String[] args) {
		for (String s : args) {
			msecsToWait = random.nextInt(240000); // up to 4 minutes
			try {
				Thread.sleep(msecsToWait);
				BufferedWriter writer = new BufferedWriter(new FileWriter(s));

				writer.write("I waited " + msecsToWait + " milliseconds. \n");

				writer.flush();
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();

			}
		}
	}
}

