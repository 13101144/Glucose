package com.github.glucose.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import com.github.glucose.util.MultiException;

public interface ClassAnalyzer {
	
	public final static String DEFAULT_IMPLEMENTATION_NAME = "default";
	
	public <T> Constructor<T> getConstructor(Class<T> clazz) throws MultiException,NoSuchMethodException;
	
	public <T> Set<Method> getInitializerMethods(Class<T> clazz) throws MultiException;
	
	public <T> Set<Field> getFields(Class<T> clazz)throws MultiException;
	
	public <T> Method getPostConstructMethod(Class<T> clazz)throws MultiException;
	
	public <T> Method getPreDestroyMethod(Class<T> clazz)throws MultiException;
}
