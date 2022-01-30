package projeto;

import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JTextField;

public class app2 implements Serializable{
	private static final long serialVersionUID=1L;

	private JFrame frmClassifier;
	private JTextArea textArea;
	Classificador C= new Classificador();
	String path;
	long classifyTime=0, bayesTime=0;
	long startTime=0;
	long endTime=0;
	double s=0;
	boolean show = false;
	boolean classify = false;
	boolean bayes = false;
	String text;
	String url = Paths.get("").toAbsolutePath().toString();
	
	
	/**
	 * Launch the app2lication.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					app2 window = new app2();
					window.frmClassifier.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the app2lication.
	 */
	public app2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {	
		frmClassifier = new JFrame();
		frmClassifier.setResizable(false);
		frmClassifier.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
		frmClassifier.setTitle("Classifier");
		frmClassifier.setIconImage(Toolkit.getDefaultToolkit().getImage(url+"/Picture1.png"));
		frmClassifier.getContentPane().setBackground(new Color(230, 230, 250));
		frmClassifier.getContentPane().setLayout(null);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Dialog", Font.PLAIN, 15));
		textArea.setBackground(new Color(240, 255, 255));
		textArea.setBounds(233, 26, 409, 302);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		frmClassifier.getContentPane().add(textArea);
		textArea.setColumns(10);
		
		@SuppressWarnings("serial")
		JTextField textArea_Class = new JTextField() {
			@Override public void setBorder(Border border) {
			}
		};
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBackground(new Color(240, 248, 255));
		textArea_1.setEditable(false);
		textArea_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		textArea_1.setBounds(50, 314, 145, 16);
		
		frmClassifier.setBackground(new Color(255, 255, 255));
		frmClassifier.setBounds(100, 100, 220, 376);
		frmClassifier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton Classify = new JButton("Classify\r\n");
		Classify.setBounds(10, 238, 187, 45);
		Classify.setFont(new Font("Dialog", Font.PLAIN, 12));
		Classify.setEnabled(false);
		Classify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int [] vector = new int[C.R.getTree().size()];
				for (int i = 0; i < vector.length-1; i++) {
					String variable = JOptionPane.showInputDialog(frmClassifier, "Choose your "+(i+1)+" variable");
					int var = Integer.parseInt(variable);
					vector[i] = var;
				}
				startTime = System.nanoTime();
				double[] res= C.classify(vector);
				endTime = System.nanoTime();
				int indice = (int) res[0];
				endTime = System.nanoTime();
				classifyTime = ((endTime-startTime)/1000000);
				classify = true;
				textArea_Class.setText(""+indice);
				textArea_1.setText((bayesTime+classifyTime)+" ms");
				textArea.setText("Result = "+indice+" with probability of "+ res[1]+"%");
			}
		});
		
		
		JFileChooser fileChooserUSED = new JFileChooser();
		fileChooserUSED.setAcceptAllFileFilterUsed(false);
		fileChooserUSED.addChoosableFileFilter(new FileNameExtensionFilter("text File (.txt)","txt"));
		
		JButton ChooseBayes = new JButton("Choose Bayes Arborean Tree");
		ChooseBayes.setBounds(10, 190, 187, 45);
		ChooseBayes.setFont(new Font("Dialog", Font.PLAIN, 12));
		ChooseBayes.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
					int r = fileChooserUSED.showOpenDialog((Component)e.getSource());
					if (r==fileChooserUSED.APPROVE_OPTION) {
						startTime=System.nanoTime();
						C.R.readBayes(fileChooserUSED.getSelectedFile().getAbsolutePath());
						endTime=System.nanoTime();
						ChooseBayes.setText("Bayes Chosen");
						bayesTime= (endTime-startTime)/1000000;
						textArea_1.setText(bayesTime+" ms");
						bayes = true;
						ChooseBayes.setEnabled(false);
						Classify.setEnabled(true);
						if (show) {
							textArea.setText(Arrays.deepToString(C.R.getTensor()).replace("],", "], \n "));
						}
				}
		}
	});
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(114, 289, 83, 16);
		btnReset.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnReset.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
					C=new Classificador();
					ChooseBayes.setEnabled(true);
					ChooseBayes.setText("Choose Bayes");
					Classify.setEnabled(false);
					textArea.setText("");	
					bayes = false;
					classify = false;
					text = "";
					textArea_1.setText(0+" ms");
				
			}
		});
		
		frmClassifier.getContentPane().add(Classify);
		
		frmClassifier.getContentPane().add(ChooseBayes);
		
		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(224, 16, 381, 249);
		//scroll.setSize( 100, 100 );
		frmClassifier.getContentPane().add(scroll);
		frmClassifier.setVisible (true);
		
		JPanel menu = new JPanel();
		menu.setBackground(new Color(240, 248, 255));
		menu.setBounds(0, 0, 207, 342);
		frmClassifier.getContentPane().add(menu);
		menu.setLayout(null);
		
		JLabel Classifier = new JLabel("");
		Classifier.setIcon(new ImageIcon(url+"/lupa.png"));
		
		Classifier.setFont(new Font("Dialog", Font.BOLD, 20));
		Classifier.setHorizontalAlignment(SwingConstants.CENTER);
		Classifier.setBounds(40, 0, 120, 174);
		menu.add(Classifier);
		
		menu.add(btnReset);
		
		JButton Show = new JButton("Show");
		Show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (show) {
					frmClassifier.setBounds(100, 100, 220, 376);
					show = false;
					Show.setText("Show");
				} else {
					frmClassifier.setBounds(100, 100, 638, 376);
					show = true;
					Show.setText("Hide");
					if(bayes && !classify) {
						textArea.setText(Arrays.deepToString(C.R.getTensor()).replace("],", "], \n "));
					}
				}
			}
		});
		
		Show.setFont(new Font("Dialog", Font.PLAIN, 12));
		Show.setBounds(10, 169, 83, 16);
		menu.add(Show);
		
		menu.add(textArea_1);
		
		JLabel lblNewLabel_3_1 = new JLabel("Timer:\r\n");
		lblNewLabel_3_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_3_1.setBounds(10, 315, 45, 13);
		menu.add(lblNewLabel_3_1);
		
		textArea_Class.setFont(new Font("Dialog", Font.BOLD, 40));
		textArea_Class.setEditable(false);
		textArea_Class.setHorizontalAlignment(SwingConstants.CENTER);
		textArea_Class.setBackground(new Color(240, 248, 255));
		textArea_Class.setBounds(70, 54, 51, 43);
		menu.add(textArea_Class);
		
		JLabel lblNewLabel = new JLabel("Developers: Beatriz Vidal, Dinis Pereira, Guilherme Gaspar & Margarida Cordeiro\r\n");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel.setBounds(217, 304, 425, 13);
		frmClassifier.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setIcon(new ImageIcon(url+"/Webp.net-resizeimage_1.png"));
		lblNewLabel_1.setBounds(565, 294, 45, 45);
		frmClassifier.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Algorithms and Computer Modeling at Técnico");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_2.setBounds(217, 316, 425, 13);
		frmClassifier.getContentPane().add(lblNewLabel_2);
		
		JButton btnAccuracy = new JButton("Accuracy");
		JFileChooser fileChooser1= new JFileChooser();
		fileChooser1.setAcceptAllFileFilterUsed(false);
		fileChooser1.addChoosableFileFilter(new FileNameExtensionFilter("Comma Separated Values (.csv)","csv"));
		btnAccuracy.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAccuracy.setBounds(516, 269, 94, 25);
		btnAccuracy.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> ficheiros= new ArrayList<String>();
				boolean done=false;
				int dataNumber=0;
				int dimNumber=0;
				int domainNumber=0;
				double tempoLOO=0;
				while(!done) {
					String path_temp="";
					int r = fileChooser1.showOpenDialog((Component)e.getSource());
					if (r==fileChooser1.APPROVE_OPTION){
						
						path_temp=fileChooser1.getSelectedFile().getAbsolutePath();
						
					}
					done=(path_temp=="");
					if(!done) {
						String s;
						s=path_temp.replace("\\","/");
						s=s.substring(s.lastIndexOf("/") + 1, s.indexOf(".csv")).concat(".csv");
						ficheiros.add(s);
					}
				}
					String LOOres="";
		
		
					for(String ficheiro : ficheiros) {
						long startTime2= System.nanoTime();
						Classificador ClTemp= new Classificador(new Amostra(ficheiro),0.5);
						ClTemp.graph();
						ClTemp.bayes(0.5);
						float res=ClTemp.leaveOneOut();
						long endTime2= System.nanoTime();
						dataNumber=dataNumber+ClTemp.A.length();
						dimNumber= dimNumber + ClTemp.A.dataDim();
						int soma=0;
						for(int i=0;i<ClTemp.A.dataDim(); i=i+1) {
							soma=soma+ClTemp.A.domain(i);
						}
						tempoLOO+=((endTime2-startTime2)/1000000);
						domainNumber= domainNumber+soma/ClTemp.A.dataDim();
						LOOres=LOOres+ "\n"+ ficheiro.replace(".csv", "").replace("large","l")+ " \t Accuracy:"+res+"% \t ("+((endTime2-startTime2)/1000000)+"ms)";
						
					}
					textArea_1.setText(tempoLOO +" ms");
					textArea.setText(LOOres+"\n\n"+ dataNumber + " analyzed vectors \nAverage of "+(dimNumber/ficheiros.size())+" variables per datum set \nAverage of " +(domainNumber/ficheiros.size())+" values per variable");
					
			}
		});
		frmClassifier.getContentPane().add(btnAccuracy);
	}
}
