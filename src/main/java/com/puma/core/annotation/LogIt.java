package com.puma.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.puma.common.LogLevelEnum;
import com.puma.common.LogTypeEnum;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogIt {
	
	String message() default "user do sth!";
	
	LogTypeEnum logType() default LogTypeEnum.RETRIEVE;
	
	LogLevelEnum logLevel() default LogLevelEnum.ALL;
}
