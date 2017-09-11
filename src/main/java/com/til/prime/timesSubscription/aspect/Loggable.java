package com.til.prime.timesSubscription.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {
	enum Level {
		DEBUG,
		INFO,
		WARN,
		ERROR
	}

	Level level() default Level.INFO;
}
