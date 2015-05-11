package archives.petrinet;

/**
 * Represents a Place of a Place/Transition Petri Net
 * 
 * @author Alan BENIER
 */
public class Place {
	private String m_id = "";				// id of the place
	private String m_name = "";				// name of the place
	private int m_initialMarking = 0;		// number of initial tokens in the place

	/**
	 * Deprecated constructor
	 * 
	 * @deprecated
	 */
	public Place() {
	}

	/**
	 * Create a place with 0 initial token
	 * 
	 * @param id id of the place
	 * @param name name of the place
	 */
	public Place(String id, String name) {
		m_id = id;
		m_name = name;
	}

	/**
	 * Create a place
	 * 
	 * @param id id of the place
	 * @param name name of the place
	 * @param initialMarking number of initial tokens of the place
	 */
	public Place(String id, String name, int initialMarking) {
		m_id = id;
		m_name = name;
		m_initialMarking = initialMarking;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the place
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the place
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Convert the place into PNML format String
	 * 
	 * @return the PNML format String representing the place
	 */
	public String toPNML() {
		if (m_initialMarking <= 0) {
			return "\t\t\t<place id=\"" + m_id + "\">\n"
					+ "\t\t\t\t<name>\n"
					+ "\t\t\t\t\t<text>" + m_name + "</text>\n"
					+ "\t\t\t\t</name>\n"
					+ "\t\t\t</place>";
		} else {
			return "\t\t\t<place id=\"" + m_id + "\">\n"
					+ "\t\t\t\t<name>\n"
					+ "\t\t\t\t\t<text>" + m_name + "</text>\n"
					+ "\t\t\t\t</name>\n"
					+ "\t\t\t\t<initialmarking>\n"
					+ "\t\t\t\t\t<text>" + Integer.toBinaryString(m_initialMarking) + "</text>\n"
					+ "\t\t\t\t</initialmarking>\n"
					+ "\t\t\t</place>";		
		}
	}
}
