package archives.workflow;

/**
 * Represent an Activity of a Workflow which is contained in a Lane
 * 
 * @author Alan BENIER
 */
public class ActivityLane extends Activity {
	private String m_lane = "";		// id of the lane which contains the activity

	/**
	 * Create an activity which is contained in a lane
	 * 
	 * @param id id of the activity
	 * @param name name of the activity
	 * @param lane id of the lane
	 */
	public ActivityLane(String id, String name, String lane) {
		super(id, name);
		m_lane = lane;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the lane that contains the activity
	 */
	public String get_lane() {
		return m_lane;
	}

	/**
	 * Convert the activity into XPDL format String
	 * 
	 * @return the XPDL format String representing the activity
	 */
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\"" + get_id() + "\" Name=\"" + get_name() + "\">\n"
				+ "\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\"" + m_lane + "\"/>\n"
				+ "\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t</xpdl:Activity>";
	}
}
