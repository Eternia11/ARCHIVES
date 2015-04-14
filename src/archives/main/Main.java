/* Author : Alan BENIER */

package archives.main;

import archives.algorithm.Algorithm;
import archives.alphaminer.AlphaMiner;

import java.util.List;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

class Main {
	private static String m_csvFile = "toy_case.csv";
	private static String m_delegateGraphFileName = "delegate.graphml";
	private static String m_informGraphFileName = "inform.graphml";
	private static SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> m_delegateGraph = null;
	private static SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> m_informGraph = null;
	private static List<List<String>> m_clusters = null;
	private static List<List<String>> m_delegateChain = null;
	private static List<String> m_IE_rules = null;

	// read a log file m_csvFile in csv format (case, timestamp, performative,
	// sender, receiver, activity), mine the hierarchy of resources, the
	// information chain and export
	// them into graphML format files
	public static void main(String[] args) {
		Algorithm algo = new Algorithm();
		algo.readLogFile(m_csvFile);
		m_informGraph = algo.buildPerformativeGraph("inform");
		m_delegateGraph = algo.buildPerformativeGraph("delegate");
		algo.exportGraphsToGraphml(m_informGraph, m_informGraphFileName);
		algo.exportGraphsToGraphml(m_delegateGraph, m_delegateGraphFileName);
		m_clusters = algo.buildClusterList(m_delegateGraph);
		for (int i = 0; i < m_clusters.size(); i++)
			System.out.println(m_clusters.get(i));
		m_delegateChain = algo.buildPerformativeChainList("delegate");
		m_IE_rules = algo.buildInform_ExecuteRules();
		for (int i = 0; i < m_IE_rules.size(); i++)
			System.out.println(m_IE_rules.get(i));
		long startTime = System.nanoTime();
		algo.runAlphaMiner();
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime)/1000000 + " ms");
	}
}
