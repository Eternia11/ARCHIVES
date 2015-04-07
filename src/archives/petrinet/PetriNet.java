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

}
