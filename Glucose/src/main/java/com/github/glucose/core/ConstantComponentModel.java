package com.github.glucose.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public class ConstantComponentModel extends ComponentModel {
	
	private Object value;
	
	public ConstantComponentModel() {
		super();
		value = null;
	}
	
	
	public ConstantComponentModel(Object value,
			Set<Type> services,
            Class<? extends Annotation> scope,
            String name,
            Set<Annotation> qualifiers,
            ComponentType componentType) {
        super(services,
                scope,
                name,
                qualifiers,
                ComponentType.CLASS,
                0,
                false);
        if (value == null) throw new IllegalArgumentException();
        
        this.value = value;
    }


	public Object getValue() {
		return value;
	}


	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
	
}
