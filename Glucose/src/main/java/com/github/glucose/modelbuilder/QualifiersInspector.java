package com.github.glucose.modelbuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import com.github.glucose.core.ComponentModel;

public class QualifiersInspector {

	private Set<String> qualifiers;
	
	public QualifiersInspector() {
		
	}
	
	public QualifiersInspector(Set<String> qualifiers) {
		this.qualifiers = qualifiers;
	}
	

	public void processModel(ComponentModel model) {
		model.setQualifiers(qualifiers);
	}
	
	public void addQualifier(String qualifier) {
		if(qualifiers == null) {
			qualifiers = new LinkedHashSet<String>();
		}
		qualifiers.add(qualifier);
	}
}
