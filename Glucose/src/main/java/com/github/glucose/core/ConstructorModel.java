package com.github.glucose.core;

import java.lang.reflect.Constructor;

public class ConstructorModel {
	
	private final Constructor<?> constructor;
	
	private final DependencyModel[] dependencies;
	
	public ConstructorModel(Constructor<?> constructor, DependencyModel[] dependencies) {
		this.constructor = constructor;
		this.dependencies = dependencies;
	}

	public Constructor<?> getConstructor() {
		return constructor;
	}

	public DependencyModel[] getDependencies() {
		return dependencies;
	}
	
	
}
