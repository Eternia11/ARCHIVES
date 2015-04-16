package archives.workflow;

public class Flow {
	private String m_id = "";
	private String m_source = "";
	private String m_target = "";

	public Flow() {
	}

	public Flow(String id, String source, String target) {
		m_id = id;
		m_source = source;
		m_target = target;
	}

	public String get_id() {
		return m_id;
	}

	public String get_source() {
		return m_source;
	}

	public String get_target() {
		return m_target;
	}

	public String toXPDL() {
		return "\t\t\t\t<xpdl:Transition From=\"" + m_source + "\" Id=\""
				+ m_id + "\" To=\"" + m_target + "\"/>";
	}
}
