package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private SimpleWeightedGraph<State, DefaultWeightedEdge> grafo;
	Map<String, State> idMap=new HashMap<>();
	
	public Model() {
		this.idMap=new HashMap<>();
	}
	
	public boolean isDigit(String annoInserito) {
		
		return annoInserito.matches("\\d+");
	}

	public boolean annoValido(String annoInserito) {
		
		int range = Integer.parseInt(annoInserito);
		return range > 1910 && range < 2014 ;
	}

	public List<String> getShapedellAnno(String annoInserito) {
		
		NewUfoSightingsDAO dao= new NewUfoSightingsDAO();
		List<String> forme=new LinkedList<>(dao.getShape(annoInserito));
		return forme;
	}

	public String creaGrafo(String anno, String forma) {
		
		int annoInserito= Integer.parseInt(anno);
		
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		NewUfoSightingsDAO dao = new NewUfoSightingsDAO();
		dao.loadAllStates(idMap);
		
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, idMap.values());
		
		List<Confine> confini=dao.getArcoEPeso(forma, anno);
		
		for(Confine c: confini) {
			State s1= idMap.get(c.getStato1());
			State s2= idMap.get(c.getStato2());
			
			Graphs.addEdge(grafo, s1, s2, c.getPeso());
		}
		String ris=" ";
		for(State s:grafo.vertexSet()) {
		 ris+= s.getName()+ " "+ grafo.vertexSet().size() +" "+ grafo.edgeSet().size()+"\n" ;
		}
		return ris;
		
		
			
		}
		

	
			
		
	
		
	

}
