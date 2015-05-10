package archives.graph;

import java.util.ArrayList;

/**
 * Represents a Graph in GraphML format
 * 
 * @param m_nodes list of nodes of the Graph
 * @param m_edges list of edges of the Graph
 */
public class Graph {
	private ArrayList<Node> m_nodes = null;
	private ArrayList<Edge> m_edges = null;

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
	 * 
	 * 
	 * @param id
	 * @return
	 */
	public boolean contains_node(String id) {
		for (Node node : m_nodes) {
			if (id.equals(node.get_id()))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
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
	 * 
	 * @param node
	 */
	public void add_node(Node node) {
		if (!contains_node(node.get_id()))
			m_nodes.add(node);
	}
	
	/**
	 * 
	 * @param label
	 */
	public void add_node(String label) {
		if (!contains_node(label))
			m_nodes.add(new Node(label));
	}

	/**
	 * 
	 * @param edge
	 */
	public void add_edge(Edge edge) {
		if (!contains_edge(edge.get_source(), edge.get_target()))
			m_edges.add(edge);
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	public void add_edge(String source, String target) {
		if (!contains_edge(source, target))
			m_edges.add(new Edge(source, target));
	}
	
	/**
	 * 
	 * @param source
	 * @param target
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
	 * 
	 * @param node
	 * @return
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
	 * 
	 * @param node
	 * @return
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
	 * 
	 * @param id
	 * @return
	 */
	public Node get_node(String id) {
		for (Node node : m_nodes) {
			if (id.equals(node.get_id()))
				return node;
		}
		return null;
	}

	/**
	 * 
	 * @return
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
