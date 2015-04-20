package archives.workflow;

public class ActivityLane extends Activity {
	private String m_lane = "";
	
	public ActivityLane(String id, String name, String lane) {
		super(id, name);
		m_lane = lane;
	}
	
	public String get_lane() {
		return m_lane;
	}
	
	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\""
			+ get_id()
			+ "\" Name=\""
			+ get_name()
			+ "\">\n\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\""
			+ m_lane
			+ "\"/>\n\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n\t\t\t\t</xpdl:Activity>";
	}
}
