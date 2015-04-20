package archives.workflow;

public class Participant {
	private String m_id = "";

	public Participant() {
	}

	public Participant(String id) {
		m_id = id;
	}

	public String get_id() {
		return m_id;
	}

	public String toXPDL() {
		return "<xpdl:Participant Id=\"" + m_id + "\"/>";
	}
}
