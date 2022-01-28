package projeto;

import java.util.Arrays;

public class Classificador {
	Amostra A;
	Grafos G;
	Bayes R;
	double s;
	
	public Classificador(Amostra a, double s) {
		this.A=a;
		this.G = new Grafos(1);
		this.R = new Bayes();
		s=this.s;
	}
	public Classificador() {
		this.s=0;
		this.A= new Amostra();
		this.G = new Grafos(1);
		this.R = new Bayes();
	}
	
	@Override
	public String toString() {
		return "Classificador [A=" + A + ", G=" + G + ", R=" + R + "]";
	}
	public Grafos graph() {
		Grafos g= new Grafos(A.dataDim());
		g.build(A);
		this.G = g;
		return this.G;
	}
	
	public Bayes bayes(double s) {
		Bayes r = new Bayes(G.max_spanning_tree(), A, s);
		R= r;
		return R;
	}
	
	public double[] classify(int[] vector) {
		double max = 0;
		int max_index = 0;
		double[] prob = new double[R.dimClass()];
		int i = 0;
		while (i < (R.dimClass())) {
			vector[vector.length-1]=i;
			double max_aux = R.prob(vector, s);
			prob[i] = max_aux;
			if (max_aux > max) {
				max=max_aux;
				max_index=i;
			}
			i=i+1;
		}
		double[]res = {max_index, max*100};
		return res;
	}
	
	public boolean leaveOneOut(int[] vector) {
		Amostra am= new Amostra();
		boolean found=false;
		for(int[] element :A.getList()) {
			if(found || element!=vector) {
				am.add(element);
			}else {
				if(vector==element) {
					found=true;
				}
			}
		}
		Classificador Cl = new Classificador(am,0);
		Cl.graph();
		Cl.bayes(0.5);
		int indiceReal= vector[(vector.length-1)];
		int indiceGuess= (int) Cl.classify(vector)[0];
		return indiceGuess == indiceReal;
	}
	
	public double leaveOneOut() {
		double counter=0;
		for(int[] v : A.getList()) {
			if(leaveOneOut(v)) {
				counter++;
			}
		}
		return (counter/A.length())*100;
	}
	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Classificador C= new Classificador(new Amostra("diabetes.csv"));
//		System.out.println(C.leaveOneOut());
//		
//		
//	}
}
	


