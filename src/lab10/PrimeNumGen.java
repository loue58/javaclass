package lab10;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.jcip.annotations.GuardedBy;

// On 4 threads with 4 available processors it took 24 seconds.
// Found 54070 prime numbers in 666666

public class PrimeNumGen extends JFrame {

	private final JTextArea aTextField = new JTextArea();
	private final JButton primeButton = new JButton("Start");
	private final JButton cancelButton = new JButton("Cancel");
	private volatile boolean cancel = false;
	private volatile long lastUpdate = System.currentTimeMillis();
	private final AtomicInteger lastChecked = new AtomicInteger(1);
	private final PrimeNumGen thisFrame;

	private final Object myLock = new Object();
	@GuardedBy(value = "myLock")
	private List<Integer> listAll = new ArrayList<Integer>();

	public static void main(String[] args) {

		PrimeNumGen png = new PrimeNumGen("Primer Number Generator");

		// don't add the action listener from the constructor
		png.addActionListeners();
		png.setVisible(true);

	}

	private PrimeNumGen(String title) {
		super(title);
		this.thisFrame = this;
		cancelButton.setEnabled(false);
		aTextField.setEditable(false);
		setSize(400, 400);
		setLocationRelativeTo(null);
		// kill java VM on exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(primeButton, BorderLayout.SOUTH);
		getContentPane().add(cancelButton, BorderLayout.EAST);
		getContentPane().add(new JScrollPane(aTextField), BorderLayout.CENTER);
		//

	}

	private class CancelOption implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			cancel = true;
		}
	}

	private void addActionListeners() {
		cancelButton.addActionListener(new CancelOption());

		primeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String num = JOptionPane.showInputDialog("Enter First Number");
				Integer max = null;

				try {
					max = Integer.parseInt(num);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(thisFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}

				if (max != null) {
					aTextField.setText("");
					primeButton.setEnabled(false);
					cancelButton.setEnabled(true);
					cancel = false;
					lastChecked.set(1);
					synchronized (myLock) {
						listAll.clear();
					}
					new Thread(new UserInput(max)).start();

				}
			}
		});
	}

	private boolean isPrime(int i) {
		for (int x = 2; x < i - 1; x++)
			if (i % x == 0)
				return false;

		return true;
	}

	private class UserInput implements Runnable {
		private final int max;

		private UserInput(int num) {
			this.max = num;
		}

		public void run() {
			long startTime = System.currentTimeMillis();
			int nbrThreads = Runtime.getRuntime().availableProcessors();
			CountDownLatch cdl = new CountDownLatch(nbrThreads);
			for (int i = 1; i <= nbrThreads; i++) {
				new Thread(new PrimeThread(max, cdl)).start();
			}
			try {
				cdl.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final StringBuffer buff = new StringBuffer();
			/*
			 * stack confined and single thread here but still synchronized for
			 * documentation
			 */
			synchronized (myLock) {
				Collections.sort(listAll);
				for (Integer i2 : listAll)
					buff.append(i2 + "\n");
			}
			if (cancel)
				buff.append("cancelled");
			long currentTime = System.currentTimeMillis();
			System.out.println("On " + nbrThreads + " threads with " + Runtime.getRuntime().availableProcessors()
					+ " available processors it took " + (currentTime - startTime) / 1000 + " seconds.");
			System.out.println("Found " + listAll.size() + " prime numbers in " + max);

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					cancel = false;
					primeButton.setEnabled(true);
					cancelButton.setEnabled(false);
					aTextField.setText((cancel ? "cancelled " : "") + buff.toString());
				}
			});

		}// end run

	} // end UserInput

	private class PrimeThread implements Runnable {
		private final int max;
		private final CountDownLatch cdl;

		private PrimeThread(int num, CountDownLatch cdl) {
			this.max = num;
			this.cdl = cdl;
		}

		public void run() {

			List<Integer> list = new ArrayList<Integer>();
			int i = 0;
			while (i < max && !cancel) {
				i = lastChecked.getAndIncrement();
				if (i <= max) {
					if (isPrime(i)) {
						list.add(i);
						// lastUdpdate is volatile but not synchronized
						// Worst thing that could happen is you update
						// more frequently than 1/2 second
						if (System.currentTimeMillis() - lastUpdate > 500) {
							synchronized (myLock) {
								listAll.addAll(list);
							}

							list.clear();

							final String outString = "Found " + listAll.size() + " in " + i + " of " + max;

							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									aTextField.setText(outString);
								}
							});
							lastUpdate = System.currentTimeMillis();
						}
					}
				}
			}
			// add the last ones
			synchronized (myLock) {
				listAll.addAll(list);
			}
			cdl.countDown();
		}// end run

	} // end PrimeThread

}
