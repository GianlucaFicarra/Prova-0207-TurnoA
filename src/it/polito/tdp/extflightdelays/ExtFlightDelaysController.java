

/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="numeroVoliTxtInput"
    private TextField numeroVoliTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {

    	// inserire una distanza minima
    	try {
    		try {
	    		int distance = Integer.parseInt(distanzaMinima.getText());
	    		model.createGraph(distance);
	    		txtResult.setText("Grafo creato!\n");
	    		txtResult.appendText("Vertici: "+model.sizeVertexSet()+"\n");
	    		txtResult.appendText("Archi: "+model.sizeEdgeSet()+"\n");

	    		cmbBoxAeroportoPartenza.getItems().addAll(model.getAllAirports());
	    		
    		} catch(NumberFormatException e) {
    			txtResult.setText("ERRORE: inserire solo valori numerici\n");
    		}
    	} catch(RuntimeException e) {
    		txtResult.setText("ERRORE AL DB\n");
    	}

    	
    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {

    	try {
    		Airport start = cmbBoxAeroportoPartenza.getValue();
    		
    		if(start == null) {
    			txtResult.setText("ERRORE: selezionare un aeroporto\n");
    			return;
    		}
    		
    		List<Airport> connessi = model.calcolaAeroportiConnessi(start);
    		
    		if(connessi.isEmpty()) {
    			txtResult.appendText("Nessun aeroporto connesso trovato \n");
    			return;
    		}
    		
    		txtResult.appendText("\nAeroporti connessi a "+start+"\n");
    		for(Airport a : connessi) {
    			txtResult.appendText(a.getAirportName()+"\n");
    			txtResult.appendText("Distanza: "+a.getDistanzaDaStart()+"\n");
    		}
    		
    	} catch(RuntimeException e) {
    		txtResult.setText("ERRORE AL DB\n");
    	}
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {

    //	try {
    		
    		try {
    			
    			int miglia = Integer.parseInt(numeroVoliTxtInput.getText());
    			// mmiglia che è disposto a percorrere
    			
    			// visitare il maggior numero di citta
    			// a partire dall aeroporto di partenza
    			Airport start = cmbBoxAeroportoPartenza.getValue();
        		
        		if(start == null) {
        			txtResult.setText("ERRORE: selezionare un aeroporto\n");
        			return;
        		}
        		model.cercaItinerario(start, miglia);
        		
        		List<Airport> bestCammino = model.getBestCammino();
        		double cammino =model.getMiglia(bestCammino);
        		if(bestCammino.isEmpty()) {
        			txtResult.setText("Nessun itinerario trovato \n");
        			return;
        		}
        		
        		txtResult.appendText("\nItinerario: \n");
        		for(Airport a : bestCammino) {
        			txtResult.appendText(a.getAirportName()+"\n");
        		}
        		txtResult.appendText("Distanza: "+cammino+"\n");
        		
    			
    		} catch(NumberFormatException e) {
    			txtResult.setText("ERRORE: inserire solo valori numerici\n");
    		}
    		
//    	} catch(RuntimeException e) {
//    		txtResult.setText("ERRORE AL DB\n");
//    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
		
	}
}

