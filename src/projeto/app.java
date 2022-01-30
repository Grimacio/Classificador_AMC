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
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;

public class app implements Serializable{
	private static final long serialVersionUID=1L;

	private JFrame frame;
	private JTextArea textArea;
	private JTextArea textArea_1;
	Classificador C= new Classificador();
	String path;
	long amostraTime=0, bayesTime=0;
	long startTime=0;
	long endTime=0;
	double s=0;
	boolean show = false;
	boolean sample = false;
	boolean bayes = false;
	String text;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					app window = new app();
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
	public app() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {	
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
		frame.setTitle("Convertor: Sample to Bayes Arborean Tree");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Asus\\Downloads\\Picture1.png"));
		frame.getContentPane().setBackground(new Color(230, 230, 250));
		frame.getContentPane().setLayout(null);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Dialog", Font.PLAIN, 15));
		textArea.setBackground(new Color(230, 230, 250));
		textArea.setBounds(233, 26, 409, 302);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBackground(new Color(240, 248, 255));
		textArea_1.setEditable(false);
		textArea_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		textArea_1.setBounds(50, 377, 145, 16);
		
		frame.setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 220, 442);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFileChooser fileChooser = new JFileChooser();
		
		JButton Export = new JButton("Export Data");
		Export.setBounds(10, 300, 187, 45);
		Export.setFont(new Font("Dialog", Font.PLAIN, 12));
		Export.setEnabled(false);
		Export.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				C.R.writeBayes(path.replace(".csv", ".txt"));
				Export.setText("Export to Another Directory");
				textArea.setText("Good to go! \n" +path.replace(".csv", ".txt"));
			}
		});
		
		JButton CreateBayes = new JButton("Create Bayes Arborean Tree");
		CreateBayes.setBounds(10, 253, 187, 45);
		CreateBayes.setFont(new Font("Dialog", Font.PLAIN, 12));
		CreateBayes.setEnabled(false);
		CreateBayes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (C.A==null){
					textArea.setText("No sample available");
				}else {
					if(C.A.length()==0) {
						textArea.setText("Empty Sample");
					}
					else {
						String pseudocontagem = JOptionPane.showInputDialog(frame, "Choose your Pseudocounting");
						startTime=System.nanoTime();
						double pseudo = Double.parseDouble(pseudocontagem);
						C.s=pseudo;
						C.graph();
						C.bayes(pseudo);
						endTime=System.nanoTime();
						bayesTime= (endTime-startTime)/1000000;
						textArea_1.setText((amostraTime+bayesTime)+" ms"); 
						bayes = true;
						sample = false;
						if (show) {
							textArea.setText(C.R.toString());
						}
						CreateBayes.setText("Bayes Created ("+pseudo+ ")");
						Export.setEnabled(true);
					}
				}
			}
			
		});
		
		
		
		
		JButton ChooseSample = new JButton("Choose Sample");
		ChooseSample.setBounds(10, 205, 187, 45);
		ChooseSample.setFont(new Font("Dialog", Font.PLAIN, 12));
		ChooseSample.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				int r = fileChooser.showOpenDialog((Component)e.getSource());
				if (r==fileChooser.APPROVE_OPTION){
					path=fileChooser.getSelectedFile().getAbsolutePath();
					startTime=System.nanoTime();
					C.A=new Amostra(path);
					endTime=System.nanoTime();
					ChooseSample.setText("Amostra Escolhida");
					ChooseSample.setEnabled(false);
					CreateBayes.setEnabled(true);
					amostraTime= (endTime-startTime)/1000000;
					textArea_1.setText(amostraTime+" ms");
					sample = true;
					if(show) {
						textArea.setText(C.A.toString().replace("],", "], \n "));
					}				
				}
			}
		});
		
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(112, 350, 83, 16);
		btnReset.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnReset.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
					C=new Classificador();
					ChooseSample.setEnabled(true);
					ChooseSample.setText("Choose Sample");
					CreateBayes.setEnabled(false);
					CreateBayes.setText("Create Bayes Arborean Tree");
					Export.setEnabled(false);
					textArea.setText("");	
					CreateBayes.setText("Create Bayes Arborean Tree");
					sample = false;
					bayes = false;
					text = "";
					textArea_1.setText(0+" ms");
				
			}
		});
		
		
		frame.getContentPane().add(Export);
		
		frame.getContentPane().add(CreateBayes);
		
		frame.getContentPane().add(ChooseSample);
		
		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(229, 24, 376, 330);
		//scroll.setSize( 100, 100 );
		frame.getContentPane().add(scroll);
		frame.setVisible (true);
		
		JPanel menu = new JPanel();
		menu.setBackground(new Color(240, 248, 255));
		menu.setBounds(0, 0, 207, 405);
		frame.getContentPane().add(menu);
		menu.setLayout(null);
		
		JLabel Classifier = new JLabel("");
		Classifier.setIcon(new ImageIcon("C:\\Users\\Asus\\Downloads\\tree (1).png"));
		
		Classifier.setFont(new Font("Dialog", Font.BOLD, 20));
		Classifier.setHorizontalAlignment(SwingConstants.CENTER);
		Classifier.setBounds(40, 10, 120, 174);
		menu.add(Classifier);
		
		menu.add(btnReset);
		
		JButton Show = new JButton("Show");
		Show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (show) {
					frame.setBounds(100, 100, 220, 442);
					show = false;
					Show.setText("Show");
				} else {
					frame.setBounds(100, 100, 640, 442);
					show = true;
					textArea.setText("");
					Show.setText("Hide");
					
					if(sample) {
						textArea.setText(C.A.toString().replace("],", "], \n "));
					}
					if (bayes) {
						textArea.setText(C.R.toString());
					}
				}
			}
		});
		
		Show.setFont(new Font("Dialog", Font.PLAIN, 12));
		Show.setBounds(10, 184, 83, 16);
		menu.add(Show);
		
		menu.add(textArea_1);
		
		JLabel lblNewLabel_3_1 = new JLabel("Timer:\r\n");
		lblNewLabel_3_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_3_1.setBounds(10, 379, 45, 13);
		menu.add(lblNewLabel_3_1);
		
		JLabel lblNewLabel = new JLabel("Developers: Beatriz Vidal, Dinis Pereira, Guilherme Gaspar & Margarida Cordeiro\r\n");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel.setBounds(217, 365, 425, 13);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Asus\\Downloads\\Webp.net-resizeimage (1).png"));
		lblNewLabel_1.setBounds(565, 355, 45, 45);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Algorithms and Computer Modeling at Técnico");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_2.setBounds(217, 377, 425, 13);
		frame.getContentPane().add(lblNewLabel_2);
	}
}
