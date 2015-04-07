package archives.alphaminer;

import java.util.List;

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
	
	public void run(List<Trace> logs) {
		// Tw
		for (Trace t : logs) {
			if (t.getPerformative().equals("delegate")) {
				m_net.addTransition(t.getActivity(), t.getActivity());
			}
		}
		// Ti
		
		// To
		
		// Xw
		
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
