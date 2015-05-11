package archives.petrinet;

/**
 * Represents a Transition of a Place/Transition Petri Net
 * 
 * @author Alan BENIER
 */
public class Transition {
	private String m_id = "";		// id of the transition
	private String m_name = "";		// name of the transition

	/**
	 * Deprecated constructor
	 * 
	 * @deprecated
	 */
	public Transition() {
	}

	/**
	 * Create a transition
	 * 
	 * @param id id of the transition
	 * @param name name of the transition
	 */
	public Transition(String id, String name) {
		m_id = id;
		m_name = name;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the transition
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the transition
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Convert the transition into PNML format String
	 * 
	 * @return the PNML format String representing the transition
	 */
	public String toPNML() {
		return "\t\t\t<transition id=\"" + m_id + "\">\n"
				+ "\t\t\t\t<name>\n"
				+ "\t\t\t\t\t<text>" + m_name + "</text>\n"
				+ "\t\t\t\t</name>\n"
				+ "\t\t\t</transition>";
	}
}
