package projeto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Floresta implements Serializable{
	private static final long serialVersionUID=1L;
	private int[] v;
	

// FUNCOES SUGERIDAS	
	
//constrói uma floresta com n nós independentes
	//O(n);
	public Floresta(int n) {
		super();
		if(n >= 0) {
			v = new int[n];
			for(int i = 0; i < v.length; i = i + 1) {
				v[i] = -1;
			}
		} else throw new RuntimeException("Cannot construct forest from negative size");
	}
	
//recebe dois nós e torna m o pai de n
	//O(1)
	public void set_parent(int n, int m) {
		if(n < v.length && m < v.length && n >=0 && m >= 0) {		// n e m pertencem a floresta
			if(v[m] != n) {											// m nao é filho de n
				v[n] = m;											// fazer de m pai de n
			} else throw new RuntimeException("Incest");
		} else throw new RuntimeException("Cannot set parent for index out of bounds");
	}
	
//recebe uma floresta e verifica se essa floresta representa uma árvore
// O(n²*log(n))
	public boolean treeQ() {
		boolean floresta = false;
		int daddy = -1;
		for(int i = 0; i < v.length; i = i+1) {					// verificar que só há uma root (indice cujo pai é -1)
			if(v[i] == -1) {
				if(daddy == -1) {
					floresta = true;
					daddy = i;
				} else floresta = false;
			}
		} if(floresta) {
			return v.length == treeQ(daddy).size();				// verificar se todos os nós estão conectados direta ou indiretamente uns com os outros
		} else return floresta;									// se só há uma root e cada nó só tem um pai e todos os nós estão conectados 
	}															// é direto que não há ciclos
	
	//O(n²*log(n))
	private LinkedList<Integer> treeQ(int daddy) {        		//esta funcao auziliar verifica quantos nos podem ser atingidos a partir da root
		LinkedList<Integer> visited = new LinkedList<Integer>();
		Queue<Integer> sons = new LinkedList<Integer>();
		sons.add(daddy);
		while(!sons.isEmpty()) {
			for (int i = 0; i < v.length; i = i+1) {
				if(v[i] == sons.peek() && !visited.contains(v[i])) {
					sons.add(i);
				}
			}
			visited.add(sons.poll());
		}
		return visited;
	}
	
	
	
// FUNCOES EXTRA	
	
//transforma o nó i na raiz de uma árvore
//O(1)
	public boolean isRoot(int i) {
		return v[i]==-1;
	}
	
//retorna o tamanho da floresta
// O(1)
	public int size() {
		return v.length;
	}

	
	
// GETTERS, SETTERS & TO STRING	
	
//retorna a floresta
//O(1)
	public int[] getForest() {
		return v;
	}
	
	@Override
	public String toString() {
		return "Forest \n" + Arrays.toString(v) ;
	}
}
