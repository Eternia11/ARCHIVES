package archives.graph;

/**
 * Represents an edge of a Graph in GraphML format
 * 
 * @param m_id id of the Node
 * @param m_label label or name of the Node
 * @param m_r red value of the RGB color of the Node
 * @param m_g green value of the RGB color of the Node
 * @param m_b blue value of the RGB color of the Node
 * @param m_size size of the Node
 */
public class Node {
	private String m_id = "";
	private String m_label = "";
	private int m_r = 0;
	private int m_g = 0;
	private int m_b = 0;
	private float m_size = 25;

	/**
	 * Depreciated Constructor
	 */
	public Node() {
	}

	/**
	 * Create a black Node of size 25 and
	 * with the same id as label
	 * 
	 * @param label label or name of the Node
	 */
	public Node(String label) {
		m_id = label;
		m_label = label;
	}

	/**
	 * Create a black Node of size 25
	 * 
	 * @param id id of the Node
	 * @param label label or name of the Node
	 */
	public Node(String id, String label) {
		m_id = id;
		m_label = label;
	}

	/**
	 * Returns the id of the Node
	 * 
	 * @return the id of the Node
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Returns the label of the Node
	 * 
	 * @return the label of the Node
	 */
	public String get_label() {
		return m_label;
	}

	/**
	 * Returns the RGB red value of the Node
	 * 
	 * @return the RGB red value of the Node
	 */
	public int get_r() {
		return m_r;
	}

	/**
	 * Returns the RGB green value of the Node
	 * 
	 * @return the RGB green value of the Node
	 */
	public int get_g() {
		return m_g;
	}

	/**
	 * Returns the RGB blue value of the Node
	 * 
	 * @return the RGB blue value of the Node
	 */
	public int get_b() {
		return m_b;
	}

	/**
	 * Returns the size of the Node
	 * 
	 * @return the size of the Node
	 */
	public float get_size() {
		return m_size;
	}

	/**
	 * Set the size of the Node
	 * 
	 * @param size new value of the size of the Node
	 */
	public void set_size(float size) {
		m_size = size;
	}

	/**
	 * Set the RGB color of the Node
	 * 
	 * @param red red value of the RGB color
	 * @param green green value of the RGB color
	 * @param blue blue value of the RGB color
	 */
	public void setRGB(int red, int green, int blue) {
		m_r = red;
		m_g = green;
		m_b = blue;
	}

	/**
	 * Convert the Node into GraphML format String
	 * 
	 * @return the GraphML format String representing this Node
	 */
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
