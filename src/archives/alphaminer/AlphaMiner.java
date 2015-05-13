package archives.alphaminer;

import java.util.ArrayList;
import java.util.List;

import archives.log.Trace;
import archives.petrinet.*;
import archives.workflow.*;

/**
 * Tool to run the Alpha Algorithm
 * 
 * @author Alan BENIER
 * @see http://en.wikipedia.org/wiki/Alpha_algorithm
 * @see http://0agr.ru/wiki/index.php/Alpha_Algorithm
 */
public class AlphaMiner {
	/**
	 * Check if an activity is contained in a log data
	 * 
	 * @param log_part log data
	 * @param activity activity to check
	 * @return true if the activity was found in the log data
	 */
	private static boolean isIn(ArrayList<Trace> log_part, String activity) {
		for (Trace t : log_part) {
			if (activity.equals(t.getActivity())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Run the classic alpha algorithm on the log data
	 * 
	 * @param logs log data
	 * @param merge_type "parameter" of the alpha algorithm 0 : generalization, 1 : specialization, 2 : average
	 * @param loops enable or disable loops of size 0 and 1
	 * @return the petri net resulting of the alpha algorithm
	 */
	public static PetriNet petriNet(List<Trace> logs, int merge_type, boolean loops) {
		PetriNet net = new PetriNet("net0", "AlphaNetARCHIVES");
		// list of the transitions of the petri net
		ArrayList<String> T = new ArrayList<String>();
		// list of the caseID of the log file
		ArrayList<String> caseID = new ArrayList<String>();

		// construction of Tw
		for (Trace t : logs) {
			// add the different transitions to the petri net and to the list
			net.addTransition(t.getActivity(), t.getActivity());
			if (!T.contains(t.getActivity())) {
				T.add(t.getActivity());
			}
			// add the different caseID
			if (!caseID.contains(t.getCaseID())) {
				caseID.add(t.getCaseID());
			}
		}

		// gather the log lines which have the same caseID
		ArrayList<ArrayList<Trace>> new_log = new ArrayList<ArrayList<Trace>>();
		for (int c = 0; c < caseID.size(); c++) {
			new_log.add(new ArrayList<Trace>());

			for (int i = 0; i < logs.size(); i++) {
				Trace t = logs.get(i);
				if (caseID.get(c).equals(t.getCaseID())) {
					new_log.get(c).add(t);
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
			// we set the relation > between each task and the one which follows it
			for (int i = 0; i < new_log.get(c).size(); i++) {
				if (i + 1 < new_log.get(c).size()) {
					int a1 = T.indexOf(new_log.get(c).get(i).getActivity());
					int a2 = T.indexOf(new_log.get(c).get(i + 1).getActivity());
					X_temp[a1][a2] = 1;
				}
			}

			X.add(X_temp);
		}

		// see Xw in the wiki link
		Integer Xw[][] = new Integer[T.size()][T.size()];
		// carry the loops of size 0
		Integer loop0[] = new Integer[T.size()];
		for (int i = 0; i < T.size(); i++) {
			loop0[i] = 0;
		}

		// "parameter" of this alpha algorithm : the way matrices will be merged into > relations
		switch (merge_type) {
		case 0:
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
		case 1:
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
		default:
			// this way is an average of presence of succession of actions in
			// the different cases
			for (int i = 0; i < T.size(); i++) {
				for (int j = 0; j < T.size(); j++) {
					int avg[] = { 0, 0, 0 };

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

		// this way, we have the couples of maximal sets of tasks
		// we gather all sets which are the same
		Y = Y.mergeLeft();
		Y = Y.mergeRight();

		// then we separates the sets which doesn't fit the definition
		// to make them fit the definition in a minimal number of fractures
		for (int i = 0; i < T.size(); i++) {
			for (int j = i; j < T.size(); j++) {
				if (Xw[i][j] != 0) {
					Y.cleave(i, j);
				}
			}
		}

		// construction of Ti
		net.addPlace("_in_", "_in_", 1);

		// construction of To
		net.addPlace("_out_", "_out_", 0);

		// construction of Pw
		net = Y.addPlaces(net);

		// construction of Fw
		net = Y.addArcs(net, T);
		for (int c = 0; c < new_log.size(); c++) {
			Trace Ti = new_log.get(c).get(0);
			Trace To = new_log.get(c).get(new_log.get(c).size() - 1);
			net.addArc("a_in_c" + c, "_in_", Ti.getActivity());
			net.addArc("a_out_c" + c, To.getActivity(), "_out_");
		}

		// construction of loops
		if (loops) {
			// loops of size 0
			for (int i = 0; i < T.size(); i++) {
				if (loop0[i] == 1) {
					net.addPlace("loop0_" + i, "loop0_" + i, 0);
					net.addArc("in_loop0_" + i, T.get(i), "loop0_" + i);
					net.addArc("out_loop0_" + i, "loop0_" + i, T.get(i));
				}
			}

			// loops of size 1
			for (ArrayList<Trace> case_first : new_log) {
				for (int i = 0; i < case_first.size(); i++) {
					if (i + 2 < case_first.size()) {
						int a1 = T.indexOf(case_first.get(i).getActivity());
						if (isIn(case_first, T.get(a1))) {
							int a2 = T.indexOf(case_first.get(i + 1).getActivity());
							// loop between T.get(a1) and T.get(a2)
							net.addPlace("loop1_p1_" + a1 + "-" + a2,
									"loop1_p1_" + a1 + "-" + a2, 0);
							net.addPlace("loop1_p2_" + a1 + "-" + a2,
									"loop1_p2_" + a1 + "-" + a2, 0);
							net.addArc("loop1_a1_" + a1 + "-" + a2,
									T.get(a1), "loop1_p1_" + a1 + "-"
											+ a2);
							net.addArc("loop1_a2_" + a1 + "-" + a2,
									"loop1_p1_" + a1 + "-" + a2,
									T.get(a2));
							net.addArc("loop1_a3_" + a1 + "-" + a2,
									T.get(a2), "loop1_p2_" + a1 + "-"
											+ a2);
							net.addArc("loop1_a4_" + a1 + "-" + a2,
									"loop1_p2_" + a1 + "-" + a2,
									T.get(a1));
						}
					}
				}
			}
		} // end loops

		// construction of alpha(W)
		return net;
	}


	/**
	 * Build a workflow that represents the situations
	 * described in the log file, following the
	 * alpha algorithm
	 * 
	 * under construction and testing
	 * 
	 * @param logs log data
	 * @param merge_type "parameter" of the alpha algorithm 0 : generalization, 1 : specialization, 2 : average
	 * @param loops enable or disable loops of size 0 and 1
	 * @return a workflow meant to represent the behavior of each resource
	 */
	public static Workflow workflow(List<Trace> logs, int merge_type, boolean loops) {
		Workflow workflow = new Workflow("wf_test", "wf_test");
		workflow.addProcess(new archives.workflow.Process("pr_archives",
				"pr_archives"));
		workflow
				.addPool(new Pool("po_archives", "po_archives", "pr_archives"));

		ArrayList<String> caseID = new ArrayList<String>(); // list of the
		// caseID of the log file

		ArrayList<String> resources = new ArrayList<String>();
		for (Trace t : logs) {
			if (!resources.contains(t.getSender()))
				resources.add(t.getSender());
			if (!resources.contains(t.getReceiver()))
				resources.add(t.getReceiver());
			// add the different caseID
			if (!caseID.contains(t.getCaseID())) {
				caseID.add(t.getCaseID());
			}
		}

		// we handle each resources separately
		for (String r : resources) {
			// gather the log lines which have the same caseID
			ArrayList<ArrayList<Trace>> new_log = new ArrayList<ArrayList<Trace>>();
			for (int c = 0; c < caseID.size(); c++)
				new_log.add(new ArrayList<Trace>());
			for (int i = 0; i < logs.size(); i++) {
				Trace t = logs.get(i);
				if (r.equals(t.getSender()))
					new_log.get(caseID.indexOf(t.getCaseID())).add(t);
			}

			ArrayList<String> T = new ArrayList<String>();

			workflow.get_pool(0).addLane(new Lane(r));
			// construction of Tw
			for (Trace t : logs) {
				if (r.equals(t.getSender())) {
					/*m_workflow.get_process(0).addActivity(
							new ActivityLane(r + "_" + t.getActivity(), t
									.getActivity(), r));*/
					if (!T.contains(t.getActivity())) {
						T.add(t.getActivity());
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
						int a1 = T.indexOf(new_log.get(c).get(i).getActivity());
						int a2 = T.indexOf(new_log.get(c).get(i + 1)
								.getActivity());
						X_temp[a1][a2] = 1;
					}
				}

				X.add(X_temp);
			}

			Integer Xw[][] = new Integer[T.size()][T.size()];
			Integer loop0[] = new Integer[T.size()];
			for (int i = 0; i < T.size(); i++) {
				loop0[i] = 0;
			}

			// "parameter" of this alpha algorithm : the way matrices will be
			// merged
			// into > relations
			switch (merge_type) {
			case 0:
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
			case 1:
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
			default:
				// this way is an average of presence of succession of actions
				// in
				// the different cases
				for (int i = 0; i < T.size(); i++) {
					for (int j = 0; j < T.size(); j++) {
						int avg[] = { 0, 0, 0 };

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
			
			// construction of Tw
			for (int i = 0; i < T.size(); i++) {
				for (int j = i; j < T.size(); j++) {
					if (Xw[i][j] != 0) {
						workflow.get_process(0).addActivity(new ActivityLane(r + "_" + T.get(i), T.get(i), r));
						workflow.get_process(0).addActivity(new ActivityLane(r + "_" + T.get(j), T.get(j), r));
					}
				}
			}

			// construction of Ti
			workflow.get_process(0).addActivity(new ActivityStartLane(r+"_start_", r));
			for (int c=0; c<new_log.size(); c++) {
				if (!new_log.get(c).isEmpty())
					workflow.get_process(0).addFlow(new Flow("f"+c+r+"_start_", r+"_start_", r+"_"+new_log.get(c).get(0).getActivity()));
			}

			// construction of To
			workflow.get_process(0).addActivity(new ActivityEndLane(r+"_end_", r));
			for (int c=0; c<new_log.size(); c++) {
				if (!new_log.get(c).isEmpty())
					workflow.get_process(0).addFlow(new Flow("f"+c+r+"_end_", r+"_"+new_log.get(c).get(new_log.get(c).size()-1).getActivity(), r+"_end_"));
			}

			// construction of Pw

			// construction of Fw
			workflow = Y.addFlows(workflow, T, r);

			// construction of loops
			if (loops) {
				// loops of size 0
				for (int i = 0; i < T.size(); i++) {
					if (loop0[i] == 1) {
						workflow.get_process(0).addFlow(
								new Flow("out_loop0_" + i, T.get(i), T.get(i)));
					}
				}

				// loops of size 1
				for (ArrayList<Trace> case_first : new_log) {
					for (int i = 0; i < case_first.size(); i++) {
						if (i + 2 < case_first.size()) {
							int a1 = T.indexOf(case_first.get(i).getActivity());
							if (case_first.get(i + 2).equals(T.get(a1))) {
								int a2 = T.indexOf(case_first.get(i + 1)
										.getActivity());
								// loop between T.get(a1) and T.get(a2)
								workflow.get_process(0).addFlow(
										new Flow("loop1_a1_" + a1 + "-" + a2, T
												.get(a1), T.get(a2)));
								workflow.get_process(0).addFlow(
										new Flow("loop1_a2_" + a1 + "-" + a2, T
												.get(a2), T.get(a1)));
							}
						}
					}
				}
			}
		} // end loops

		// construction of alpha(W)
		return workflow;
	}
}
