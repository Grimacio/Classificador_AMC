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
		Floresta tree = new Floresta(dim);				//floresta com os nós {0,...,dim} (sem arestas)
		double[] adj = new double[dim];					//lista onde se atualiza o peso das arestas dos nós adjacentes
		int[] adj_index = new int[dim];					//o peso adj[x] é o peso entre os nós x em adj e adj_index[x]
		boolean[] visited = new boolean[dim];				//lista onde os nós prontos ser inseridos na árvore estão true
		for(int i = 0; i < dim; i = i+1) {
			adj[i] = Double.MIN_NORMAL;				//atribuir o peso mínimo possivel a todas arestas
			adj_index[i] = -1;					//atribuir que este peso provém da ligação a um nó impossível (-1)
		} 
		adj[dim-1] = 0;							//atribuir peso 0 à raiz da árvore
		visited[dim-1] = true;						//sendo a raiz da árvore está pronto para ir para ela (true)
		int pivot = dim-1;							
		for(int j = 1; j < dim; j = j+1) {
			for(Adj element : graph[pivot]) {			//para todos os nós adjacentes ao pivot
				if(!visited[element.x] && adj[element.x] < element.cost) {		//se o nó não estiver na árvore e puder ter um peso maior da aresta com o novo pivot
				adj[element.x] = element.cost;			//alterar para o novo máximo de peso
				adj_index[element.x] = pivot;			//alterar para que nó forma a nova aresta com máximo peso
				}
			}
			int max_index = max_index(adj,visited);     		//obter o índice para o qual existe um máximo peso
			visited[max_index] = true;				//max_index será o próximo na árvore
			tree.set_parent(max_index, adj_index[max_index]);	//adicionar relação pai e filho relativamente à mais pesada aresta encontrada
			pivot = max_index;					//tornar o novo elemento da árvore no novo pivot
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
		return Arrays.deepToString(graph);
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
