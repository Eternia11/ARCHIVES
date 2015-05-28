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
			
			// link the occurrence to the activity and the resources
			r_sender.addOccurrenceAsSender(o_occurrence);
			r_receiver.addOccurrenceAsReceiver(o_occurrence);
			a_activity.addOccurrence(o_occurrence);
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

	/**
	 * Simple test to use the Archive class
	 * 
	 * @return the list of resources of the archives who have only activity with "delegate" performative
	 */
	public ArrayList<Resource> onlyDelegatedAsReceiver() {
		// resources who have only delegated actions as they are receiver
		ArrayList<Resource> ret = new ArrayList<Resource>();
		
		for (Resource r : m_resources) {
			if ((r.get_p_asReceiver().size() == 1)&&(r.containsPerformativeAsReceiver("delegate"))) {
				ret.add(r);
			}
		}
		
		System.out.println("\n\n\n\n\n\n\n\n\nonlyDelegatedAsReceiver :");
		for (Resource r : ret) {
			System.out.println("\t" + r.toString());
		}
		
		return ret;
	}

	/**
	 * Build a list of clusters of resources
	 * Two resources are in the same cluster iff
	 * they have the same linked activities
	 * 
	 * @return the list of clusters, each cluster is a list of resources
	 */
	public ArrayList<ArrayList<Resource>> clusterResources() {
		ArrayList<ArrayList<Resource>> ret = new ArrayList<ArrayList<Resource>>();

		for (Resource r : m_resources) {
			boolean in_cluster = false;

			// we check if the resource is not already in a cluster
			for (ArrayList<Resource> cluster : ret) {
				for (Resource r_cluster : cluster) {
					if (r.get_name().equals(r_cluster.get_name()))
						in_cluster = true;
				}
			}

			// then if it is not in a cluster, we create a new cluster and store
			// all the similar resources in it
			if (!in_cluster) {
				ArrayList<Resource> cluster = new ArrayList<Resource>();

				for (Resource r_other : m_resources) {
					if ((r.get_a_asReceiver().containsAll(r_other.get_a_asReceiver()))
							&& (r_other.get_a_asReceiver().containsAll(r.get_a_asReceiver()))
							&& (r.get_a_asSender().containsAll(r_other.get_a_asSender()))
							&& (r_other.get_a_asSender().containsAll(r.get_a_asSender())))
						cluster.add(r_other);
				}

				ret.add(cluster);
			}
		}

		System.out.println("\n\n\n\n\n\n\n\n");
		int i = 1;
		for (ArrayList<Resource> cluster : ret) {
			System.out.print("Cluster " + i + " :   [");
			for (Resource r_cluster : cluster)
				System.out.print(", " + r_cluster.get_name());
			i++;
			System.out.println("]");
		}

		return ret;
	}

	public void test() {
		int nb_occ = m_occurrences.size();
		int nb_act = m_activities.size();
		int nb_res = m_resources.size();
		int a_count[] = new int[nb_act];
		Activity best_act[] = new Activity[nb_res];
		
		for (Occurrence o : m_occurrences) {
			a_count[m_activities.indexOf(o.get_activity())]++;
		}
		
		System.out.println("\n\n\n\n\n\n\n");
		for (int i=0; i<nb_act; i++) {
			System.out.println(m_activities.get(i).get_name() + "\t\t\t\t" + a_count[i] + " times");
		}
		
		System.out.println("\n\n\n\n\n\n\n");
		for (int k=0; k<nb_res; k++) {
			Resource r = m_resources.get(k);
			double r_count[] = new double[nb_act];
			for (double i : r_count) {
				i = 0.;
			}
			for (Occurrence o_sender : r.get_o_asSender()) {
				Activity act = o_sender.get_activity();
				int act_index = m_activities.indexOf(act);
				
				r_count[act_index] = 1/a_count[act_index];
			}
			for (Occurrence o_receiver : r.get_o_asReceiver()) {
				Activity act = o_receiver.get_activity();
				int act_index = m_activities.indexOf(act);
				
				r_count[act_index] = 1/(0.9999*a_count[act_index]);
			}
			
			double max = r_count[0];
			int indexOf_max = 0;
			
			for (int i=0; i<nb_act; i++) {
				if (r_count[i] > max) {
					max = r_count[i];
					indexOf_max = i;
				}
			}
			
			best_act[k] = m_activities.get(indexOf_max);
			
			System.out.println(r.get_name() + "\t\t\t" + best_act[k].get_name());
		}
		
		ArrayList<ArrayList<Resource>> cluster_list = new ArrayList<ArrayList<Resource>>();
		
		for (int i=0; i<nb_res; i++) {
			Resource r = m_resources.get(i);
			boolean in_cluster = false;

			// we check if the resource is not already in a cluster
			for (ArrayList<Resource> cluster : cluster_list) {
				for (Resource r_cluster : cluster) {
					if (r.get_name().equals(r_cluster.get_name()))
						in_cluster = true;
				}
			}

			// then if it is not in a cluster, we create a new cluster and store
			// all the similar resources in it
			if (!in_cluster) {
				ArrayList<Resource> cluster = new ArrayList<Resource>();
				for (int j=0; j<nb_res; j++) {
					Resource r_other = m_resources.get(j);
					if (best_act[i] == best_act[j])
						cluster.add(r_other);
				}
				cluster_list.add(cluster);
			}
		}
		
		System.out.println("\n\n\n\n\n\n\n\n");
		int i = 1;
		for (ArrayList<Resource> cluster : cluster_list) {
			System.out.print("Cluster " + i + " :   [");
			for (Resource r_cluster : cluster)
				System.out.print(", " + r_cluster.get_name());
			i++;
			System.out.println("]");
		}
	}
}
