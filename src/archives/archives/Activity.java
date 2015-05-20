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

	/**
	 * Create an activity with no linked resource
	 * 
	 * @param name name of the activity
	 */
	public Activity(String name) {
		m_name = name;
		m_senders = new ArrayList<Resource>();
		m_receivers = new ArrayList<Resource>();
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
	 * Link a resource to this activity as sender of this activity
	 * After checking that is not already the case
	 * 
	 * @param sender resource to link
	 */
	public void addSender(Resource sender) {
		m_senders.add(sender);
	}

	/**
	 * Link a resource to this activity as receiver of this activity
	 * After checking that is not already the case
	 * 
	 * @param receiver resource to link
	 */
	public void addReceiver(Resource receiver) {
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
}
