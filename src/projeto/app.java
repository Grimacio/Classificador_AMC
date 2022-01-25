package projeto;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JTextPane;


public class app implements Serializable{
	private static final long serialVersionUID=1L;

	private JFrame frame;
	Amostra A;
	Grafos G;
	Bayes R;
	private JTextArea textArea;
	
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
		frame.setBounds(100, 100, 461, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textArea = new JTextArea();
		textArea.setBounds(1, 1, 369, 185);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);
		
		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(29, 56, 388, 204);
		//scroll.setSize( 100, 100 );
		frame.getContentPane().add(scroll);
		frame.setVisible (true);
		
		
		JFileChooser fileChooser = new JFileChooser();
		
		JButton btnTouchMe = new JButton("Select Sample");
		btnTouchMe.setBounds(32, 21, 117, 25);
		btnTouchMe.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				int r = fileChooser.showOpenDialog((Component)e.getSource());
				if (r==fileChooser.APPROVE_OPTION){
					A=new Amostra(fileChooser.getSelectedFile().getAbsolutePath());
					textArea.setText(A.toString().replace("],", "], \n "));					
				}
			}
		});
		frame.getContentPane().add(btnTouchMe);
		
		JButton btnBayes = new JButton("Create Bayes");
		btnBayes.setBounds(159, 21, 117, 25);
		btnBayes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if (A==null){
						textArea.setText("No sample available");
					}else {
						if(A.length()==0) {
							textArea.setText("Empty Sample");
						}
						else {
							String pseudocontagem = JOptionPane.showInputDialog(frame, "Choose your Pseudocounting");
							float pseudo = Float.parseFloat(pseudocontagem);
							G = new Grafos(A.dataDim());
							G.build(A);
							R = new Bayes(G.max_spanning_tree(), A, pseudo);
							
							
							textArea.setText(R+" "+pseudocontagem);	
						}
					}
		
				
			}
		});
		frame.getContentPane().add(btnBayes);
		
		JFileChooser fileChooser2 = new JFileChooser();
		
		JButton btnExport = new JButton("Export");
		btnExport.setBounds(286, 23, 117, 25);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileOutputStream fout;
				
				try {
					fout = new FileOutputStream
							(fileChooser2.getSelectedFile().getAbsolutePath() + "/A", true);
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(A);
					oos.close();
					fout.close();
				
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    
			}
		});
		
		frame.getContentPane().add(btnExport);
	}
}
