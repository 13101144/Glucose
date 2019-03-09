package com.github.glucose.resolvers;

import com.github.glucose.core.DependencyModel;

public class DefaultDependencyResolver implements IDependencyResolver{

	public boolean canResolve() {

		return false;
	}

	public Object resolve(DependencyModel dependencyModel) {

		return null;
	}

}
