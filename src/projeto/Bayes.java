package projeto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class Bayes implements Serializable{
	private static final long serialVersionUID=1L;
	private Floresta tree;
	private double[][][] tensor;

//cria uma rede de Bayes através de uma floresta, uma amostra e uma pseudo-contagem
	public Bayes(Floresta floresta, Amostra amostra, double s) {
		super();
		if(!floresta.treeQ()) {
			throw new RuntimeException("This forest is not a tree");
		}
		this.tree=floresta;
		tensor= tensorConstructor(floresta, amostra, s);		
	}
	
//cria uma rede de Bayes através de uma floresta, uma amostra e uma pseudo-contagem	
	public double[][][] tensorConstructor(Floresta floresta, Amostra amostra, double s) {
		double[][][] Tensor = new double[floresta.size()][][];
		for (int i = 0; i < floresta.size(); i++) {
			int daddy= floresta.getForest()[i];
			if(floresta.isRoot(i)) {
				Tensor[i]=matrixCondRoot(amostra,i, s);
			}else {
			Tensor[i]= matrixCond(amostra,i,daddy,s);
			}
		}
		return Tensor;
	}
	
	public double[][][] getTensor() {
		return tensor;
	}
	
	public void setTree(Floresta tree) {
		this.tree = tree;
	}

	public void setTensor(double[][][] tensor) {
		this.tensor = tensor;
	}
	
	public Floresta getTree() {
		return tree;
	}

	public void readBayes(String path) { // iii
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			Bayes rede = (Bayes) ois.readObject();
			setTensor(rede.getTensor());
			setTree(rede.getTree());
			ois.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeBayes(String path) { // ii
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(this);
			oos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double[][] matrixCondRoot(Amostra amostra,int root, double s) {
		double[][] newMatrix= new double[1][amostra.domain(root)];
		for(int j=0; j<newMatrix[0].length;j++) {
			int[] vars= {root};
			int[] varsValue = {j};
			int intersecao= amostra.count(vars, varsValue);
			newMatrix[0][j] = (intersecao+s)/(amostra.length()+s*amostra.domain(root));
			}
		return newMatrix;
	}
	
	private double[][] matrixCond(Amostra amostra,int son, int daddy, double s) {
	
		double[][] newMatrix= new double[amostra.domain(daddy)][amostra.domain(son)];
		for(int i=0;i<newMatrix.length;i++) {
			int[] vars= {daddy};
			int[] varsValue = {i};
			int daddyCount = amostra.count(vars, varsValue);
			for(int j=0; j<newMatrix[0].length;j++) {
				
				newMatrix[i][j]=DFO(amostra, son, daddy, i , j, daddyCount, s);
			}
		}
		return newMatrix;
	}
	
	private double DFO(Amostra amostra, int son, int daddy, int sonValue, int daddyValue, int daddyCount, double s){
		int[] vars= {son, daddy};
		int[] varsValue = {sonValue, daddyValue};
		int intersecao= amostra.count(vars, varsValue);
		return (intersecao+s)/(daddyCount+s*amostra.domain(son));
	}
	
	
// WHAT IS THIS
	public String prob(int[] vector) {
		int[] vTree= this.tree.getForest();
		double prob=1;
		for (int i = 0; i < vTree.length; i++) {
			int daddy = vTree[i];
			if(daddy!=-1) {
				int babyVal= vector[i];
				int daddyVal = vector[daddy];
				prob*=tensor[i][daddyVal][babyVal];
			}
		}
		return (prob*100+"%");
	}

	@Override
	public String toString() {
		return tree + "\nTensor \n" + Arrays.deepToString(tensor) + "]";
	}
}
