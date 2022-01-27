package projeto;

public class Classificador {
	Amostra A;
	Grafos G;
	Bayes R;
	
	public Classificador(Amostra a) {
		this.A=a;
		this.G = new Grafos(1);
	}
	public Classificador() {
		this.R= new Bayes();
		this.G = new Grafos(1);
	}
	
	@Override
	public String toString() {
		return "Classificador [A=" + A + ", G=" + G + ", R=" + R + "]";
	}
	public Grafos graph() {
		Grafos g= new Grafos(A.dataDim());
		g.build(A);
		System.out.println(g);
		this.G = g;
		return this.G;
	}
	
	public Bayes bayes(double s) {
		if(R==null) {
			Bayes r = new Bayes(G.max_spanning_tree(), A, s);
			R= r;
		}
		return R;
	}
	
	public Amostra getA() {
		return A;
	}

	public void setA(Amostra a) {
		A = a;
	}

	public Grafos getG() {
		return G;
	}

	public Bayes getR() {
		return R;
	}

	public void setG(Grafos g) {
		G = g;
	}

	public void setR(Bayes r) {
		R = r;
	}
	
	

	public String classify(int[] vector) {
		double max = 0;
		int max_index = 0;
		double[] prob = new double[R.dimClass()];
		int i = 0;
		while (i < (R.dimClass())) {
			vector[vector.length-1]=i;
			double max_aux = R.prob(vector);
			prob[i] = max_aux;
			if (max_aux > max) {
				max=max_aux;
				max_index=i;
			}
			i=i+1;
		}
		return "result = "+max_index+", with probabilty of "+(max*100)+"%";
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Classificador C= new Classificador(new Amostra("bcancer.csv"));
		C.graph();
		System.out.println(C.G);
		
	}
}
	


