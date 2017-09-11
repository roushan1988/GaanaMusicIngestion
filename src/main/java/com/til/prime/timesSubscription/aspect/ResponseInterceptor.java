package com.til.prime.timesSubscription.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.lang.reflect.Method;

@Aspect
@Component
public class ResponseInterceptor extends WebContentInterceptor {

	private static final Logger LOG = Logger.getLogger(ResponseInterceptor.class);

	@Around(value = "@annotation(loggable)", argNames = "joinPoint,loggable")
	public Object process(ProceedingJoinPoint joinPoint, final Loggable loggable) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		if(method != null){
			LOG.info("ResponseInterceptor api name "+method.getName());
		}
		if (null != method.getParameterTypes() && null != joinPoint.getArgs() && joinPoint.getArgs().length > 0) {
			LOG.info("ResponseInterceptor Request "+ joinPoint.getArgs()[0].toString());
		}
		Object returnValue = null;
		Long start = System.currentTimeMillis();
		returnValue = joinPoint.proceed();
		Long end = System.currentTimeMillis();
		if (null != returnValue ) {
			LOG.info("ResponseInterceptor Response "+ returnValue.toString());
		}
		LOG.info("ResponseInterceptor time taken in millis :"+(end-start));
		return returnValue;
	}

}
