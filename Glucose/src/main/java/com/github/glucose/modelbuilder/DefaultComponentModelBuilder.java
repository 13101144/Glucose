package com.github.glucose.modelbuilder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.github.glucose.core.ComponentModel;



public class DefaultComponentModelBuilder implements IComponentModelBuilder{
	
	private String implementation;
	
	private GenericInspector genericInspector;
	
	private NameInspector nameInspector;
	
	private ScopeInspector scopeInspector;
	
	private QualifiersInspector qualifiersInspector;
	
	private ServicesInspector servicesInspector;
	
	private List<Inspector> extendInspectors;
	
	public DefaultComponentModelBuilder() {
		this(null);
	}
	
	public DefaultComponentModelBuilder(String implementation) {
		this(implementation,true);
	}
	
	public DefaultComponentModelBuilder(String implementation, boolean addToContracts) {
		genericInspector = new GenericInspector();
		nameInspector = new NameInspector();
		scopeInspector = new ScopeInspector();
		qualifiersInspector = new QualifiersInspector();
		servicesInspector = new ServicesInspector();
		if(implementation != null) {
			this.implementation = implementation;
		    if (addToContracts) {
		    	servicesInspector.addService(implementation);
		    }
		}
	}
	
	public IComponentModelBuilder named(String name)
			throws IllegalArgumentException {
		nameInspector.setName(name);
		return this;
	}

	public IComponentModelBuilder to(Class<?> service)
			throws IllegalArgumentException {
		if (service == null) throw new IllegalArgumentException();
		return to(service.getName());
	}



	public IComponentModelBuilder to(String service)
			throws IllegalArgumentException {
		if(service == null) throw new IllegalArgumentException();
		servicesInspector.addService(service);
		return this;
	}



	public IComponentModelBuilder in(Class<? extends Annotation> scope)
			throws IllegalArgumentException {
		if(scope == null) throw new IllegalArgumentException();
		return in(scope.getName());
	}



	public IComponentModelBuilder in(String scope)
			throws IllegalArgumentException {
		if (scope == null) {
		      throw new IllegalArgumentException();
		}
		scopeInspector.setScope(scope);
		return this;
	}



	public IComponentModelBuilder qualifiedBy(Annotation annotation)
			throws IllegalArgumentException {
		if (annotation == null) throw new IllegalArgumentException();
		
		return qualifiedBy(annotation.annotationType().getName());
	}



	public IComponentModelBuilder qualifiedBy(String annotation)
			throws IllegalArgumentException {
		if(annotation == null) throw new IllegalArgumentException();
		qualifiersInspector.addQualifier(annotation);
		
		return this;
	}
	
	
	
	public void addInspector(Inspector inspector) {
		if(extendInspectors == null) {
			extendInspectors = new ArrayList<Inspector>();
		}
		extendInspectors.add(inspector);
	}

	public ComponentModel build() throws IllegalArgumentException {
		ComponentModel model = new ComponentModel();
		genericInspector.processModel(model);
		nameInspector.processModel(model);
		scopeInspector.processModel(model);
		servicesInspector.processModel(model);
		qualifiersInspector.processModel(model);
		if(extendInspectors != null) {
			for(Inspector inspector : extendInspectors) {
				inspector.processModel(model);
			}
		}
		return model;
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public ComponentModel buildFactory() throws IllegalArgumentException {
		return null;
	}

	public ComponentModel buildProvideMethod() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
