package projeto;


//Apenas uma forma de juntar todas as funcoes que as interfaces graficas usam de modo a estarem melhor organizadas
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
	
	public int run(int[] vector) {
		graph();
		bayes(0.5);	
		return (int) classify(vector)[0];
	}
	
	public double[] classify(int[] vector) {
		int aux =vector[vector.length-1];
		double max = 0;
		int max_index = 0;
		int i = 0;
		while (i < (R.dimClass())) {
			vector[vector.length-1]=i;
			double max_aux = R.prob(vector, s);
			if (max_aux > max) {
				max=max_aux;
				max_index=i;
			}
			i=i+1;
		}
		vector[vector.length-1]=aux;
		double[]res = {max_index, max*100};
		return res;
	}
	//bruh foi so trocar para o tensor e ja deu
	public float leaveOneOut() {
		float contador=0;
		Amostra a= new Amostra();
		int[][][][] backup = A.getCountTensor();
		a.setCountTensor(backup);
		a.setList(A);
		for(int k=0; k<A.length(); k=k+1) {
			int[] nextEl= A.element(k);
			for(int i=0; i<A.dataDim()-1; i=i+1) {
				for(int j=i+1; j<A.dataDim(); j=j+1) {
					a.getCountTensor()[i][j][nextEl[i]][nextEl[j]]-=1;
					a.getCountTensor()[i][j][nextEl[i]][A.domain(j)]-=1;
					a.getCountTensor()[i][j][A.domain(i)][nextEl[j]]-=1;
				}
			}
			Classificador Cl = new Classificador(a,0.5);
			if(Cl.run(nextEl)==nextEl[nextEl.length-1]) {
				contador=contador+1;
			}
			
			for(int i=0; i<A.dataDim()-1; i=i+1) {
				for(int j=i+1; j<A.dataDim(); j=j+1) {
					a.getCountTensor()[i][j][nextEl[i]][nextEl[j]]+=1;
					a.getCountTensor()[i][j][nextEl[i]][A.domain(j)]+=1;
					a.getCountTensor()[i][j][A.domain(i)][nextEl[j]]+=1;
				}
			}
		}

		return (contador/A.length())*100;
	}
}
