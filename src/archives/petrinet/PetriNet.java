package archives.petrinet;

import java.util.ArrayList;

public class PetriNet {
	private String m_id = "";
	private String m_name = "";
	private ArrayList<Place> m_places = null;
	private ArrayList<Transition> m_transitions = null;
	private ArrayList<Arc> m_arcs = null;

	public PetriNet() {
		m_places = new ArrayList<Place>();
		m_transitions = new ArrayList<Transition>();
		m_arcs = new ArrayList<Arc>();
	}

	public PetriNet(String id, String name) {
		m_id = id;
		m_name = name;
		m_places = new ArrayList<Place>();
		m_transitions = new ArrayList<Transition>();
		m_arcs = new ArrayList<Arc>();
	}

	public String get_id() {
		return m_id;
	}

	public String get_name() {
		return m_name;
	}

	public ArrayList<Place> get_places() {
		return m_places;
	}

	public ArrayList<Transition> get_transitions() {
		return m_transitions;
	}

	public ArrayList<Arc> get_arcs() {
		return m_arcs;
	}

	public boolean containsPlace(String place_id) {
		for (Place p : m_places) {
			if (p.get_id().equals(place_id)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsTransition(String transition_id) {
		for (Transition t : m_transitions) {
			if (t.get_id().equals(transition_id)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsArc(String arc_id) {
		for (Arc a : m_arcs) {
			if (a.get_id().equals(arc_id)) {
				return true;
			}
		}
		return false;
	}
	
	public void addPlace(String id, String name, int initialMarking) {
		if (!containsPlace(id)) {
			m_places.add(new Place(id, name, initialMarking));
		}
	}
	
	public void addTransition(String id, String name) {
		if (!containsTransition(id)) {
			m_transitions.add(new Transition(id, name));
		}
	}
	
	public void addArc(String id, String source, String target) {
		if (!containsArc(id)) {
			m_arcs.add(new Arc(id, source, target));
		}
	}
	
	public String toPNML() {
		String ret = "<pnml xmlns=\"http://www.pnml.org/version-2009/grammar/pnml\">\n\t<net id=\""
				+ m_id
				+ "\" type=\"http://www.pnml.org/version-2009/grammar/ptnet\">\n\t\t<name>\n\t\t\t<text>"
				+ m_name + "</text>\n\t\t</name>\n\t\t<page id=\"unique-page\"";
		for (Place p : m_places) {
			ret += "\n"+p.toPNML();
		}
		for (Transition t : m_transitions) {
			ret += "\n"+t.toPNML();
		}
		for (Arc a : m_arcs) {
			ret += "\n"+a.toPNML();
		}
		ret += "\n\t\t</page>\n\t</net>\n</pnml>";
		return ret;
	}
}
