package archives.alphaminer;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import archives.petrinet.PetriNet;
import archives.workflow.Flow;
import archives.workflow.Gateway;
import archives.workflow.Workflow;

/**
 * Class used to represent the couples of set of tasks
 * named Y in the wiki page
 * 
 * @author Alan BENIER
 * @see http://en.wikipedia.org/wiki/Alpha_algorithm
 */
class YClass {
	private ArrayList<Pair<ArrayList<Integer>, ArrayList<Integer>>> m_Y = null;		// list of couples of set of the index of the tasks

	/**
	 * Constructor
	 */
	protected YClass() {
		m_Y = new ArrayList<Pair<ArrayList<Integer>, ArrayList<Integer>>>();
	}

	/**
	 * Getter
	 * 
	 * @return the list of couples of set of the index of the tasks
	 */
	protected ArrayList<Pair<ArrayList<Integer>, ArrayList<Integer>>> get_Y() {
		return m_Y;
	}

	/**
	 * Add a new couple of tasks
	 * 
	 * @param left index of a task of the couple
	 * @param right index of the other task of the couple
	 */
	protected void add(Integer left, Integer right) {
		// create and add the new couple
		m_Y.add(Pair.of(new ArrayList<Integer>(), new ArrayList<Integer>()));
		// add the value to the couple we have just created
		m_Y.get(m_Y.size() - 1).getLeft().add(left);
		m_Y.get(m_Y.size() - 1).getRight().add(right);
	}

	/**
	 * Check if a couple of sets is contained
	 * in this object
	 * 
	 * @param left left set
	 * @param right right set
	 * @return true if the couple is contained in this object
	 */
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

	/**
	 * Merge the right part of the couples that have
	 * the same left part
	 * 
	 * @return the same object with only one occurrence of each left part set
	 */
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

	/**
	 * Merge the left part of the couples that have
	 * the same right part
	 * 
	 * @return the same object with only one occurrence of each right part set
	 */
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

	/**
	 * Check if the two given tasks are in the left
	 * or the right part of a couple, and if it is
	 * the case, then delete this couple and
	 * create two new alike the deleted one
	 * but the side which contained the two tasks
	 * are the same without the first or the second task
	 * 
	 * @param index1 index of a task
	 * @param index2 index of a task
	 */
	protected void cleave(Integer index1, Integer index2) {
		for (int i = 0; i < m_Y.size(); i++) {
			ArrayList<Integer> left = m_Y.get(i).getLeft();
			ArrayList<Integer> right = m_Y.get(i).getRight();
			// if the tasks are in the left part
			if ((left.contains(index1)) && (left.contains(index2))) {
				// we add the new couple
				m_Y.add(i, Pair.of(new ArrayList<Integer>(left),
						new ArrayList<Integer>(right)));
				// then we delete one of the index in each couple (the new one and the old one)
				left.remove(left.indexOf(index1));
				ArrayList<Integer> new_left = m_Y.get(i).getLeft();
				new_left.remove(new_left.indexOf(index2));
			}
			// if the tasks are in the right part
			if ((right.contains(index1)) && (right.contains(index2))) {
				// we add the new couple
				m_Y.add(i, Pair.of(new ArrayList<Integer>(left),
						new ArrayList<Integer>(right)));
				// then we delete one of the index in each couple (the new one and the old one)
				right.remove(right.indexOf(index1));
				ArrayList<Integer> new_right = m_Y.get(i).getRight();
				new_right.remove(new_right.indexOf(index2));
			}
		}
	}

	/**
	 * Add the places calculated with the alpha algorithm
	 * that are contained in this object
	 * 
	 * @param net petri net to modify
	 * @return the petri net with the new places
	 * @see Y
	 */
	protected PetriNet addPlaces(PetriNet net) {
		for (int i = 0; i < m_Y.size(); i++) {
			net.addPlace("p" + Integer.toString(i),
					"p" + Integer.toString(i), 0);
		}
		return net;
	}

	/**
	 * Add the arcs calculated with the alpha algorithm
	 * that are contained in this object
	 * 
	 * @param net petri net to modify
	 * @param T name of the taks (= transitions)
	 * @return the petri net with the new arcs
	 * @see F
	 */
	protected PetriNet addArcs(PetriNet net, ArrayList<String> T) {
		for (int i = 0; i < m_Y.size(); i++) {
			ArrayList<Integer> left = m_Y.get(i).getLeft();
			ArrayList<Integer> right = m_Y.get(i).getRight();
			for (int j = 0; j < left.size(); j++) {
				net.addArc(
						"in_a" + Integer.toString(i) + "-"
								+ Integer.toString(j), T.get(left.get(j)),
						"p" + Integer.toString(i));
			}
			for (int j = 0; j < right.size(); j++) {
				net.addArc(
						"out_a" + Integer.toString(i) + "-"
								+ Integer.toString(j),
						"p" + Integer.toString(i), T.get(right.get(j)));
			}
		}
		return net;
	}

	/**
	 * Add the flows corresponding to the log file
	 * into the workflow
	 * 
	 * @param workflow workflow to modify
	 * @param T names of the tasks
	 * @param resource current resource who executes the tasks
	 * @return the workflow with the lane corresponding to the current resources filled with appropriate tasks
	 */
	protected Workflow addFlows(Workflow workflow, ArrayList<String> T, String resource) {
		for (int i = 0; i < m_Y.size(); i++) {
			ArrayList<Integer> left = m_Y.get(i).getLeft();
			ArrayList<Integer> right = m_Y.get(i).getRight();

			workflow.get_process(0).addActivity(
					new Gateway(resource+"_"+"G" + i, resource+"_"+"G" + i, resource));

			for (int j = 0; j < left.size(); j++) {
				workflow.get_process(0).addFlow(
						new Flow("in_a" + i + "-" + j, resource+"_"+T.get(left.get(j)),
								resource+"_"+"G" + i));
			}
			for (int j = 0; j < right.size(); j++) {
				workflow.get_process(0)
						.addFlow(
								new Flow("out_a" + i + "-" + j, resource+"_"+"G"
										+ i, resource+"_"+T.get(right
										.get(j))));
			}
		}
		return workflow;
	}
}
