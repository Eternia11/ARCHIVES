/* Author : Alan BENIER */

package archives.algorithm;

import archives.alphaminer.AlphaMiner;
import archives.log.Trace;
import archives.petrinet.PetriNet;
import archives.workflow.Workflow;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;

import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.xml.sax.SAXException;
import org.apache.commons.csv.*;

public class Algorithm {
	private static String m_caseID = "Lieu";
	private static String m_timestamp = "dd/MM/yyyy HH:mm";
	private static String m_performative = "Performative";
	private static String m_sender = "Sender";
	private static String m_receiver = "Receiver";
	private static String m_activity = "Objet";
	private static String m_system = "System";
	private static String m_dateFormat = "dd/MM/yyyy HH:mm";
	private List<Trace> m_traces = null;

	// export the graph g into the graphml format file f
	public void exportGraphsToGraphml(
			SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> g, String f) {
		Writer f_writer = null;
		try {
			f_writer = new OutputStreamWriter(new FileOutputStream(f));
		} catch (FileNotFoundException e) {
			System.out
					.println("One of the graph file cannot be created or opened. Please check the names twice.");
			e.printStackTrace();
			System.exit(1);
		}

		// given by developers of JgraphT
		GraphMLExporter<String, DefaultWeightedEdge> gml;
		VertexNameProvider<String> vertex_name = new VertexNameProvider<String>() {
			public String getVertexName(String vertex) {
				return vertex;
			}
		};
		EdgeNameProvider<DefaultWeightedEdge> edge_ID = new EdgeNameProvider<DefaultWeightedEdge>() {
			public String getEdgeName(DefaultWeightedEdge edge) {
				return edge.toString();
			}
		};
		EdgeNameProvider<DefaultWeightedEdge> edge_weight = new EdgeNameProvider<DefaultWeightedEdge>() {
			public String getEdgeName(DefaultWeightedEdge edge) {
				return Integer.toString((int) g.getEdgeWeight(edge));
			}
		};
		gml = new GraphMLExporter<String, DefaultWeightedEdge>(vertex_name,
				vertex_name, edge_ID, edge_weight);
		try {
			gml.export(f_writer, g);
		} catch (TransformerConfigurationException | SAXException e) {
			System.out
					.println("Error during the export the graph into the graphml file"
							+ f);
			e.printStackTrace();
			System.exit(2);
		}
	}

