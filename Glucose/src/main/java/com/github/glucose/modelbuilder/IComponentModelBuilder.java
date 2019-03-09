package com.github.glucose.modelbuilder;

import java.lang.annotation.Annotation;
import com.github.glucose.core.ComponentModel;

public interface IComponentModelBuilder {
	
	public IComponentModelBuilder named(String named) throws IllegalArgumentException;
	
	public IComponentModelBuilder to(Class<?> contract) throws IllegalArgumentException;
	
	public IComponentModelBuilder to(String contract) throws IllegalArgumentException;
	
	public IComponentModelBuilder in(Class<? extends Annotation> scope) throws IllegalArgumentException;
	
	public IComponentModelBuilder in(String scope) throws IllegalArgumentException;
	
	public IComponentModelBuilder qualifiedBy(Annotation annotation) throws IllegalArgumentException;
	
	public IComponentModelBuilder qualifiedBy(String annotation) throws IllegalArgumentException;
	
	public ComponentModel build() throws IllegalArgumentException;
	
	public ComponentModel buildFactory() throws IllegalArgumentException;
	
	public ComponentModel buildProvideMethod() throws IllegalArgumentException;
}
