package archives.graph;

import java.util.ArrayList;

/**
 * Represents a Graph
 * 
 * @author Alan BENIER
 */
public class Graph {
	private ArrayList<Node> m_nodes = null;		// list of nodes of the Graph
	private ArrayList<Edge> m_edges = null;		// list of edges of the Graph

	/**
	 * Create a Graph with no node nor edge
	 */
	public Graph() {
		m_nodes = new ArrayList<Node>();
		m_edges = new ArrayList<Edge>();
	}

	/**
	 * Returns the list of nodes
	 * 
	 * @return the list of nodes
	 */
	public ArrayList<Node> get_nodes() {
		return m_nodes;
	}

	/**
	 * Returns the list of edges
	 * 
	 * @return the list of edges
	 */
	public ArrayList<Edge> get_edges() {
		return m_edges;
	}

	/**
	 * Returns the node which have the given index
	 * 
	 * @param index index of the node to retrieve
	 * @return the designated node
	 */
	public Node get_node(int index) {
		return m_nodes.get(index);
	}

	/**
	 * Returns the edge which have the given index
	 * 
	 * @param index index of the edge to retrieve
	 * @return the designated edge
	 */
	public Edge get_edge(int index) {
		return m_edges.get(index);
	}

	/**
	 * Check if a node designated by its id
	 * is contained in the list of nodes
	 * We assume that two nodes are the same
	 * iff they have the same id
	 * 
	 * @param id id of the node to check
	 * @return true if the node is in the list
	 */
	public boolean contains_node(String id) {
		for (Node node : m_nodes) {
			if (id.equals(node.get_id()))
				return true;
		}
		return false;
	}

	/**
	 * Check if an edge designated by its source and target
	 * is contained in the list of edges
	 * We assume that two edges are the same
	 * iff they have the same source
	 * and the same target
	 * 
	 * @param source id of the source node of the edge to check
	 * @param target id of the target node of the edge to check
	 * @return true if the edges is in the list
	 */
	public boolean contains_edge(String source, String target) {
		for (Edge edge : m_edges) {
			if (source.equals(edge.get_source())
					&& target.equals(edge.get_target()))
				return true;
		}
		return false;
	}

	/**
	 * Add a node to the list of nodes
	 * 
	 * @param node node to add
	 */
	public void add_node(Node node) {
		if (!contains_node(node.get_id()))
			m_nodes.add(node);
	}
	
	/**
	 * Create then add a node to the list of nodes
	 * Its id will be the same as its label
	 * 
	 * @param label label of the node to add
	 */
	public void add_node(String label) {
		if (!contains_node(label))
			m_nodes.add(new Node(label));
	}

	/**
	 * Add an edge to the list of edges
	 * 
	 * @param edge edge to add
	 */
	public void add_edge(Edge edge) {
		if (!contains_edge(edge.get_source(), edge.get_target()))
			m_edges.add(edge);
	}
	
	/**
	 * Create then add an edge to the list of edges
	 * 
	 * @param source id of source of the edge to add
	 * @param target id of target of the edge to add
	 */
	public void add_edge(String source, String target) {
		if (!contains_edge(source, target))
			m_edges.add(new Edge(source, target));
	}
	
	/**
	 * Retrieve in the list the corresponding edge (source and target)
	 * then add 1 to its current weight
	 * 
	 * @param source id of the source of the edge to modify
	 * @param target id of the target of the edge to modify
	 */
	public void increase_weight(String source, String target) {
		for (Edge edge : m_edges) {
			if (source.equals(edge.get_source()) && (target.equals(edge.get_target()))) {
				edge.set_weight(edge.get_weight() + 1);
				break;
			}
		}
	}
	
	/**
	 * Create then return the list of edges which
	 * have the source node given in parameter
	 * 
	 * @param node source node of the edges to retrieve
	 * @return the list of edges which source is given in parameter
	 */
	public ArrayList<Edge> outgoingEdgesOf(Node node) {
		ArrayList<Edge> out = new ArrayList<Edge>();
		for (Edge edge : m_edges) {
			if (node.get_id().equals(edge.get_source()))
				out.add(edge);
		}
		return out;
	}
	
	/**
	 * Create then return the list of edges which
	 * have the target node given in parameter
	 * 
	 * @param node target node of the edges to retrieve
	 * @return the list of edges which target is given in parameter
	 */
	public ArrayList<Edge> incomingEdgesOf(Node node) {
		ArrayList<Edge> in = new ArrayList<Edge>();
		for (Edge edge : m_edges) {
			if (node.get_id().equals(edge.get_target()))
				in.add(edge);
		}
		return in;
	}
	
	/**
	 * Retrieve in the list of nodes the node which
	 * id is given in parameter and then return it
	 * We assume that two nodes are the same
	 * iff they have the same id
	 * 
	 * @param id id of the node to retrieve
	 * @return the node if contained in the list, else null
	 */
	public Node get_node(String id) {
		for (Node node : m_nodes) {
			if (id.equals(node.get_id()))
				return node;
		}
		return null;
	}

	/**
	 * Convert the Graph into GraphML format String
	 * 
	 * @return the GraphML format String representing this Graph
	 */
	public String toGRAPHML() {
		String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n"
				+ "\t<key attr.name=\"label\" attr.type=\"string\" for=\"node\" id=\"label\"/>\n"
				+ "\t<key attr.name=\"Edge Label\" attr.type=\"string\" for=\"edge\" id=\"edgelabel\"/>\n"
				+ "\t<key attr.name=\"weight\" attr.type=\"double\" for=\"edge\" id=\"weight\"/>\n"
				+ "\t<key attr.name=\"Edge Id\" attr.type=\"string\" for=\"edge\" id=\"edgeid\"/>\n"
				+ "\t<key attr.name=\"r\" attr.type=\"int\" for=\"node\" id=\"r\"/>\n"
				+ "\t<key attr.name=\"g\" attr.type=\"int\" for=\"node\" id=\"g\"/>\n"
				+ "\t<key attr.name=\"b\" attr.type=\"int\" for=\"node\" id=\"b\"/>\n"
				+ "\t<key attr.name=\"size\" attr.type=\"float\" for=\"node\" id=\"size\"/>\n"
				+ "\t<graph edgedefault=\"directed\">\n";
		for (Node node : m_nodes) {
			ret += node.toGRAPHML() + "\n";
		}
		for (Edge edge : m_edges) {
			ret += edge.toGRAPHML() + "\n";
		}
		ret += "</graph>\n" + "</graphml>";
		return ret;
	}
}
