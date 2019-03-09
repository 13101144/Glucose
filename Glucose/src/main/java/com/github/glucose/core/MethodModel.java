package com.github.glucose.core;

import java.lang.reflect.Method;

public class MethodModel {
	
	private final Method method;
	private final DependencyModel[] dependencies;
	
	public MethodModel(Method method, DependencyModel[] dependencies) {
		this.method = method;
		this.dependencies = dependencies;
	}

	public Method getMethod() {
		return method;
	}

	public DependencyModel[] getDependencies() {
		return dependencies;
	}
	
	
	
	
	
}
