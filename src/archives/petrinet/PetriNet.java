package archives.petrinet;

import java.util.ArrayList;

/**
 * Represents a Place/Transition Petri Net
 * 
 * @author Alan BENIER
 */
public class PetriNet {
	private String m_id = "";								// id of the petri net
	private String m_name = "";								// name of the petri net
	private ArrayList<Place> m_places = null;				// list of places of the petri net
	private ArrayList<Transition> m_transitions = null;		// list of transitions of the petri net
	private ArrayList<Arc> m_arcs = null;					// list of arcs of the petri net

	/**
	 * Create a Place/Transition Petri Net with no name
	 */
	public PetriNet() {
		m_places = new ArrayList<Place>();
		m_transitions = new ArrayList<Transition>();
		m_arcs = new ArrayList<Arc>();
	}

	/**
	 * Create a Place/transition Petri Net
	 * 
	 * @param id id of the petri net
	 * @param name name of the petri net
	 */
	public PetriNet(String id, String name) {
		m_id = id;
		m_name = name;
		m_places = new ArrayList<Place>();
		m_transitions = new ArrayList<Transition>();
		m_arcs = new ArrayList<Arc>();
	}

	/**
	 * Getter
	 * 
	 * @return the id of the petri net
	 */
	public String get_id() {
		return m_id;
	}

	/**
	 * Getter
	 * 
	 * @return the name of the petri net
	 */
	public String get_name() {
		return m_name;
	}

	/**
	 * Getter
	 * 
	 * @return the list of places of the petri net
	 */
	public ArrayList<Place> get_places() {
		return m_places;
	}

	/**
	 * Getter
	 * 
	 * @return the list of transitions of the petri net
	 */
	public ArrayList<Transition> get_transitions() {
		return m_transitions;
	}

	/**
	 * Getter
	 * 
	 * @return the list of arcs of the petri net
	 */
	public ArrayList<Arc> get_arcs() {
		return m_arcs;
	}

	/**
	 * Check if the petri net contains a place
	 * designated by its id
	 * We assume that two places are equals iff
	 * they have the same id
	 * 
	 * @param place_id id of the place to check
	 * @return true if the place is contained in the petri net
	 */
	public boolean containsPlace(String place_id) {
		for (Place p : m_places) {
			if (p.get_id().equals(place_id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the petri net contains a transition
	 * designated by its id
	 * We assume that two transitions are equals iff
	 * they have the same id
	 * 
	 * @param transition_id id of the transition to check
	 * @return true if the place is contained in the petri net
	 */
	public boolean containsTransition(String transition_id) {
		for (Transition t : m_transitions) {
			if (t.get_id().equals(transition_id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the petri net contains an arc
	 * designated by its source and target
	 * We assume that two arcs are equals iff
	 * they have the same id as source, and as target
	 * 
	 * @param arc_source id of the source of the arc to check
	 * @param arc_target id of the target of the arc to check
	 * @return true if the arc is contained in the petri net
	 */
	public boolean containsArc(String arc_source, String arc_target) {
		for (Arc a : m_arcs) {
			if ((a.get_sourceId().equals(arc_source)) && (a.get_targetId().equals(arc_target))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create then add a place in the petri net
	 * 
	 * @param id id of the place to add
	 * @param name name of the place to add
	 * @param initialMarking number of initial tokens of the place to add
	 */
	public void addPlace(String id, String name, int initialMarking) {
		if (!containsPlace(id)) {
			m_places.add(new Place(id, name, initialMarking));
		}
	}

	/**
	 * Create then add a place in the petri net
	 * 
	 * @param id id of the transition to ad
	 * @param name name of the transition to add
	 */
	public void addTransition(String id, String name) {
		if (!containsTransition(id)) {
			m_transitions.add(new Transition(id, name));
		}
	}

	/**
	 * Creat then add an arc in the etri net
	 * 
	 * @param id id of the arc to add
	 * @param source id of the source of the arc to add
	 * @param target id of the target of the arc to add
	 */
	public void addArc(String id, String source, String target) {
		if (!containsArc(source, target)) {
			m_arcs.add(new Arc(id, source, target));
		}
	}

	/**
	 * Convert the petri net into PNML format String
	 * 
	 * @return the PNML format String representing the petri net
	 */
	public String toPNML() {
		String ret = "<pnml xmlns=\"http://www.pnml.org/version-2009/grammar/pnml\">\n\t<net id=\"" + m_id + "\" type=\"http://www.pnml.org/version-2009/grammar/ptnet\">\n\t\t<name>\n"
				+ "\t\t\t<text>" + m_name + "</text>\n"
				+ "\t\t</name>\n"
				+ "\t\t<page id=\"unique-page\">";
		for (Place p : m_places) {
			ret += "\n"+p.toPNML();
		}
		for (Transition t : m_transitions) {
			ret += "\n"+t.toPNML();
		}
		for (Arc a : m_arcs) {
			ret += "\n"+a.toPNML();
		}
		ret += "\n\t\t</page>\n"
				+ "\t</net>\n"
				+ "</pnml>";
		return ret;
	}
}
