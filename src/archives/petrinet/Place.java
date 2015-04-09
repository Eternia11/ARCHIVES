package archives.petrinet;

public class Place {
	private String m_id = "";
	private String m_name = "";
	private int m_initialMarking = 0;

	public Place() {
	}

	public Place(String id, String name) {
		m_id = id;
		m_name = name;
	}
	
	public Place(String id, String name, int initialMarking) {
		m_id = id;
		m_name = name;
		m_initialMarking = initialMarking;
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}

	public String toPNML() {
		if (m_initialMarking <= 0) {
			return "\t\t\t<place id=\""
					+ m_id
					+ "\">\n\t\t\t\t<name>\n\t\t\t\t\t<text>"
					+ m_name
					+ "</text>\n\t\t\t\t</name>\n\t\t\t</place>";
		} else {
			return "\t\t\t<place id=\""
					+ m_id
					+ "\">\n\t\t\t\t<name>\n\t\t\t\t\t<text>"
					+ m_name
					+ "</text>\n\t\t\t\t</name>\n\t\t\t\t<initialmarking>\n\t\t\t\t\t<text>"
					+ Integer.toBinaryString(m_initialMarking)
					+ "</text>\n\t\t\t\t</initialmarking>\n\t\t\t</place>";		
		}
	}
}
