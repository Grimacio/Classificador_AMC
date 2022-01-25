package projeto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Floresta implements Serializable{
	private static final long serialVersionUID=1L;
	private int[] v;
	
//constrói uma floresta com n nós independentes
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
	public void set_parent(int n, int m) {
		if(n < v.length && m < v.length && n >=0 && m >= 0) {
			if(v[m] != n) {
				v[n] = m;
			} else throw new RuntimeException("Incest");
		} else throw new RuntimeException("Cannot set parent for index out of bounds");
	}
	
//recebe uma floresta e verifica se essa floresta representa uma árvore
	public boolean treeQ() {
		boolean floresta = false;
		int daddy = -1;
		for(int i = 0; i < v.length; i = i+1) {
			if(v[i] == -1) {
				if(daddy == -1) {
					floresta = true;
					daddy = i;
				} else floresta = false;
			}
		} if(floresta) {
			return v.length == treeQ(daddy).size();
		} else return floresta;
	}
	
	private LinkedList<Integer> treeQ(int daddy) {
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
	
//transforma o nó i na raiz de uma árvore
	public boolean isRoot(int i) {
		return v[i]==-1;
	}
	
//retorna o tamanho da floresta
	public int size() {
		return v.length;
	}
	
//retorna a floresta
	public int[] getForest() {
		return v;
	}
	
	@Override
	public String toString() {
		return "Floresta \n" + Arrays.toString(v) ;
	}
}
