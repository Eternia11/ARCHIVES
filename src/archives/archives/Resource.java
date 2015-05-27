package archives.archives;

import java.util.ArrayList;

/**
 * Represents a resource cited in an archive
 * 
 * @author Alan BENIER
 */
public class Resource {
	private String m_name = "";									// name of the resource
	private ArrayList<Occurrence> m_o_asSender = null;			// occurrences where the resource appears as sender in the archive
	private ArrayList<Occurrence> m_o_asReceiver = null;		// occurrences where the resource appears as receiver in the archive
	private ArrayList<Activity> m_a_asSender = null;			// activities of the occurrences where the resource appears as sender in the archive
	private ArrayList<Activity> m_a_asReceiver = null;			// activities of the occurrences where the resource appears as receiver in the archive
	private ArrayList<String> m_p_asSender = null;				// performatives of the occurrences where the resource appears as sender in the archive
	private ArrayList<String> m_p_asReceiver = null;			// performatives of the occurrences where the resource appears as receiver in the archive

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Resource() {
		m_o_asSender = new ArrayList<Occurrence>();
		m_o_asReceiver = new ArrayList<Occurrence>();
		m_a_asSender = new ArrayList<Activity>();
		m_a_asReceiver = new ArrayList<Activity>();
		m_p_asSender = new ArrayList<String>();
		m_p_asReceiver = new ArrayList<String>();
	}

	/**
	 * Create a resource with no linked occurrence nor linked activity nor linked performative
	 * 
	 * @param name name of the resource
	 */
	public Resource(String name) {
		m_name = name;
		m_o_asSender = new ArrayList<Occurrence>();
		m_o_asReceiver = new ArrayList<Occurrence>();
		m_a_asSender = new ArrayList<Activity>();
		m_a_asReceiver = new ArrayList<Activity>();
		m_p_asSender = new ArrayList<String>();
		m_p_asReceiver = new ArrayList<String>();
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
	 * @return the occurrences where the resource appear as sender in the archive
	 */
	public ArrayList<Occurrence> get_o_asSender() {
		return m_o_asSender;
	}

	/**
	 * Getter
	 * 
	 * @return the occurrences where the resource appear as receiver in the archive
	 */
	public ArrayList<Occurrence> get_o_asReceiver() {
		return m_o_asReceiver;
	}

	/**
	 * Getter
	 * 
	 * @return the activities of the occurrences where the resource appears as sender in the archive
	 */
	public ArrayList<Activity> get_a_asSender() {
		return m_a_asSender;
	}

	/**
	 * Getter
	 * 
	 * @return the activities of the occurrences where the resource appears as receiver in the archive
	 */
	public ArrayList<Activity> get_a_asReceiver() {
		return m_a_asReceiver;
	}

	/**
	 * Getter
	 * 
	 * @return the performatives of the occurrences where the resource appears as sender in the archive
	 */
	public ArrayList<String> get_p_asSender() {
		return m_p_asSender;
	}

	/**
	 * Getter
	 * 
	 * @return the performatives of the occurrences where the resource appears as receiver in the archive
	 */
	public ArrayList<String> get_p_asReceiver() {
		return m_p_asReceiver;
	}

	/**
	 * Link an activity to the resource as this one's sender
	 * After checking that is not already the case
	 * 
	 * @param activity activity to link
	 */
	public void addActivityAsSender(Activity activity) {
		if (!containsActivityAsSender(activity.get_name()))
			m_a_asSender.add(activity);
	}

	/**
	 * Link an activity to the resource as this one's receiver
	 * After checking that is not already the case
	 * 
	 * @param activity activity to link
	 */
	public void addActivityAsReceiver(Activity activity) {
		if (!containsActivityAsReceiver(activity.get_name()))
			m_a_asReceiver.add(activity);
	}

	/**
	 * Check if an activity is already linked to this resource as sender
	 * 
	 * @param activity name of the activity to check
	 * @return true if the activity is already linked
	 */
	public boolean containsActivityAsSender(String activity) {
		for (Activity a : m_a_asSender) {
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
		for (Activity a : m_a_asReceiver) {
			if (activity.equals(a.get_name()))
				return true;
		}
		return false;
	}

	/**
	 * Link an occurrence to this resource in what it appears as sender
	 * No verification
	 * 
	 * @param occurrence occurrence to link
	 */
	public void addOccurrenceAsSender(Occurrence occurrence) {
		m_o_asSender.add(occurrence);
		addActivityAsSender(occurrence.get_activity());
		addPerformativeAsSender(occurrence.get_performative());
	}

	/**
	 * Link an occurrence to this resource in what it appears as receiver
	 * No verification
	 * 
	 * @param occurrence occurrence to link
	 */
	public void addOccurrenceAsReceiver(Occurrence occurrence) {
		m_o_asReceiver.add(occurrence);
		addActivityAsReceiver(occurrence.get_activity());
		addPerformativeAsReceiver(occurrence.get_performative());
	}

	/**
	 * Link a performative to the resource that
	 * appears in the same occurrence as sender
	 * After checking that is not already the case
	 * 
	 * @param performative performative to link
	 */
	public void addPerformativeAsSender(String performative) {
		if (!containsPerformativeAsSender(performative))
			m_p_asSender.add(performative);
	}

	/**
	 * Link a performative to the resource that
	 * appears in the same occurrence as receiver
	 * After checking that is not already the case
	 * 
	 * @param performative performative to link
	 */
	public void addPerformativeAsReceiver(String performative) {
		if (!containsPerformativeAsReceiver(performative))
			m_p_asReceiver.add(performative);
	}

	/**
	 * Check if an performative is already
	 * linked to this resource as sender
	 * 
	 * @param performative name of the performative to check
	 * @return true if the performative is already linked
	 */
	public boolean containsPerformativeAsSender(String performative) {
		return m_p_asSender.contains(performative);
	}

	/**
	 * Check if an performative is already
	 * linked to this resource as receiver
	 * 
	 * @param performative name of the performative to check
	 * @return true if the performative is already linked
	 */
	public boolean containsPerformativeAsReceiver(String performative) {
		return m_p_asReceiver.contains(performative);
	}

	/**
	 * toString overload
	 */
	public String toString() {
		String ret = "Resource<" + m_name + ", [";

		if (!m_a_asSender.isEmpty()) {
			ret += m_a_asSender.get(0).get_name();
		}
		for (int i = 1; i < m_a_asSender.size(); i++) {
			ret += ", " + m_a_asSender.get(i).get_name();
		}

		ret += "], [";

		if (!m_a_asReceiver.isEmpty()) {
			ret += m_a_asReceiver.get(0).get_name();
		}
		for (int i = 1; i < m_a_asReceiver.size(); i++) {
			ret += ", " + m_a_asReceiver.get(i).get_name();
		}

		ret += "]>";

		return ret;
	}
}
