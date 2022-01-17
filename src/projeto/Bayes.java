package projeto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Bayes {
	Floresta tree;
	//Queremos array int[] de LinkedList de LinkedList
	double[][][] tensor;
	
	
	public Bayes( Floresta floresta, Amostra amostra, double s ) {
		super();
		if(!floresta.treeQ()) {
			throw new RuntimeException("This forest is not a tree");
		}
		this.tree=floresta;
		tensor= tensorConstructor(floresta, amostra, s);
		
		
				
	}
	
	public double[][][] tensorConstructor(Floresta floresta, Amostra amostra, double s) {
		double[][][] Tensor = new double[floresta.v.length][][];
		for (int i = 0; i < floresta.v.length; i++) {
			int daddy= floresta.v[i];
			if(floresta.isRoot(i)) {
				Tensor[i]=matrixCondRoot(amostra,i, s);
			}else {
			Tensor[i]= matrixCond(amostra,i,daddy,s);
			}
		}
		return Tensor;
	}
	
	public double[][] matrixCondRoot(Amostra amostra,int root, double s) {
		double[][] newMatrix= new double[1][amostra.domain_var(root)];
		for(int j=0; j<newMatrix[0].length;j++) {
			int[] vars= {root};
			int[] varsValue = {j};
			int intersecao= amostra.count(vars, varsValue);
			newMatrix[0][j] = (intersecao+s)/(amostra.length()+s*amostra.domain_var(root));
			}
		return newMatrix;
	}
	
	public double[][] matrixCond(Amostra amostra,int son, int daddy, double s) {
	
		double[][] newMatrix= new double[amostra.domain_var(daddy)][amostra.domain_var(son)];
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
	
	public double DFO(Amostra amostra, int son, int daddy, int sonValue, int daddyValue, int daddyCount, double s){
		int[] vars= {son, daddy};
		int[] varsValue = {sonValue, daddyValue};
		int intersecao= amostra.count(vars, varsValue);
		return (intersecao+s)/(daddyCount+s*amostra.domain_var(son));
	}
	
	public String prob(int[] vector) {
		int[] vTree= this.tree.v;
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
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Bayes a = new Bayes(null, null, 0.5);
		long startTime = System.nanoTime();
		Floresta floresta = new Floresta(10);
		floresta.set_parent(0, 1);
		floresta.set_parent(2, 1);
		floresta.set_parent(3, 2);
		floresta.set_parent(4, 3);
		floresta.set_parent(5, 2);
		floresta.set_parent(6, 5);
		floresta.set_parent(7, 5);
		floresta.set_parent(8, 7);
		floresta.set_parent(9, 1);
		Amostra amostra = new Amostra("bcancer.csv");
		Bayes rede = new Bayes(floresta, amostra, 0.5);
//		int[][] mat= {{1,2,3},{4,5,6}};
//		int[][] mat2= {{1,2,3,4},{4,5,6,8}};
//		int[] vars= {1};
//		int[] varsValue = {0};
//		System.out.println(Arrays.deepToString(rede.matrixCond(amostra, 5,2,0.5)));
		int[] teste = {0,0,0,0,1,0,0,1,0,0,0};
		System.out.println(rede.prob(teste));
		long endTime = System.nanoTime();
		System.out.println((endTime-startTime)/1000000 + " mili");
		
	
		
	}

}
