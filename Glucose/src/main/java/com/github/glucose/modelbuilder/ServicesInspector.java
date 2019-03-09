package com.github.glucose.modelbuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import com.github.glucose.core.ComponentModel;

public class ServicesInspector implements Inspector{
	
	private Set<String> services;
	
	public ServicesInspector() {

	}
	
	public ServicesInspector(Set<String> services) {
		this.services = services;
	}
	
	public void processModel(ComponentModel model) {
		model.setServices(services);
	}
	
	public void addService(String service) {
		if(services == null) {
			services = new LinkedHashSet<String>();
		}
		services.add(service);
	}

}
