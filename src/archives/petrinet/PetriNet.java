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

	public boolean contains(Place p) {
		for (Place pl : m_places) {
			if (p.equals(pl)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(Transition t) {
		for (Transition tr : m_transitions) {
			if (t.equals(tr)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(Arc a) {
		for (Arc ar : m_arcs) {
			if (a.equals(ar)) {
				return true;
			}
		}
		return false;
	}
	
	public void addPlace(String id, String name, int initialMarking) {
		m_places.add(new Place(id, name, initialMarking));
	}
	
	public void addTransition(String id, String name) {
		m_transitions.add(new Transition(id, name));
	}
	
	public void addArc(String id, String source, String target) {
		m_arcs.add(new Arc(id, source, target));
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
