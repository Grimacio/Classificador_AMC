package projeto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

class Adj {
	private int node;
	private double cost;
	
	public Adj(int node, double cost) {
		super();
		this.node = node;
		this.cost = cost;
	}
	//O(1)
	public int getNode() {
		return node;
	}
	//O(1)
	public double getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return "(" + node + " , " + cost + ")";
	}
}

public class Grafos implements Serializable{
	private static final long serialVersionUID=1L;
	private LinkedList<Adj>[] graph;
	private int dim;
	
//constroi um grafo com nos
//O(n), n=#nos
	@SuppressWarnings("unchecked")
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
	
	public LinkedList<Adj>[] getGraph() {
		return graph;
	}

	public int getDim() {
		return dim;
	}

	//adiciona no grafo uma aresta com um certo custo entre dois nós
	//O(1)
	public void add_edge(int x, int y, double cost) {
		if(x < dim && y < dim && x >= 0 && y >= 0) {
			Adj n = new Adj(y,cost);
			graph[x].add(n);
			n = new Adj(x,cost);
			graph[y].add(n);
		} else throw new RuntimeException("Graph: Cannot add edge with dimensions out of bounds");
	}
	
//transforma um grafo numa árvore com o maior peso possível nas suas arestas
//O(n+n³ + n²log(n))
	public Floresta max_spanning_tree() {
		Floresta tree = new Floresta(dim);		//O(n) , n=nos				//floresta com os nos {0,...,dim} (sem arestas)
		double[] adj = new double[dim];			//O(1)			//lista onde se atualiza o peso das arestas dos nos adjacentes
		int[] adj_index = new int[dim];			//O(1)			//o peso adj[x] o o peso entre os nos x em adj e adj_index[x]
		boolean[] visited = new boolean[dim];	//O(1)				//lista onde os nos prontos ser inseridos na arvore estao true
		for(int i = 0; i < dim; i = i+1) {
			adj[i] = -Integer.MAX_VALUE;		//O(n) numero de nos					//atribuir o peso minimo possivel a todas arestas
			adj_index[i] = -1;									//atribuir que este peso provem da ligaçao a um n� imposs�vel (-1)
		} 
		adj[dim-1] = 0;						//O(1)	//atribuir peso 0 � raiz da �rvore
		visited[dim-1] = true;				//O(1)		//sendo a raiz da �rvore est� pronto para ir para ela (true)
		int pivot = dim-1;					//O(1)	
		for(int j = 1; j < dim; j = j+1) {	//O(n)
			
			for(Adj element : graph[pivot]) {	//O(n)													//para todos os n�s adjacentes ao pivot
				if(!visited[element.getNode()] && adj[element.getNode()] < element.getCost()) {		//se o n� n�o estiver na �rvore e puder ter um peso maior da aresta com o novo pivot
				adj[element.getNode()] = element.getCost();								//O(n)			//alterar para o novo m�ximo de peso
				adj_index[element.getNode()] = pivot;												//alterar para que n� forma a nova aresta com m�ximo peso
				}
			}
			int max_index = max_index(adj,visited);		 //O(1) 		//obter o �ndice para o qual existe um m�ximo peso
			visited[max_index] = true;					//O(1)			//max_index ser� o pr�ximo na �rvore
			tree.set_parent(max_index, adj_index[max_index]);	//O(n)	//adicionar rela��o pai e filho relativamente � mais pesada aresta encontrada
			pivot = max_index;							//O(1)				//tornar o novo elemento da �rvore no novo pivot
		}

		if(tree.treeQ()) { // O(n²*log(n)+n)
			return tree;
		} else throw new RuntimeException("Max Tree: Impossible return a tree");
	}

	//O(n)
	private int max_index(double[] adj, boolean[] visited) {
		double max = -Integer.MAX_VALUE;
		int max_index = 0;
		for(int i = 0; i < dim; i = i+1) {
			if(!visited[i] && adj[i] > max) {
				max = adj[i];
				max_index = i;
			}
		}
		return max_index;
	}
	
//a partir de uma amostra adiciona no grafo todas as arestas possíveis
//	O(n³+n²+n)
	public void build(Amostra amostra) {
		boolean[] alone = amostra.Alone();
		for (int i = 0; i < alone.length; i++) {
			if (alone[i] == false) {
				for (int j = i+1; j < alone.length; j++) {
					add_edge(i, j, amostra.mutualInfo(i,j));
				}
			} else {
				for (int j = i+1; j < alone.length; j++) {
					add_edge(i, j, 0);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return dim+" "+ Arrays.deepToString(graph);
	}
}
