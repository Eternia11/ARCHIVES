package archives.workflow;

import java.util.ArrayList;

public class Pool {
	private String m_id = "";
	private String m_name = "";
	private String m_process = "";
	private ArrayList<Lane> m_lanes = null;

	public Pool() {
		m_lanes = new ArrayList<Lane>();
	}

	public Pool(String id, String name, String process) {
		m_id = id;
		m_name = name;
		m_process = process;
		m_lanes = new ArrayList<Lane>();
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}

	public String get_process() {
		return m_process;
	}

	public int sizeLanes() {
		return m_lanes.size();
	}

	public Lane get_lane(int index) {
		return m_lanes.get(index);
	}

	public boolean containsLane(Lane lane) {
		for (Lane l : m_lanes) {
			if (lane.get_id().equals(l.get_id()))
				return true;
		}
		return false;
	}

	public void addLane(Lane lane) {
		if (!containsLane(lane))
			m_lanes.add(lane);
	}

	public String toXPDL() {
		String ret = "\t\t<xpdl:Pool Id=\"" + m_id + "\" Name=\"" + m_name
				+ "\" Process=\"" + m_process + "\">\n\t\t\t<xpdl:Lanes>";
		for (Lane lane : m_lanes) {
			ret += "\n" + lane.toXPDL();
		}
		ret += "\n\t\t\t</xpdl:Lanes>\n\t\t</xpdl:Pool>";
		return ret;
	}
}
