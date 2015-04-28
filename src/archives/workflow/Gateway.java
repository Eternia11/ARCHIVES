package archives.workflow;

public class Gateway extends ActivityLane {

	public Gateway(String id, String name, String lane) {
		super(id, name, lane);
	}
	
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\""
			+ get_id()
			+ "\">\n\t\t\t\t\t<xpdl:Route GatewayType=\"Parallel\"/>\n\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\""
			+ get_lane()
			+ "\"/>\n\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n\t\t\t\t</xpdl:Activity>";
	}
}
