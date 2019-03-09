package com.github.glucose.modelbuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import com.github.glucose.annotation.Contract;
import com.github.glucose.annotation.PerLookup;
import com.github.glucose.core.ComponentDescriptor;
import com.github.glucose.core.ComponentModel;
import com.github.glucose.core.ComponentType;
import com.github.glucose.core.ConstantComponentModel;
import com.github.glucose.core.Factory;
import com.github.glucose.util.ReflectionUtils;

public class BuilderHelper {
	
	public static IComponentModelBuilder link(String implementationClass, boolean addToServices) throws IllegalArgumentException {
		if (implementationClass == null) throw new IllegalArgumentException();
		
		return new DefaultComponentModelBuilder(implementationClass, addToServices);
	}
	
	public static IComponentModelBuilder link(String implementationClass) throws IllegalArgumentException {
	    return link(implementationClass);
	}
	
	public static IComponentModelBuilder link(Class<?> implementationClass, boolean addToContracts) throws IllegalArgumentException {
        if (implementationClass == null) throw new IllegalArgumentException();
        
        IComponentModelBuilder builder = link(implementationClass.getName(), addToContracts);
        
        return builder;
    }
	
	public static IComponentModelBuilder link(Class<?> implementationClass) throws IllegalArgumentException {
	    if (implementationClass == null) throw new IllegalArgumentException();
	    
	    boolean isFactory = (Factory.class.isAssignableFrom(implementationClass));
	    
	    IComponentModelBuilder db = link(implementationClass, !isFactory);
	    
	    return db;
	}
	
	
	
	public static ComponentModel createConstantComponent(Object constant) {
        if (constant == null) throw new IllegalArgumentException();
        
        return new ConstantComponentModel(
                constant,
                ReflectionUtils.getAdvertisedTypesFromObject(constant, Contract.class),
                ReflectionUtils.getScopeFromObject(constant, PerLookup.class),
                ReflectionUtils.getName(constant.getClass()),
                ReflectionUtils.getQualifiersFromObject(constant),
                ComponentType.CLASS);
    }
	
	public static ComponentModel createComponentFromClass(Class<?> clazz) {
        if (clazz == null) return new ComponentModel();
        
        Set<Type> contracts = ReflectionUtils.getAdvertisedTypesFromClass(clazz, Contract.class);
        String name = ReflectionUtils.getName(clazz);
        Class<? extends Annotation> scope = ReflectionUtils.getScopeFromClass(clazz, PerLookup.class);
        Set<Annotation> qualifiers = ReflectionUtils.getQualifierAnnotations(clazz);
        ComponentType type = ComponentType.CLASS;
        if (Factory.class.isAssignableFrom(clazz)) {
            type = ComponentType.PROVIDE_METHOD;
        }
        
        // TODO:  Can we get metadata from @Service?
        return new ComponentModel(
                contracts,
                scope,
                name,
                qualifiers,
                type,
                0,
                false);
    }
	
	public static ComponentModel deepCopyDescriptor(ComponentDescriptor copyMe) {
	    return new ComponentModel(copyMe);
	}
	
	
}
