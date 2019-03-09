package com.github.glucose.modelbuilder;


import com.github.glucose.core.ComponentModel;

public class NameInspector implements Inspector{
	
	private String name;
	
	public NameInspector() {
		
	}
	
	public NameInspector(String name) {
		this.name = name;
	}
	
	public void processModel(ComponentModel model) {
		model.setName(name);
	}

	public void setName(String name) {
		if(name != null) {
			this.name = name;
		}
	}
	
	

}
