package archives.workflow;

/**
 * Represent a parallel Gateway of a Workflow which is contained in a lane
 * 
 * @author Alan BENIER
 */
public class Gateway extends ActivityLane {

	/**
	 * Create a parallel gateway
	 * 
	 * @param id id of the gateway
	 * @param name name of the gateway
	 * @param lane id of the containing lane
	 */
	public Gateway(String id, String name, String lane) {
		super(id, name, lane);
	}

	/**
	 * Convert the parallel gateway into XPDL format String
	 * 
	 * @return the XPDL format String representing the parallel gateway
	 */
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\"" + get_id() + "\">\n"
				+ "\t\t\t\t\t<xpdl:Route GatewayType=\"Parallel\"/>\n"
				+ "\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\"" + get_lane() + "\"/>\n"
				+ "\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n"
				+ "\t\t\t\t</xpdl:Activity>";
	}
}
