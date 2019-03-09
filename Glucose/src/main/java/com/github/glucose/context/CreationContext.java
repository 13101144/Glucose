package com.github.glucose.context;

import com.github.glucose.core.ComponentModel;

public interface CreationContext<T> {
	
	public T create(ComponentModel componentModel);
	
	public void dispose(T instance);
}
