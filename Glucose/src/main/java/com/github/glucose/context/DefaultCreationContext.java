package com.github.glucose.context;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.glucose.core.ComponentFinder;
import com.github.glucose.core.ComponentModel;
import com.github.glucose.core.DependencyModel;
import com.github.glucose.core.ServiceHandle;
import com.github.glucose.util.ExceptionCollector;
import com.github.glucose.util.ReflectionUtils;

public class DefaultCreationContext<T> implements CreationContext<T>{
	
	private final ComponentFinder finder;
	private final Class<?> implClass;
	
	private ComponentModel componentModel;
	
	private List<DependencyModel> allDependencies;
	
	private final Set<ResolutionInfo> myInitializers = new HashSet<ResolutionInfo>();
    private final Set<ResolutionInfo> myFields = new HashSet<ResolutionInfo>();
	
	private ClassAnalyzer analyzer;
	private ResolutionInfo myConstructor;

	private Method postConstructMethod;
	private Method preDestroyMethod;
	
	public DefaultCreationContext(ComponentFinder finder, Class<?> implClass,ExceptionCollector collector){
		this.finder = finder;
		this.implClass = implClass;
		initialize(collector);
	}
	
	void initialize(ExceptionCollector collector){
		this.componentModel = new ComponentModel();
		analyzer = new DefaultClassAnalyzer();
		
		List<DependencyModel> baseallDependencies = new LinkedList<DependencyModel>();
		
		AnnotatedElement element;
        List<DependencyModel> dependencies;
        
        element = ReflectionUtils.getChosenConstructor(implClass, collector);
        if (element == null) {
            myConstructor = null;
            return;
        }
        
        dependencies = ReflectionUtils.getConstructorDependencyModel((Constructor<?>) element);
        if (dependencies == null) {
            myConstructor = null;
            return;
        }
        
        baseallDependencies.addAll(dependencies);
        
        myConstructor = new ResolutionInfo(element, dependencies);
        
        Set<Method> initMethods = ReflectionUtils.findInitializerMethods(implClass, collector);
        for(Method initMethod : initMethods) {
        	element = initMethod;
        	
        	dependencies = ReflectionUtils.getMethodDependencyModel(initMethod);
        	if(dependencies==null) return;
        	
        	baseallDependencies.addAll(dependencies);
        	
        	myInitializers.add(new ResolutionInfo(element, dependencies));
        }
        
        Set<Field> fields = ReflectionUtils.findInitializerFields(implClass, collector);
        for (Field field : fields) {
            element = field;
            
            dependencies = ReflectionUtils.getFiledDependencyModel(field);
            if (dependencies == null) return;
            
            baseallDependencies.addAll(dependencies);
            
            myFields.add(new ResolutionInfo(element, dependencies));
        }
        
        postConstructMethod = ReflectionUtils.findPostConstruct(implClass, collector);
        preDestroyMethod = ReflectionUtils.findPreDestroy(implClass, collector);
        
        allDependencies = Collections.unmodifiableList(baseallDependencies);
        
	}
	
	private Map<DependencyModel, Object> resolveAllDependencies(ServiceHandle<?> root) throws IllegalStateException {
        Map<DependencyModel, Object> retVal = new HashMap<DependencyModel, Object>();
        
        InjectionResolver<?> resolver = ReflectionUtils.getInjectionResolver(finder, myConstructor.baseElement);
        for (DependencyModel injectee : myConstructor.dependencies) {
            retVal.put(injectee, resolver.resolve(injectee, root));
        }
        
        for (ResolutionInfo fieldRI : myFields) {
            resolver = ReflectionUtils.getInjectionResolver(finder, fieldRI.baseElement);
            for (DependencyModel injectee : fieldRI.dependencies) {
                retVal.put(injectee, resolver.resolve(injectee, root));
            }
        }
        
        for (ResolutionInfo methodRI : myInitializers) {
            resolver = ReflectionUtils.getInjectionResolver(finder, methodRI.baseElement);
            for (DependencyModel injectee : methodRI.dependencies) {
                retVal.put(injectee, resolver.resolve(injectee, root));
            }
        }
        
        return retVal;
    }
	
	public T create(ComponentModel componentModel) {
		// TODO Auto-generated method stub
		return null;
	}

	public void dispose(T instance) {
		// TODO Auto-generated method stub
		
	}
	
	private static class ResolutionInfo {
        private final AnnotatedElement baseElement;
        private final List<DependencyModel> dependencies = new LinkedList<DependencyModel>();

        private ResolutionInfo(AnnotatedElement baseElement, List<DependencyModel> injectees) {
            this.baseElement = baseElement;
            this.dependencies.addAll(injectees);
        }
    }

}
