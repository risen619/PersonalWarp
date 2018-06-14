package com.github.risen619.Database;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ForeignKey
{
	public enum Action {
		NO_ACTION,
		RESTRICT,
		SET_NULL,
		SET_DEFAULT,
		CASCADE
	};
	
	String refTable();
	String refField() default "id";
	Action onDelete() default Action.NO_ACTION;
	Action onUpdate() default Action.NO_ACTION;
}