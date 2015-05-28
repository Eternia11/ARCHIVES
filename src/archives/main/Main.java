package archives.main;

import java.util.ArrayList;

import archives.alphaminer.AlphaMiner;
import archives.archives.Archive;
import archives.graph.Graph;
import archives.log.Trace;
import archives.petrinet.PetriNet;
import archives.tools.Tools;
import archives.workflow.Workflow;

/**
 * 
 * 
 * @author Alan BENIER
 */
class Main {
	private final static String m_csvFile = "resources\\toy_case.csv";					// import path of the log file
	private final static String m_delegateGraphFileName = "gen\\delegate.graphml";		// export path of the graph of resources' hierarchy
	private final static String m_informGraphFileName = "gen\\inform.graphml";			// export path of the graph of resources' interaction
	private static ArrayList<Trace> m_traces = null;									// list of cases of the log file
	private static Graph m_delegateGraph = null;										// graph of resources' hierarchy
	private static Graph m_informGraph = null;											// graph of resources' interaction
	private static ArrayList<ArrayList<String>> m_delegateChain = null;					// list of series of activities associated with list of resources to who the activities have been delegated
	private static ArrayList<String> m_IE_rules = null;									// list of relations between "inform" and "execute" cases
	private static PetriNet m_net = null;												// petri net resulting of the Alpha Algorithm
	private static Workflow m_workflow = null;											// workflow describing the log file
	private final static String m_netFile = "gen\\petri.pnml";							// export path of the petri net
	private final static String m_wfFile = "gen\\workflow.xpdl";						// export path of the workflow
	private final static String m_dateFormat = "dd/MM/yyyy HH:mm";

	public static void main(String[] args) {
		m_traces = Tools.readLogFile(m_csvFile); // read the log file
		/*m_informGraph = Tools.buildPerformativeGraph(m_traces, "inform"); // build a hierarchical graph of the resources of the log file
		m_delegateGraph = Tools.buildPerformativeGraph(m_traces, "delegate"); // build a graph of interactions between the resources of the log file
		Tools.buildClusterList(m_delegateGraph); // color the nodes of the hierarchy graph finding simple clusters
		Tools.exportToGraphml(m_informGraph, m_informGraphFileName); // export the interactions graph
		Tools.exportToGraphml(m_delegateGraph, m_delegateGraphFileName); // export the hierarchy graph
		m_delegateChain = Tools.buildPerformativeChainList(m_traces, "delegate"); // build series of activities associated with list of resources to who the activities have been delegated
		// build then print the relations between "inform" and "execute" lines
		m_IE_rules = Tools.buildInform_ExecuteRules(m_traces);
		for (int i = 0; i < m_IE_rules.size(); i++)
			System.out.println(m_IE_rules.get(i));
		long startTime = System.nanoTime(); // timer
		m_net = AlphaMiner.petriNet(m_traces, 2, false); // alpha algorithm
		long endTime = System.nanoTime(); // timer
		Tools.exportToPNML(m_net, m_netFile);
		System.out.println("Alpha Algorithm took "+(endTime - startTime)/1000000 + " ms"); // print the time of execution of the Alpha Algorithm
		m_workflow = AlphaMiner.workflow(m_traces, 2, false); // my test of workflow
		Tools.exportToXPDL(m_workflow, m_wfFile);*/
		
		long startTime = System.nanoTime();
		Archive archive = new Archive(m_traces, m_dateFormat);
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime)/1000000 + " ms");
		System.out.println(archive.toString());
		archive.onlyDelegatedAsReceiver();
		archive.clusterResources();
		archive.test();
	}
}
