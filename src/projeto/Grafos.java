package projeto;

import java.util.Arrays;
import java.util.LinkedList;

class Adj {
	int x;
	double cost;
	
	public Adj(int x, double cost) {
		super();
		this.x = x;
		this.cost = cost;
	}
	
	public int x() {
		return this.x;
	}

	@Override
	public String toString() {
		return "(" + x + " , " + cost + ")";
	}
}

public class Grafos {
	LinkedList<Adj>[] graph;
	int dim;
	
	public Grafos(int n) {
		super();
		if(n >= 0) {
			this.dim = n;
			this.graph = new LinkedList[n];
			for(int i = 0; i < n; i = i+1) {
				graph[i] = new LinkedList<>();
			}
		} else throw new RuntimeException("Graph: Cannot create graph with negative dimensions");
	}
	
	public void add_edge(int x, int y, double cost) {
		if(x < dim && y < dim && x >= 0 && y >= 0) {
			Adj n = new Adj(y,cost);
			graph[x].add(n);
			n = new Adj(x,cost);
			graph[y].add(n);
		} else throw new RuntimeException("Graph: Cannot add edge with dimensions out of bounds");
	}
	
	public Floresta max_spanning_tree() {
		Floresta tree = new Floresta(dim);				//floresta com os n�s {0,...,dim} (sem arestas)
		double[] adj = new double[dim];					//lista onde se atualiza o peso das arestas dos n�s adjacentes
		int[] adj_index = new int[dim];					//o peso adj[x] � o peso entre os n�s x em adj e adj_index[x]
		boolean[] visited = new boolean[dim];				//lista onde os n�s prontos ser inseridos na �rvore est�o true
		for(int i = 0; i < dim; i = i+1) {
			adj[i] = Double.MIN_NORMAL;				//atribuir o peso m�nimo possivel a todas arestas
			adj_index[i] = -1;					//atribuir que este peso prov�m da liga��o a um n� imposs�vel (-1)
		} 
		adj[dim-1] = 0;							//atribuir peso 0 � raiz da �rvore
		visited[dim-1] = true;						//sendo a raiz da �rvore est� pronto para ir para ela (true)
		int pivot = dim-1;							
		for(int j = 1; j < dim; j = j+1) {
			for(Adj element : graph[pivot]) {			//para todos os n�s adjacentes ao pivot
				if(!visited[element.x] && adj[element.x] < element.cost) {		//se o n� n�o estiver na �rvore e puder ter um peso maior da aresta com o novo pivot
				adj[element.x] = element.cost;			//alterar para o novo m�ximo de peso
				adj_index[element.x] = pivot;			//alterar para que n� forma a nova aresta com m�ximo peso
				}
			}
			int max_index = max_index(adj,visited);     		//obter o �ndice para o qual existe um m�ximo peso
			visited[max_index] = true;				//max_index ser� o pr�ximo na �rvore
			tree.set_parent(max_index, adj_index[max_index]);	//adicionar rela��o pai e filho relativamente � mais pesada aresta encontrada
			pivot = max_index;					//tornar o novo elemento da �rvore no novo pivot
		}
		if(tree.treeQ()) {
			return tree;
		} else throw new RuntimeException("Max Tree: Impossible return a tree");
	}

	private int max_index(double[] adj, boolean[] visited) {
		double max = Double.MIN_NORMAL;
		int max_index = 0;
		for(int i = 0; i < dim; i = i+1) {
			if(!visited[i] && adj[i] > max) {
				max = adj[i];
				max_index = i;
			}
		}
		return max_index;
	}
	
	
	@Override
	public String toString() {
		return dim+" "+ Arrays.deepToString(graph);
	}
 
	public static void main(String[] args) {
		Grafos m = new Grafos(5);
		int[][] edges = {{0,1,2},{0,3,6},{1,3,8},{1,2,3},{1,4,5},{2,4,7},{3,4,9}};
		for(int[] x : edges)
			m.add_edge(x[0],x[1],x[2]); 
		System.out.println(m);
		System.out.println(m.max_spanning_tree());
	}
}
