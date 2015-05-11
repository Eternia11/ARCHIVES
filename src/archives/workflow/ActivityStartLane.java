package archives.workflow;

/**
 * Represent an start Activity of a Workflow which is contained in a Lane
 * 
 * @author Alan BENIER
 */
public class ActivityStartLane extends ActivityLane {

	/**
	 * Create an start activity contained in a lane
	 * Note that an start activity has no name
	 * 
	 * @param id id of the activity
	 * @param lane lane of the activity
	 */
	public ActivityStartLane(String id, String lane) {
		super(id, id, lane);
	}

	/**
	 * Convert the activity into XPDL format String
	 * 
	 * @return the XPDL format String representing the activity
	 */
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\"" + get_id() + "\">\n"
				+ "\t\t\t\t\t<xpdl:Event>\n"
				+ "\t\t\t\t\t\t<xpdl:StartEvent/>\n"
				+ "\t\t\t\t\t</xpdl:Event>\n"
				+ "\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\"" + get_lane() + "\"/>\n"
				+ "\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t</xpdl:Activity>";
	}
}
