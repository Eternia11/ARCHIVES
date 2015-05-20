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

	/**
	 * Create a resource with no linked activity
	 * 
	 * @param name name of the resource
	 */
	public Resource(String name) {
		m_name = name;
		m_asSender = new ArrayList<Activity>();
		m_asReceiver = new ArrayList<Activity>();
	}

	/**
	 * Getter
	 * 
	 * @return the name of the resource
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Getter
	 * 
	 * @return the list of activities that appear in the same lines in which appear this resource as sender
	 */
	public ArrayList<Activity> get_asSender() {
		return m_asSender;
	}

	/**
	 * Getter
	 * 
	 * @return the list of activities that appear in the same lines in which appear this resource as receiver
	 */
	public ArrayList<Activity> get_asReceiver() {
		return m_asReceiver;
	}

	/**
	 * Link an activity to the resource as this one's sender
	 * After checking that is not already the case
	 * 
	 * @param activity activity to link
	 */
	public void addActivityAsSender(Activity activity) {
		if (!containsActivityAsSender(activity.get_name()))
			m_asSender.add(activity);
	}

	/**
	 * Link an activity to the resource as this one's receiver
	 * After checking that is not already the case
	 * 
	 * @param activity activity to link
	 */
	public void addActivityAsReceiver(Activity activity) {
		if (!containsActivityAsReceiver(activity.get_name()))
			m_asReceiver.add(activity);
	}

	/**
	 * Check if an activity is already linked to this resource as sender
	 * 
	 * @param activity name of the activity to check
	 * @return true if the activity is already linked
	 */
	public boolean containsActivityAsSender(String activity) {
		for (Activity a : m_asSender) {
			if (activity.equals(a.get_name()))
				return true;
		}
		return false;
	}

	/**
	 * Check if an activity is already linked to this resource as receiver
	 * 
	 * @param activity name of the activity to check
	 * @return true if the activity is already linked
	 */
	public boolean containsActivityAsReceiver(String activity) {
		for (Activity a : m_asReceiver) {
			if (activity.equals(a.get_name()))
				return true;
		}
		return false;
	}
}
