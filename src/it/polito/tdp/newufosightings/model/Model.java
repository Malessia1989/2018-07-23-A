package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	SimpleWeightedGraph<State, DefaultWeightedEdge> grafo;
	NewUfoSightingsDAO dao;
	Map<String,State> idMap;
	Map<State,State> confini;
	 
	
	
	public Model() {
		dao=new NewUfoSightingsDAO();
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new HashMap<>();
		confini=new HashMap<>();
		dao.loadAllStates(idMap);
	}
	
   
	public boolean isValid(String anno) {
		
		if(!anno.matches("\\d{4}"))
			return false;
		
		int annoValido=Integer.parseInt(anno);
		
		return annoValido >= 1910 && annoValido <= 2014;
	}

	public List<String> getForme(String anno) {
		
		return dao.getForme(anno);
	}


	public String creaGrafo(String anno, String forma) {
		
		Graphs.addAllVertices(grafo, idMap.values());

		List<Confine> vicini=dao.getConfini(idMap);
		List<PesiStato> pesi= dao.getPeso(anno, forma, idMap);
		
		for(Confine c: vicini) {
			double peso1=0;
			double peso2=0;
			
			for(PesiStato ps:pesi) {
				if( ps.getS() == c.getS1()) {
					peso1=ps.getPeso();
				}else if(ps.getS()==c.getS2()) {
					peso2=ps.getPeso();
				}
				
			}
			DefaultWeightedEdge edge= grafo.getEdge(c.getS1(), c.getS2());
			if(edge == null) {
				Graphs.addEdgeWithVertices(grafo, c.getS1(), c.getS2(), peso1+peso2);
			}else {
				grafo.setEdgeWeight(edge, peso1+peso2);
			}
						
		}
		
		String result="";
		
		for(State s: grafo.vertexSet()) {
			double sommaPesi=0;
		List<State> tuttiVicini=Graphs.neighborListOf(grafo, s);
		
		for(State s2: tuttiVicini) {
			DefaultWeightedEdge edge=grafo.getEdge(s, s2);
			sommaPesi+=grafo.getEdgeWeight(edge);
			
		}
		result+= "la somma dei pesi degli archi adiacenti a " +s.getName() +"-" +s.getId() + " è: " +sommaPesi +"\n";
		}
		
			
//		System.out.println(grafo.vertexSet().size() +" vertici"+ grafo.edgeSet().size() + " archi ");
//		for(DefaultWeightedEdge edge: grafo.edgeSet()) {
//			System.out.println(grafo.getEdgeWeight(edge));
//			}
//		
				
		return result;
	}

}
