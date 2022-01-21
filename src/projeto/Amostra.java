package projeto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


class Verify {
	int value;
	boolean isIt;
	
	public Verify(int v) {
		super();
		value= v;
		isIt= false;
	}
	
	public boolean isAlone() {
		return isIt;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setIsIt(boolean isIt) {
		this.isIt = isIt;
	}
	
	
}

public class Amostra {
	private ArrayList<int []> list;

 //construtor vazio
	public Amostra() {
		this.list = new ArrayList<int []>();
	}
	
// construtor com ficheiro .csv
	public Amostra(String csvFile) {
		this.list = new ArrayList<int []>();;

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] country     = line.split(cvsSplitBy);
				int[] stringToIntVec = new int[country.length];
				for (int i = 0; i < country.length; i++)
					stringToIntVec[i] = Integer.parseInt(country[i]);	
				add(stringToIntVec);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
// adiciona vetor a amostra se tiver as dimensoes certas
	public void add (int[] v){
		if(list.isEmpty()&& v.length!=0) {
			list.add(v);
		} else {
			if(v.length == list.get(0).length) {
				list.add(v);
			} else throw new RuntimeException("Cannot add element for wrong dimensions");
		}	
	}
	
//retorna numero de vetores da amostra
	public int length() {
		return list.size();
	}

// retorna o vetor com indice i da amostra
	public int[] element(int i) {
		if(i>=0 && i<length()) {
			return list.get(i);
		} else throw new RuntimeException("Cannot calculate element for index out of bounds");
	}
// retorna o num de valores que uma variavel pode assumir nesta amostra, assumindo que varia entre 0 e o seu valor maximo 
	public int domain(int i) {
		if(i>=0 && i<length()) {
			int max = 0;
			for(int[] x : this.list) {
				if(x[i] > max) {
					max = x[i];
				}
			} return max+1;
		} else throw new RuntimeException("Cannot calculate collumn max for index "+i+ " out of bounds");
	}
	
	@Override
	public String toString() {
		String s="[";
		if (list.size()>0) s+=Arrays.toString(list.get(0));
		for (int i=1; i<list.size();i++)
			s+=","+Arrays.toString(list.get(i));
		s+="]";
			
	return " Amostra\n" + s;
		
	}
//retorna o numero de vezes que cada elemento de indice i do vetor v tem o valor de indice i em w
	public int count(int[] v, int[] w) {
		int contador=0;	
		int i=0;
		while(i<length()) {
			boolean nice=true;
			int j=0;
			while(j<v.length && nice) {
				if(list.get(i)[v[j]]!= w[j]) {
					nice=false;
				}
				j++;
			}
			if(nice) {
				contador++;
			}
			i++;
			
		}
		return contador;
		
	}
// recebe duas variaveis e retorna a matriz cujas linhas sao os valores de uma variavel e as colunas os valores da outra, cada entrada ij
// é a contagem de ocorrencias em que a variavel daddy é i e son é j. A matriz tem uma linha e coluna extras que são o total de ocorrências 
// em que daddy é i (coluna) e son é j (linha)
	public double[][] matrixAux(int son, int daddy) {
		double[][] matrix= new double[domain(daddy)+1][domain(son)+1];
		for (int[] element : this.list) {
			matrix[element[daddy]][element[son]]++;
			matrix[element[daddy]][domain(son)]++;
			matrix[domain(daddy)][element[son]]++;
		}
		
		return matrix;
	}
	
//retorna o numero de variaveis que os vetores de uma amostra têm
	public int dataDim() {
		if(list!=null) {
			return this.list.get(0).length;
		}else return 0;
	}
	
	
//calcula a informaçao mutua entre duas variaveis, recebendo a matriz de contagens das interseçoes e total
	public double mutualInfo(double[][] matrix) {
		double soma=0;
		double dim= length();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if(j!=matrix[0].length-1 && i!= matrix.length-1) {
					soma+=(matrix[i][j]/dim) * Math.log(dim*(matrix[i][j]/(matrix[i][matrix[0].length-1]* matrix[matrix.length-1][j])));
				}
			}
		}
		return soma;
	}
	
//	public Verify[] maria(Amostra a) {
//		Verify[] res = new Verify[dataDim()];
//		for (int i=0; i< res.length; i++) {
//			res[i].setValue(a.list.get(0)[i]);
//		}
//		for (int i=1; i<length(); i++) {
//			
//		}
//	}

	
	public static void main(String[] args) {
		Amostra amostra = new Amostra("bcancer.csv");
//		int[] u= {1,2,3,4,5,6,7,8,9,10,11};
//		amostra.add(u);
//		int[] a= {0,0,1,2};
//		int[] b= {1,0,1,1};
//		int[] c= {0,0,1,0};
//		amostra.add(a);
//		amostra.add(b);
//		amostra.add(c);
//		System.out.println(amostra);
		int[] v= {0,1,2,3,4,5,6,7,8,9,10};
		int[] w= {0,0,1,0,1,0,1,2,2,2,1};
		int[] x= {0,1};
		int[] z= {0,1};
//		int[] a= {5};
//		long startTime = System.nanoTime();
//		System.out.println(amostra.count(v,w));
//		long endTime = System.nanoTime();
//		System.out.println(endTime-startTime);
//		long startTime2=System.nanoTime();
//		System.out.println(amostra.count_mau(v,w));
//		long endTime2 = System.nanoTime();
//		System.out.println(endTime2-startTime2);
		System.out.println(Arrays.deepToString(amostra.matrixAux(1,1)));
		System.out.println(amostra.mutualInfo(amostra.matrixAux(1,1)));
//		System.out.println(amostra.count(x,z));	
	}
}

