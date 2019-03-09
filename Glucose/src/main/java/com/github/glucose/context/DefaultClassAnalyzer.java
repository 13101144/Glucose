package com.github.glucose.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;



import com.github.glucose.util.ExceptionCollector;
import com.github.glucose.util.MultiException;
import com.github.glucose.util.ReflectionUtils;

public class DefaultClassAnalyzer implements ClassAnalyzer{
	
	public DefaultClassAnalyzer(){
		
	}

	public <T> Constructor<T> getConstructor(Class<T> clazz)
			throws MultiException, NoSuchMethodException {
		ExceptionCollector collector = new ExceptionCollector();
		
		Constructor<T> retVal = (Constructor<T>)
				ReflectionUtils.getChosenConstructor(clazz, collector);
		
		try {
            collector.throwIfErrors();
        }
        catch (MultiException me) {
            for (Throwable th : me.getErrors()) {
                if (th instanceof NoSuchMethodException) {
                    throw (NoSuchMethodException) th;
                }
            }
            
            throw me;
        }
        
        return retVal;
	}

	public <T> Set<Method> getInitializerMethods(Class<T> clazz)
			throws MultiException {
		ExceptionCollector collector = new ExceptionCollector();
		
		Set<Method> retVal = ReflectionUtils.findInitializerMethods(clazz, collector);
		
		collector.throwIfErrors();
		
		return retVal;
	}

	public <T> Set<Field> getFields(Class<T> clazz) throws MultiException {
		ExceptionCollector collector = new ExceptionCollector();
        
        Set<Field> retVal = ReflectionUtils.findInitializerFields(clazz, collector);
        
        collector.throwIfErrors();
        
        return retVal;
	}

	public <T> Method getPostConstructMethod(Class<T> clazz)
			throws MultiException {
		ExceptionCollector collector = new ExceptionCollector();
        
        Method retVal = ReflectionUtils.findPostConstruct(clazz, collector);
        
        collector.throwIfErrors();
        
        return retVal;
	}

	public <T> Method getPreDestroyMethod(Class<T> clazz) throws MultiException {
		ExceptionCollector collector = new ExceptionCollector();
        
        Method retVal = ReflectionUtils.findPreDestroy(clazz, collector);
        
        collector.throwIfErrors();
        
        return retVal;
	}
	
}
