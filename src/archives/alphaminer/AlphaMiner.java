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
			m_Y.get(m_Y.size() - 1).getLeft().add(left);
			m_Y.get(m_Y.size() - 1).getRight().add(right);
		}

		protected boolean containsPair(ArrayList<Integer> left,
				ArrayList<Integer> right) {
			for (Pair<ArrayList<Integer>, ArrayList<Integer>> pair : m_Y) {
				if ((left.containsAll(pair.getLeft()))
						&& (right.containsAll(pair.getRight()))) {
					return true;
				}
			}
			return false;
		}

		protected YClass mergeLeft() {
			YClass new_Y = new YClass();
			for (int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> curr_left = m_Y.get(i).getLeft();
				ArrayList<Integer> new_right = new ArrayList<Integer>();

				for (int j = 0; j < m_Y.size(); j++) {
					if (curr_left.containsAll(m_Y.get(j).getLeft())) {
						new_right.addAll(m_Y.get(j).getRight());
					}
				}

				if (!new_Y.containsPair(curr_left, new_right)) {
					new_Y.get_Y().add(
							Pair.of(new ArrayList<Integer>(curr_left),
									new ArrayList<Integer>(new_right)));
				}
			}
			return new_Y;
		}

		protected YClass mergeRight() {
			YClass new_Y = new YClass();
			for (int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> curr_right = m_Y.get(i).getRight();
				ArrayList<Integer> new_left = new ArrayList<Integer>();

				for (int j = 0; j < m_Y.size(); j++) {
					if (curr_right.containsAll(m_Y.get(j).getRight())) {
						new_left.addAll(m_Y.get(j).getLeft());
					}
				}

				if (!new_Y.containsPair(new_left, curr_right)) {
					new_Y.get_Y().add(
							Pair.of(new ArrayList<Integer>(new_left),
									new ArrayList<Integer>(curr_right)));
				}
			}
			return new_Y;
		}

		protected void cleave(Integer index1, Integer index2) {
			for (int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> left = m_Y.get(i).getLeft();
				ArrayList<Integer> right = m_Y.get(i).getRight();
				if ((left.contains(index1)) && (left.contains(index2))) {
					m_Y.add(i, Pair.of(new ArrayList<Integer>(left),
							new ArrayList<Integer>(right)));
					left.remove(left.indexOf(index1));
					ArrayList<Integer> new_left = m_Y.get(i).getLeft();
					new_left.remove(new_left.indexOf(index2));
				}
				if ((right.contains(index1)) && (right.contains(index2))) {
					m_Y.add(i, Pair.of(new ArrayList<Integer>(left),
							new ArrayList<Integer>(right)));
					right.remove(right.indexOf(index1));
					ArrayList<Integer> new_right = m_Y.get(i).getRight();
					new_right.remove(new_right.indexOf(index2));
				}
			}
		}

		protected void addPlaces() {
			for (int i = 0; i < m_Y.size(); i++) {
				m_net.addPlace("p" + Integer.toString(i),
						"p" + Integer.toString(i), 0);
			}
		}

		protected void addArcs(ArrayList<String> T) {
			for (int i = 0; i < m_Y.size(); i++) {
				ArrayList<Integer> left = m_Y.get(i).getLeft();
				ArrayList<Integer> right = m_Y.get(i).getRight();
				for (int j = 0; j < left.size(); j++) {
					m_net.addArc(
							"in_a" + Integer.toString(i) + "-"
									+ Integer.toString(j), T.get(left.get(j)),
							"p" + Integer.toString(i));
				}
				for (int j = 0; j < right.size(); j++) {
					m_net.addArc(
							"out_a" + Integer.toString(i) + "-"
									+ Integer.toString(j),
							"p" + Integer.toString(i), T.get(right.get(j)));
				}
			}
		}
	}

	// run the Alpha Algorithm following the method given in the wiki page :
	// http://en.wikipedia.org/wiki/Alpha_algorithm
	// loops : enable or disable the add of loops of size 0 and 1
	// merge_type : 0 : generalization, 1 : specialization, 2 : average
	public void run(List<Trace> logs, int merge_type, boolean loops) {
		ArrayList<String> T = new ArrayList<String>(); // list of the
														// transitions of the
														// petri net
		ArrayList<String> caseID = new ArrayList<String>(); // list of the
															// caseID of the log
															// file

		// construction of Tw
		for (Trace t : logs) {
			// add the different transitions to the petri net and to the list
			m_net.addTransition(t.getActivity(), t.getActivity());
			if (!T.contains(t.getActivity())) {
				T.add(t.getActivity());
			}
			// add the different caseID
			if (!caseID.contains(t.getCaseID())) {
				caseID.add(t.getCaseID());
			}
		}

		// sort the log file by case, then by timestamp and store it into
		// new_log
		ArrayList<ArrayList<ArrayList<Trace>>> new_log = new ArrayList<ArrayList<ArrayList<Trace>>>();
		for (int c = 0; c < caseID.size(); c++) {
			String prev_time = "";
			new_log.add(new ArrayList<ArrayList<Trace>>());

			for (int i = 0; i < logs.size(); i++) {
				Trace t = logs.get(i);
				if (caseID.get(c).equals(t.getCaseID())) {
					if (!prev_time.equals(t.getTimestamp())) {
						new_log.get(c).add(new ArrayList<Trace>());
					}
					new_log.get(c).get(new_log.get(c).size() - 1).add(t);
					prev_time = t.getTimestamp();
				}
			}
		}

		// construction of Xw
		ArrayList<Integer[][]> X = new ArrayList<Integer[][]>();
		for (int c = 0; c < new_log.size(); c++) {
			Integer X_temp[][] = new Integer[T.size()][T.size()];

			for (int i = 0; i < T.size(); i++) {
				for (int j = 0; j < T.size(); j++) {
					X_temp[i][j] = 0;
				}
			}

			// 1 represents > between 2 transitions in the matrix X_temp
			for (int i = 0; i < new_log.get(c).size(); i++) {
				if (i + 1 < new_log.get(c).size()) {
					for (int j1 = 0; j1 < new_log.get(c).get(i).size(); j1++) {
						int a1 = T.indexOf(new_log.get(c).get(i).get(j1)
								.getActivity());
						for (int j2 = 0; j2 < new_log.get(c).get(i + 1).size(); j2++) {
							int a2 = T.indexOf(new_log.get(c).get(i + 1)
									.get(j2).getActivity());
							X_temp[a1][a2] = 1;
						}
					}
				}
			}

			// set the activities which have the same timestamp into parallel
			// mode
			for (ArrayList<Trace> time_first : new_log.get(c)) {
				for (int i = 0; i < time_first.size(); i++) {
					int a1 = T.indexOf(time_first.get(i).getActivity());
					for (int j = 0; j < time_first.size(); j++) {
						if (i != j) {
							int a2 = T.indexOf(time_first.get(j).getActivity());
							if (a1 != a2)
								X_temp[a1][a2] = 2;
						}
					}
				}
			}

			X.add(X_temp);
		}

		Integer Xw[][] = new Integer[T.size()][T.size()];
		Integer loop0[] = new Integer[T.size()];
		for (int i = 0; i < T.size(); i++) {
			loop0[i] = 0;
		}

		// "parameter" of this alpha algorithm : the way matrices will be merged into > relations
		switch (merge_type) {
		case 0 :
			// this way is a total generalization
			for (int i = 0; i < T.size(); i++) {
				for (int j = 0; j < T.size(); j++) {
					Xw[i][j] = 0;
					for (int c = 0; c < new_log.size(); c++) {
						if (X.get(c)[i][j] == 1) {
							Xw[i][j] = 1;
						}
						// preserve parallelism
						if (X.get(c)[i][j] == 2) {
							Xw[i][j] = 1;
							Xw[j][i] = 1;
						}
					}
				}
			}
		case 1 :
			// this way is a total specialization
			for (int i = 0; i < T.size(); i++) {
				for (int j = 0; j < T.size(); j++) {
					Xw[i][j] = 0;
					boolean same = true;
					Integer previous = X.get(0)[i][j];
					for (int c = 0; same && c < new_log.size(); c++) {
						if (X.get(c)[i][j] != previous) {
							same = false;
						}
					}
					if (same) {
						Xw[i][j] = previous;
					}
				}
			}
		default :
			// this way is an average of presence of succession of actions in the different cases
			for (int i = 0; i < T.size(); i++) {
				for (int j = 0; j < T.size(); j++) {
					int avg[] = {0, 0, 0};
					
					for (int c = 0; c < new_log.size(); c++) {
						avg[X.get(c)[i][j]]++;
					}
					
					if ((avg[1] > avg[0]) && (avg[1] > avg[2])) {
						Xw[i][j] = 1;
					} else if ((avg[2] > avg[1]) && (avg[2] > avg[1])) {
						Xw[i][j] = 2;
					} else {
						Xw[i][j] = 0;
					}
				}
			}
		}
		
		// transform the > relations into ->, # and || relations
		for (int i = 0; i < T.size(); i++) {
			for (int j = 0; j < T.size(); j++) {
				if (i != j) {
					if ((Xw[i][j] == 1) && (Xw[j][i] == 1)) {
						Xw[i][j] = 2;
						Xw[j][i] = 2;
					}
					if ((Xw[i][j] == 1) && (Xw[j][i] == 0)) {
						Xw[i][j] = 1;
						Xw[j][i] = -1;
					}
					if ((Xw[i][j] == 0) && (Xw[j][i] == 1)) {
						Xw[i][j] = -1;
						Xw[j][i] = 1;
					}
					if ((Xw[i][j] == 2) && (Xw[j][i] != 2)) {
						Xw[j][i] = 2;
					}
				} else {
					if (Xw[i][j] == 1) {
						loop0[i] = 1;
					}
					Xw[i][j] = 0;
				}
			}
		}

		// construction of Yw
		YClass Y = new YClass();

		for (int i = 0; i < T.size(); i++) {
			for (int j = i; j < T.size(); j++) {
				if (i != j) {
					if (Xw[i][j] == 1) {
						Y.add(i, j);
					}
					if (Xw[i][j] == -1) {
						Y.add(j, i);
					}
				}
			}
		}

		Y = Y.mergeLeft();
		Y = Y.mergeRight();

		for (int i = 0; i < T.size(); i++) {
			for (int j = i; j < T.size(); j++) {
				if (Xw[i][j] != 0) {
					Y.cleave(i, j);
				}
			}
		}

		// construction of Ti
		m_net.addPlace("_in_", "_in_", 1);

		// construction of To
		m_net.addPlace("_out_", "_out_", 0);

		// construction of Pw
		Y.addPlaces();

		// construction of Fw
		Y.addArcs(T);
		for (int c = 0; c < new_log.size(); c++) {
			ArrayList<Trace> Ti = new_log.get(c).get(0);
			ArrayList<Trace> To = new_log.get(c).get(new_log.get(c).size() - 1);
			for (int i = 0; i < Ti.size(); i++) {
				m_net.addArc("a_in-" + Integer.toString(i) + "_c" + c, "_in_",
						Ti.get(i).getActivity());
			}
			for (int i = 0; i < To.size(); i++) {
				m_net.addArc("a_out-" + Integer.toString(i) + "_c" + c,
						To.get(i).getActivity(), "_out_");
			}
		}

		// construction of loops
		if (loops) {
			// loops of size 0
			for (int i = 0; i < T.size(); i++) {
				if (loop0[i] == 1) {
					m_net.addPlace("loop0_" + i, "loop0_" + i, 0);
					m_net.addArc("in_loop0_" + i, T.get(i), "loop0_" + i);
					m_net.addArc("out_loop0_" + i, "loop0_" + i, T.get(i));
				}
			}
	
			// loops of size 1
			for (ArrayList<ArrayList<Trace>> case_first : new_log) {
				for (int i = 0; i < case_first.size(); i++) {
					if (i + 2 < case_first.size()) {
						for (int j1 = 0; j1 < case_first.get(i).size(); j1++) {
							int a1 = T.indexOf(case_first.get(i).get(j1)
									.getActivity());
							if (isIn(case_first.get(i + 2), T.get(a1))) {
								for (int j2 = 0; j2 < case_first.get(i + 1).size(); j2++) {
									int a2 = T.indexOf(case_first.get(i + 1)
											.get(j2).getActivity());
									// loop between T.get(a1) and T.get(a2)
									m_net.addPlace("loop1_p1_" + a1 + "-" + a2,
											"loop1_p1_" + a1 + "-" + a2, 0);
									m_net.addPlace("loop1_p2_" + a1 + "-" + a2,
											"loop1_p2_" + a1 + "-" + a2, 0);
									m_net.addArc("loop1_a1_" + a1 + "-" + a2,
											T.get(a1), "loop1_p1_" + a1 + "-" + a2);
									m_net.addArc("loop1_a2_" + a1 + "-" + a2,
											"loop1_p1_" + a1 + "-" + a2, T.get(a2));
									m_net.addArc("loop1_a3_" + a1 + "-" + a2,
											T.get(a2), "loop1_p2_" + a1 + "-" + a2);
									m_net.addArc("loop1_a4_" + a1 + "-" + a2,
											"loop1_p2_" + a1 + "-" + a2, T.get(a1));
								}
							}
						}
					}
				}
			}
		} // end loops

		// construction of alpha(W)

		PrintWriter writer;
		try {
			writer = new PrintWriter(m_netFile, "UTF-8");
			writer.println(m_net.toPNML());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out
					.println("The file "
							+ m_netFile
							+ " cannot be created/opened or does not have the UTF-8 encoding.");
			e.printStackTrace();
			System.exit(6);
		}
	}
}
