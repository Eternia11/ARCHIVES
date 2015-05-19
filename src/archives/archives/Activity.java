package archives.archives;

import java.util.ArrayList;

/**
 * Respresents an activity cited in an archive
 * 
 * @author Alan BENIER
 */
public class Activity {
	private String m_name;								// name of the activity
	private ArrayList<Resource> m_senders = null;		// resources appearing as sender in the lines where appears the activity
	private ArrayList<Resource> m_receivers = null;		// resources appearing as resources in the lines where appears the activity

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Activity() {
		m_senders = new ArrayList<Resource>();
		m_receivers = new ArrayList<Resource>();
	}
	
	public Activity(String name) {
		m_name = name;
		m_senders = new ArrayList<Resource>();
		m_receivers = new ArrayList<Resource>();
	}

}
