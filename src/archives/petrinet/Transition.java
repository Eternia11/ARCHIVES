package archives.petrinet;

public class Transition {
	private String m_id = "";
	private String m_name = "";
	private int m_xsize = 10;
	private int m_ysize = 10;
	private int m_xpos = 0;
	private int m_ypos = 0;

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

	public int get_xpos() {
		return m_xpos;
	}

	public int get_ypos() {
		return m_ypos;
	}

	public int get_xsize() {
		return m_xsize;
	}

	public int get_ysize() {
		return m_ysize;
	}
	
	public boolean equals(Transition t) {
		return (t.get_id() == m_id);
	}
	
	public String toPNML() {
		return "\t\t\t<transition id=\""
				+ m_id
				+ "\">\n\t\t\t\t<name>\n\t\t\t\t\t<text>"
				+ m_name
				+ "</text>\n\t\t\t\t\t<graphics>\n\t\t\t\t\t\t<offset x=\""
				+ Integer.toString(m_xsize)
				+ "\" y=\""
				+ Integer.toString(m_ysize)
				+ "\">\n\t\t\t\t\t</offset></graphics>\n\t\t\t\t</name>\n\t\t\t\t<graphics>\n\t\t\t\t\t<position x=\""
				+ Integer.toString(m_xpos) + "\" y=\""
				+ Integer.toString(m_ypos)
				+ "\">\n\t\t\t\t</position></graphics>\n\t\t\t</transition>";
	}
}
