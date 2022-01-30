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
import java.util.Iterator;


public class Amostra implements Serializable{
	private static final long serialVersionUID=1L;
	private ArrayList<int []> list;
	private int[][][][] countTensor;
	private int[] domain;
	
//FUNCOES SUGERIDAS	
	
//adiciona um vetor a amostra se tiver as dimensoes certas
	//O(1)
	public void add (int[] v){
		if(list.isEmpty() && v.length!=0) {				//Se a lista for vazia, adicionar 
			list.add(v);
		} else {
			if(v.length == list.get(0).length) {		//Se a lista já contiver um elemento, apenas adicionar se o 
				list.add(v);							//novo elemento tiver as mesmas dimensões do primeiro elemento
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

// retorna o numero de variaveis no que o no i pode assumir
// O(n*m) da primeira vez que é chamado
// O(1) todo o resto das vezes que é chamado
	public int domain(int i) {
		if(domain==null) {
			domain();
		} return domain[i];
	}

//O(n*m)
	private void domain() {
		int[] res= new int[dataDim()];
		for (int i = 0; i < res.length; i++) {			//cria um vetor vazio com dimensao = #variaveis
			res[i]=0;
		}
		for (int i = 0; i < length(); i++) {
			for (int j = 0; j < dataDim(); j++) {		//acrescenta para cada entrada o valor máximo que essa variável assume
				if(list.get(i)[j]> res[j]) {
					res[j]=list.get(i)[j]; 
				}
			}
		}
		for (int i = 0; i < res.length; i++) {
			res[i]=res[i]+1;							//acrescenta 1 a cada valor para contarmos com o valor 0
		}
		this.domain=res;
	}	
	
	
	
//nao usamos o count de todo, preferimos sempre usar o tensor pois evita que tenhamos que fazer counts desnecessarios
// retorna o numero de vezes que cada elemento de indice i do vetor v tem o valor de indice i em w
// O(n*m), n= # elementos da amostra, m= # dataDim()
	
	public int count(int[] v, int[] w) {
		if(v.length==w.length) {
			for (int i=0; i<v.length;i=i+1) {			//se algum dos valores estiver fora do dominio da sua variavel, return 0
				if(w[i]>=domain[v[i]]) {
					return 0;
				}
			}
			if(v.length==1) {
				if(v[0]!=dataDim()-1) {
					return countTensor[v[0]][dataDim()-1][w[0]][domain[dataDim()-1]];
				}else {
					return countTensor[0][v[0]][domain[0]][w[0]];									//no caso de ser um count trivial de 1 ou 2 variáveis
				}
			}																						//podemos fazer uso do countTensor 
			if(v.length==2) {
				if(v[0]<v[1]) {
					return countTensor[v[0]][v[1]][w[0]][w[1]];	
				}else {
					return countTensor[v[1]][v[0]][w[1]][w[0]];							//temos que nos certificar que estamos a procurar na diagonal superior
				}
			} 													
			int contador=0;	
			int i=0;
			while(i<length()) {
				boolean nice=true;									//se forem mais que duas variáveis temos que percorrer
				int j=0;											//a amostra e contar normalmente
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
		
		} else throw new RuntimeException("Count: given vectors don't have equal dimensions");
	}
	
	
	
// CONSTRUTORES
	
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

	
	
// FUNCOES EXTRA
	
//retorna o numero de variaveis dos da amostra
// O(1)
	public int dataDim() {
		if(list!=null) {
			return this.list.get(0).length;
		}else throw new RuntimeException("Cannot calculate data dimension for not existent sample");
	}
	
//calcula a informaÃ§ao mutua entre duas variaveis, recebendo a matriz de contagens das interseÃ§oes e total
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
						double second= (double) matrix[i][matrix[0].length-1];
						double third = (double)matrix[matrix.length-1][j];
						soma+= (first/dim) * Math.log(dim*(first/(second* third)));
					}
				}
			}
		}
		return soma;
	}
	
// retorna as variÃ¡veis onde existe apenas um Ãºnico valor ao longo de toda a amostra
//!!! deve dar para usar o tensor para tornar mais eficiente
//O(dataDim*domain(k)*domain(0))
	public boolean[] Alone() {
		if(dataDim()>1) {	
			boolean[] Alone = new boolean[dataDim()];
			int aux;
			for (int k = 0; k < dataDim(); k++) {
				aux = 0;
				for (int i = 0; i < domain(k) & aux <= 1 ; i++) {
					boolean find = false;
					if (k == 0) {
						for (int j = 0; j < domain(1) & !find; j++) {
							if (countTensor[0][1][i][j] != 0) {
								aux++;
								find = true;
							}
						}
					} else {
						for (int j = 0; j < domain(0) & !find; j++) {
							if (countTensor[0][k][j][i] != 0) {
								aux++;
								find = true;
							}
						}
					}
				}
				Alone[k] = (aux <= 1);
			}
			return Alone;
		} else throw new AssertionError ("Alone: Dumb ass, you're trying to do this with just 1 atribute");
	}
	
//O(n*m²)
	public void build() {
		countTensor= new int[dataDim()-1][dataDim()][][];
		for (int i = 0; i < dataDim()-1; i++) {
			for (int j = i+1; j < dataDim(); j++) {
				countTensor[i][j]= new int[domain(i)+1][domain(j)+1];
			}
		}
		for (int k = 0; k < length(); k++) {
			for (int i = 0; i < dataDim()-1; i++) {
				for (int j = i+1; j < dataDim(); j++) {
					countTensor[i][j][list.get(k)[i]][list.get(k)[j]]+=1;
					countTensor[i][j][list.get(k)[i]][domain(j)]+=1;
					countTensor[i][j][domain(i)][list.get(k)[j]]+=1;
				}
			}
		}
	}

	
	
//SETTERS, GETTERS & TO STRING
	
// O(1)	
	public void setCountTensor(int[][][][] countTensor) {
		this.countTensor = countTensor;
	}
// O(1)
	public int[][][][] getCountTensor() {
		return countTensor;
	}
// O(1)
	public ArrayList<int[]> getList() {
		return list;
	}
	
// O(1)
	public void setList(Amostra am) {
		this.list = am.list;
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
	
	

// READ
	
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
	
	public static void main(String[] Args) {
		Amostra a= new Amostra("bcancer.csv");
		int[] v= {10,0};
		int[] w= {0,0};
		System.out.println(a.count(v, w));
		System.out.println(Arrays.deepToString(a.getCountTensor()));
	}
	
}



class Verify {
	private int value;
	private boolean isIt;
	
	
	
// CONSTRUTOR
	
	public Verify(int v) {
		super();
		value= v;
		isIt= true;
	}
	
	
	
// SETTER, GETTERS & TO STRING
	
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
