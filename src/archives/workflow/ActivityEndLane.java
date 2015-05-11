package archives.workflow;

/**
 * Represent an end Activity of a Workflow which is contained in a Lane
 * 
 * @author Alan BENIER
 */
public class ActivityEndLane extends ActivityLane {

	/**
	 * Create an end activity contained in a lane
	 * Note that an end activity has no name
	 * 
	 * @param id id of the activity
	 * @param lane lane of the activity
	 */
	public ActivityEndLane(String id, String lane) {
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
				+ "\t\t\t\t\t\t<xpdl:EndEvent/>\n"
				+ "\t\t\t\t\t</xpdl:Event>\n"
				+ "\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\"" + get_lane() + "\"/>\n"
				+ "\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t</xpdl:Activity>";
	}
}
