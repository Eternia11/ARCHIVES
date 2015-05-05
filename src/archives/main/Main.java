/* Author : Alan BENIER */

package archives.main;

import archives.algorithm.Algorithm;
import archives.graph.Graph;

import java.util.List;

class Main {
	private static String m_csvFile = "resources\\toy_case.csv";
	private static String m_delegateGraphFileName = "gen\\delegate.graphml";
	private static String m_informGraphFileName = "gen\\inform.graphml";
	private static Graph m_delegateGraph = null;
	private static Graph m_informGraph = null;
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
		algo.buildClusterList(m_delegateGraph);
		algo.exportToGraphml(m_informGraph, m_informGraphFileName);
		algo.exportToGraphml(m_delegateGraph, m_delegateGraphFileName);
		m_delegateChain = algo.buildPerformativeChainList("delegate");
		m_IE_rules = algo.buildInform_ExecuteRules();
		for (int i = 0; i < m_IE_rules.size(); i++)
			System.out.println(m_IE_rules.get(i));
		long startTime = System.nanoTime();
		algo.runAlphaMiner(2, false);
		long endTime = System.nanoTime();
		System.out.println("Alpha Algorithm took "+(endTime - startTime)/1000000 + " ms");
		algo.alphaWorkflow();
	}
}
