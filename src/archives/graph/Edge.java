package archives.graph;

public class Edge {
	private String m_label = "";
	private String m_source = "";
	private String m_target = "";
	private float m_weight = 1;

	public Edge() {
	}

	public Edge(String label, String source, String target) {
		m_label = label;
		m_source = source;
		m_target = target;
	}
	
	public Edge(String source, String target) {
		m_source = source;
		m_target = target;
	}

	public String get_label() {
		return m_label;
	}

	public String get_source() {
		return m_source;
	}

	public String get_target() {
		return m_target;
	}

	public float get_weight() {
		return m_weight;
	}

	public void set_weight(float weight) {
		m_weight = weight;
	}

	public String toGRAPHML() {
		return "\t\t<edge source=\"" + m_source + "\" target=\"" + m_target + "\" type=\"directed\">\n"
				+ "\t\t\t<data key=\"weight\">" + m_weight + "</data>\n"
				+ "\t\t\t<data key=\"Edge Label\">" + m_label + "</data>\n"
				+ "\t\t</edge>";
	}
}
