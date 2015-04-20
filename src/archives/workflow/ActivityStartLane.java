package archives.workflow;

public class ActivityStartLane extends ActivityLane {
	public ActivityStartLane(String id, String lane) {
		super(id, id, lane);
	}

	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\""
				+ get_id()
				+ "\">\n\t\t\t\t\t<xpdl:Event>\n\t\t\t\t\t\t<xpdl:StartEvent/>\n\t\t\t\t\t</xpdl:Event>\n\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\""
				+ get_lane()
				+ "\"/>\n\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n\t\t\t\t</xpdl:Activity>";
	}
}
