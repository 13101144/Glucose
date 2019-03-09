package com.github.glucose.modelbuilder;


import com.github.glucose.core.ComponentModel;

public class ScopeInspector implements Inspector{
	
	private String scope;
	
	public ScopeInspector() {
		
	}
	
	public ScopeInspector(String scope) {
		this.scope = scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}

	public void processModel(ComponentModel model) {
		if(scope != null) {
			model.setScope(scope);
		}
	}

}
