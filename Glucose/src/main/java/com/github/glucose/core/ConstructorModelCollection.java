package com.github.glucose.core;

import java.util.ArrayList;
import java.util.List;

public class ConstructorModelCollection {
	
	public List<ConstructorModel> constructors = new ArrayList<ConstructorModel>();
	
	public ConstructorModelCollection() {
		
	}
	
	public ConstructorModel index(int i) {
		return constructors.get(i);
	}
	
	public void add(ConstructorModel constructor) {
		constructors.add(constructor);
	}
	
	public boolean remove(ConstructorModel constructor) {
		return constructors.remove(constructor);
	}
}
