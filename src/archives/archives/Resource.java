package archives.archives;

import java.util.ArrayList;

/**
 * Represents a resource cited in an archive
 * 
 * @author Alan BENIER
 */
public class Resource {
	private String m_name = "";								// name of the resource
	private ArrayList<Activity> m_asSender = null;			// activities of the lines where the resource appear as sender in the archive
	private ArrayList<Activity> m_asReceiver = null;		// activities of the lines where the resource appear as receiver in the archive

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Resource() {
		m_asSender = new ArrayList<Activity>();
		m_asReceiver = new ArrayList<Activity>();
	}

	public Resource(String name) {
		m_name = name;
		m_asSender = new ArrayList<Activity>();
		m_asReceiver = new ArrayList<Activity>();
	}

}
