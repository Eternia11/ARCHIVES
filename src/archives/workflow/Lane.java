package archives.workflow;

/**
 * Represent a Lane of a Workflow
 * 
 * @author Alan BENIER
 */
public class Lane {
	private String m_id = "";		// id of the lane
	private String m_name = "";		// name of the lane

	/**
	 * Deprecated Constructor
	 * 
	 * @deprecated
	 */
	public Lane() {
	}

	/**
	 * Create a lane which name is the same as its id
	 * 
	 * @param id id and name of the lane
	 */
	public Lane(String id) {
		m_id = id;
		m_name = id;
	}

	/**
	 * Create a lane
	 * 
	 * @param id id of the lane
	 * @param name name of the lane
	 */
	public Lane(String id, String name) {
		m_id = id;
		m_name = name;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the lane
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the lane
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Convert the lane into XPDL format String
	 * 
	 * @return the XPDL format String representing the lane
	 */
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Lane Id=\"" + m_id + "\" Name=\"" + m_name + "\"/>";
	}
}
