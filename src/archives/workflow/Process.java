package archives.workflow;

import java.util.ArrayList;

/**
 * Represent a Process of a Workflow
 * 
 * @author Alan BENIER
 */
public class Process {
	private String m_id = "";								// id of the process
	private String m_name = "";								// name of the process
	private ArrayList<Activity> m_activities = null;		// list of activities of the process
	private ArrayList<Flow> m_flows = null;					// list of flows of the process

	/**
	 * Create a Process with no name nor id nor activity nor flow
	 * 
	 * @deprecated
	 */
	public Process() {
		m_activities = new ArrayList<Activity>();
		m_flows = new ArrayList<Flow>();
	}

	/**
	 * Create a process with no activity nor flow yet
	 * 
	 * @param id id of the process
	 * @param name name of the process
	 */
	public Process(String id, String name) {
		m_id = id;
		m_name = name;
		m_activities = new ArrayList<Activity>();
		m_flows = new ArrayList<Flow>();
	}

	/**
	 * Getter
	 * 
	 * @return the id of the process
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the process
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Returns the number of activities of the process
	 * 
	 * @return the size of the list of activities
	 */
	public int sizeActivities() {
		return m_activities.size();
	}

	/**
	 * Retrieve the activity which is at the
	 * given position in the list of activities
	 * 
	 * @param index position in the list
	 * @return the corresponding activity
	 */
	public Activity get_activity(int index) {
		return m_activities.get(index);
	}

	/**
	 * Returns the number of flows of the process
	 * 
	 * @return the size of the list of flows
	 */
	public int sizeFlows() {
		return m_flows.size();
	}

	/**
	 * Retrieve the flow which is at the
	 * given position in the list of flows
	 * 
	 * @param index position in the list
	 * @return the corresponding flow
	 */
	public Flow get_flow(int index) {
		return m_flows.get(index);
	}
	
	/**
	 * Check if an activity is one of the process
	 * We assume that two activities are equals
	 * iff they have the same id
	 * 
	 * @param activity activity to check
	 * @return true if the given activity is one of the process
	 */
	public boolean containsActivity(Activity activity) {
		for (Activity a : m_activities) {
			if (activity.get_id().equals(a.get_id()))
				return true;
		}
		return false;
	}

	/**
	 * Add an activity to the process
	 * 
	 * @param activity activity to add
	 */
	public void addActivity(Activity activity) {
		if (!containsActivity(activity))
			m_activities.add(activity);
	}

	/**
	 * Check if a flow is one of the process
	 * We assume that two flows are equals
	 * iff they have the same source's id
	 * and the same target's id
	 * 
	 * @param flow flow to check
	 * @return true if the given flow is one of the process
	 */
	public boolean containsFlow(Flow flow) {
		for (Flow f : m_flows) {
			if (flow.get_id().equals(f.get_id()))
				return true;
			if ((flow.get_source().equals(f.get_source()))
					&& (flow.get_target().equals(f.get_target())))
				return true;
		}
		return false;
	}

	/**
	 * Add a flow to the process
	 * 
	 * @param flow flow to add
	 */
	public void addFlow(Flow flow) {
		if (!containsFlow(flow))
			m_flows.add(flow);
	}

	/**
	 * Convert the process into XPDL format String
	 * 
	 * @return the XPDL format String representing the process
	 */
	public String toXPDL() {
		String ret = "\t\t<xpdl:WorkflowProcess Id=\"" + m_id + "\" Name=\"" + m_name + "\">\n"
				+ "\t\t\t<xpdl:Activities>";
		for (Activity activity : m_activities) {
			ret += "\n" + activity.toXPDL();
		}
		ret += "\n\t\t\t</xpdl:Activities>";

		if (!m_flows.isEmpty()) {
			ret += "\n\t\t\t<xpdl:Transitions>";
			for (Flow flow : m_flows) {
				ret += "\n" + flow.toXPDL();
			}
			ret += "\n\t\t\t</xpdl:Transitions>";
		}

		ret += "\n\t\t</xpdl:WorkflowProcess>";

		return ret;
	}
}
