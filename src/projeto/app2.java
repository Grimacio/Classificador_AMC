package projeto;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class app2 {

	private JFrame frame;
	Bayes R = new Bayes();
	private JTextArea textArea;
	double pseudoGrande=0;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					app2 window = new app2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public app2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(new Color(0, 128, 128));
		frame.setBounds(100, 100, 461, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textArea = new JTextArea();
		textArea.setBounds(10, 85, 369, 185);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);
		
		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(31, 67, 388, 215);
		//scroll.setSize( 100, 100 );
		frame.getContentPane().add(scroll);
		frame.setVisible (true);
		
		JFileChooser fileChooserUSED = new JFileChooser();
		
		JButton btnNewButton = new JButton("Choose Bayes");
		btnNewButton.setBounds(63, 26, 99, 31);
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
					int r = fileChooserUSED.showOpenDialog((Component)e.getSource());
					if (r==fileChooserUSED.APPROVE_OPTION) {
						R.readBayes(fileChooserUSED.getSelectedFile().getAbsolutePath());
						textArea.setText(Arrays.deepToString(R.getTensor()).replace("],", "], \n "));
					}
			}
		});
		
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Classify");
		btnNewButton_1.setBounds(198, 26, 90, 31);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int [] vector = new int[R.getTree().size()];
				for (int i = 0; i < vector.length-1; i++) {
					String variable = JOptionPane.showInputDialog(frame, "Choose your "+(i+1)+" variable");
					int var = Integer.parseInt(variable);
					vector[i] = var;
				}
				long startTime = System.nanoTime();
				double max = 0;
				int max_index = 0;
				double[] prob = new double[R.dimClass()];
				int i = 0;
				//O(r*n)
				while (i < (R.dimClass())) {
					vector[vector.length-1]=i;
					System.out.println("---"+R.prob(vector));
					double max_aux = R.prob(vector);
					prob[i] = max_aux;
					if (max_aux > max) {
						max=max_aux;
						max_index=i;
					}
					i=i+1;
				}
				textArea.setText("result = "+max_index+", with probabilty of "+(max*100)+"%");
				long endTime = System.nanoTime();
				System.out.println("create Bayes"+(endTime-startTime)/1000000);
			}
		});
		
		frame.getContentPane().add(btnNewButton_1);
		
		

	}
}
