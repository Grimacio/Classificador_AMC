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
	
	
	

// retorna o numero de vezes que cada elemento de indice i do vetor v tem o valor de indice i em w
//O(1) para 2 ou menos variáveis, O(n*dataDim()) para mais variáveis
	
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
			if(v.length==2) {																		//O(1)
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
				int j=0;											//a amostra e contar normalmente com complexidade maior
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
	
//calcula a informacao mutua entre duas variaveis, usando a matriz de contagens das intersecoes vindas do countTensor e os valores totais
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
		for (int i = 0; i < matrix.length-1; i++) {										//vamos andar pelos valores da variável menor (linhas)
			for (int j = 0; j < matrix[0].length-1; j++) {								//vamos andar pelos valores da variável maior (colunas)
				if(j!=matrix[0].length-1 && i!= matrix.length-1) {					
					if (matrix[i][j]!=0) {												//sabendo que se a interseção for 0 nao incrementamos o somatorio
						double first= (double) matrix[i][j];							//aplica-se a formula da informacao mutua
						double second= (double) matrix[i][matrix[0].length-1];
						double third = (double)matrix[matrix.length-1][j];				
						soma+= (first/dim) * Math.log(dim*(first/(second* third)));		//incrementa-se a soma com o resultado
					}
				}
			}
		}
		return soma;
	}
	
// retorna as variaveis onde existe apenas um unico valor ao longo de toda a amostra
//O(dataDim*domain(k)*domain(0))
	public boolean[] Alone() {
		if(dataDim()>1) {	
			boolean[] Alone = new boolean[dataDim()];
			int aux;
			for (int k = 0; k < dataDim(); k++) {							//para cada variavel da amostra
				aux = 0;													//variavel que guarda quantos valores diferentes de 0 a variavel k toma
				for (int i = 0; i < domain(k) & aux <= 1 ; i++) {			//para cada valor do seu dominio
					boolean find = false;									
					if (k == 0) {
						for (int j = 0; j < domain(1) & !find; j++) {		//excecao no caso de ser a primeira variável da amostra
							if (countTensor[0][1][i][j] != 0) {				
								aux++;
								find = true;
							}
						}
					} else {
						for (int j = 0; j < domain(0) & !find; j++) {		//ver na primeira linha, coluna k
							if (countTensor[0][k][j][i] != 0) {				//se algum dos valores de k tiver contagem !=0 para valores da variável 0, consideramos que
								aux++;										//k assume aux+1 valores e movemo-nos para o próximo valor de k
								find = true;
							}
						}
					}
				}
				Alone[k] = (aux <= 1);										//se no fim, a variável k tiver mais que 1 valor (aux>1), a sua entrada no vetor Alone[] é false;
			}
			return Alone;
		} else throw new AssertionError ("Alone: Impossible to do this for Sample with only 1 atribute");
	}
	
//O(n*m²)
	public void build() {
		countTensor= new int[dataDim()-1][dataDim()][][];
		for (int i = 0; i < dataDim()-1; i++) {									//inicializar as matrizes interiores do tensor com dimensão [domain(i)+1] x [domain(j)+1]
			for (int j = i+1; j < dataDim(); j++) {								//para alem dos valores de intersecao, queremos que a ultima linha e ultima coluna sejam os 
				countTensor[i][j]= new int[domain(i)+1][domain(j)+1];			//os valores totais de contagem de cada valor de cada variavel dessa matriz
			}		
		}																		//!!!So inicializamos matrizes da diagonal superior, as outras seriam redundantes
		for (int k = 0; k < length(); k++) {									//para cada datum
			for (int i = 0; i < dataDim()-1; i++) {								//para cada variavel
				for (int j = i+1; j < dataDim(); j++) {							//para cada outra variavel maior que a primeira (diagonal superior)
					countTensor[i][j][list.get(k)[i]][list.get(k)[j]]+=1;		//incrementamos a contagem correspondente da intersecao
					countTensor[i][j][list.get(k)[i]][domain(j)]+=1;			//incrementamos a contagem total daquele valor daquela variavel
					countTensor[i][j][domain(i)][list.get(k)[j]]+=1;			//incrementamos a contagem total daquele valor da outra variavel
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
}




