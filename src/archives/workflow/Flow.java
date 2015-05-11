package archives.workflow;

/**
 * Represent a Flow of a Workflow
 * 
 * @author Alan BENIER
 */
public class Flow {
	private String m_id = "";			// id of the flow
	private String m_source = "";		// id of the source of the flow (activity)
	private String m_target = "";		// id of the target of the flow (activity)

	/**
	 * Deprecated Constructor
	 * 
	 * @deprecated
	 */
	public Flow() {
	}

	/**
	 * Create a flow which id is a concatenation
	 * of its source's id and target's id
	 * 
	 * @param source id of the source of the flow
	 * @param target id of the target of the flow
	 */
	public Flow(String source, String target) {
		m_id = source + "---" + target;
		m_source = source;
		m_target = target;
	}

	/**
	 * Create a flow
	 * 
	 * @param id id of the flow
	 * @param source id of the source of the flow
	 * @param target id of the target of the flow
	 */
	public Flow(String id, String source, String target) {
		m_id = id;
		m_source = source;
		m_target = target;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the flow
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the source of the flow
	 */
	public String get_source() {
		return m_source;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the target of the flow
	 */
	public String get_target() {
		return m_target;
	}

	/**
	 * Convert the flow into XPDL format String
	 * 
	 * @return the XPDL format String representing the flow
	 */
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Transition From=\"" + m_source + "\" Id=\"" + m_id + "\" To=\"" + m_target + "\"/>";
	}
}
