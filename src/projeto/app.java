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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JTextPane;
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
	Classificador C= new Classificador();
	String path;
	long amostraTime=0, bayesTime=0;
	long startTime=0;
	long endTime=0;
	double s=0;
	
	
	
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
		textArea.setFont(new Font("Dialog", Font.PLAIN, 15));
		textArea.setBackground(new Color(230, 230, 250));
		textArea.setBounds(233, 26, 409, 302);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);
		frame.setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 871, 611);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFileChooser fileChooser = new JFileChooser();
		
		JButton Export = new JButton("Export Data");
		Export.setBounds(10, 305, 187, 45);
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
		CreateBayes.setBounds(10, 258, 187, 45);
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
						textArea.setText(C.R+" "+pseudo+ ", "+ "["+bayesTime+"ms], TOTAL= " +(bayesTime+amostraTime) + "ms");
						
						CreateBayes.setText("Bayes Created ("+pseudo+ ")");
						Export.setEnabled(true);
						
						
						
					}
				}
			}
			
		});
		
		
		
		
		JButton ChooseSample = new JButton("Choose Sample");
		ChooseSample.setBounds(10, 210, 187, 45);
		ChooseSample.setFont(new Font("Dialog", Font.PLAIN, 12));
		ChooseSample.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				int r = fileChooser.showOpenDialog((Component)e.getSource());
				if (r==fileChooser.APPROVE_OPTION){
					path=fileChooser.getSelectedFile().getAbsolutePath();
					startTime=System.nanoTime();
					C.A=new Amostra(fileChooser.getSelectedFile().getAbsolutePath());
					endTime=System.nanoTime();
					amostraTime= (endTime-startTime)/1000000;
					ChooseSample.setText("Amostra Escolhida");
					ChooseSample.setEnabled(false);
					CreateBayes.setEnabled(true);
					textArea.setText(C.A.toString().replace("],", "], \n ")+ "["+amostraTime+"ms]");	
				}
			}
		});
		
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(112, 354, 83, 16);
		btnReset.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnReset.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
					C=new Classificador();
					ChooseSample.setEnabled(true);
					ChooseSample.setText("Choose Sample");
					CreateBayes.setEnabled(false);
					ChooseSample.setText("Create Bayes Arborean Tree");
					Export.setEnabled(false);
					textArea.setText("");	
					CreateBayes.setText("Create Bayes Arborean Tree");
				
			}
		});
		
		
		frame.getContentPane().add(Export);
		
		frame.getContentPane().add(CreateBayes);
		
		frame.getContentPane().add(ChooseSample);
		
		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(233, 24, 618, 543);
		//scroll.setSize( 100, 100 );
		frame.getContentPane().add(scroll);
		frame.setVisible (true);
		
		JPanel menu = new JPanel();
		menu.setBackground(new Color(240, 248, 255));
		menu.setBounds(0, 0, 207, 382);
		frame.getContentPane().add(menu);
		menu.setLayout(null);
		
		JLabel Classifier = new JLabel("");
		Classifier.setIcon(new ImageIcon(getClass().getResource("/tree_1.png")));
		
		
		Classifier.setFont(new Font("Dialog", Font.BOLD, 20));
		Classifier.setHorizontalAlignment(SwingConstants.CENTER);
		Classifier.setBounds(40, 15, 120, 174);
		menu.add(Classifier);
		
		menu.add(btnReset);
		
		JLabel lblNewLabel = new JLabel("Developers: Beatriz Vidal, Dinis Pereira, Guilherme Gaspar & Margarida Cordeiro\r\n");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel.setBounds(233, 342, 425, 13);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\Asus\\Downloads\\Webp.net-resizeimage (1).png"));
		lblNewLabel_1.setBounds(597, 332, 45, 45);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Algorithms and Computer Modeling at Técnico");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_2.setBounds(233, 355, 425, 13);
		frame.getContentPane().add(lblNewLabel_2);
	}
}
