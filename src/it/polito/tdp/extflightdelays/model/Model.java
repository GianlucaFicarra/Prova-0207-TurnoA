package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> graph;
	private List<Airport> airports;
	private AirportIdMap airportMap;
	private ExtFlightDelaysDAO dao;
	private List<Arco> edgesInitial;
	
	private List<Airport> bestCammino;
	private double distanzaMax;
	
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		airportMap = new AirportIdMap();
		airports = dao.loadAllAirports(airportMap);
		edgesInitial = dao.getEdges(airportMap);
	}
	

	public void createGraph(int distance) {
		
		// analizza aeroporti
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, airports);
		
		// archi = rotte tra gli aeroporti collegati
		for(Arco a : edgesInitial) {
			if(a != null) {
				if(a.getMedia() > distance) {
					Graphs.addEdgeWithVertices(this.graph, a.getSource(), a.getDestination(), a.getMedia());;
				}
			}
		}	
	}
	
	public List<Airport> getAllAirports() {
		return this.airports;
	}


	public List<Airport> calcolaAeroportiConnessi(Airport start) {

		List<Airport> connessi = Graphs.neighborListOf(this.graph, start);
		
		for(Airport a : connessi) {
			// calcolo la distanza da start
			DefaultWeightedEdge e = graph.getEdge(start, a);
			double peso = graph.getEdgeWeight(e);
			a.setDistanzaDaStart(peso);
		}
		
		Collections.sort(connessi, new Comparator<Airport>() {

			@Override
			public int compare(Airport o1, Airport o2) {
				return Double.compare(o2.getDistanzaDaStart(), o1.getDistanzaDaStart());
			}
			
		});
		
		return connessi;
		
	}
	
	public int sizeVertexSet() {
		return this.graph.vertexSet().size();
	}
	
	public int sizeEdgeSet() {
		return this.graph.edgeSet().size();
	}


	public void cercaItinerario(Airport start, int miglia) {

		bestCammino = new ArrayList<>();
		distanzaMax = Double.MIN_VALUE;
		List<Airport> parziale = new ArrayList<>();
		
		parziale.add(start);
		int step = 0;
		doRecursive(step, parziale, miglia);
		
	}


	private void doRecursive(int step, List<Airport> parziale, int miglia) {

		System.out.println(parziale.toString());
		
		if(parziale.size() > bestCammino.size()) {
			bestCammino = new ArrayList<>(parziale);
			
		}
			
		Airport ultimo = parziale.get(parziale.size()-1);
		
		List<Airport> vicini = Graphs.neighborListOf(this.graph, ultimo);
		System.out.println(vicini.size());
		
		for(Airport vicino : vicini) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				if(getMiglia(parziale) <= miglia) {
					doRecursive(step+1, parziale, miglia);
				}
				parziale.remove(parziale.size()-1);
			}
		}
		
	}


	public double getMiglia(List<Airport> parziale) {
		
		double miglia = 0;
		
		for(int i=0; i<parziale.size()-1; i++) {
			DefaultWeightedEdge e = graph.getEdge(parziale.get(i), parziale.get(i+1));
			miglia += graph.getEdgeWeight(e);
		}
		
		return miglia;
	}

	
	public List<Airport> getBestCammino() {
		return this.bestCammino;
	}
	
	public double getDistanzaMax() {
		return this.distanzaMax;
	}
}
