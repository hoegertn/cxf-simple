package de.hoegertn.demo.cxfsimple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

/**
 * Copyright 2014 Hoegernet<br>
 * <br>
 * 
 * @author Thorsten Hoeger
 *
 */
public class SpringDaemonTestRunner extends BlockJUnit4ClassRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringDaemonTestRunner.class);
	
	private SpringTest springTest;
	
	
	/**
	 * @param klass the class
	 * @throws InitializationError on error
	 */
	public SpringDaemonTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}
	
	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object test) {
		final Statement invoker = super.methodInvoker(method, test);
		return new Statement() {
			
			@Override
			public void evaluate() throws Throwable {
				invoker.evaluate();
			}
		};
	}
	
	@Override
	protected Statement withAfterClasses(Statement statement) {
		final Statement next = super.withAfterClasses(statement);
		return new Statement() {
			
			@Override
			public void evaluate() throws Throwable {
				next.evaluate();
				SpringDaemonTestRunner.this.springTest.stop();
			}
		};
	}
	
	@Override
	protected Statement withBeforeClasses(Statement statement) {
		final Statement next = super.withBeforeClasses(statement);
		return new Statement() {
			
			@Override
			public void evaluate() throws Throwable {
				final RunnerConfiguration cfgClass = this.findConfigAnnotation(SpringDaemonTestRunner.this.getTestClass().getJavaClass());
				if (cfgClass == null) {
					// Die on missing annotation
					throw new RuntimeException("Missing @RunnerConfiguration");
				}
				
				try {
					SpringDaemonTestRunner.this.springTest = new SpringTest() {
						
						@Override
						protected String getSpringResource() {
							return cfgClass.beanFile();
						}
						
					};
					SpringDaemonTestRunner.this.springTest.start();
					next.evaluate();
				} catch (BeansException | IllegalStateException e) {
					SpringDaemonTestRunner.logger.error("Starting Spring context failed", e);
					throw new RuntimeException("Starting Spring context failed", e);
				}
			}
			
			private RunnerConfiguration findConfigAnnotation(Class<?> clazz) {
				if (clazz == null) {
					return null;
				}
				RunnerConfiguration cfgClass = clazz.getAnnotation(RunnerConfiguration.class);
				if (cfgClass == null) {
					cfgClass = this.findConfigAnnotation(clazz.getSuperclass());
				}
				return cfgClass;
			}
		};
	}
	
	@Override
	protected Object createTest() throws Exception {
		return this.springTest.getContext().getBeanFactory().createBean(this.getTestClass().getJavaClass());
	}
	
	
	/**
	 * Copyright 2014 Hoegernet<br>
	 * <br>
	 * 
	 * @author Thorsten Hoeger
	 *
	 */
	@Target({ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface RunnerConfiguration {
		
		/**
		 * @return the Spring file nme
		 */
		String beanFile() default "spring-test/beans.xml";
		
	}
}