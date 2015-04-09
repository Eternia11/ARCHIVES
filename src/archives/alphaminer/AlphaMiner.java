package archives.alphaminer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import archives.log.Trace;
import archives.petrinet.*;

public class AlphaMiner {
	private PetriNet m_net = null;
	private String m_netFile = "petri.pnml";
	
	public AlphaMiner() {
		m_net = new PetriNet("net0", "AlphaNetARCHIVES");
	}
	
	private boolean isIn(ArrayList<Trace> log_part, String activity) {
		for (Trace t : log_part) {
			if (activity.equals(t.getActivity())) {
				return true;
			}
		}
		return false;
	}
	
	private class YClass {
		ArrayList<Pair<ArrayList<Integer>, ArrayList<Integer>>> m_Y = null;
		
		protected YClass() {
			m_Y = new ArrayList<Pair<ArrayList<Integer>, ArrayList<Integer>>>();
		}
		
		protected ArrayList<Pair<ArrayList<Integer>, ArrayList<Integer>>> get_Y() {
			return m_Y;
		}
		
		protected void add(Integer left, Integer right) {
			m_Y.add(Pair.of(new ArrayList<Integer>(), new ArrayList<Integer>()));
			m_Y.get(m_Y.size()-1).getLeft().add(left);
			m_Y.get(m_Y.size()-1).getRight().add(right);
		}
		
		protected YClass mergeLeft() {
			YClass new_Y = new YClass();
			for(int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> curr_left = m_Y.get(i).getLeft();
				ArrayList<Integer> new_right = new ArrayList<Integer>();
				
				for(int j = 0; j < m_Y.size(); j++) {
					if (curr_left.containsAll(m_Y.get(j).getLeft())) {
						new_right.addAll(m_Y.get(j).getRight());
						m_Y.remove(j);
						j--;
					}
				}
				
				new_Y.get_Y().add(Pair.of(new ArrayList<Integer>(curr_left), new ArrayList<Integer>(new_right)));
			}
			return new_Y;
		}
		
		protected YClass mergeRight() {
			YClass new_Y = new YClass();
			for(int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> curr_right = m_Y.get(i).getRight();
				ArrayList<Integer> new_left = new ArrayList<Integer>();
				
				for(int j = 0; j < m_Y.size(); j++) {
					if (curr_right.containsAll(m_Y.get(j).getRight())) {
						new_left.addAll(m_Y.get(j).getLeft());
						m_Y.remove(j);
						j--;
					}
				}
				
				new_Y.get_Y().add(Pair.of(new ArrayList<Integer>(new_left), new ArrayList<Integer>(curr_right)));
			}
			return new_Y;
		}
		
		protected void cleave(Integer index1, Integer index2) {
			for(int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> left = m_Y.get(i).getLeft();
				ArrayList<Integer> right = m_Y.get(i).getRight();
				if ((left.contains(index1)) && (left.contains(index2))) {
					m_Y.add(i, Pair.of(new ArrayList<Integer>(left), new ArrayList<Integer>(right)));
					left.remove(left.indexOf(index1));
					ArrayList<Integer> new_left = m_Y.get(i).getLeft();
					new_left.remove(new_left.indexOf(index2));
				}
				if ((right.contains(index1)) && (right.contains(index2))) {
					m_Y.add(i, Pair.of(new ArrayList<Integer>(left), new ArrayList<Integer>(right)));
					right.remove(right.indexOf(index1));
					ArrayList<Integer> new_right = m_Y.get(i).getRight();
					new_right.remove(new_right.indexOf(index2));
				}
			}
		}
		
		protected void addPlaces() {
			for (int i = 0 ; i < m_Y.size(); i++) {
				m_net.addPlace("p"+Integer.toString(i), "p"+Integer.toString(i), 0);
			}
		}
		
		protected void addArcs(ArrayList<String> T) {
			for (int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> left = m_Y.get(i).getLeft();
				ArrayList<Integer> right = m_Y.get(i).getRight();
				for (int j = 0; j < left.size(); j++) {
					m_net.addArc("in_a"+Integer.toString(i)+"-"+Integer.toString(j), T.get(left.get(j)), "p"+Integer.toString(i));
				}
				for (int j = 0; j < right.size(); j++) {
					m_net.addArc("out_a"+Integer.toString(i)+"-"+Integer.toString(j), "p"+Integer.toString(i), T.get(right.get(j)));
				}
			}
		}
		
