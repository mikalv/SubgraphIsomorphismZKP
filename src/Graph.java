import java.util.Random;

public class Graph {

	public int[][] g; // if g[i][j] == 2, then it means this is a subgraph, and g[i][j] of the original is not included
	public int n;

	/**
	 * Constructor
	 * 
	 * @param n
	 *            number of vertices of the graph
	 */
	public Graph(int n) {
		this.n = n;
		g = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				g[i][j] = 0;
			}
		}
	}

	public Graph(String s) throws Exception {
		this.g = deserialize(s).g;
		this.n = Integer.parseInt(s.split(" ")[0]);
	}

	public String serialize() {
		StringBuilder s = new StringBuilder();
		s.append(n);
		s.append(" ");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s.append(g[i][j]);
				s.append(" ");
			}
		}
		return s.toString();
	}

	public static Graph deserialize(String s) throws Exception {
		String[] items = s.split(" ");
		int n = Integer.parseInt(items[0]);
		if (items.length != n * n + 1) {
			throw new Exception("Invalid input for deseriablization!");
		}
		Graph G = new Graph(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				G.g[i][j] = Integer.parseInt(items[n * i + j + 1]);
			}
		}
		return G;
	}

	/**
	 * Verify if P = permutation(G)
	 * in our case, P & G will either both be graph, or subgraph
	 * 
	 * @param G
	 *            a graph
	 * @param permutation
	 *            a permutation of G's nodes
	 */
	public boolean isIsomorphic(Graph P, int[] perm) {
		if ((P.n != this.n) || (perm.length != P.n)) {
			return false;
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (g[i][j] == 1 && g[i][j] != P.g[perm[i]][perm[j]]) {
					return false;
				} else if (g[i][j] != 1 && P.g[perm[i]][perm[j]] == 1) {
					// if g[i][j] != 1, then it might be 0 or 2(equivalent to 0, but indicate it's a missing entry from the original graph)
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param v
	 *            an integer used to generate the commitment graph returns a
	 *            commitement graph C s.t. C[i][j] = Hash(i + j + G[i][j] + v)
	 */
	public CommitmentGraph getCommitment(int v) {
		return new CommitmentGraph(this, v);
	}

	/**
	 * @param permutation
	 *            a permutation of G's nodes returns a new graph P =
	 *            permutation(G)
	 */
	public Graph getPermutation(int[] permutation) throws Exception {
		if (n != permutation.length) {
			throw new Exception("invalid permutation on graph G!");
		}
		Graph P = new Graph(this.n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				P.g[permutation[i]][permutation[j]] = g[i][j];
			}
		}
		return P;
	}

	public Graph depermutation(int[] permutation) throws Exception {
		if (n != permutation.length) {
			throw new Exception("invalid permutation on graph G!");
		}
		Graph P = new Graph(this.n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				P.g[i][j] = g[permutation[i]][permutation[j]];
			}
		}
		return P;
	}

	public Graph getSubgraph() {
		Random r = new Random();
		Graph SG = new Graph(n);
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (r.nextInt(2) == 0) {
					// g[i][j] and g[j][i] is not included
					SG.g[i][j] = 2;
					SG.g[j][i] = 2;
				} else {
					// g[i][j] and g[j][i] is included
					SG.g[i][j] = this.g[i][j];
					SG.g[j][i] = this.g[j][i];
				}
			}
		}
		return SG;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s += g[i][j];
				s += " ";
			}
			s += "\n";
		}
		return s;
	}

}
