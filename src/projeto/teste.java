package Classes;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class AmostraIF {

	private JFrame frame;
	Amostra A;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AmostraIF window = new AmostraIF();
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
	public AmostraIF() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textArea = new JTextArea();




		textArea.setBounds(29, 59, 391, 139);
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);

		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(29, 59, 391, 139);
		//scroll.setSize( 100, 100 );
		frame.getContentPane().add(scroll);
		frame.setVisible (true);

		JFileChooser fileChooser = new JFileChooser();
		
		JFileChooser fileChooser2 = new JFileChooser();

		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				int r = fileChooser.showOpenDialog((Component)e.getSource());
				if (r==fileChooser.APPROVE_OPTION){
					A=new Amostra(fileChooser.getSelectedFile().getAbsolutePath());
					textArea.setText(A.toString());					
				}
			}
		});

		btnNewButton.setBounds(19, 18, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		
		
		JButton btnNewButton_1 = new JButton("Save Object");
		btnNewButton_1.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				int r = fileChooser2.showSaveDialog((Component)e.getSource());
				if (r == fileChooser2.APPROVE_OPTION){
					try {
						 
			            FileOutputStream fileOut = new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath());
			            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			            objectOut.writeObject(A);
			            objectOut.close();
			 
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
				}
			}
			
		});
		btnNewButton_1.setBounds(325, 209, 95, 43);
		frame.getContentPane().add(btnNewButton_1);
	}
}