package com.github.glucose.core;

public interface Factory<T> {
	
	public T provide();
	
	public void dispose(T instance);
	
}
