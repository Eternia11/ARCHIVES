package archives.workflow;

public class ActivityParticipant extends Activity {
	private String m_participant = "";

	public ActivityParticipant(String id, String name, String participant) {
		super(id, name);
		m_participant = participant;
	}

	public String get_articipant() {
		return m_participant;
	}

	public String toXPDL() {
		return "\t\t\t\t<xpdl:Activity Id=\"" + get_id() + "\" Name=\""
				+ get_name() + "\">\n\t\t\t\t\t<xpdl:Performer>"
				+ m_participant + "</xpdl:Performer>\n\t\t\t\t</xpdl:Activity>";
	}
}
