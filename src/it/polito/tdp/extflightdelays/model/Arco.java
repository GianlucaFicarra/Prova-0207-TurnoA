package it.polito.tdp.extflightdelays.model;

public class Arco {
	
	private Airport source;
	private Airport destination;
	private double media;
	public Arco(Airport source, Airport destination, double media) {
		super();
		this.source = source;
		this.destination = destination;
		this.media = media;
	}
	public Airport getSource() {
		return source;
	}
	public void setSource(Airport source) {
		this.source = source;
	}
	public Airport getDestination() {
		return destination;
	}
	public void setDestination(Airport destination) {
		this.destination = destination;
	}
	public double getMedia() {
		return media;
	}
	public void setMedia(double media) {
		this.media = media;
	}
	
	

}
