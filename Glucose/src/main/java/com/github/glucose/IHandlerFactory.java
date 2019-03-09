package com.github.glucose;

import com.github.glucose.core.ComponentModel;

public interface IHandlerFactory {
	
	IHandler create(ComponentModel model);
}
