package com.github.glucose.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


import com.github.glucose.util.ReflectionUtils;

public class ComponentModel {
	private final static Set<Annotation> EMPTY_QUALIFIER_SET = Collections.emptySet();
	
	private ComponentDescriptor descriptor;
	
	private Set<Type> serviceTypeSet = new LinkedHashSet<Type>();
    private Class<? extends Annotation> scopeAnnotation;
    private Set<Annotation> qualifierAnnotationSet;
	
    private boolean isReified = true;

    private ConstructorModelCollection constructors;
    
    private MethodModelCollection methods;
    
    private PropertyModelCollection properties;
    
    public ComponentModel() {
    	
    }
    
    public ComponentModel(ComponentDescriptor descriptor) {
    	this.descriptor = descriptor;
    }
    
    public ComponentModel(Set<Type> serviceTypeSet,
    		Class<? extends Annotation> scopeAnnotation,
            String name,
            Set<Annotation> qualifierAnnotationSet,
            ComponentType componentType,
            int ranking,
            Boolean proxy) {
    	
		this.scopeAnnotation = scopeAnnotation;
		this.serviceTypeSet.addAll(serviceTypeSet);
		if (qualifierAnnotationSet != null && !qualifierAnnotationSet.isEmpty()) {
			this.qualifierAnnotationSet = new LinkedHashSet<Annotation>();
			this.qualifierAnnotationSet.addAll(qualifierAnnotationSet);
		}
		descriptor = new ComponentDescriptor();
		descriptor.setRank(ranking);
		descriptor.setComponentType(componentType);
		descriptor.setName(name);
		descriptor.setProxiable(proxy);
		
		if(scopeAnnotation != null) {
			descriptor.setScope(scopeAnnotation.getName());
		}
		
		for(Type t : serviceTypeSet) {
			Class<?> rawType = ReflectionUtils.getRawClass(t);
			if(rawType == null) continue;
			
			descriptor.addAdvertisedService(rawType.getName());
		}
		
		if(qualifierAnnotationSet != null) {
			for (Annotation q : qualifierAnnotationSet) {
				descriptor.addQualifier(q.annotationType().getName());
            }
		}
    	
    }
       
    public boolean removeQualifierAnnotation(Annotation annotation) {
        if (annotation == null) return false;
        if (qualifierAnnotationSet == null) return false;
        
        boolean retVal = qualifierAnnotationSet.remove(annotation);
        descriptor.removeQualifier(annotation.annotationType().getName());
        
        return retVal;
    }
    
    public Set<Type> getServiceTypes() {
    	return Collections.unmodifiableSet(serviceTypeSet);
    }
    
    public void addServiceType(Type type) {
    	if(type == null) return;
    	
    	serviceTypeSet.add(type);
    	
    	Class<?> rawClass = ReflectionUtils.getRawClass(type);
    	if(rawClass == null) return;
    	descriptor.addAdvertisedService(rawClass.getName());
    }
    
    public boolean removeServiceType(Type type) {
    	if(type == null) return false;
    	
    	boolean retVal = serviceTypeSet.remove(type);
    	Class<?> rawClass = ReflectionUtils.getRawClass(type);
        if (rawClass == null) return retVal;
        
        return descriptor.removeAdvertisedContract(rawClass.getName());
    }

    public void setScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
    	this.scopeAnnotation = scopeAnnotation;
    	setScope(this.scopeAnnotation.getName());
    }
     
    public Class<? extends Annotation> getScopeAnnotation() {
        return scopeAnnotation;
    }
    
    public void setScope(String scope) {
    	descriptor.setScope(scope);
    }
    
    public void setServices(Set<String> services) {
    	descriptor.setServices(services);
    }
    
    public void setQualifiers(Set<String> qualifiers) {
    	descriptor.setQualifiers(qualifiers);
    }
    
    public void setServiceTypes(Set<Type> serviceTypeSet) {
    	this.serviceTypeSet = serviceTypeSet;
    }
      
    public void addQualifierAnnotation(Annotation annotation) {
    	if(annotation == null) return;
    	if(qualifierAnnotationSet == null) qualifierAnnotationSet = new LinkedHashSet<Annotation>();
    	qualifierAnnotationSet.add(annotation);
    }
    
    public void setImplementation(String implementation) {
    	this.descriptor.setImplementation(implementation);
    }
    
    public boolean isReified() {
    	return isReified;
    }
    
    public void setReified(boolean reified) {
    	this.isReified = reified;
    }

	public ComponentDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(ComponentDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public ConstructorModelCollection getConstructors() {
		return constructors;
	}

	public void setConstructors(ConstructorModelCollection constructors) {
		this.constructors = constructors;
	}

	public MethodModelCollection getMethods() {
		return methods;
	}

	public void setMethods(MethodModelCollection methods) {
		this.methods = methods;
	}

	public PropertyModelCollection getProperties() {
		return properties;
	}

	public void setProperties(PropertyModelCollection properties) {
		this.properties = properties;
	}
	
	public void setRank(int rank) {
		descriptor.setRank(rank);
	}

	public String getName() {
		return descriptor.getName();
	}

	public void setName(String name) {
		descriptor.setName(name);
	}

	public Set<Annotation> getQualifierAnnotationSet() {
		if (qualifierAnnotationSet == null)
			return EMPTY_QUALIFIER_SET;

		return Collections.unmodifiableSet(qualifierAnnotationSet);
	}

	public void setQualifierAnnotationSet(Set<Annotation> qualifierAnnotationSet) {
		this.qualifierAnnotationSet = qualifierAnnotationSet;
	}

	public Set<Type> getServiceSet() {
		return serviceTypeSet;
	}
	
	
    
    
}
