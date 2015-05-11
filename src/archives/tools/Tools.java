/* Author : Alan BENIER */

package archives.tools;

import archives.alphaminer.AlphaMiner;
import archives.graph.Edge;
import archives.graph.Graph;
import archives.graph.Node;
import archives.log.Trace;

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
	 * and the activity for each case
	 * 
	 * @param logs path of the log file to import (must be in .csv format)
	 * @return the list of cases representing the log file
	 */
	public static ArrayList<Trace> readLogFile(String logs) {
		CSVParser parser = null;
		ArrayList<Trace> traces = new ArrayList<Trace>();
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(',');
		try {
			parser = new CSVParser(new FileReader(logs), format);
			for (CSVRecord record : parser) {
				Trace t = new Trace(record.get(m_caseID),
						record.get(m_timestamp), record.get(m_performative),
						record.get(m_sender), record.get(m_receiver),
						record.get(m_activity));
				traces.add(t);
			}
			parser.close();
		} catch (IOException e) {
			System.out
					.println("Error. Could not read the log file "
							+ logs
							+ ".\nMake sure to use the right column names : 'CaseID', 'Timestamp', 'Performative', 'Sender', 'Receiver', 'Activity'.");
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

	// build the graph of interactions of type performative between resources of
	// the log file extracted in m_traces
	/**
	 * 
	 * @param traces
	 * @param performative
	 * @return
	 */
	public static Graph buildPerformativeGraph(ArrayList<Trace> traces, String performative) {
		Graph g = new Graph();
		
		for (int i = 0; i < traces.size(); i++) {
			Trace t = traces.get(i);
			if (t.getPerformative().equals(performative)) {
				String sender = t.getSender();
				String receiver = t.getReceiver();
				
				g.add_node(sender);
				g.add_node(receiver);
				if (!g.contains_edge(sender, receiver))
					g.add_edge(sender, receiver);
				else
					g.increase_weight(sender, receiver);
				}
		}
		
		return g;
	}

	// export the graph g into the graphml format file f
	/**
	 * 
	 * @param g
	 * @param file
	 */
	public static void exportToGraphml(Graph g, String file) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			writer.println(g.toGRAPHML());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out
					.println("The file "
							+ file
							+ " cannot be created/opened or does not have the UTF-8 encoding.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	// build the list of chains of delegation of tasks of type performative between resources of
	// the log file extracted in m_traces
	/**
	 * 
	 * @param traces
	 * @param performative
	 * @return
	 */
	public static ArrayList<ArrayList<String>> buildPerformativeChainList(ArrayList<Trace> traces, String performative) {
		ArrayList<ArrayList<String>> chainList = new ArrayList<ArrayList<String>>();
		
		// we assume that the log is sort from the earliest date to the later
		// and that an action cannot be delegated from and to the same agents
		// two times in a same case
		ArrayList<Integer> nb_occ = new ArrayList<Integer>();
		for (int i = 0; i < traces.size(); i++) {
			Trace t = traces.get(i);
			if (t.getPerformative().equals("delegate")) {
				String action = t.getActivity();
				ArrayList<String> new_chain = new ArrayList<String>();
				String sender = t.getSender();
				String receiver = t.getReceiver();
				String last_receiver = receiver;

				new_chain.add(action);
				new_chain.add(sender);
				if (receiver.equals(m_system)) {
					break;
				}
				new_chain.add(receiver);

				for (int j = 0; j < traces.size(); j++) {
					if ((traces.get(j).getActivity().equals(action))
							&& traces.get(j).getPerformative().equals(performative)) {
						// TODO gérer le caseID
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

		// print the action chains
		if (nb_occ.size() != chainList.size()) {
			// TODO handle error
			System.out
					.println("Error occured when mining the action delegation chains.");
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

	// build the list of clusters of vertex of g and color the nodes of this graph
	/**
	 * 
	 * @param g
	 */
	public static void buildClusterList(Graph g) {
		// clustering
		ArrayList<ArrayList<String>> clusterList = new ArrayList<ArrayList<String>>();
		ArrayList<Node> nodes = g.get_nodes();
		ArrayList<ArrayList<String>> targets = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> sources = new ArrayList<ArrayList<String>>();
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
		// classification
		for (int i = 0; i < nodes.size(); i++) {
			boolean classified = false;
			for (int j = 0; j < clusterList.size() && !classified; j++) {
				if (clusterList.get(j).contains(nodes.get(i).get_id())) {
					classified = true;
				}
			}
			if (!classified) {
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
	
	// build the list of bounds between informs and executed actions, in a very simple way, without taking in account the caseID
	/**
	 * 
	 * @param traces
	 * @return
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
	
	// loops : enable or disable the add of loops of size 0 and 1
	// merge_type : 0 : generalization, 1 : specialization, 2 : average
	/**
	 * 
	 * @param traces
	 * @param merge_type
	 * @param loops
	 */
	public static  void runAlphaMiner(ArrayList<Trace> traces, int merge_type, boolean loops) {
		AlphaMiner alpha = new AlphaMiner();
		alpha.run(traces, merge_type, loops);
	}
	
	// under construction and testing
	/**
	 * 
	 * @param traces
	 */
	public static void alphaWorkflow(ArrayList<Trace> traces) {
		AlphaMiner alpha = new AlphaMiner();
		alpha.alphaWorkflow(traces, 0, true);
		
		/*Workflow wf = new Workflow("wf_test", "wf_test");
		String WF_file = "gen\\workflow.xpdl";
		
		ArrayList<String> resources = new ArrayList<String>();
		for (Trace t : m_traces) {
			if (!resources.contains(t.getSender()))
				resources.add(t.getSender());
			if (!resources.contains(t.getReceiver()))
				resources.add(t.getReceiver());
		}*/
		
		// each resource is a lane and we fill them with the resource's activities
		/*wf.addProcess(new Process("pr_archives", "pr_archives"));
		wf.addPool(new Pool("po_archives", "po_archives", "pr_archives"));
		for (int r = 0; r < resources.size(); r++) {
			String tested_resource = resources.get(r);
			wf.get_pool(0).addLane(new Lane(tested_resource, tested_resource));
			ArrayList<String> resource_activities = new ArrayList<String>();
			
			for (Trace t : m_traces) {
				if (tested_resource.equals(t.getSender()))
					resource_activities.add(t.getActivity());
			}
			
			if (!resource_activities.isEmpty()) {
				wf.get_process(0).addActivity(new ActivityStartLane(tested_resource+"_start_", tested_resource));
				wf.get_process(0).addActivity(new ActivityLane(tested_resource+"-"+resource_activities.get(0), resource_activities.get(0), tested_resource));
				wf.get_process(0).addFlow(new Flow (tested_resource+"_start_", tested_resource+"-"+resource_activities.get(0)));
				for (int i = 0; i < resource_activities.size() - 1; i++) {
					wf.get_process(0).addActivity(new ActivityLane(tested_resource+"-"+resource_activities.get(i+1), resource_activities.get(i + 1), tested_resource));
					String a1 = resource_activities.get(i);
					String a2 = resource_activities.get(i+1);
					wf.get_process(0).addFlow(new Flow(tested_resource+"-"+a1, tested_resource+"-"+a2));
				}
				wf.get_process(0).addActivity(new ActivityEndLane(tested_resource+"_end_", tested_resource));
				wf.get_process(0).addFlow(new Flow (tested_resource+"-"+resource_activities.get(resource_activities.size() - 1), tested_resource+"_end_"));
			}
		}*/
		
		// interactions with everyone
		/*wf.addProcess(new Process("pr_archives", "pr_archives"));
		wf.addPool(new Pool("po_archives", "po_archives", "pr_archives"));
		for (Trace t : m_traces) {
			if (t.getPerformative().equals("delegate")) {
				wf.get_pool(0).addLane(new Lane(t.getSender()));
				wf.get_pool(0).addLane(new Lane(t.getReceiver()));
				
				wf.get_process(0).addActivity(new ActivityLane(t.getSender()+"_"+t.getActivity(), t.getActivity(), t.getSender()));
				wf.get_process(0).addActivity(new ActivityLane(t.getReceiver()+"_"+t.getActivity(), t.getActivity(), t.getReceiver()));
				
				wf.get_process(0).addFlow(new Flow(t.getSender()+"_"+t.getActivity(), t.getReceiver()+"_"+t.getActivity()));
			}
		}*/
		
		// interaction for only one resource
		/*String actor = "TTaGL";
		wf.addProcess(new Process("pr_archives", "pr_archives"));
		wf.addPool(new Pool("po_archives", "po_archives", "pr_archives"));
		String prev_act = "";
		for (Trace t : m_traces) {
			if ((t.getSender().equals(actor)) || (t.getReceiver().equals(actor))) {
				if (t.getReceiver().equals(m_system)) {
					wf.get_pool(0).addLane(new Lane(t.getSender()));
					wf.get_process(0).addActivity(new ActivityLane(t.getSender()+"_"+t.getActivity(), t.getActivity(), t.getSender()));
					
					if (!prev_act.equals("")) {
						wf.get_process(0).addFlow(new Flow(prev_act, t.getSender()+"_"+t.getActivity()));
					}
					prev_act = t.getSender()+"_"+t.getActivity();
					
				} else if (t.getSender().equals(m_system)) {
					wf.get_pool(0).addLane(new Lane(t.getReceiver()));
					wf.get_process(0).addActivity(new ActivityLane(t.getReceiver()+"_"+t.getActivity(), t.getActivity(), t.getReceiver()));
					
					if (!prev_act.equals("")) {
						wf.get_process(0).addFlow(new Flow(prev_act, t.getReceiver()+"_"+t.getActivity()));
					}
					prev_act = t.getReceiver()+"_"+t.getActivity();
				} else {
					wf.get_pool(0).addLane(new Lane(t.getSender()));
					wf.get_pool(0).addLane(new Lane(t.getReceiver()));
					
					wf.get_process(0).addActivity(new ActivityLane(t.getSender()+"_"+t.getActivity(), t.getActivity(), t.getSender()));
					wf.get_process(0).addActivity(new ActivityLane(t.getReceiver()+"_"+t.getActivity(), t.getActivity(), t.getReceiver()));
					
					wf.get_process(0).addFlow(new Flow(t.getSender()+"_"+t.getActivity(), t.getReceiver()+"_"+t.getActivity()));
					
					prev_act = actor+"_"+t.getActivity();
				}
			}
		}*/
		
		// first resource (very test)
		/*for (int r = 0; r < resources.size(); r++) {
			String tested_resource = resources.get(r);
			wf.addProcess(new Process("pr_"+tested_resource, "pr_"+tested_resource));
			wf.addPool(new Pool("po_"+tested_resource, "po_"+tested_resource,"pr_"+tested_resource));
			wf.get_pool(r).addLane(new Lane(tested_resource, tested_resource));
			ArrayList<String> resource_activities = new ArrayList<String>();
			
			for (Trace t : m_traces) {
				if (tested_resource.equals(t.getSender()))
					resource_activities.add(t.getActivity());
			}
			
			if (!resource_activities.isEmpty()) {
				wf.get_process(r).addActivity(new ActivityLane(tested_resource+"-"+resource_activities.get(0), resource_activities.get(0), tested_resource));
				wf.get_process(r).connectToStart(tested_resource+"-"+resource_activities.get(0));
				for (int i = 0; i < resource_activities.size() - 1; i++) {
					wf.get_process(r).addActivity(new ActivityLane(tested_resource+"-"+resource_activities.get(i+1), resource_activities.get(i + 1), tested_resource));
					String a1 = resource_activities.get(i);
					String a2 = resource_activities.get(i+1);
					wf.get_process(r).addFlow(new Flow(tested_resource+"-"+a1 + "-" + tested_resource+"-"+a2, tested_resource+"-"+a1, tested_resource+"-"+a2));
				}
				wf.get_process(r).connectToEnd(tested_resource+"-"+resource_activities.get(resource_activities.size() - 1));
			}
		}*/
		
		/*PrintWriter writer;
		try {
			writer = new PrintWriter(WF_file, "UTF-8");
			writer.println(wf.toXPDL());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out
					.println("The file "
							+ WF_file
							+ " cannot be created/opened or does not have the UTF-8 encoding.");
			e.printStackTrace();
			System.exit(6);
		}*/
	}
}
