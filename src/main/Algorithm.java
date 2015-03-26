/* Author : Alan BENIER
 * Hierarchy Miner inspired from ...
 */

package main;

import archives.log.Trace;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

class Algorithm {
	private String m_caseID = "Lieu";
	private String m_timestamp = "dd/MM/yyyy HH:mm";
	private String m_performative = "Performative";
	private String m_sender = "Sender";
	private String m_receiver = "Receiver";
	private String m_activity = "Objet";
	private String m_system = "System";
	private String m_dateFormat = "dd/MM/yyyy HH:mm";
	private String m_delegateGraphFileName = "delegate.graphml";
	private String m_informGraphFileName = "inform.graphml";
	private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> m_delegateGraph = null;
	private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> m_informGraph = null;
	private List<Trace> m_traces = null;

	// export the graphs of the Algorithm into the graphml format files which
	// names are specified in the attributes
	private void exportGraphsToGraphml() {
		Writer delegateFile = null;
		Writer informFile = null;
		try {
			delegateFile = new OutputStreamWriter(new FileOutputStream(
					m_delegateGraphFileName));
			informFile = new OutputStreamWriter(new FileOutputStream(
					m_informGraphFileName));
		} catch (FileNotFoundException e) {
			System.out
					.println("One of the graph file cannot be created or opened. Please check the names twice.");
			e.printStackTrace();
			System.exit(1);
		}

		// given by developers of JgraphT
		GraphMLExporter<String, DefaultWeightedEdge> delegate_gml, inform_gml;
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
		EdgeNameProvider<DefaultWeightedEdge> delegate_edge_weight = new EdgeNameProvider<DefaultWeightedEdge>() {
			public String getEdgeName(DefaultWeightedEdge edge) {
				return Integer.toString((int) m_delegateGraph
						.getEdgeWeight(edge));
			}
		};
		EdgeNameProvider<DefaultWeightedEdge> inform_edge_weight = new EdgeNameProvider<DefaultWeightedEdge>() {
			public String getEdgeName(DefaultWeightedEdge edge) {
				return Integer
						.toString((int) m_informGraph.getEdgeWeight(edge));
			}
		};
		delegate_gml = new GraphMLExporter<String, DefaultWeightedEdge>(
				vertex_name, vertex_name, edge_ID, delegate_edge_weight);
		inform_gml = new GraphMLExporter<String, DefaultWeightedEdge>(
				vertex_name, vertex_name, edge_ID, inform_edge_weight);
		try {
			delegate_gml.export(delegateFile, m_delegateGraph);
			inform_gml.export(informFile, m_informGraph);
		} catch (TransformerConfigurationException | SAXException e) {
			System.out
					.println("Error during the export of one of the graphs into a graphml file. No more information available.");
			e.printStackTrace();
			System.exit(2);
		}
	}

	// read a log file *logs in csv format and store it in m_traces
	private void readLogFile(String logs) {
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

	// build the graph of interactions of type 'delegate' between resources of
	// the log file extracted in m_traces
	private void buildDelegateGraph() {
		m_delegateGraph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t = m_traces.get(i);
			if (t.getPerformative().equals("delegate")) {
				String sender = t.getSender();
				String receiver = t.getReceiver();
				if (!m_delegateGraph.containsVertex(sender)) {
					m_delegateGraph.addVertex(sender);
				}
				if (!m_delegateGraph.containsVertex(receiver)) {
					m_delegateGraph.addVertex(receiver);
				}
				if (m_delegateGraph.containsEdge(sender, receiver)) {
					DefaultWeightedEdge previous_edge = m_delegateGraph
							.getEdge(sender, receiver);
					m_delegateGraph.setEdgeWeight(previous_edge,
							m_delegateGraph.getEdgeWeight(previous_edge) + 1.0);
				} else {
					m_delegateGraph.addEdge(sender, receiver);
				}
			}
		}
	}

