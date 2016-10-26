package lab7;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.*;

public class AaquizGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -636296879242171912L;

	public static String[] SHORT_NAMES = { "A", "R", "N", "D", "C", "Q", "E", "G", "H", "I", "L", "K", "M", "F", "P",
			"S", "T", "W", "Y", "V" };

	public static String[] FULL_NAMES = { "alanine", "arginine", "asparagine", "aspartic acid", "cysteine", "glutamine",
			"glutamic acid", "glycine", "histidine", "isoleucine", "leucine", "lysine", "methionine", "phenylalanine",
			"proline", "serine", "threonine", "tryptophan", "tyrosine", "valine" };

	private volatile int nbrSecs;
	private Random random = new Random();
	private long startTime = System.currentTimeMillis();
	private long currentTime = System.currentTimeMillis();
	private int aaIndex = 0;
	int correctAnswers = 0;

	private JButton startButton = new JButton("Start");
	private JButton cancelButton = new JButton("Cancel");
	private JTextField aaGameSecs = new JTextField();
	private JTextField aaMessage = new JTextField();
	private JTextArea aaInstruction = new JTextArea();
	private JTextField aaLongName = new JTextField();
	private JTextField aaAnswer = new JTextField();

	volatile boolean cancel = false;

	public AaquizGui(int nbrSecs) {
		super("Amino Acid Quiz");
		this.nbrSecs = nbrSecs;
		setLocationRelativeTo(null);
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(6, 0));
		getContentPane().add(aaGameSecs);
		getContentPane().add(aaInstruction);
		aaInstruction.setEditable(false);
		getContentPane().add(aaLongName);
		aaLongName.setEditable(false);
		getContentPane().add(aaAnswer);
		aaAnswer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// submit new thread
				cancel = false;
				cancelButton.setEnabled(true);
				new Thread(new GiveQuiz()).start();
			}
		});
		getContentPane().add(aaMessage);
		aaMessage.setEditable(false);
		getContentPane().add(getButtonPanel());
		aaMessage.setText(" ");
		aaAnswer.setEditable(false);
		aaGameSecs.setText("Game will run for " + nbrSecs + " seconds");
		aaInstruction.setText("\n Press Start to begin the Quiz.");
		setJMenuBar(getMyMenuBar());
		cancelButton.setEnabled(false);
		startButton.setEnabled(true);
		setVisible(true);

	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		panel.add(startButton);
		panel.add(cancelButton);
		startButton.addActionListener(new StartActionListener());
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel = true;
				aaMessage.setText(" ");
				aaLongName.setText("");
				aaAnswer.setText("");
				aaAnswer.setEditable(false);
				aaInstruction.setText("\n Cancelled - Press Start to begin the Quiz again.");
				cancelButton.setEnabled(false);
				startButton.setEnabled(true);
			}
		});
		startButton.setMnemonic('S');
		cancelButton.setMnemonic('C');
		return panel;
	}

	private class StartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//
			cancel = false;
			startButton.setEnabled(false);
			startTime = System.currentTimeMillis();
			correctAnswers = 0;
			aaInstruction.setText("\n Please enter an amino acid one letter abbreviation and press Enter");
			aaLongName.setText(getQuestion());
			aaAnswer.setEditable(true);
			aaAnswer.setText("");
			aaAnswer.requestFocus();
		}

	}

	private class GiveQuiz implements Runnable {

		public void run() {
			//
			try {

				// just do something long to test Cancel
				int numTimes = 0;
				while (!cancel && numTimes < 100) {
					numTimes++;
					Thread.sleep(30);
				}
				if (!cancel) {
					if (aaAnswer.getText() != null & aaAnswer.getText().length() > 0) {
						if (aaAnswer.getText().toUpperCase().equals(SHORT_NAMES[aaIndex])) {
							aaMessage.setText("Correct!");
							correctAnswers++;
						} else {
							aaMessage.setText("Wrong! " + "Abbreviation for " + FULL_NAMES[aaIndex] + " is "
									+ SHORT_NAMES[aaIndex]);
						}
					}
					currentTime = System.currentTimeMillis();
					// if user has changed nbrSecs in middle of a quiz - use the
					// new value
					// maybe they got tired and wanted to shorten the quiz
					if (currentTime - startTime > nbrSecs * 1000) {
						aaMessage.setText(aaMessage.getText()+ ".   You got " + correctAnswers + " right");
						startButton.setEnabled(true);
						aaLongName.setText("");
						aaInstruction.setText("\n Press Start to play again.");
						aaAnswer.setText("");
						aaAnswer.setEditable(false);
						cancelButton.setEnabled(false);
						startButton.setEnabled(true);
					} else {
						aaLongName.setText(getQuestion());
						aaAnswer.setText("");
						aaInstruction.setText(
								"\n You have " + (nbrSecs - (currentTime - startTime) / 1000) + " seconds left.");
						cancelButton.setEnabled(false);
					}
				}

			} catch

			(

			Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	private String getQuestion() {
		aaIndex = random.nextInt(20);
		return FULL_NAMES[aaIndex];

	}

	private JMenuBar getMyMenuBar() {
		JMenuBar jmenuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		jmenuBar.add(fileMenu);
		JMenuItem openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		openItem.setMnemonic('O');
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadFromFile();
			}
		});
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		saveItem.setMnemonic('S');
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToFile();
			}
		});
		JMenu quizMenu = new JMenu("Quiz");
		quizMenu.setMnemonic('Z');
		jmenuBar.add(quizMenu);
		JMenuItem setNbrSecsItem = new JMenuItem("Set Quiz Number of Seconds");
		quizMenu.add(setNbrSecsItem);
		setNbrSecsItem.setMnemonic('S');
		setNbrSecsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String s = (String) JOptionPane.showInputDialog("How many seconds long should the quiz be? ");

				if ((s != null) && (s.length() > 0)) {
					nbrSecs = Integer.parseInt(s);
					aaGameSecs.setText("Game will run for " + nbrSecs + " seconds");
					return;
				}

			}
		});

		return jmenuBar;

	}

	private void saveToFile() {
		JFileChooser jfc = new JFileChooser();
		if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		if (jfc.getSelectedFile() == null)
			return;
		File chosenFile = jfc.getSelectedFile();
		if (jfc.getSelectedFile().exists()) {
			String message = "File " + jfc.getSelectedFile().getName() + " exists.  Overwrite?";
			if (JOptionPane.showConfirmDialog(this, message) != JOptionPane.YES_OPTION)
				return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(chosenFile));
			writer.write(this.nbrSecs + "\n");
			writer.flush();
			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not write file", JOptionPane.ERROR_MESSAGE);

		}
	}

	private void loadFromFile() {
		JFileChooser jfc = new JFileChooser();
		if (jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		if (jfc.getSelectedFile() == null)
			return;
		File chosenFile = jfc.getSelectedFile();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(chosenFile));
			String line = reader.readLine();

			if (line == null || reader.readLine() != null)
				throw new Exception("Unexpected file format");
			StringTokenizer sToken = new StringTokenizer(line);

			if (sToken.countTokens() != 1)
				throw new Exception("Unexpected file format");

			try {
				this.nbrSecs = Integer.parseInt(sToken.nextToken());

			} catch (Exception ex) {
				throw new Exception("Unexpected file format");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		new AaquizGui(30);

	}
}
