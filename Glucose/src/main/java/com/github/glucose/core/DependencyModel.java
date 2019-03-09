package com.github.glucose.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

/**
 * 依赖的关系
 * @author yu
 *
 */
public class DependencyModel {
	
	private Type requiredType;
    private Set<Annotation> qualifiers;
    private int position = -1;
    private Class<?> pClass;
    private boolean isOptional = false;

	
	public DependencyModel() {
		
	}
	
	public DependencyModel(Type requiredType) {
		this.requiredType = requiredType;
	}
	
	public DependencyModel(DependencyModel copyMe) {
        requiredType = copyMe.getRequiredType();
        position = copyMe.getPosition();
        pClass = copyMe.getInjecteeClass();
        qualifiers = Collections.unmodifiableSet(copyMe.getRequiredQualifiers());
        isOptional = copyMe.isOptional();
	}
	
	public DependencyModel(Type requiredType,
            Set<Annotation> qualifiers,
            int position,
            Class<?> pClass,
            boolean isOptional) {
        this.requiredType = requiredType;
        this.position = position;
        this.qualifiers = Collections.unmodifiableSet(qualifiers);
        this.pClass = pClass;
        this.isOptional = isOptional;         
    }
	
	public Type getRequiredType() {
        return requiredType;
    }
	
	public void setRequiredType(Type requiredType) {
        this.requiredType = requiredType;
    }
	
	public Set<Annotation> getRequiredQualifiers() {
        if (qualifiers == null) return Collections.emptySet();
        return qualifiers;
    }
	
	public void setRequiredQualifiers(Set<Annotation> requiredQualifiers) {
        qualifiers = Collections.unmodifiableSet(requiredQualifiers);    
    }
	
	public int getPosition() {
        return position;
    }
	
	public void setPosition(int position) {
        this.position = position;
    }
	
	public Class<?> getInjecteeClass() {
        return pClass;
    }
	
	public boolean isOptional() {
        return isOptional;
    }
	
	public void setOptional(boolean optional) {
        this.isOptional = optional;
    }
	
//	public boolean isSelf() {
//        return isSelf;
//    }
//	
//	public void setSelf(boolean self) {
//        isSelf = self;
//    }
	
//	public String toString() {
//        return "InjecteeImpl(requiredType=" + Pretty.type(requiredType) +
//                ",parent=" + Pretty.clazz(pClass) +
//                ",qualifiers=" + Pretty.collection(qualifiers) +
//                ",position=" + position +
//                ",optional=" + isOptional +
//                ",self=" + isSelf ;
//    }
	
		
}
