package com.github.glucose.resolvers;

import com.github.glucose.core.DependencyModel;


public interface IDependencyResolver {
	
	boolean canResolve();
	
	Object resolve(DependencyModel dependencyModel);
}
