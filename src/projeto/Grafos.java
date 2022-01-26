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
	
	public int getNode() {
		return node;
	}
	
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
	public void add_edge(int x, int y, double cost) {
		if(x < dim && y < dim && x >= 0 && y >= 0) {
			Adj n = new Adj(y,cost);
			graph[x].add(n);
			n = new Adj(x,cost);
			graph[y].add(n);
		} else throw new RuntimeException("Graph: Cannot add edge with dimensions out of bounds");
	}
	
//transforma um grafo numa árvore com o maior peso possível nas suas arestas
	public Floresta max_spanning_tree() {
		Floresta tree = new Floresta(dim);				//floresta com os nos {0,...,dim} (sem arestas)
		double[] adj = new double[dim];					//lista onde se atualiza o peso das arestas dos nos adjacentes
		int[] adj_index = new int[dim];					//o peso adj[x] o o peso entre os nos x em adj e adj_index[x]
		boolean[] visited = new boolean[dim];				//lista onde os nos prontos ser inseridos na arvore estao true
		for(int i = 0; i < dim; i = i+1) {
			adj[i] = -Integer.MAX_VALUE;							//atribuir o peso minimo possivel a todas arestas
			adj_index[i] = -1;					//atribuir que este peso provem da ligaçao a um n� imposs�vel (-1)
		} 
		adj[dim-1] = 0;							//atribuir peso 0 � raiz da �rvore
		visited[dim-1] = true;						//sendo a raiz da �rvore est� pronto para ir para ela (true)
		int pivot = dim-1;							
		for(int j = 1; j < dim; j = j+1) {
			
			for(Adj element : graph[pivot]) {			//para todos os n�s adjacentes ao pivot
				if(!visited[element.getNode()] && adj[element.getNode()] < element.getCost()) {		//se o n� n�o estiver na �rvore e puder ter um peso maior da aresta com o novo pivot
				adj[element.getNode()] = element.getCost();			//alterar para o novo m�ximo de peso
				adj_index[element.getNode()] = pivot;			//alterar para que n� forma a nova aresta com m�ximo peso
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
