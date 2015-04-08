package archives.petrinet;

public class Transition {
	private String m_id = "";
	private String m_name = "";

	public Transition() {
	}

	public Transition(String id, String name) {
		m_id = id;
		m_name = name;
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}
	
	public String toPNML() {
		return "\t\t\t<transition id=\""
				+ m_id
				+ "\">\n\t\t\t\t<name>\n\t\t\t\t\t<text>"
				+ m_name
				+ "</text>\n\t\t\t\t</name>\n\t\t\t</transition>";
	}
}
