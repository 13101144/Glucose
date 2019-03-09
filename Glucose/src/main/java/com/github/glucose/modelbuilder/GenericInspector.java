package com.github.glucose.modelbuilder;

import com.github.glucose.core.ComponentModel;

public class GenericInspector implements Inspector{
	
	private int rank;
	
	private String implementation;
	
	public GenericInspector() {
		
	}
	
	public GenericInspector(int rank, String implementation) {
		this.rank = rank;
		this.implementation = implementation;
	}


	public void processModel(ComponentModel model) {
		model.setImplementation(implementation);
		model.setRank(rank);	
	}


	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		ensureParameterNotNull(rank);
		this.rank = rank;
	}


	public String getImplementation() {
		return implementation;
	}


	public void setImplementions(String implemention) {
		ensureParameterNotNull(implemention);
		this.implementation = implemention;
	}
	
	private void ensureParameterNotNull(Object parameter) {
		

	}
	
}
