package projeto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


class Verify {
	private int value;
	private boolean isIt;
	
	public Verify(int v) {
		super();
		value= v;
		isIt= true;
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

	@Override
	public String toString() {
		return "Verify [value=" + value + ", isIt=" + isIt + "]";
	}
	
}

public class Amostra implements Serializable{
	private static final long serialVersionUID=1L;
	private ArrayList<int []> list;
	private int[][][][] countTensor;
	private int[] domain;
	
	
//constroi uma amostra vazia
	public Amostra() {
		this.list = new ArrayList<int []>();
	}
	
//constroi uma amostra apartir de um ficheiro .csv
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
		build();
	}
	
	public void setCountTensor(int[][][][] countTensor) {
		this.countTensor = countTensor;
	}

	public int[][][][] getCountTensor() {
		return countTensor;
	}

	// O(1)
	public ArrayList<int[]> getList() {
			return list;
		}
	//O(n*m²)
	public void build() {
		countTensor= new int[dataDim()-1][dataDim()][][];
		for (int i = 0; i < dataDim()-1; i++) {
			for (int j = i+1; j < dataDim(); j++) {
				countTensor[i][j]= new int[domain()[i]+1][domain()[j]+1];
			}
		}
		for (int k = 0; k < length(); k++) {
			for (int i = 0; i < dataDim()-1; i++) {
				for (int j = i+1; j < dataDim(); j++) {
					countTensor[i][j][list.get(k)[i]][list.get(k)[j]]+=1;
					countTensor[i][j][list.get(k)[i]][domain()[j]]+=1;
					countTensor[i][j][domain()[i]][list.get(k)[j]]+=1;
				}
			}
		}
	
	}
	
	
	// O(1)
	public void setList(Amostra am) {
		this.list = am.list;
	}
	
	public void readAm(String path) { // iii
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			Amostra amostra = (Amostra) ois.readObject();
			this.setList(amostra);
			ois.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//adiciona um vetor a amostra se tiver as dimensoes certas
	//O(1)
	public void add (int[] v){
		if(list.isEmpty() && v.length!=0) {
			list.add(v);
		} else {
			if(v.length == list.get(0).length) {
				list.add(v);
			} else throw new RuntimeException("Cannot add element for wrong dimensions");
		}	
	}
	
//retorna numero de vetores da amostra
// O(1)
	public int length() {
		return list.size();
	}

// retorna o vetor com indice i da amostra
// O(1)
	public int[] element(int i) {
		if(i>=0 && i<length()) {
			return list.get(i);
		} else throw new RuntimeException("Cannot calculate element for index out of bounds");
	}

// retorna o número de valores que uma variavel pode assumir nesta amostra, assumindo que varia entre 0 e o seu valor maximo 
// O(n)	
	public int domain(int i) {
		if(i>=0 && i<dataDim()) {
			int max = 0;
			for(int[] x : this.list) {
				if(x[i] > max) {
					max = x[i];
				}
			} return max+1;
		} else throw new RuntimeException("Cannot calculate collumn max for index "+i+ " out of bounds");
	}
//O(n*m) da primeira vez que corre, depois é O(1)
	public int[] domain() {
		if(domain==null) {
			int[] res= new int[dataDim()];
			for (int i = 0; i < res.length; i++) {
				res[i]=0;
			}
			for (int i = 0; i < length(); i++) {
				for (int j = 0; j < dataDim(); j++) {
					if(list.get(i)[j]> res[j]) {
						res[j]=list.get(i)[j]; 
					}
				}
			}
			for (int i = 0; i < res.length; i++) {
				res[i]=res[i]+1;
			}
			this.domain=res;
		}
		return domain;
	}
	
	@Override
	public String toString() {
		String s="[";
		if (list.size()>0) s+=Arrays.toString(list.get(0));
		for (int i=1; i<list.size();i++)
			s+=","+Arrays.toString(list.get(i));
		s+="]";
	return s;
	}

//Não usamos o count de todo, preferimos sempre usar o tensor pois evita que tenhamos que fazer counts desnecessários
// retorna o numero de vezes que cada elemento de indice i do vetor v tem o valor de indice i em w
// O(n*m), n= # elementos da amostra, m= # dataDim()
	public int count(int[] v, int[] w) {
		if(v.length==w.length) {
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
		}else throw new RuntimeException("Count: given vectors don't have equal dimensions");
	}

	
//retorna o numero de variaveis que os vetores de uma amostra têm
// O(1)
	public int dataDim() {
		if(list!=null) {
			return this.list.get(0).length;
		}else throw new RuntimeException("Cannot calculate data dimension for not existent sample");
	}
	
//calcula a informaçao mutua entre duas variaveis, recebendo a matriz de contagens das interseçoes e total
	//O(d*s)
	public double mutualInfo(int x, int y) {
		int[][] matrix;
		if(x<y) {
			matrix= countTensor[x][y];
		}else {
			matrix = countTensor[y][x];
		}
		double soma=0;
		double dim= (double) length();
		for (int i = 0; i < matrix.length-1; i++) {
			for (int j = 0; j < matrix[0].length-1; j++) {
				if(j!=matrix[0].length-1 && i!= matrix.length-1) {
					if (matrix[i][j]!=0) {
						double first= (double) matrix[i][j];
						double dimD= dim;
						double second= (double) matrix[i][matrix[0].length-1];
						double third = (double)matrix[matrix.length-1][j];
						soma+= (first/dimD) * Math.log(dimD*(first/(second* third)));
					}
				}
			}
		}
		return soma;
	}
	
	// retorna as variáveis onde existe apenas um único valor ao longo de toda a amostra
	//!!! deve dar para usar o tensor para tornar mais eficiente
	// O(dataDim()*n)
	public boolean[] Alone() {
		Verify[] res = new Verify[dataDim()];
		boolean[] Alone = new boolean[dataDim()];
		for (int i=0; i< res.length; i++) {
			res[i] = new Verify(list.get(0)[i]);
			Alone[i] = true;
		}
		for (int i=1; i<length(); i++) {
			for (int j=0; j< res.length; j++) {
				if(res[j].isAlone()) {
					if(res[j].getValue() != element(i)[j]) {
						res[j].setIsIt(false);
						Alone[j] = false;
					}
				}
			}
		}
		
		return Alone;
	}
	
	public static void main(String[] Args) {
		
		Amostra a= new Amostra("letter.csv");
		System.out.println(Arrays.deepToString(a.getCountTensor()));
	}
}
