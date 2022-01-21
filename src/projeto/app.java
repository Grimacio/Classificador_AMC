package projeto;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class app {

	private JFrame frame;
	Amostra A;
	Grafos G;
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
		frame.setBounds(100, 100, 455, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textArea = new JTextArea();
		textArea.setBounds(22, 56, 409, 207);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);
		
		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(22, 56, 409, 207);
		//scroll.setSize( 100, 100 );
		frame.getContentPane().add(scroll);
		frame.setVisible (true);
		
		
		JFileChooser fileChooser = new JFileChooser();
		
		JButton btnTouchMe = new JButton("touch me ;)");
		btnTouchMe.setBounds(12, 0, 117, 25);
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
		
		JButton btnBayes = new JButton("Bayes");
		btnBayes.setBounds(181, 0, 117, 25);
		btnBayes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					if (A==null){
						textArea.setText("No sample available");
					}else {
						if(A.length()==0) {
							textArea.setText("Empty Sample");
						}
						else {
							G=new Grafos(A.dataDim());
							textArea.setText(G.toString());	
						}
					}
		
				
			}
		});
		frame.getContentPane().add(btnBayes);
	}
}
