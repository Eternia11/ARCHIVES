package archives.tools;

import archives.graph.Edge;
import archives.graph.Graph;
import archives.graph.Node;
import archives.log.Trace;
import archives.petrinet.PetriNet;
import archives.workflow.Workflow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Color;

import org.apache.commons.csv.*;

/**
 * This class contains several tools meant to be used in ARCHIVES
 * project that aims to generate GAMA code from archives
 * 
 * @author Alan BENIER
 */
public class Tools {
	private final static String m_caseID = "Lieu";						// name of the column of the caseID in log file
	private final static String m_timestamp = "dd/MM/yyyy HH:mm";		// name of the column of the timestamp in log file
	private final static String m_performative = "Performative";		// name of the column of the performative in log file
	private final static String m_sender = "Sender";					// name of the column of the sender in log file
	private final static String m_receiver = "Receiver";				// name of the column of the receiver in log file
	private final static String m_activity = "Objet";					// name of the column of the activity in log file
	private final static String m_system = "System";					// name of the neutral or environmental resource
	private final static String m_dateFormat = "dd/MM/yyyy HH:mm";		// date format used in the timestamp column in the log file

	/**
	 * Import a log file read in .csv format
	 * 
	 * A log file must contain 6 column (no precise order or name) representing :
	 * the caseID, the timestamp, the performative, the sender, the receiver
	 * and the activity for each line
	 * 
	 * @param logs path of the log file to import (must be in .csv format)
	 * @return the list of lines representing the log file
	 */
	public static ArrayList<Trace> readLogFile(String logs) {
		CSVParser parser = null;
		ArrayList<Trace> traces = new ArrayList<Trace>();
		// define the char which separate the fields in the log file
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(',');
		try {
			// parse the log file
			parser = new CSVParser(new FileReader(logs), format);
			for (CSVRecord record : parser) {
				// read a line of the log file
				Trace t = new Trace(record.get(m_caseID),
						record.get(m_timestamp), record.get(m_performative),
						record.get(m_sender), record.get(m_receiver),
						record.get(m_activity));
				traces.add(t);
			}
			parser.close();
		} catch (IOException e) {
			// if the log file cannot be opened
			System.out.println("Error. Could not read the log file " + logs + ".\n"
							+ "Make sure to use the right column names : 'CaseID', 'Timestamp', 'Performative', 'Sender', 'Receiver', 'Activity'.");
			e.printStackTrace();
			System.exit(2);
		}
		
		return traces;
	}

	/**
	 * Print the log data imported from a log file
	 * 
	 * @param traces log data to print
	 */
	public static void displayLogs(ArrayList<Trace> traces) {
		for (int i = 0; i < traces.size(); i++)
			System.out.println(traces.get(i));
	}

	/**
	 * Builds and returns the graph of interactions of which
	 * have the given performative between resources
	 * regarding the cases described in the log file
	 * 
	 * @param traces log data
	 * @param performative FIPA-ACL performative
	 * @return the graph of interactions between the resources
	 */
	public static Graph buildPerformativeGraph(ArrayList<Trace> traces, String performative) {
		Graph g = new Graph();
		
		for (int i = 0; i < traces.size(); i++) {
			Trace t = traces.get(i);
			// we handle only the lines which have the right performative
			if (t.getPerformative().equals(performative)) {
				String sender = t.getSender();
				String receiver = t.getReceiver();
				
				// we add the nodes (if they are already in the graph, they are not added)
				g.add_node(sender);
				g.add_node(receiver);
				// add the current edge or increase its weight by 1
				if (!g.contains_edge(sender, receiver))
					g.add_edge(sender, receiver);
				else
					g.increase_weight(sender, receiver);
				}
		}
		
		return g;
	}

