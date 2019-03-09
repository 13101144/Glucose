package com.github.glucose.filter;

import com.github.glucose.core.ComponentModel;

public class AlwayTrueFilter implements Filter{
	
	private static AlwayTrueFilter INSTANCE = new AlwayTrueFilter();
	  
	public static AlwayTrueFilter getDescriptorFilter() { return INSTANCE; }
	
	public boolean matches(ComponentModel componentModel) {
		return true;
	}

}
