package archives.petrinet;

public class Arc {
	private String m_id = "";
	private String m_sourceId = "";
	private String m_targetId = "";
	private int m_inscription = 1;

	public Arc() {
	}

	public Arc(String id, String sourceId, String targetId) {
		m_id = id;
		m_sourceId = sourceId;
		m_targetId = targetId;
	}

	public String get_id() {
		return m_id;
	}

	public String get_sourceId() {
		return m_sourceId;
	}

	public String get_targetId() {
		return m_targetId;
	}

	public int get_inscription() {
		return m_inscription;
	}

	public String toPNML() {
		return "\t<arc id=\"" + m_id + "\" source=\"" + m_sourceId
				+ "\" target=\"" + m_targetId
				+ "\">\n\t\t<inscription>\n\t\t\t<text>"
				+ Integer.toString(m_inscription)
				+ "</text>\n\t\t</inscription>\n\t</arc>";
	}

}