	/**
	 * Export a graph into a graphML file
	 * 
	 * @param g graph to export
	 * @param file path of the exported file
	 */
	public static void exportToGraphml(Graph g, String file) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			writer.println(g.toGRAPHML());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// if the file cannot be opened or created
			System.out.println("The file " + file + " cannot be created/opened or does not have the UTF-8 encoding.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Build the list of chains of resources who are
	 * in a line as receiver with the given performative
	 * A resource is linked to another iff the previous
	 * resource who was the receiver is now the sender
	 * 
	 * @param traces log data
	 * @param performative FIPA-ACL performative
	 * @return a list of chains of resources
	 */
	public static ArrayList<ArrayList<String>> buildPerformativeChainList(ArrayList<Trace> traces, String performative) {
		ArrayList<ArrayList<String>> chainList = new ArrayList<ArrayList<String>>();
		
		// we assume that the log is sorted from the earliest timestamp to the later
		// and that an action cannot be delegated from and to the same agents
		// two times in a same case
		ArrayList<Integer> nb_occ = new ArrayList<Integer>();
		for (int i = 0; i < traces.size(); i++) {
			Trace t = traces.get(i);
			if (t.getPerformative().equals(performative)) {
				String action = t.getActivity();
				ArrayList<String> new_chain = new ArrayList<String>();
				String sender = t.getSender();
				String receiver = t.getReceiver();
				String last_receiver = receiver;

				new_chain.add(action);
				new_chain.add(sender);
				// if the neutral resource interferes then we break the chain
				if (receiver.equals(m_system)) {
					break;
				}
				new_chain.add(receiver);

				for (int j = 0; j < traces.size(); j++) {
					if ((traces.get(j).getActivity().equals(action))
							&& traces.get(j).getPerformative().equals(performative)) {
						// TODO g�rer le caseID
						sender = traces.get(j).getSender();
						receiver = traces.get(j).getReceiver();

						if (last_receiver.equals(sender)) {
							if (receiver.equals(m_system)) {
								break;
							}
							new_chain.add(receiver);

							last_receiver = receiver;
						}
					}
				}

				// if the action chain exists already then we add 1 to the occurence counter
				if (!new_chain.isEmpty()) {
					boolean same_action_chain = false;
					int index_where_same = 0;
					for (int j = 0; j < chainList.size()
							&& !same_action_chain; j++) {
						if (new_chain.size() == chainList.get(j)
								.size()) {
							same_action_chain = true;
							for (int k = 0; k < new_chain.size()
									&& same_action_chain; k++) {
								if (!new_chain.get(k).equals(
										chainList.get(j).get(k))) {
									same_action_chain = false;
								}
							}
							index_where_same = j;
						}
					}
					if (!same_action_chain) {
						chainList.add(new_chain);
						nb_occ.add(1);
					} else {
						nb_occ.set(index_where_same,
								nb_occ.get(index_where_same) + 1);
					}
				}
			}
		}

		// prints the action chains
		if (nb_occ.size() != chainList.size()) {
			System.out.println("Error occured when mining the action delegation chains.");
			System.exit(4);
		}
		for (int i = 0; i < chainList.size(); i++) {
			if (chainList.get(i).size() > 3) {
				System.out.print("Action \"" + chainList.get(i).get(0)
						+ "\" :");
				for (int j = 1; j < chainList.get(i).size(); j++) {
					System.out.print(" -> " + chainList.get(i).get(j));
				}
				System.out.println(" found " + nb_occ.get(i) + " time(s)");
			}
		}
		
		return chainList;
	}

	/**
	 * Clusters the nodes of the graph g and assign
	 * a color to each cluster and then color
	 * the nodes with it
	 * For now two nodes are in the same cluster
	 * iff they have the same outgoing edges
	 * and the same incoming edges
	 * 
	 * @param g graph to color according the clusters
	 */
	public static void buildClusterList(Graph g) {
		ArrayList<ArrayList<String>> clusterList = new ArrayList<ArrayList<String>>();
		ArrayList<Node> nodes = g.get_nodes();
		ArrayList<ArrayList<String>> targets = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> sources = new ArrayList<ArrayList<String>>();

		// we build the list of targets and sources of each node
		for (int i = 0; i < nodes.size(); i++) {
			targets.add(new ArrayList<String>());
			sources.add(new ArrayList<String>());

			ArrayList<Edge> outgoingEdges = new ArrayList<Edge>(g.outgoingEdgesOf(nodes.get(i)));
			ArrayList<Edge> incomingEdges = new ArrayList<Edge>(g.incomingEdgesOf(nodes.get(i)));

			for (int j = 0; j < outgoingEdges.size(); j++) {
				targets.get(i).add(outgoingEdges.get(j).get_target());
			}
			for (int j = 0; j < incomingEdges.size(); j++) {
				sources.get(i).add(incomingEdges.get(j).get_source());
			}
		}
		
		// clustering
		for (int i = 0; i < nodes.size(); i++) {
			// we check if the current node is not already in a cluster
			boolean in_cluster = false;
			for (int j = 0; j < clusterList.size() && !in_cluster; j++) {
				if (clusterList.get(j).contains(nodes.get(i).get_id())) {
					in_cluster = true;
				}
			}
			// if the current node is not in a cluster yet
			if (!in_cluster) {
				// we create a new cluster and find all the nodes which must be in the same cluster
				ArrayList<String> new_cluster = new ArrayList<String>();
				new_cluster.add(nodes.get(i).get_id());

				for (int j = 0; j < nodes.size(); j++) {
					if ((i != j)
							&& (targets.get(i).containsAll(targets.get(j)))
							&& (targets.get(j).containsAll(targets.get(i)))
							&& (sources.get(i).containsAll(sources.get(j)))
							&& (sources.get(j).containsAll(sources.get(i)))) {
						new_cluster.add(nodes.get(j).get_id());
					}
				}
				clusterList.add(new_cluster);
			}
		}
		
		// coloring
		int nb_clusters = clusterList.size();
		for (int i = 0; i < clusterList.size(); i++) {
			Color c = new Color(Color.HSBtoRGB((float) i/nb_clusters, 1, 1));
			for (int j = 0; j < clusterList.get(i).size(); j++) {
				Node n = g.get_node(clusterList.get(i).get(j));
				if (n != null) {
					n.setRGB(c.getRed(), c.getGreen(), c.getBlue());
				}
			}
		}
	}

	/**
	 * Build the list of bounds between informs and executed actions, in a very simple way, without taking in account the caseID
	 * 
	 * @param traces log data
	 * @return a fuzzy list for now
	 */
	public static ArrayList<String> buildInform_ExecuteRules(ArrayList<Trace> traces) {
		// test : print the date of the first line of the log
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(m_dateFormat);
		try {
			d = sdf.parse(traces.get(0).getTimestamp());
		} catch (ParseException e) {
			System.out.println("Error. Could not read the date at line "
					+ ". Please check out the log file.");
			e.printStackTrace();
			System.exit(3);
		}
		System.out.println(d.toString());
	
		// TODO prendre en compte le caseID
		ArrayList<String> IE_rules = new ArrayList<String>();
		for (int i = 0; i < traces.size(); i++) {
			Trace t1 = traces.get(i);
			if (t1.getPerformative().equals("execute")) {
				String sender = t1.getSender();
				for (int j = 0; j < traces.size(); j++) {
					Trace t2 = traces.get(j);
					if ((t2.getPerformative().equals("inform"))
							&& (t2.getReceiver().equals(sender))) {
						IE_rules.add(sender + " did " + t1.getActivity()
								+ " following to the information given by "
								+ t2.getSender() + " : " + t2.getActivity());
					}
				}
			}
		}
		
		return IE_rules;
	}

	/**
	 * Export a petri net into a PNML file
	 * 
	 * @param net petri net to export
	 * @param file path of the exported file
	 */
	public static void exportToPNML(PetriNet net, String file) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			writer.println(net.toPNML());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("The file " + file
					+ " cannot be created/opened or does not have the UTF-8 encoding.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Export a workflow into a XPDL file
	 * 
	 * @param workflow workflow to export
	 * @param file path of the exported file
	 */
	public static void exportToXPDL(Workflow workflow, String file) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			writer.println(workflow.toXPDL());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out
					.println("The file " + file
							+ " cannot be created/opened or does not have the UTF-8 encoding.");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
