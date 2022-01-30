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
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class app2 {

	private JFrame frame;
	Classificador C= new Classificador();
	private JTextArea textArea;
	long startTime=0;
	long endTime=0;
	long time;
	String path;


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
		frame.setBounds(100, 100, 597, 358);
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
		scroll.setBounds(31, 67, 480, 215);
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
						C.R.readBayes(fileChooserUSED.getSelectedFile().getAbsolutePath());
						textArea.setText(Arrays.deepToString(C.R.getTensor()).replace("],", "], \n "));
					}
			}
		});
		
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Classify");
		btnNewButton_1.setBounds(198, 26, 90, 31);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int [] vector = new int[C.R.getTree().size()];
				for (int i = 0; i < vector.length-1; i++) {
					String variable = JOptionPane.showInputDialog(frame, "Choose your "+(i+1)+" variable");
					int var = Integer.parseInt(variable);
					vector[i] = var;
				}
				
//				double max = 0;
//				int max_index = 0;
//				double[] prob = new double[R.dimClass()];
//				int i = 0;
//				//O(r*n)
//				while (i < (R.dimClass())) {
//					vector[vector.length-1]=i;
//					System.out.println("---"+R.prob(vector));
//					double max_aux = R.prob(vector);
//					prob[i] = max_aux;
//					if (max_aux > max) {
//						max=max_aux;
//						max_index=i;
//					}
//					i=i+1;
//				}
				System.out.println(Arrays.toString(vector));
				startTime = System.nanoTime();
				double[] res= C.classify(vector);
				endTime = System.nanoTime();
				int indice = (int) res[0];
				String resultado="Resultado= "+indice+" com probabilidade "+ res[1]+"%";
				endTime = System.nanoTime();
				time= (endTime-startTime);
				textArea.setText(resultado + "\n------> "+ time + " nanossegundos");
				
			}
		});
		
		frame.getContentPane().add(btnNewButton_1);
		JFileChooser fileChooser1= new JFileChooser();
		JButton btnLeaveoneout = new JButton("LeaveOneOut");
		btnLeaveoneout.setBounds(362, 289, 149, 25);
		btnLeaveoneout.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> ficheiros= new ArrayList<String>();
				boolean done=false;
				while(!done) {
					String path_temp="";
					int r = fileChooser1.showOpenDialog((Component)e.getSource());
					if (r==fileChooser1.APPROVE_OPTION){
						
						path_temp=fileChooser1.getSelectedFile().getAbsolutePath();
						
					}
					done=(path_temp=="");
					if(!done) {
						String s;
						s=path_temp.substring(path_temp.lastIndexOf("/") + 1, path_temp.indexOf(".csv")).concat(".csv");
						ficheiros.add(s);
					}
				}
					String LOOres="";
		
					long startTime= System.nanoTime();
					for(String ficheiro : ficheiros) {
						long startTime2= System.nanoTime();
						Classificador ClTemp= new Classificador(new Amostra(ficheiro),0.5);
						ClTemp.graph();
						ClTemp.bayes(0.5);
						float res=ClTemp.leaveOneOut();
						long endTime2= System.nanoTime();
						LOOres=LOOres+ "\n"+ ficheiro.replace(".csv", "").replace("large","l")+ " \t Precisao:"+res+"% \t ("+((endTime2-startTime2)/1000000)+"ms)";
						
					}
					long endTime= System.nanoTime();
					double tempoLOO=((endTime-startTime)/1000000);
					textArea.setText(LOOres+"\n\ntempo total: "+ tempoLOO +" ms");
					
			}
		});
		frame.getContentPane().add(btnLeaveoneout);
		
		

	}
}
