package archives.petrinet;

/**
 * Represents an Arc of a Place/Transition Petri Net
 * 
 * @author Alan BENIER
 */
public class Arc {
	private String m_id = "";			// id of the arc
	private String m_sourceId = "";		// id of the source of the arc (place or transition)
	private String m_targetId = "";		// id of the target of the arc (transition or place)

	/**
	 * Deprecated constructor
	 * 
	 * @deprecated
	 */
	public Arc() {
	}

	/**
	 * Create an arc
	 * 
	 * @param id id of the arc
	 * @param sourceId id of the source of the arc (place or transition)
	 * @param targetId id of the target of the arc (transition or place)
	 */
	public Arc(String id, String sourceId, String targetId) {
		m_id = id;
		m_sourceId = sourceId;
		m_targetId = targetId;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the arc
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the source of the arc
	 */
	public String get_sourceId() {
		return m_sourceId;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the target of the arc
	 */
	public String get_targetId() {
		return m_targetId;
	}
	
	/**
	 * Convert the arc into PNML format String
	 * 
	 * @return the PNML format String representing the arc
	 */
	public String toPNML() {
		return "\t\t\t<arc id=\"" + m_id + "\" source=\"" + m_sourceId + "\" target=\"" + m_targetId + "\">\n"
				+ "\t\t\t\t<inscription>\n"
				+ "\t\t\t\t\t<text>1</text>\n"
				+ "\t\t\t\t</inscription>\n"
				+ "\t\t\t</arc>";
	}

}
