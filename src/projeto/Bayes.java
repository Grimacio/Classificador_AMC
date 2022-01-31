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
	

// FUNCOES SUGERIDAS

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
	
//cria e retorna um tensor atraves de uma floresta, uma amostra e uma pseudo-contagem	
//O(nds)
	private double[][][] tensorConstructor(Floresta floresta, Amostra amostra, double s) {
		
		double[][][] Tensor = new double[floresta.size()][][];			//inicializamos o tensor como um array de dimensao igual ao #nos que contém matrizes
		for (int i = 0; i < floresta.size(); i=i+1) {					//para cada no
			int daddy= floresta.getForest()[i];
			if(floresta.isRoot(i)) {									//excecao se se trata da root (nao tem pai)
				Tensor[i]=matrixCondRoot(amostra,i, s); //O(r*n)
			}else {														//acrescentamos a matriz criada com as DFO's ao tensor no indice correspondente
				Tensor[i]= matrixCond(amostra,i,daddy,s); //O(ds)
			}
		}
		return Tensor;
	}

//retorna a matriz que terá as DFO's entre os valores que a root apresenta e o numero total de elementos da amostra
	//O(r*n)
	private double[][] matrixCondRoot(Amostra amostra,int root, double s) {
		double[][] newMatrix= new double[1][amostra.domain(root)];
		for(int j=0; j<newMatrix[0].length;j=j+1) {
			double intersecao;
			int[] countVars= {root};												//aplica-se a formula das DFO para a root
			int[] countVal= {j};
			intersecao = (double) amostra.count(countVars, countVal);
			newMatrix[0][j] = (intersecao+s)/(amostra.length()+s*amostra.domain(root));
		}
	return newMatrix;
	}

//retorna a matriz que terá as DFO's entre os valores dos nos son e daddy, para uma pseudo-contagem s
	//O(d*s)
	private double[][] matrixCond(Amostra amostra,int son, int daddy, double s) {
		double[][] newMatrix= new double[amostra.domain(daddy)][amostra.domain(son)];	//inicializar a matriz com as dimensoes necessarias
		for(int i=0;i<newMatrix.length;i=i+1) {											//para cada valor de daddy
			int daddyCount;																//realizar a contagem
			int[] countVars= {daddy};													//esta contagem e reutilizada para os diversos valores do son
			int[] countVal= {i};
			daddyCount=amostra.count(countVars, countVal);
			for(int j=0; j<newMatrix[0].length;j=j+1) {									//para cada filho, a entrada correspondente na matriz a acrescentar ao tensor
				newMatrix[i][j]=DFO(amostra, son, daddy, j , i, daddyCount, s);			//sera o resultado da formula da DFO entre daddy=i e son=j
			}
		}
		return newMatrix;
	}
//retorna o valor de DFO para cada par son e daddy, para valores sonValue e daddyValue e para uma certa contagem total daddyCount calculada previamente
//O(1)
	private double DFO(Amostra amostra, int son, int daddy, int sonValue, int daddyValue, int daddyCount, double s){
		double intersecao;
		int[] countVars= {daddy,son};
		int[] countVal= {daddyValue, sonValue};
		intersecao= amostra.count(countVars, countVal);									//calcular a contagem de interseção
		return (intersecao+s)/(daddyCount+s*amostra.domain(son));						//aplicar a formula
	}
	

//O(n);
	public double prob(int[] vector, double s) {
		int[] vTree= this.tree.getForest();
		double prob=1;				//elemento neutro da multiplicacao
		int i = 0;
while (i < vTree.length-1) { 												//para cada no da arvore
		int daddy = vTree[i];												//ha um pai
		if(daddy!=-1) {														//no caso de o no em questao nao ser a root:
				if (vector[i] < tensor[i][0].length) {						//o valor dessa variavel no vetor a analisar tem que pertencer ao dominio dessa variavel
					int babyVal= vector[i]; 								
					if (vector[daddy] < tensor[i].length) {					//o mesmo para o valor da variavel do no pai
						int daddyVal = vector[daddy];
						prob=prob* tensor[i][daddyVal][babyVal];			//multiplicar a probabilidade pela entrada correspondente do tensor
					} else {
						prob=prob*(s/(s*tensor[i][0].length));				//no caso do valor da variavel daddy estar fora do dominio, aplicar a formula DFO com a intersecao=contagemDeDaddy=0		
					}
				} else {													//se o valor do filho estiver fora do dominio, aplicar a DFO
					if (vector[daddy] < tensor[i].length) {						//considerando a intersecao=0
						prob=prob *(s/(tensor[i][vector[daddy]][tensor[i][0].length-1]+s*tensor[i][0].length));
					} else {													//considerar intersecao=contagemDeDaddy=0
						prob=prob*(s/(s*tensor[i][0].length));
					}
				}
				i=i+1;
			}
		}
		prob=prob* tensor[i][0][vector[i]];									//multiplicar pela entrada do tensor da root para aquele valor!
		return prob;														//All done :)
	}
	
	
	
// FUNCOES EXTRA
		
	//O(1)
	public int dimClass() {
		return tensor[tensor.length-1][0].length;
	}

	

// SETTERS, GETTERS & TO STRING
	
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
	
	@Override
	public String toString() {
		return tree + "\nTensor \n" + Arrays.deepToString(tensor) + "]";
	}
	
	
// READ & WRITE
	
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
	
}
