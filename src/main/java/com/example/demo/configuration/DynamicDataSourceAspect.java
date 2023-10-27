/**
 *
 */
package com.example.demo.configuration;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.annotation.RoutingTransactional;
import com.example.demo.constant.DataSourceType;

/**
 * 
 */
@Aspect
@Order(-100) // To ensure execute before @Transactional
@Component
public class DynamicDataSourceAspect {

	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	@Before("execution(* com.example.demo.service.*.*(..)) && @annotation(com.example.demo.annotation.RoutingTransactional) ")
	public void beforeExecutionService(JoinPoint call) {
		MethodSignature signature = (MethodSignature) call.getSignature();
		Method method = signature.getMethod();
		RoutingTransactional target = method.getAnnotation(RoutingTransactional.class);

		if (target.readonly()) {
			DynamicDataSourceContextHolder.setDataSourceKey(DataSourceType.READ_ONLY);
		} else {
			DynamicDataSourceContextHolder.setDataSourceKey(DataSourceType.READ_WRITE);
		}
		logger.info("The AOP check execution before service !!!");
	}

	@After("execution(* com.example.demo.service.*.*(..))")
	public void afterExecutionService() {
		DynamicDataSourceContextHolder.clear();
		logger.info("The AOP check execution after service !!!");
	}
}
