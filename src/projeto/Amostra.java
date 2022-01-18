package projeto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Amostra {
	private ArrayList<int []> list;


	public Amostra() {
		this.list = new ArrayList<int []>();
	}
	
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
	
	public void add (int[] v){
		if(list.isEmpty()&& v.length!=0) {
			list.add(v);
		} else {
			if(v.length == list.get(0).length) {
				list.add(v);
			} else throw new RuntimeException("Cannot add element for wrong dimensions");
		}	
	}
	
	public int length() {
		return list.size();
	}

	public int[] element(int i) {
		if(i>=0 && i<length()) {
			return list.get(i);
		} else throw new RuntimeException("Cannot calculate element for index out of bounds");
	}
	
	public int domain(int[] v) {
		if(v.length == 0) {
			throw new RuntimeException("Cannot calculate domain for empty vector");
		}
		int prod = 1;
		LinkedList<Integer> visited = new LinkedList<Integer>();
		for(int i : v) {
			if(visited.contains(i)) {
				throw new RuntimeException("Cannot calculate domain for repeated index");
			} visited.add(i);
			prod = prod * domain_var(i);
		}
		return prod;
	}
	
	public int domain_var(int i) {
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
			
		return " Amostra = " + s;
	}
	
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
	
	public double[][] matrixAux(int son, int daddy) {
		//v= vetor com as variaveis que queremos pesquisar; {filho, pai}
//		LinkedList<LinkedList<LinkedList<Integer>>> newTensor= new LinkedList<LinkedList<LinkedList<Integer>>>();
		double[][] matrix= new double[domain_var(daddy)+1][domain_var(son)+1];
		
		for (int[] element : this.list) {
			matrix[element[daddy]][element[son]]++;
			matrix[element[daddy]][domain_var(son)]++;
			matrix[domain_var(daddy)][element[son]]++;
		}
		
		return matrix;
	}
//	public int count2(int[] v, int[] w) {
//		return matrixAux(v[0], v[1])[w[1]][w[0]];
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Amostra amostra = new Amostra("bcancer.csv");
		System.out.println(amostra);
		
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
		System.out.println(Arrays.deepToString(amostra.matrixAux(0,1)));
		System.out.println(amostra.count(x,z));	
	}
}

