package archives.alphaminer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import archives.log.Trace;
import archives.petrinet.*;

public class AlphaMiner {
	private PetriNet m_net = null;
	
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
			T.add(t.getActivity());
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
		
		for (int i = 0; i < T.size(); i++) {
			for (int j = 0; j < T.size(); j++) {
				System.out.print(X[i][j]+"\t");
			}
			System.out.println();
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
		Y.display();
		
		//Pw
		
		// Fw
		
		// alpha(W)
		
		System.out.println(m_net.toPNML());
	}
}
