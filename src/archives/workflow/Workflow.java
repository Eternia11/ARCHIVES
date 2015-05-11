package archives.workflow;

import java.util.ArrayList;

/**
 * Represent a Workflow
 * 
 * @author Alan BENIER
 */
public class Workflow {
	private String m_id = "";							// id of the workflow
	private String m_name = "";							// name of the workflow
	private ArrayList<Pool> m_pools = null;				// list of pools of the workflow
	private ArrayList<Process> m_processes = null;		// list of processes of the workflow

	/**
	 * Create a workflow with no id nor name nor process nor pool
	 */
	public Workflow() {
		m_pools = new ArrayList<Pool>();
		m_processes = new ArrayList<Process>();
	}

	/**
	 * Create a workflow with no pool nor process yet
	 * 
	 * @param id
	 * @param name
	 */
	public Workflow(String id, String name) {
		m_id = id;
		m_name = name;
		m_pools = new ArrayList<Pool>();
		m_processes = new ArrayList<Process>();
	}

	/**
	 * Getter
	 * 
	 * @return the id of the workflow
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the workflow
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Returns the number of pools of the workflow
	 * 
	 * @return the size of the list of the pools
	 */
	public int sizePools() {
		return m_pools.size();
	}

	/**
	 * Retrieve the pool of the workflow
	 * which is at the given position in the list
	 * 
	 * @param index position of the pool
	 * @return the corresponding pool
	 */
	public Pool get_pool(int index) {
		return m_pools.get(index);
	}

	/**
	 * Returns the number of processes of the workflow
	 * 
	 * @return the size of the list of the processes
	 */
	public int sizeProcess() {
		return m_processes.size();
	}

	/**
	 * Retrieve the process of the workflow
	 * which is at the given position in the list
	 * 
	 * @param index position of the process
	 * @return the corresponding process
	 */
	public Process get_process(int index) {
		return m_processes.get(index);
	}

	/**
	 * Check if the given pool is one of the workflow
	 * We assume that two pools are equals
	 * iff they have the same id
	 * 
	 * @param pool pool to check
	 * @return true if the given pool is one of the workflow
	 */
	public boolean containsPool(Pool pool) {
		for (Pool p : m_pools) {
			if (pool.get_id().equals(p.get_id()))
				return true;
		}
		return false;
	}

	/**
	 * Add the given pool to the workflow
	 * 
	 * @param pool pool to add
	 */
	public void addPool(Pool pool) {
		if (!containsPool(pool))
			m_pools.add(pool);
	}

	/**
	 * Check if the given process is one of the workflow
	 * We assume that two processes are equals
	 * iff they have the same id
	 * 
	 * @param process process to check
	 * @return true if the given process is one of the workflow
	 */
	public boolean containsProcess(Process process) {
		for (Process p : m_processes) {
			if (process.get_id().equals(p.get_id()))
				return true;
		}
		return false;
	}

	/**
	 * Add the given process to the workflow
	 * 
	 * @param process process to add
	 */
	public void addProcess(Process process) {
		if (!containsProcess(process))
			m_processes.add(process);
	}

	/**
	 * Convert the workflow into XPDL format String
	 * 
	 * @return the XPDL format String representing the workflow
	 */
	public String toXPDL() {
		String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
				+ "<xpdl:Package xmlns:xpdl=\"http://www.wfmc.org/2008/XPDL2.1\" xmlns=\"http://www.wfmc.org/2008/XPDL2.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" Id=\"" + m_id + "\" Name=\"" + m_name + "\" xsi:schemaLocation=\"http://www.wfmc.org/2008/XPDL2.1 http://www.wfmc.org/standards/docs/bpmnxpdl_31.xsd\">";
		if (!m_pools.isEmpty()) {
			ret += "\n\t<xpdl:Pools>";
			for (Pool pool : m_pools) {
				ret += "\n" + pool.toXPDL();
			}
			ret += "\n\t</xpdl:Pools>";
		}

		if (!m_processes.isEmpty()) {
			ret += "\n\t<xpdl:WorkflowProcesses>";
			for (Process process : m_processes) {
				ret += "\n" + process.toXPDL();
			}
			ret += "\n\t</xpdl:WorkflowProcesses>";
		}

		ret += "\n</xpdl:Package>";

		return ret;
	}
}
