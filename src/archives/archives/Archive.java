package archives.archives;

import java.util.ArrayList;

/**
 * Represents an archive
 * 
 * @author Alan BENIER
 */
public class Archive {
	private ArrayList<Resource> m_resources = null;
	private ArrayList<Activity> m_activities = null;
	private ArrayList<Occurrence> m_occurrences = null;

	/**
	 * Constructor
	 */
	public Archive() {
		m_resources = new ArrayList<Resource>();
		m_activities = new ArrayList<Activity>();
		m_occurrences = new ArrayList<Occurrence>();
	}

}
