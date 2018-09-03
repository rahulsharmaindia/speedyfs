package org.speedyfs.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonAspect {
	private Log log = LogFactory.getLog(this.getClass());

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	private void controllerMethods() {
	}

	@Pointcut("@annotation(org.springframework.stereotype.Service)")
	private void serviceMethods() {
	}

	@Pointcut("@annotation(org.springframework.stereotype.Repository)")
	private void daoMethods() {
	}

	@Before("controllerMethods()")
	private void logRequestStart(JoinPoint joinPoint) {
		log.info("  -------------------REST SERVICE ENDPOINT INVOKED--------------------  " + joinPoint.getSignature().getDeclaringTypeName());
		log.info("  -------------------PARAMETERS : " + (joinPoint.getArgs().length == 1 ? joinPoint.getArgs()[0]
				: (joinPoint.getArgs().length == 2 ? String.valueOf(joinPoint.getArgs()[0]) + String.valueOf(joinPoint.getArgs()[1]) : "")));
	}

	@After("controllerMethods()")
	private void logRequestEnd(JoinPoint joinPoint) {
		log.info("  -------------------REST SERVICE ENDPOINT FINISHED--------------------  " + joinPoint.getSignature().getDeclaringTypeName());
	}

	@AfterReturning(pointcut = "controllerMethods()", returning = "response")
	public void afterReturningResponse(JoinPoint joinPoint, Object response) {
		log.info("  -------------------REST SERVICE ENDPOINT RETURNED--------------------  " + joinPoint.getSignature().getDeclaringTypeName()
				+ " --------IS RETURNING:---------------" + (response == null ? "" : response.toString()));
	}

	@Before("serviceMethods()")
	private void logServiceStart(JoinPoint joinPoint) {
		log.info("  -------------------SERVICE METHOD START--------------------  " + joinPoint.getSignature().getDeclaringTypeName());
		log.info("  -------------------PARAMETERS : " + (joinPoint.getArgs().length == 1 ? joinPoint.getArgs()[0]
				: (joinPoint.getArgs().length == 2 ? String.valueOf(joinPoint.getArgs()[0]) + String.valueOf(joinPoint.getArgs()[1]) : "")));
	}

	@After("serviceMethods()")
	private void logServiceEnd(JoinPoint joinPoint) {
		log.info("  -------------------SERVICE METHOD FINISHED--------------------  " + joinPoint.getSignature().getDeclaringTypeName());
	}

	@AfterReturning(pointcut = "serviceMethods()", returning = "response")
	public void afterReturningService(JoinPoint joinPoint, Object response) {
		log.info("  -------------------SERVICE METHOD RETURNED--------------------  " + joinPoint.getSignature().getDeclaringTypeName() + " --------IS RETURNING:---------------"
				+ (response == null ? "" : response.toString()));
	}

	@Before("daoMethods()")
	private void logDaoStart(JoinPoint joinPoint) {
		log.info("  -------------------DAO METHOD START--------------------  " + joinPoint.getSignature().getDeclaringTypeName());
		log.info("  -------------------PARAMETERS : " + (joinPoint.getArgs().length == 1 ? joinPoint.getArgs()[0]
				: (joinPoint.getArgs().length == 2 ? String.valueOf(joinPoint.getArgs()[0]) + String.valueOf(joinPoint.getArgs()[1]) : "")));
	}

	@After("daoMethods()")
	private void logDaoEnd(JoinPoint joinPoint) {
		log.info("  -------------------DAO METHOD FINISHED--------------------  " + joinPoint.getSignature().getDeclaringTypeName());
	}

	@AfterReturning(pointcut = "daoMethods()", returning = "response")
	public void afterReturningAdvice(JoinPoint joinPoint, Object response) {
		log.info("  -------------------DAO METHOD RETURNED--------------------  " + joinPoint.getSignature().getDeclaringTypeName() + " --------IS RETURNING:---------------"
				+ (response == null ? "" : response.toString()));
	}
}