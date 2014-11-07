package de.hoegertn.demo.cxfsimple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public abstract class SpringTest {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AbstractXmlApplicationContext context;
	
	
	public AbstractXmlApplicationContext getContext() {
		return this.context;
	}
	
	public void start() {
		try {
			this.doBeforeSpringStart();
		} catch (Exception e) {
			this.logger.error("Before spring failed", e);
			throw new RuntimeException(e);
		}
		
		try {
			this.context = this.createSpringContext();
			
			final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setProperties(System.getProperties());
			this.context.addBeanFactoryPostProcessor(configurer);
			
			this.context.setConfigLocation(this.getSpringResource());
			this.context.refresh();
		} catch (Exception e) {
			this.logger.error("Spring context failed", e);
			throw new RuntimeException(e);
		}
		
		try {
			this.doAfterSpringStart();
		} catch (Exception e) {
			this.logger.error("After spring failed", e);
			throw new RuntimeException(e);
		}
	}
	
	protected void doAfterSpringStart() {
		//
	}
	
	protected void doBeforeSpringStart() {
		//
	}
	
	protected void doAfterSpringStop() {
		//
	}
	
	protected void doBeforeSpringStop() {
		//
	}
	
	/**
	 * @return the created Spring context
	 */
	protected AbstractXmlApplicationContext createSpringContext() {
		return new ClassPathXmlApplicationContext();
	}
	
	/**
	 * @return the name of the Spring resource
	 */
	protected abstract String getSpringResource();
	
	public void stop() {
		try {
			this.doBeforeSpringStop();
		} catch (Exception e) {
			this.logger.error("Before spring stop failed", e);
			throw new RuntimeException(e);
		}
		try {
			this.context.stop();
			this.context.close();
		} catch (Exception e) {
			this.logger.error("spring stop failed", e);
			throw new RuntimeException(e);
		}
		try {
			this.doAfterSpringStop();
		} catch (Exception e) {
			this.logger.error("After spring stop failed", e);
			throw new RuntimeException(e);
		}
	}
	
}