		protected void display() {
			for (Pair<ArrayList<Integer>, ArrayList<Integer>> p : m_Y) {
				System.out.println(p.getLeft()+" | "+p.getRight());
			}
			System.out.println();
		}
	}
	
	public void run(List<Trace> logs) {
		ArrayList<String> T = new ArrayList<String>();
		// Tw
		for (Trace t : logs) {
			m_net.addTransition(t.getActivity(), t.getActivity());
			if (!T.contains(t.getActivity())) {
				T.add(t.getActivity());
			}
		}
		// Ti
		
		// To
		
		// Xw
		// reoganisation des logs rassemblés par timestamp
		ArrayList<ArrayList<Trace>> new_log = new ArrayList<ArrayList<Trace>>();
		String prev_time = "";
		for (int i = 0; i < logs.size(); i++) {
			Trace t = logs.get(i);
			if (!prev_time.equals(t.getTimestamp())) {
				new_log.add(new ArrayList<Trace>());
			}
			new_log.get(new_log.size()-1).add(t);
			prev_time = t.getTimestamp();
		}
		
		// 1 = >;
		int X[][] = new int[T.size()][T.size()];
		
		for (int[] row : X) {
			for (int x : row) {
				x = 0;
			}
		}
		
		for (int i = 0; i < T.size(); i++) {
			for (int j = 0; j < new_log.size(); j++) {
				for (int k = 0; k < T.size(); k++) {
					if (isIn(new_log.get(j), T.get(i))) {
						if ((j+1 < new_log.size()) && (isIn(new_log.get(j+1), T.get(k)))) {
							X[i][k] = 1;
						}
					}
				}
			}
		}
		
		// -1 = <-; 0 = #; 1 = ->; 2 = ||
		for (int i = 0; i < T.size(); i++) {
			for (int j = 0; j < T.size(); j++) {
				if ((X[i][j] == 1) && (X[j][i] == 1)) {
					X[i][j] = 2;
					X[j][i] = 2;
				}
				if ((X[i][j] == 1) && (X[j][i] == 0)) {
					X[i][j] = 1;
					X[j][i] = -1;
				}
				if ((X[i][j] == 0) && (X[j][i] == 1)) {
					X[i][j] = -1;
					X[j][i] = 1;
				}
				if (i == j) {
					X[i][j] = 0;
				}
			}
		}
		
		// Yw
		YClass Y = new YClass();
		
		for (int i = 0; i < T.size(); i++) {
			for (int j = i; j < T.size(); j++) {
				if (X[i][j] == 1) {
					Y.add(i, j);
				}
				if (X[i][j] == -1) {
					Y.add(j, i);
				}
			}
		}
		
		Y = Y.mergeLeft();
		Y = Y.mergeRight();
		
		for (int i = 0; i < T.size(); i++) {
			for (int j = i; j < T.size(); j++) {
				if (X[i][j] != 0) {
					Y.cleave(i, j);
				}
			}
		}
		
		//Pw
		m_net.addPlace("_in_", "_in_", 1);
		m_net.addPlace("_out_", "_out_", 1);
		Y.addPlaces();
		
		// Fw
		Y.addArcs(T);
		ArrayList<Trace> Ti = new_log.get(0);
		ArrayList<Trace> To = new_log.get(new_log.size()-1);
		for (int i = 0 ; i < Ti.size(); i++) {
			m_net.addArc("a_in"+Integer.toString(i), "_in_", Ti.get(i).getActivity());
		}
		for (int i = 0 ; i < To.size(); i++) {
			m_net.addArc("a_in"+Integer.toString(i), To.get(i).getActivity(), "_out_");
		}
		
		// alpha(W)
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(m_netFile, "UTF-8");
			writer.println(m_net.toPNML());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("The file "+m_netFile+" cannot be created/opened or does not have the UTF-8 encoding.");
			e.printStackTrace();
			System.exit(6);
		}
	}
}
