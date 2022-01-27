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

class IHandler{
    public double currentLoad;
    public String currentPrice;
    public String configArgs[];
};

public class app implements Serializable{
	private static final long serialVersionUID=1L;

	private JFrame frame;
	private JTextArea textArea;
	Amostra A;
	Grafos G;
	Bayes R;
	IHandler H = new IHandler();
	
	public static void serializeDataOut(IHandler ish)throws IOException{
	    String fileName= "Test.txt";
	    FileOutputStream fos = new FileOutputStream(fileName);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(ish);
	    oos.close();
	}
	
	
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
		frame.setBounds(100, 100, 685, 419);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFileChooser fileChooser = new JFileChooser();
		
		JButton ChooseSample = new JButton("Choose Sample");
		ChooseSample.setBounds(10, 210, 187, 45);
		ChooseSample.setFont(new Font("Dialog", Font.PLAIN, 12));
		ChooseSample.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				int r = fileChooser.showOpenDialog((Component)e.getSource());
				if (r==fileChooser.APPROVE_OPTION){
					A=new Amostra(fileChooser.getSelectedFile().getAbsolutePath());
					textArea.setText(A.toString().replace("],", "], \n "));	
				}
			}
		});
		
		JButton CreateBayes = new JButton("Create Bayes Arborean Tree");
		CreateBayes.setBounds(10, 258, 187, 45);
		CreateBayes.setFont(new Font("Dialog", Font.PLAIN, 12));
		CreateBayes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (A==null){
					textArea.setText("No sample available");
				}else {
					if(A.length()==0) {
						textArea.setText("Empty Sample");
					}
					else {
						String pseudocontagem = JOptionPane.showInputDialog(frame, "Choose your Pseudocounting");
						double pseudo = Double.parseDouble(pseudocontagem);
						long startTime = System.nanoTime();
						G = new Grafos(A.dataDim());
						G.build(A);
						R = new Bayes(G.max_spanning_tree(), A, pseudo);
						
						textArea.setText(R+" "+pseudocontagem);	
						long endTime = System.nanoTime();
						System.out.println("create Bayes"+(endTime-startTime)/1000000);
					}
				}
			}
			
		});
		
		JFileChooser fileChooser2 = new JFileChooser();
		
		JButton Export = new JButton("Export Data");
		Export.setBounds(10, 305, 187, 45);
		Export.setFont(new Font("Dialog", Font.PLAIN, 12));
		Export.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				FileOutputStream fout;
					int r = fileChooser2.showOpenDialog((Component)e.getSource());
					if (r==fileChooser2.APPROVE_OPTION){
						R.writeBayes(fileChooser2.getSelectedFile().getAbsolutePath()+ ".txt");
					}
			}
		});
		
		frame.getContentPane().add(Export);
		
		frame.getContentPane().add(CreateBayes);
		
		frame.getContentPane().add(ChooseSample);
		
		JScrollPane scroll = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(233, 24, 409, 302);
		//scroll.setSize( 100, 100 );
		frame.getContentPane().add(scroll);
		frame.setVisible (true);
		
		JPanel menu = new JPanel();
		menu.setBackground(new Color(240, 248, 255));
		menu.setBounds(0, 0, 207, 382);
		frame.getContentPane().add(menu);
		menu.setLayout(null);
		
		JLabel Classifier = new JLabel("");
		Classifier.setIcon(new ImageIcon("C:\\Users\\Asus\\Downloads\\tree (1).png"));
		Classifier.setFont(new Font("Dialog", Font.BOLD, 20));
		Classifier.setHorizontalAlignment(SwingConstants.CENTER);
		Classifier.setBounds(40, 15, 120, 174);
		menu.add(Classifier);
		
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
