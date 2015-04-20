package archives.workflow;

public class Activity {
	private String m_id = "";
	private String m_name = "";

	public Activity(String id, String name) {
		m_id = id;
		m_name = name;
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}

	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\"" + m_id + "\" Name=\"" + m_name
				+ "\"/>";
	}
}