	// read a log file *logs in csv format and store it in m_traces
	public void readLogFile(String logs) {
		CSVParser parser = null;
		m_traces = new ArrayList<Trace>();
		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(',');
		try {
			parser = new CSVParser(new FileReader(logs), format);
			for (CSVRecord record : parser) {
				Trace t = new Trace(record.get(m_caseID),
						record.get(m_timestamp), record.get(m_performative),
						record.get(m_sender), record.get(m_receiver),
						record.get(m_activity));
				m_traces.add(t);
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
	}

	// display the log data on stdout
	public void displayLogs() {
		for (int i = 0; i < m_traces.size(); i++)
			System.out.println(m_traces.get(i));
	}

	// build the graph of interactions of type performative between resources of
	// the log file extracted in m_traces
	public SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> buildPerformativeGraph(
			String performative) {
		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t = m_traces.get(i);
			if (t.getPerformative().equals(performative)) {
				String sender = t.getSender();
				String receiver = t.getReceiver();
				if (!g.containsVertex(sender)) {
					g.addVertex(sender);
				}
				if (!g.containsVertex(receiver)) {
					g.addVertex(receiver);
				}
				if (g.containsEdge(sender, receiver)) {
					DefaultWeightedEdge previous_edge = g.getEdge(sender,
							receiver);
					g.setEdgeWeight(previous_edge,
							g.getEdgeWeight(previous_edge) + 1.0);
				} else {
					g.addEdge(sender, receiver);
				}
			}
		}
		return g;
	}
	
	// build the list of chains of delegation of tasks of type performative between resources of
	// the log file extracted in m_traces
	public List<List<String>> buildPerformativeChainList(String performative) {
		List<List<String>> chainList = new ArrayList<List<String>>();
		
		// we assume that the log is sort from the earliest date to the later
		// and that an action cannot be delegated from and to the same agents
		// two times in a same case
		List<Integer> nb_occ = new ArrayList<Integer>();
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t = m_traces.get(i);
			if (t.getPerformative().equals("delegate")) {
				String action = t.getActivity();
				List<String> new_chain = new ArrayList<String>();
				String sender = t.getSender();
				String receiver = t.getReceiver();
				String last_receiver = receiver;

				new_chain.add(action);
				new_chain.add(sender);
				if (receiver.equals(m_system)) {
					break;
				}
				new_chain.add(receiver);

				for (int j = 0; j < m_traces.size(); j++) {
					if ((m_traces.get(j).getActivity().equals(action))
							&& m_traces.get(j).getPerformative().equals(performative)) {
						// TODO gérer le caseID
						sender = m_traces.get(j).getSender();
						receiver = m_traces.get(j).getReceiver();

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

	// build the list of clusters of vertex of g
	public List<List<String>> buildClusterList(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> g) {
		List<List<String>> clusterList = new ArrayList<List<String>>();
		List<String> vertex = new ArrayList<String>(g.vertexSet());
		List<List<String>> toVertex = new ArrayList<List<String>>();
		List<List<String>> fromVertex = new ArrayList<List<String>>();

		// for each vertex, we retrieve their source and target edges
		for (int i = 0; i < vertex.size(); i++) {
			toVertex.add(new ArrayList<String>());
			fromVertex.add(new ArrayList<String>());

			List<DefaultWeightedEdge> outgoingEdges = new ArrayList<DefaultWeightedEdge>(
					g.outgoingEdgesOf(vertex.get(i)));
			List<DefaultWeightedEdge> incomingEdges = new ArrayList<DefaultWeightedEdge>(
					g.incomingEdgesOf(vertex.get(i)));

			for (int j = 0; j < outgoingEdges.size(); j++) {
				toVertex.get(i).add(
						g.getEdgeTarget(outgoingEdges.get(j)));
			}
			for (int j = 0; j < incomingEdges.size(); j++) {
				fromVertex.get(i).add(
						g.getEdgeSource(incomingEdges.get(j)));
			}
		}

		// classification
		for (int i = 0; i < vertex.size(); i++) {
			boolean classified = false;

			for (int j = 0; j < clusterList.size() && !classified; j++) {
				if (clusterList.get(j).contains(vertex.get(i))) {
					classified = true;
				}
			}

			if (!classified) {
				List<String> new_cluster = new ArrayList<String>();
				new_cluster.add(vertex.get(i));

				for (int j = 0; j < vertex.size(); j++) {
					if ((i != j)
							&& (toVertex.get(i).containsAll(toVertex.get(j)))
							&& (toVertex.get(j).containsAll(toVertex.get(i)))
							&& (fromVertex.get(i)
									.containsAll(fromVertex.get(j)))
							&& (fromVertex.get(j)
									.containsAll(fromVertex.get(i)))) {
						new_cluster.add(vertex.get(j));
					}
				}

				clusterList.add(new_cluster);
			}
		}
		
		return clusterList;
	}
	
	// build the list of bounds between informs and executed actions, in a very simple way, without taking in account the caseID
	public List<String> buildInform_ExecuteRules() {
		// test : print the date of the first line of the log
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(m_dateFormat);
		try {
			d = sdf.parse(m_traces.get(0).getTimestamp());
		} catch (ParseException e) {
			System.out.println("Error. Could not read the date at line "
					+ ". Please check out the log file.");
			e.printStackTrace();
			System.exit(3);
		}
		System.out.println(d.toString());
	
		// TODO prendre en compte le caseID
		List<String> IE_rules = new ArrayList<String>();
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t1 = m_traces.get(i);
			if (t1.getPerformative().equals("execute")) {
				String sender = t1.getSender();
				for (int j = 0; j < m_traces.size(); j++) {
					Trace t2 = m_traces.get(j);
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
	public void runAlphaMiner(int merge_type, boolean loops) {
		AlphaMiner alpha = new AlphaMiner();
		alpha.run(m_traces, merge_type, loops);
	}
	
	// under construction and testing
	public void findWorkflow() {
		Workflow wf = new Workflow("wf_test", "wf_test");
		String WF_file = "gen\\workflow.xpdl";
		
		
		
		
		PrintWriter writer;
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
		}
	}
}
