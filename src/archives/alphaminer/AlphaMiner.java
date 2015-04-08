package archives.alphaminer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jgrapht.ext.MatrixExporter;

import archives.log.Trace;
import archives.petrinet.*;

/*
import fr.lip6.move.pnml.framework.general.PnmlExport;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;

import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.RefPlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
*/
/*
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.NameHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PageHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.RefPlaceHLAPI;
import fr.lip6.move.pnml.pnmlcoremodel.hlapi.TransitionHLAPI;
*/
public class AlphaMiner {
	private PetriNet m_net = null;
	
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
	
	public void run(List<Trace> logs) {
		ArrayList<String> T = new ArrayList<String>();
		// Tw
		for (Trace t : logs) {
			m_net.addTransition(t.getActivity(), t.getActivity());
			T.add(t.getActivity());
		}
		// Ti
		
		// To
		
		// Xw
		// reoganisation des logs rassemblés par timestamp
		ArrayList<ArrayList<Trace>> new_log = new ArrayList<ArrayList<Trace>>();
		String prev_time = "";
		for (int i = 0; i < logs.size(); i++) {
			Trace t = logs.get(i);
			if (!prev_time.equals(t.getTimestamp())) {
				new_log.add(new ArrayList<Trace>());
			}
			new_log.get(new_log.size()-1).add(t);
			prev_time = t.getTimestamp();
		}
		
		// 1 = >;
		int X[][] = new int[T.size()][T.size()];
		
		for (int[] row : X) {
			for (int x : row) {
				x = 0;
			}
		}
		
		for (int i = 0; i < T.size(); i++) {
			for (int j = 0; j < new_log.size(); j++) {
				for (int k = 0; k < T.size(); k++) {
					if (isIn(new_log.get(j), T.get(i))) {
						if ((j+1 < new_log.size()) && (isIn(new_log.get(j+1), T.get(k)))) {
							X[i][k] = 1;
						}
					}
				}
			}
		}
		
		// -1 = <-; 0 = #; 1 = ->; 2 = ||
		for (int i = 0; i < T.size(); i++) {
			for (int j = 0; j < T.size(); j++) {
				if ((X[i][j] == 1) && (X[j][i] == 1)) {
					X[i][j] = 2;
					X[j][i] = 2;
				}
				if ((X[i][j] == 1) && (X[j][i] == 0)) {
					X[i][j] = 1;
					X[j][i] = -1;
				}
				if ((X[i][j] == 0) && (X[j][i] == 1)) {
					X[i][j] = -1;
					X[j][i] = 1;
				}
			}
		}
		
		for (int i = 0; i < T.size(); i++) {
			for (int j = 0; j < T.size(); j++) {
				System.out.print(X[i][j]+"\t");
			}
			System.out.println();
		}
		
		// Yw
		
		//Pw
		
		// Fw
		
		// alpha(W)
		
		System.out.println(m_net.toPNML());
	}
	
	/*public AlphaMiner() throws InvalidIDException, VoidRepositoryException {
		
		ModelRepository.getInstance().createDocumentWorkspace("void");
		System.out.println("a");
		
		PetriNetDocHLAPI doc = new PetriNetDocHLAPI();
		System.out.println("d");
		PetriNetHLAPI net = new PetriNetHLAPI("net0", PNTypeHLAPI.COREMODEL,new NameHLAPI("PetriNet"), doc);
		PageHLAPI page = new PageHLAPI("toppage", new NameHLAPI("Page"),null, net); //use of "null" is authorized but not encouraged 
		PageHLAPI subpage = new PageHLAPI("subpage", new NameHLAPI("Subpage"),null, page); //same
		System.out.println("c");
		PlaceHLAPI p1 = new PlaceHLAPI("place1");
		PlaceHLAPI p2 = new PlaceHLAPI("place2");
		PlaceHLAPI p3 = new PlaceHLAPI("place3");

		TransitionHLAPI t1 = new TransitionHLAPI("transistion1");
		TransitionHLAPI t2 = new TransitionHLAPI("transistion2");
		System.out.println("b");
		RefPlaceHLAPI r1 = new RefPlaceHLAPI("reftop3", p3);

		new ArcHLAPI("a1", p1, t1, page);
		new ArcHLAPI("a2", t1, r1, page);
		new ArcHLAPI("a3", p3, t2, subpage);
		new ArcHLAPI("a4", t2, p2, subpage);

		p1.setContainerPageHLAPI(page);
		t1.setContainerPageHLAPI(page);
		r1.setContainerPageHLAPI(page);

		p3.setContainerPageHLAPI(subpage);
		p2.setContainerPageHLAPI(subpage);
		t2.setContainerPageHLAPI(subpage);

	  PnmlExport pex = new PnmlExport();
	  System.out.println(doc.toPNML());
	  //pex.exportObject(doc, System.getenv("fpath") + "exporttest.pnml");

		ModelRepository.getInstance().destroyCurrentWorkspace();
	}*/

}
