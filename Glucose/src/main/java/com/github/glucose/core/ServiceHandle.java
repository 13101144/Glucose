package com.github.glucose.core;

public interface ServiceHandle<T> {
	
    public T getService();
    
    public boolean isActive();

    public void destroy();
}
