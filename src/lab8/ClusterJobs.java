package lab8;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ClusterJobs {

	private static int countNum = 0;
	// no input in current run
	// public static final File IN_DIR = new
	// File("/users/louellet/someData/In");
	
	// my windows machine
	public static final File OUT_DIR = new File("/users/Lisa/lab8/Out");
	public static final File RUN_DIR = new File("/users/Lisa/lab8/Run");
	// mamba
	// public static final File OUT_DIR = new File("/users/louellet/someData/Out");
	// public static final File RUN_DIR = new File("/users/louellet/someData/Run");

	public static final String OUT_SUFFIX = "Out.txt";

	public static void main(String[] args) throws Exception {
		// String[] files = IN_DIR.list();
		 List<File> allShFiles = new ArrayList<File>();

		// for(String s : files)
		// if( s.endsWith("fas"))
		// {
		for (int i = 1; i < 101; i++) {
			countNum++;
//			File inFile = new File(IN_DIR.getAbsolutePath() + File.separator + s);

			File OutFile = new File(OUT_DIR.getAbsolutePath() + File.separator + "lisa" + countNum + OUT_SUFFIX);

			File runFile = new File(RUN_DIR.getAbsoluteFile() + File.separator + "run_" + countNum + ".sh");

			BufferedWriter writer = new BufferedWriter(new FileWriter(runFile));

			writer.write("java -jar /users/louellet/lab8/dist/lab8.jar " + "-o \""
					+ OutFile.getAbsolutePath() + "\"\n");

			writer.write("gzip " + OutFile.getAbsolutePath() + " \n");

			writer.flush();
			writer.close();

			allShFiles.add(runFile);
		}

		// }

		BufferedWriter writer = new BufferedWriter(
				new FileWriter(new File(RUN_DIR.getAbsoluteFile() + File.separator + "runAll.sh")));

		for (File f : allShFiles) {
			writer.write("qsub " + f.getAbsolutePath() + "\n");
		}

		writer.flush();
		writer.close();
	}

}
