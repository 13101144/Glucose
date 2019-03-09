package com.github.glucose.filter;

import com.github.glucose.core.ComponentModel;

public interface Filter {
	
	public boolean matches(ComponentModel componentModel);
}
