package archives.workflow;

public class Activity {
	private String m_id = "";
	private String m_name = "";
	private String m_lane = "";

	public Activity() {
	}

	public Activity(String id, String name) {
		m_id = id;
		m_name = name;
	}

	public Activity(String id, String name, String lane) {
		m_id = id;
		m_name = name;
		m_lane = lane;
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}

	public String get_lane() {
		return m_lane;
	}

	public void set_lane(String m_lane) {
		this.m_lane = m_lane;
	}

	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\""
				+ m_id
				+ "\" Name=\""
				+ m_name
				+ "\">\n\t\t\t\t\t<xpdl:NodeGraphicsInfos>\n\t\t\t\t\t\t<xpdl:NodeGraphicsInfo LaneId=\""
				+ m_lane
				+ "\"/>\n\t\t\t\t\t</xpdl:NodeGraphicsInfos>\n\t\t\t\t</xpdl:Activity>";
	}
}
