package archives.archives;

import java.util.ArrayList;

import archives.log.Trace;

/**
 * Represents an archive
 * 
 * @author Alan BENIER
 */
public class Archive {
	private ArrayList<Resource> m_resources = null;			// list of resources of the archive
	private ArrayList<Activity> m_activities = null;		// list of activities of the archive
	private ArrayList<Occurrence> m_occurrences = null;		// list of occurrences (or lines) of the archive

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Archive() {
		m_resources = new ArrayList<Resource>();
		m_activities = new ArrayList<Activity>();
		m_occurrences = new ArrayList<Occurrence>();
	}
	
	/**
	 * Create an archive from log data
	 * 
	 * @param log log data
	 * @param dateFormat format of the date of the timestamps of the log
	 */
	public Archive(ArrayList<Trace> log, String dateFormat) {
		m_resources = new ArrayList<Resource>();
		m_activities = new ArrayList<Activity>();
		m_occurrences = new ArrayList<Occurrence>();
		
		for (Trace t : log) {
			String sender = t.getSender();
			String receiver = t.getReceiver();
			String activity = t.getActivity();
			String performative = t.getPerformative();
			String caseID = t.getCaseID();
			String timestamp = t.getTimestamp();
			Resource r_sender = null;
			Resource r_receiver = null;
			Activity a_activity = null;
			Occurrence o_occurrence = null;
			
			// create resources if does not exist
			if (!containsResource(sender)) {
				addResource(sender);
			}
			if (!containsResource(receiver)) {
				addResource(receiver);
			}
			
			// create activity if does not exist
			if (!containsActivity(activity)) {
				addActivity(activity);
			}
			
			r_sender = getResource(sender);
			r_receiver = getResource(receiver);
			a_activity = getActivity(activity);
			
			// create the occurrence and link everything
			try {
				o_occurrence = new Occurrence(a_activity, r_sender, r_receiver, performative, caseID, timestamp, dateFormat);
			} catch (OccurrenceException e) {
				e.printStackTrace();
				System.out.println("Something went wrong during the creation of the archive.");
				System.exit(1);
			}
			m_occurrences.add(o_occurrence);
			
			if ((r_receiver == null) || (r_sender == null) || (a_activity == null) || (o_occurrence == null)) {
				System.out.println("Something went wrong during the creation of the archive.");
				System.exit(1);
			}
			
			r_sender.addActivityAsSender(a_activity);
			r_receiver.addActivityAsReceiver(a_activity);
			a_activity.addSender(r_sender);
			a_activity.addReceiver(r_receiver);
		}
	}

	/**
	 * Getter
	 * 
	 * @return the list of resources of the archive
	 */
	public ArrayList<Resource> get_resources() {
		return m_resources;
	}

	/**
	 * Getter
	 * 
	 * @return the list of activities of the archive
	 */
	public ArrayList<Activity> get_activities() {
		return m_activities;
	}

	/**
	 * Getter
	 * 
	 * @return the list of occurrences of the archive
	 */
	public ArrayList<Occurrence> get_occurrences() {
		return m_occurrences;
	}

	/**
	 * Check if a resource exists in the list of resources
	 * 
	 * @param resource name of the resource to check
	 * @return true if the resource is found in the list
	 */
	public boolean containsResource(String resource) {
		for (Resource r : m_resources) {
			if (resource.equals(r.get_name()))
				return true;
		}
		return false;
	}

	/**
	 * Add a resource to the list of resources
	 * 
	 * @param resource name of the resource to add
	 */
	public void addResource(String resource) {
		m_resources.add(new Resource(resource));
	}

	/**
	 * Check if an activity exists in the list of activity
	 * 
	 * @param activity name of the activity to check
	 * @return true if the activity is found in the list
	 */
	public boolean containsActivity(String activity) {
		for (Activity a : m_activities) {
			if (activity.equals(a.get_name()))
				return true;
		}
		return false;
	}
	
	/**
	 * Add an activity to the list of activities
	 * 
	 * @param activity name of the activity to add
	 */
	public void addActivity(String activity) {
		m_activities.add(new Activity(activity));
	}

	/**
	 * Retrieve a resource of the archive
	 * 
	 * @param resource name of the resource to find
	 * @return the resource if found, null pointer if not
	 */
	public Resource getResource(String resource) {
		for (Resource r : m_resources) {
			if (resource.equals(r.get_name()))
				return r;
		}
		return null;
	}

	/**
	 * Retrieve an activity of the archive
	 * 
	 * @param activity name of the activity to find
	 * @return the activity if found, null pointer if not
	 */
	public Activity getActivity(String activity) {
		for (Activity a : m_activities) {
			if (activity.equals(a.get_name()))
				return a;
		}
		return null;
	}

	/**
	 * toString overload
	 */
	public String toString() {
		String ret = "Archive :";
		
		ret += "\n\tResources :";
		for (Resource r : m_resources) {
			ret += "\n\t\t" + r.toString();
		}
		
		ret += "\n\tActivities :";
		for (Activity a : m_activities) {
			ret += "\n\t\t" + a.toString();
		}
		
		ret += "\n\tOccurrences :";
		for (Occurrence o : m_occurrences) {
			ret += "\n\t\t" + o.toString();
		}
		
		return ret;
	}
}
