package archives.workflow;

import java.util.ArrayList;

/**
 * Represent a pool of a Workflow
 * 
 * @author Alan BENIER
 */
public class Pool {
	private String m_id = "";					// id of the pool
	private String m_name = "";					// name of the pool
	private String m_process = "";				// id of the process which contains the pool
	private ArrayList<Lane> m_lanes = null;		// list of lanes contained in the pool

	/**
	 * Deprecated Constructor
	 * @deprecated
	 */
	public Pool() {
		m_lanes = new ArrayList<Lane>();
	}

	/**
	 * Create a pool with no lane yet
	 * 
	 * @param id id of the pool
	 * @param name name of the pool
	 * @param process id of the process which will contain the pool
	 */
	public Pool(String id, String name, String process) {
		m_id = id;
		m_name = name;
		m_process = process;
		m_lanes = new ArrayList<Lane>();
	}

	/**
	 * Getter
	 * 
	 * @return the id of the pool
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the pool
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Getter
	 * 
	 * @return the id of the process containing the pool
	 */
	public String get_process() {
		return m_process;
	}

	/**
	 * Returns the number of lanes contained in the pool
	 * 
	 * @return the size of the list of lanes
	 */
	public int sizeLanes() {
		return m_lanes.size();
	}

	/**
	 * Retrieve the lane which have the given position
	 * in the list of lanes
	 * 
	 * @param index position in the list
	 * @return the corresponding lane
	 */
	public Lane get_lane(int index) {
		return m_lanes.get(index);
	}

	/**
	 * Check if a lane is contained in the pool
	 * We assume that two lanes are equals
	 * iff they have the same id
	 * 
	 * @param lane id of the lane to check
	 * @return true if the lane is contained in the pool
	 */
	public boolean containsLane(Lane lane) {
		for (Lane l : m_lanes) {
			if (lane.get_id().equals(l.get_id()))
				return true;
		}
		return false;
	}

	/**
	 * Add the given lane into the pool
	 * 
	 * @param lane lane to add
	 */
	public void addLane(Lane lane) {
		if (!containsLane(lane))
			m_lanes.add(lane);
	}

	/**
	 * Convert the pool into XPDL format String
	 * 
	 * @return the XPDL format String representing the pool
	 */
	public String toXPDL() {
		String ret = "\t\t<xpdl:Pool Id=\"" + m_id + "\" Name=\"" + m_name + "\" Process=\"" + m_process + "\">\n"
				+ "\t\t\t<xpdl:Lanes>";
		for (Lane lane : m_lanes) {
			ret += "\n" + lane.toXPDL();
		}
		ret += "\n\t\t\t</xpdl:Lanes>\n"
				+ "\t\t</xpdl:Pool>";
		return ret;
	}
}
