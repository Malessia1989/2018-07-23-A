package it.polito.tdp.newufosightings.model;

import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	
	public boolean isDigit(String annoInserito) {
		
		return annoInserito.matches("\\d+");
	}

	public boolean annoValido(String annoInserito) {
		int range=Integer.parseInt(annoInserito);
		return range > 1910 && range < 2014 ;
	}

	public List<String> getShapedellAnno(String annoInserito) {
		NewUfoSightingsDAO dao= new NewUfoSightingsDAO();
		List<String> forme=new LinkedList<>(dao.getShape(annoInserito));
		return forme;
	}

}
