package com.github.glucose.core;

import java.lang.reflect.Field;

/**
 * 表示类的属性
 * @author yu
 *
 */
public class PropertyModel {
	
	private final Field field;
	private final DependencyModel dependency;
	
	public PropertyModel(Field field, DependencyModel dependency) {
		this.field = field;
		this.dependency = dependency;
	}

	public Field getField() {
		return field;
	}

	public DependencyModel getDependency() {
		return dependency;
	}

	
}
