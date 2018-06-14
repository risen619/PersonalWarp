package com.github.risen619.Database;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface TableField
{
	String name();
	String type();
	String defaultValue() default "null";
	boolean nullable() default false;
	boolean unique() default false;
}