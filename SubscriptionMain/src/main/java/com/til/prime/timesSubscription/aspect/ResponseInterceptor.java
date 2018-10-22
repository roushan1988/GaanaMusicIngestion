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
		StringBuilder sb = new StringBuilder();
		Object returnValue = null;
		Long start = System.currentTimeMillis();
		returnValue = joinPoint.proceed();
		Long end = System.currentTimeMillis();
		if(method != null){
			sb.append("ResponseTimeInterceptor controller: "+joinPoint.getTarget().getClass()+" api name: "+method.getName()).append(", ");
		}
		sb.append("Time taken in millis :"+(end-start)).append(", ");
		if (null != method.getParameterTypes() && null != joinPoint.getArgs() && joinPoint.getArgs().length > 0) {
			sb.append("Request: ");
			for(int i=0; i<joinPoint.getArgs().length; i++){
				sb.append(joinPoint.getArgs()[i]!=null? joinPoint.getArgs()[i].toString(): "").append(", ");
			}
		}
		if (null != returnValue ) {
			sb.append(", Response: "+ returnValue.toString()).append(", ");
		}
		LOG.info(sb);
		return returnValue;
	}

}
