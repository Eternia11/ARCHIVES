package archives.graph;

public class Node {
	private String m_id = "";
	private String m_label = "";
	private int m_r = 0;
	private int m_g = 0;
	private int m_b = 0;
	private float m_size = 25;

	public Node() {
	}

	public Node(String label) {
		m_id = label;
		m_label = label;
	}

	public Node(String id, String label) {
		m_id = id;
		m_label = label;
	}

	public String get_id() {
		return m_id;
	}

	public String get_label() {
		return m_label;
	}

	public int get_r() {
		return m_r;
	}

	public int get_g() {
		return m_g;
	}

	public int get_b() {
		return m_b;
	}

	public float get_size() {
		return m_size;
	}

	public void set_size(float size) {
		m_size = size;
	}

	public void setRGB(int red, int green, int blue) {
		m_r = red;
		m_g = green;
		m_b = blue;
	}

	public String toGRAPHML() {
		return "\t\t<node id=\"" + m_id + "\">\n"
				+ "\t\t\t<data key=\"label\">" + m_label + "</data>\n"
				+ "\t\t\t<data key=\"size\">" + m_size + "</data>\n"
				+ "\t\t\t<data key=\"r\">" + Integer.toString(m_r) + "</data>\n"
				+ "\t\t\t<data key=\"g\">" + Integer.toString(m_g) + "</data>\n"
				+ "\t\t\t<data key=\"b\">" + Integer.toString(m_b) + "</data>\n"
				+ "\t\t</node>";
	}
}
