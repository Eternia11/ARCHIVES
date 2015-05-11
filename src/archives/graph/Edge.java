package archives.graph;

/**
 * Represents an edge of a Graph
 * 
 * @author Alan BENIER
 */
public class Edge {
	private String m_label = "";	// label or name of the Edge
	private String m_source = "";	// id of the source node
	private String m_target = "";	// id of the target node
	private float m_weight = 1;		// weight of the Edge

	/**
	 * Deprecated Constructor
	 * 
	 * @deprecated
	 */
	public Edge() {
	}

	/**
	 * Create an Edge of weight 1
	 * 
	 * @param label label or name of the Edge
	 * @param source id of the source node
	 * @param target id of the target node
	 */
	public Edge(String label, String source, String target) {
		m_label = label;
		m_source = source;
		m_target = target;
	}
	
	/**
	 * Create an Edge of weight 1 with no label
	 * 
	 * @param source id of the source node
	 * @param target id of the target node
	 */
	public Edge(String source, String target) {
		m_source = source;
		m_target = target;
	}

	/**
	 * Returns the label of the Edge
	 * 
	 * @return the label of the Edge
	 */
	public String get_label() {
		return m_label;
	}
	
	/**
	 * Returns the id of the source node of the Edge
	 * 
	 * @return the id of the source node
	 */
	public String get_source() {
		return m_source;
	}

	/**
	 * Returns the id of the target node of the Edge
	 * 
	 * @return the the id of the target node
	 */
	public String get_target() {
		return m_target;
	}

	/**
	 * Returns the weight of the Edge
	 * 
	 * @return the weights of the Edge
	 */
	public float get_weight() {
		return m_weight;
	}

	/**
	 * Set the weight of the Edge
	 * 
	 * @param weight the new value of the weight
	 */
	public void set_weight(float weight) {
		m_weight = weight;
	}

	/**
	 * Convert the Edge into GraphML format String
	 * 
	 * @return the GraphML format String representing this Edge
	 */
	public String toGRAPHML() {
		return "\t\t<edge source=\"" + m_source + "\" target=\"" + m_target + "\" type=\"directed\">\n"
				+ "\t\t\t<data key=\"weight\">" + m_weight + "</data>\n"
				+ "\t\t\t<data key=\"Edge Label\">" + m_label + "</data>\n"
				+ "\t\t</edge>";
	}
}
