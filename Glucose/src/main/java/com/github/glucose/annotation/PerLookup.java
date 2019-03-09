package com.github.glucose.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

/**
 * 每次查找时都会创建新的实例
 * @author yu
 *
 */
@Documented
@Retention(RUNTIME)
@Scope
@Target( { TYPE, METHOD })
public @interface PerLookup {

}
