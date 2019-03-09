package com.github.glucose.core;

import java.util.ArrayList;
import java.util.List;

public class PropertyModelCollection {
	
	public List<PropertyModel> properties = new ArrayList<PropertyModel>();
	
	public PropertyModelCollection() {
		
	}
	
	public PropertyModel index(int i) {
		return properties.get(i);
	}
	
	public void add(PropertyModel property) {
		properties.add(property);
	}
	
	public boolean remove(PropertyModel property) {
		return properties.remove(property);
	}
}
