package archives.workflow;

/**
 * Represent an default Activity of a Workflow
 * 
 * @author Alan BENIER
 */
public class Activity {
	private String m_id = "";		// id of the activity
	private String m_name = "";		// name of the activity

	/**
	 * Deprecated Constructor
	 * 
	 * @deprecated
	 */
	public Activity() {
	}

	/**
	 * Create an activity
	 * 
	 * @param id id of the activity
	 * @param name name of the activity
	 */
	public Activity(String id, String name) {
		m_id = id;
		m_name = name;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the activity
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the activity
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Convert the activity into XPDL format String
	 * 
	 * @return the XPDL format String representing the activity
	 */
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\"" + m_id + "\" Name=\"" + m_name + "\"/>";
	}
}
