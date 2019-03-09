package com.github.glucose.core;

import java.util.ArrayList;
import java.util.List;

public class MethodModelCollection {
	
	public List<MethodModel> methodInfo = new ArrayList<MethodModel>();
	
	public MethodModelCollection() {
		
	}
	
	public MethodModel index(int i) {
		return methodInfo.get(i);
	}
	
	public void add(MethodModel method) {
		methodInfo.add(method);
	}
	
	public boolean remove(MethodModel method) {
		return methodInfo.remove(method);
	}
}