	// build the graph of interactions of type 'inform' between resources of
	// the log file extracted in m_traces
	private void buildInformGraph() {
		m_informGraph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t = m_traces.get(i);
			if (t.getPerformative().equals("inform")) {
				String sender = t.getSender();
				String receiver = t.getReceiver();
				if (!m_informGraph.containsVertex(sender)) {
					m_informGraph.addVertex(sender);
				}
				if (!m_informGraph.containsVertex(receiver)) {
					m_informGraph.addVertex(receiver);
				}
				if (m_informGraph.containsEdge(sender, receiver)) {
					DefaultWeightedEdge previous_edge = m_informGraph.getEdge(
							sender, receiver);
					m_informGraph.setEdgeWeight(previous_edge,
							m_informGraph.getEdgeWeight(previous_edge) + 1.0);
				} else {
					m_informGraph.addEdge(sender, receiver);
				}
			}
		}
	}

	// build the chain of delegation of tasks between resources of
	// the log file extracted in m_traces
	private void buildDelegateChain() {
		// we assume that the log is sort from the earliest date to the later
		// and that an action cannot be delegated from and to the same agents
		// two times in a same case
		List<Integer> nb_occ = new ArrayList<Integer>();
		List<List<String>> action_chains = new ArrayList<List<String>>();
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t = m_traces.get(i);
			if (t.getPerformative().equals("delegate")) {
				String action = t.getActivity();
				List<String> new_action_chain = new ArrayList<String>();
				String sender = t.getSender();
				String receiver = t.getReceiver();
				String last_receiver = receiver;
				
				new_action_chain.add(action);
				new_action_chain.add(sender);
				if (receiver.equals(m_system)) {
					break;
				}
				new_action_chain.add(receiver);

				for (int j = 0; j < m_traces.size(); j++) {
					if ((m_traces.get(j).getActivity().equals(action))
							&& m_traces.get(j).getPerformative()
									.equals("delegate")) { // TODO gérer le
						// caseID
						sender = m_traces.get(j).getSender();
						receiver = m_traces.get(j).getReceiver();

						if (last_receiver.equals(sender)) {
							if (receiver.equals(m_system)) {
								break;
							}
							new_action_chain.add(receiver);

							last_receiver = receiver;
						}
					}
				}

				// if the action chain exists already then we add 1 to the
				// occurence counter
				if (!new_action_chain.isEmpty()) {
					boolean same_action_chain = false;
					int index_where_same = 0;
					for (int j = 0; j < action_chains.size()
							&& !same_action_chain; j++) {
						if (new_action_chain.size() == action_chains.get(j).size()) {
							same_action_chain = true;
							for (int k = 0; k < new_action_chain.size() && same_action_chain; k++) {
								if (!new_action_chain.get(k).equals(action_chains.get(j).get(k))) {
									same_action_chain = false;
								}
							}
							index_where_same = j;
						}
					}
					if (!same_action_chain) {
						action_chains.add(new_action_chain);
						nb_occ.add(1);
					} else {
						nb_occ.set(index_where_same,
								nb_occ.get(index_where_same) + 1);
					}
				}
			}
		}

		// print the action chains
		if (nb_occ.size() != action_chains.size()) {
			// TODO handle error
			System.out
					.println("Error occured when mining the action delegation chains.");
			System.exit(4);
		}
		for (int i = 0; i < action_chains.size(); i++) {
			if (action_chains.get(i).size() > 3) {
				System.out.print("Action \"" + action_chains.get(i).get(0)
						+ "\" :");
				for (int j = 1; j < action_chains.get(i).size(); j++) {
					System.out.print(" -> " + action_chains.get(i).get(j));
				}
				System.out.println(" found " + nb_occ.get(i) + " time(s)");
			}
		}
	}

	public void buildExecuteList() {
		// test : list the executioning resources
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t = m_traces.get(i);
			if (t.getPerformative().equals("execute")) {
				String sender = t.getSender();
				if (!l.contains(sender)) {
					l.add(sender);
				}
			}
		}
		for (int i = 0; i < l.size(); i++)
			System.out.println(l.get(i));

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

		// test : first attempt of bounding inform to execute, very simple way,
		// without taking in account the caseID
		for (int i = 0; i < m_traces.size(); i++) {
			Trace t1 = m_traces.get(i);
			if (t1.getPerformative().equals("execute")) {
				String sender = t1.getSender();
				for (int j = 0; j < m_traces.size(); j++) {
					Trace t2 = m_traces.get(j);
					if ((t2.getPerformative().equals("inform"))
							&& (t2.getReceiver().equals(sender))) {
						System.out.println(sender + " did " + t1.getActivity()
								+ " following to the information given by "
								+ t2.getSender() + " : " + t2.getActivity());
					}
				}
			}
		}

		// test : first attempt of clusterisation
		List<List<String>> clusters = new ArrayList<List<String>>();
		List<String> vertex = new ArrayList<String>(m_delegateGraph.vertexSet());
		List<List<String>> toVertex = new ArrayList<List<String>>();
		List<List<String>> fromVertex = new ArrayList<List<String>>();

		// for each vertex, we retrieve their source and target edges
		for (int i = 0; i < vertex.size(); i++) {
			toVertex.add(new ArrayList<String>());
			fromVertex.add(new ArrayList<String>());

			List<DefaultWeightedEdge> outgoingEdges = new ArrayList<DefaultWeightedEdge>(
					m_delegateGraph.outgoingEdgesOf(vertex.get(i)));
			List<DefaultWeightedEdge> incomingEdges = new ArrayList<DefaultWeightedEdge>(
					m_delegateGraph.incomingEdgesOf(vertex.get(i)));

			for (int j = 0; j < outgoingEdges.size(); j++) {
				toVertex.get(i).add(
						m_delegateGraph.getEdgeTarget(outgoingEdges.get(j)));
			}
			for (int j = 0; j < incomingEdges.size(); j++) {
				fromVertex.get(i).add(
						m_delegateGraph.getEdgeSource(incomingEdges.get(j)));
			}
		}

		// classification
		for (int i = 0; i < vertex.size(); i++) {
			boolean classified = false;

			for (int j = 0; j < clusters.size() && !classified; j++) {
				if (clusters.get(j).contains(vertex.get(i))) {
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

				clusters.add(new_cluster);
			}
		}

		// print clusters
		for (int i = 0; i < clusters.size(); i++) {
			System.out.print("Cluster N°" + i + " :");
			for (int j = 0; j < clusters.get(i).size(); j++) {
				System.out.print(" " + clusters.get(i).get(j));
			}
			System.out.println();
		}
	}

	// read a log file *logs in csv format (case, timestamp, performative,
	// sender, receiver, activity), mine the hierarchy of resources, the
	// information chain and export
	// them into graphML format files
	public void run(String logs) {
		readLogFile(logs);
		buildDelegateGraph();
		buildInformGraph();
		exportGraphsToGraphml();
		buildExecuteList();
		buildDelegateChain();
	}

	public static void main(String[] args) {
		String in_file = "toy_case.csv";
		Algorithm algo = new Algorithm();
		algo.run(in_file);
	}
}
