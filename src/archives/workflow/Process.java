package archives.workflow;

import java.util.ArrayList;

public class Process {
	private String m_id = "";
	private String m_name = "";
	private ArrayList<Activity> m_activities = null;
	private ArrayList<Flow> m_flows = null;
	private String m_start = "_start_";
	private String m_end = "_end_";

	public Process() {
		m_activities = new ArrayList<Activity>();
		m_flows = new ArrayList<Flow>();
	}

	public Process(String id, String name) {
		m_id = id;
		m_name = name;
		m_activities = new ArrayList<Activity>();
		m_flows = new ArrayList<Flow>();
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}

	public int sizeActivities() {
		return m_activities.size();
	}

	public Activity get_activity(int index) {
		return m_activities.get(index);
	}

	public int sizeFlows() {
		return m_flows.size();
	}

	public Flow get_flow(int index) {
		return m_flows.get(index);
	}
	
	public boolean containsActivity(Activity activity) {
		for (Activity a : m_activities) {
			if (activity.get_id().equals(a.get_id()))
				return true;
		}
		return false;
	}

	public void addActivity(Activity activity) {
		if (!containsActivity(activity))
			m_activities.add(activity);
	}

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

	public void addFlow(Flow flow) {
		if (!containsFlow(flow))
			m_flows.add(flow);
	}

	public void connectToStart(String activity_id) {
		addFlow(new Flow(m_start + m_id + "-" + activity_id, m_start + m_id,
				activity_id));
	}

	public void connectToEnd(String activity_id) {
		addFlow(new Flow(m_end + m_id + "-" + activity_id, activity_id, m_end
				+ m_id));
	}

	public String toXPDL() {
		String ret = "\t\t<xpdl:WorkflowProcess Id=\"" + m_id + "\" Name=\""
				+ m_name + "\">\n\t\t\t<xpdl:Activities>";
		// start event
		// ret += "\n\t\t\t\t<xpdl:Activity Id=\"" + m_start + m_id +
		// "\">\n\t\t\t\t\t<xpdl:Event>\n\t\t\t\t\t\t<xpdl:StartEvent/>\n\t\t\t\t\t</xpdl:Event>\n\t\t\t\t</xpdl:Activity>";
		// end event
		// ret += "\n\t\t\t\t<xpdl:Activity Id=\"" + m_end + m_id +
		// "\">\n\t\t\t\t\t<xpdl:Event>\n\t\t\t\t\t\t<xpdl:EndEvent/>\n\t\t\t\t\t</xpdl:Event>\n\t\t\t\t</xpdl:Activity>";
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
