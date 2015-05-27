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
	private ArrayList<Occurrence> m_occurrences = null;		// occurrences in which the activity appears
	private ArrayList<String> m_performative = null;		// performatives of the occurrences where the activity appears in the archive

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Activity() {
		m_senders = new ArrayList<Resource>();
		m_receivers = new ArrayList<Resource>();
		m_occurrences = new ArrayList<Occurrence>();
		m_performative = new ArrayList<String>();
	}

	/**
	 * Create an activity with no linked resource
	 * nor linked occurrence nor linked performative
	 * 
	 * @param name name of the activity
	 */
	public Activity(String name) {
		m_name = name;
		m_senders = new ArrayList<Resource>();
		m_receivers = new ArrayList<Resource>();
		m_occurrences = new ArrayList<Occurrence>();
		m_performative = new ArrayList<String>();
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
	 * Getter
	 * 
	 * @return the list of performatives of the occurrences where the activity appears in the archive
	 */
	public ArrayList<String> get_performatives() {
		return m_performative;
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
		addSender(occurrence.get_sender());
		addReceiver(occurrence.get_receiver());
		addPerformative(occurrence.get_performative());
	}

	/**
	 * Link a performative to the activity
	 * After checking that is not already the case
	 * 
	 * @param performative performative to link
	 */
	public void addPerformative(String performative) {
		if (!containsPerformative(performative))
			m_performative.add(performative);
	}

	/**
	 * Check if an performative is already
	 * linked to this activity
	 * 
	 * @param performative name of the performative to check
	 * @return true if the performative is already linked
	 */
	public boolean containsPerformative(String performative) {
		return m_performative.contains(performative);
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
