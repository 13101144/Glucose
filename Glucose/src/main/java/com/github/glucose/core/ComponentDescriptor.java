package com.github.glucose.core;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.github.glucose.annotation.PerLookup;

/**
 * 组件描述符
 * @author yu
 *
 */
public class ComponentDescriptor {
	
	private final static Set<String> EMPTY_SERVICES_SET = Collections.emptySet();
    private final static Set<String> EMPTY_QUALIFIER_SET = Collections.emptySet();
	
	private Set<String> services;
	private String implementation;
	private String name;
	private String scope = PerLookup.class.getName();
	private Set<String> qualifiers;
	private ComponentType componentType = ComponentType.CLASS;

	private int rank;
	private Boolean proxiable;
	private Long id;
	
	public ComponentDescriptor() {
		
	}
	
	
	public Set<String> getServices() {
		if(services == null) return EMPTY_SERVICES_SET;
		return services;
	}

	public void setServices(Set<String> services) {
		this.services = services;
	}
	
	public void addAdvertisedService(String service) {
		if(service == null) return;
		if(services == null) services = new LinkedHashSet<String>();
		services.add(service);
	}
	
	public boolean removeAdvertisedContract(String service) {
	    if (service == null || services == null) return false;
	    return services.remove(service);
	}

	public String getImplementation() {
		return implementation;
	}
	
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public Set<String> getQualifiers() {
		if(qualifiers == null) return EMPTY_QUALIFIER_SET;
		return qualifiers;
	}
	
	public void setQualifiers(Set<String> qualifiers) {
		this.qualifiers = qualifiers;
	}
	
	public void addQualifier(String qualifier) {
	    if (qualifier == null) return;
	    if (qualifiers == null) qualifiers = new LinkedHashSet<String>();
	    qualifiers.add(qualifier);
	}
	
	public boolean removeQualifier(String qualifier) {
	    if (qualifier == null) return false;
	    if (qualifiers == null) return false;
	    return qualifiers.remove(qualifier);
	}
	
	public ComponentType getComponentType() {
		return componentType;
	}
	
	public void setComponentType(ComponentType componentType) {
		this.componentType = componentType;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public Boolean getProxiable() {
		return proxiable;
	}
	
	public void setProxiable(Boolean proxiable) {
		this.proxiable = proxiable;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
