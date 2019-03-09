package com.github.glucose.filter;

import com.github.glucose.core.ComponentModel;

public class GeneralFilter implements Filter{
	
	private final String contract;
    private final String name;
    
    public GeneralFilter(String contract, String name) {
        this.contract = contract;
        this.name = name;
    }
    
	public boolean matches(ComponentModel componentModel) {
		if(!name.equals(componentModel.getName()) ||
				!componentModel.getDescriptor().getServices().contains(contract)){
			return false;
		}
		return false;
	}
	
    public String getAdvertisedContract() {
        return contract;
    }

    public String getName() {
        return name;
    }

}
