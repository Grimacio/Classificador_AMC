package projeto;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
//they/them
public class Bayes implements Serializable{
	private static final long serialVersionUID=1L;
	private Floresta tree;
	private double[][][] tensor;

	public Bayes() {
		super();
		tree = null;
	}

//cria uma rede de Bayes através de uma floresta, uma amostra e uma pseudo-contagem
//O(n²(log(n)+ds) +n(ds²+r))
	public Bayes(Floresta floresta, Amostra amostra, double s) {
		super();
		this.tree=floresta;
		this.tensor= tensorConstructor(floresta, amostra, s);
	}
	
//cria uma rede de Bayes através de uma floresta, uma amostra e uma pseudo-contagem	
//O(nds)
	public double[][][] tensorConstructor(Floresta floresta, Amostra amostra, double s) {
		
		double[][][] Tensor = new double[floresta.size()][][];
		for (int i = 0; i < floresta.size(); i++) {
			int daddy= floresta.getForest()[i];
			if(floresta.isRoot(i)) {
				Tensor[i]=matrixCondRoot(amostra,i, s); //O(r*n)
			}else {
				Tensor[i]= matrixCond(amostra,i,daddy,s); //O(ds)
			}
		}
		return Tensor;
	}
	//O(1)
	public double[][][] getTensor() {
		return tensor;
	}
	//O(1)
	public void setTree(Floresta tree) {
		this.tree = tree;
	}
	//O(1)
	public void setTensor(double[][][] tensor) {
		this.tensor = tensor;
	}
	//O(1)
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
	
	public void writeBayes(String path) { 
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(this);
			oos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//O(r*n)
	private double[][] matrixCondRoot(Amostra amostra,int root, double s) {
		double[][] newMatrix= new double[1][amostra.domain()[root]];
		for(int j=0; j<newMatrix[0].length;j++) {
//			int[] vars= {root};
//			int[] varsValue = {j};
			double intersecao;//= amostra.count(vars, varsValue);
			intersecao = (double) amostra.getCountTensor()[0][root][amostra.domain()[0]][j];
			newMatrix[0][j] = (intersecao+s)/(amostra.length()+s*amostra.domain()[root]);
		}
	return newMatrix;
	}
	
	//O(d*s)
	private double[][] matrixCond(Amostra amostra,int son, int daddy, double s) {
		//System.out.println(daddy+" new daddy ");
		double[][] newMatrix= new double[amostra.domain()[daddy]][amostra.domain()[son]];
		for(int i=0;i<newMatrix.length;i++) {
			int daddyCount;
			if(daddy==0) {
				daddyCount= amostra.getCountTensor()[daddy][1][i][amostra.domain()[1]];
			}else {
				daddyCount = amostra.getCountTensor()[0][daddy][amostra.domain()[0]][i];
			}
			for(int j=0; j<newMatrix[0].length;j++) {
				newMatrix[i][j]=DFO(amostra, son, daddy, j , i, daddyCount, s);
			}
		}
		return newMatrix;
	}
//O(1)
	private double DFO(Amostra amostra, int son, int daddy, int sonValue, int daddyValue, int daddyCount, double s){
		double intersecao;
		if(son<daddy) {
			intersecao= (double) amostra.getCountTensor()[son][daddy][sonValue][daddyValue];
		}else {
			intersecao= (double) amostra.getCountTensor()[daddy][son][daddyValue][sonValue];
		}
		return (intersecao+s)/(daddyCount+s*amostra.domain()[son]);
	}
	
	
// WHAT IS THIS 
//O(n);
	public double prob(int[] vector, double s) {
		int[] vTree= this.tree.getForest();
		double prob=1;
		int i = 0;
		while (i < vTree.length-1) { //n
			int daddy = vTree[i];
			if(daddy!=-1) {
				if (vector[i] < tensor[i][0].length) {
					int babyVal= vector[i]; 
					if (vector[daddy] < tensor[i].length) {
						int daddyVal = vector[daddy];
						prob*=tensor[i][daddyVal][babyVal];
					} else {
						prob*=s/(s*tensor[i][0].length);
					}
				} else {
					if (vector[daddy] < tensor[i].length) {
						prob*=s/(tensor[i][vector[daddy]][tensor[i][0].length-1]+s*tensor[i][0].length);
					} else {
						prob*=s/(s*tensor[i][0].length);
					}
				}
				i++;
			}
		}
		prob*= tensor[i][0][vector[i]];
		return prob;
	}
	//O(1)
	public int dimClass() {
		return tensor[tensor.length-1][0].length;
	}

	@Override
	public String toString() {
		return tree + "\nTensor \n" + Arrays.deepToString(tensor) + "]";
	}
	
	public static void main(String[] Args) {
		Amostra A= new Amostra("bcancer.csv");
		Grafos G = new Grafos(A.dataDim());
		G.build(A);
		Bayes B = new Bayes(G.max_spanning_tree(), A, 0.5);
		System.out.println(Arrays.deepToString(B.getTensor()));
	}
	
}
