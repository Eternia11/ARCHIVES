package archives.workflow;

import java.util.ArrayList;

public class Workflow {
	private String m_id = "";
	private String m_name = "";
	private ArrayList<Pool> m_pools = null;
	private ArrayList<Process> m_processes = null;

	public Workflow() {
		m_pools = new ArrayList<Pool>();
		m_processes = new ArrayList<Process>();
	}

	public Workflow(String id, String name) {
		m_id = id;
		m_name = name;
		m_pools = new ArrayList<Pool>();
		m_processes = new ArrayList<Process>();
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}

	public int sizePools() {
		return m_pools.size();
	}

	public Pool get_pool(int index) {
		return m_pools.get(index);
	}

	public int sizeProcess() {
		return m_processes.size();
	}

	public Process get_process(int index) {
		return m_processes.get(index);
	}

	public boolean containsPool(Pool pool) {
		for (Pool p : m_pools) {
			if (pool.get_id().equals(p.get_id()))
				return true;
		}
		return false;
	}

	public void addPool(Pool pool) {
		if (!containsPool(pool))
			m_pools.add(pool);
	}

	public boolean containsProcess(Process process) {
		for (Process p : m_processes) {
			if (process.get_id().equals(p.get_id()))
				return true;
		}
		return false;
	}

	public void addProcess(Process process) {
		if (!containsProcess(process))
			m_processes.add(process);
	}

	public String toXPDL() {
		String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<xpdl:Package xmlns:xpdl=\"http://www.wfmc.org/2008/XPDL2.1\" xmlns=\"http://www.wfmc.org/2008/XPDL2.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" Id=\""
				+ m_id
				+ "\" Name=\""
				+ m_name
				+ "\" xsi:schemaLocation=\"http://www.wfmc.org/2008/XPDL2.1 http://www.wfmc.org/standards/docs/bpmnxpdl_31.xsd\">\n\t<xpdl:Pools>";
		for (Pool pool : m_pools) {
			ret += "\n" + pool.toXPDL();
		}
		ret += "\n\t</xpdl:Pools>\n\t<xpdl:WorkflowProcesses>";
		for (Process process : m_processes) {
			ret += "\n" + process.toXPDL();
		}
		ret += "\n\t</xpdl:WorkflowProcesses>\n</xpdl:Package>";
		return ret;
	}
}
