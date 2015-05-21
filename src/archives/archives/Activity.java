package archives.archives;

import java.util.ArrayList;

/**
 * Respresents an activity cited in an archive
 * 
 * @author Alan BENIER
 */
public class Activity {
	private String m_name;									// name of the activity
	private ArrayList<Resource> m_senders = null;			// resources appearing as sender in the lines where appears the activity
	private ArrayList<Resource> m_receivers = null;			// resources appearing as resources in the lines where appears the activity
	private ArrayList<Occurrence> m_occurrences = null;		// occurrences in which the activity appear

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Activity() {
		m_senders = new ArrayList<Resource>();
		m_receivers = new ArrayList<Resource>();
		m_occurrences = new ArrayList<Occurrence>();
	}

	/**
	 * Create an activity with no linked resource nor linked occurrence
	 * 
	 * @param name name of the activity
	 */
	public Activity(String name) {
		m_name = name;
		m_senders = new ArrayList<Resource>();
		m_receivers = new ArrayList<Resource>();
		m_occurrences = new ArrayList<Occurrence>();
	}

	/**
	 * Getter
	 * 
	 * @return the name of the activity
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Getter
	 * 
	 * @return the list of resources who appear as sender in the lines in which appear this activity
	 */
	public ArrayList<Resource> get_senders() {
		return m_senders;
	}

	/**
	 * Getter
	 * 
	 * @return the list of resources who appear as receiver in the lines in which appear this activity
	 */
	public ArrayList<Resource> get_receivers() {
		return m_receivers;
	}

	/**
	 * Getter
	 * 
	 * @return the list of occurrences in which the activity appear
	 */
	public ArrayList<Occurrence> get_occurrences() {
		return m_occurrences;
	}

	/**
	 * Link a resource to this activity as sender of this activity
	 * After checking that is not already the case
	 * 
	 * @param sender resource to link
	 */
	public void addSender(Resource sender) {
		if (!containsSender(sender.get_name()))
			m_senders.add(sender);
	}

	/**
	 * Link a resource to this activity as receiver of this activity
	 * After checking that is not already the case
	 * 
	 * @param receiver resource to link
	 */
	public void addReceiver(Resource receiver) {
		if (!containsReceiver(receiver.get_name()))
			m_receivers.add(receiver);
	}

	/**
	 * Check if a resource is linked to this activity as sender
	 * 
	 * @param sender name of the resource to check
	 * @return true if the resource is already linked to this activity
	 */
	public boolean containsSender(String sender) {
		for (Resource r : m_senders) {
			if (sender.equals(r.get_name()))
				return true;
		}
		return false;
	}

	/**
	 * Check if a resource is linked to this activity as receiver
	 * 
	 * @param receiver name of the resource to check
	 * @return true if the resource is already linked to this activity
	 */
	public boolean containsReceiver(String receiver) {
		for (Resource r : m_receivers) {
			if (receiver.equals(r.get_name()))
				return true;
		}
		return false;
	}

	/**
	 * Link an occurrence to this activity
	 * No verification
	 * 
	 * @param occurrence occurrence to link
	 */
	public void addOccurrence(Occurrence occurrence) {
		m_occurrences.add(occurrence);
	}

	/**
	 * toString overload
	 */
	public String toString() {
		String ret = "Activity<" + m_name + ", [";

		if (!m_senders.isEmpty()) {
			ret += m_senders.get(0).get_name();
		}
		for (int i = 1; i < m_senders.size(); i++) {
			ret += ", " + m_senders.get(i).get_name();
		}

		ret += "], [";

		if (!m_receivers.isEmpty()) {
			ret += m_receivers.get(0).get_name();
		}
		for (int i = 1; i < m_receivers.size(); i++) {
			ret += ", " + m_receivers.get(i).get_name();
		}

		ret += "]>";

		return ret;
	}
}
