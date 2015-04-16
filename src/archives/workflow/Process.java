package archives.workflow;

import java.util.ArrayList;

public class Process {
	private String m_id = "";
	private String m_name = "";
	private ArrayList<Activity> m_activities = null;
	private ArrayList<Flow> m_flows = null;

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

	public String toXPDL() {
		String ret = "\t\t<xpdl:WorkflowProcess Id=\"" + m_id + "\" Name=\""
				+ m_name + "\">\n\t\t\t<xpdl:Activities>";
		// start event
		ret += "\t\t\t\t<xpdl:Activity Id=\"_start_"
				+ m_id
				+ "\">\n\t\t\t\t\t<xpdl:Event>\n\t\t\t\t\t\t<xpdl:StartEvent/>\n\t\t\t\t\t</xpdl:Event>";
		// end event
		ret += "\t\t\t\t<xpdl:Activity Id=\"_end_"
				+ m_id
				+ "\">\n\t\t\t\t\t<xpdl:Event>\n\t\t\t\t\t\t<xpdl:EndEvent/>\n\t\t\t\t\t</xpdl:Event>";
		for (Activity activity : m_activities) {
			ret += "\n" + activity.toXPDL();
		}
		ret += "\n\t\t\t</xpdl:Activities>\n\t\t\t<xpdl:Transitions>";
		for (Flow flow : m_flows) {
			ret += "\n" + flow.toXPDL();
		}
		ret += "\n\t\t\t</xpdl:Transitions>\n\t\t</xpdl:WorkflowProcess>";
		return ret;
	}
}