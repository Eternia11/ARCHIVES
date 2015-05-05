package archives.graph;

import java.util.ArrayList;

public class Graph {
	private ArrayList<Node> m_nodes = null;
	private ArrayList<Edge> m_edges = null;

	public Graph() {
		m_nodes = new ArrayList<Node>();
		m_edges = new ArrayList<Edge>();
	}

	public ArrayList<Node> get_nodes() {
		return m_nodes;
	}

	public ArrayList<Edge> get_edges() {
		return m_edges;
	}

	public Node get_node(int index) {
		return m_nodes.get(index);
	}

	public Edge get_edge(int index) {
		return m_edges.get(index);
	}

	public boolean contains_node(String id) {
		for (Node node : m_nodes) {
			if (id.equals(node.get_id()))
				return true;
		}
		return false;
	}

	public boolean contains_edge(String source, String target) {
		for (Edge edge : m_edges) {
			if (source.equals(edge.get_source())
					&& target.equals(edge.get_target()))
				return true;
		}
		return false;
	}

	public void add_node(Node node) {
		if (!contains_node(node.get_id()))
			m_nodes.add(node);
	}
	
	public void add_node(String label) {
		if (!contains_node(label))
			m_nodes.add(new Node(label));
	}

	public void add_edge(Edge edge) {
		if (!contains_edge(edge.get_source(), edge.get_target()))
			m_edges.add(edge);
	}
	
	public void add_edge(String source, String target) {
		if (!contains_edge(source, target))
			m_edges.add(new Edge(source, target));
	}
	
	public void increase_weight(String source, String target) {
		for (Edge edge : m_edges) {
			if (source.equals(edge.get_source()) && (target.equals(edge.get_target()))) {
				edge.set_weight(edge.get_weight() + 1);
				break;
			}
		}
	}
	
	public ArrayList<Edge> outgoingEdgesOf(Node node) {
		ArrayList<Edge> out = new ArrayList<Edge>();
		for (Edge edge : m_edges) {
			if (node.get_id().equals(edge.get_source()))
				out.add(edge);
		}
		return out;
	}
	
	public ArrayList<Edge> incomingEdgesOf(Node node) {
		ArrayList<Edge> in = new ArrayList<Edge>();
		for (Edge edge : m_edges) {
			if (node.get_id().equals(edge.get_target()))
				in.add(edge);
		}
		return in;
	}
	
	public Node get_node(String id) {
		for (Node node : m_nodes) {
			if (id.equals(node.get_id()))
				return node;
		}
		return null;
	}

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
