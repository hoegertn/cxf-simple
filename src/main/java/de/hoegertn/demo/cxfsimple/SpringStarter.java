package de.hoegertn.demo.cxfsimple;

/*
 * #%L Daemon with Spring and CXF %% Copyright (C) 2013 Taimos GmbH %% Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License. #L%
 */

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public abstract class SpringStarter {
	
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	private final AtomicReference<AbstractXmlApplicationContext> context = new AtomicReference<>(null);
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public final void doStart() throws Exception {
		try {
			this.doBeforeSpringStart();
		} catch (Exception e) {
			throw new RuntimeException("Before spring failed", e);
		}
		
		Lock writeLock = this.rwLock.writeLock();
		AbstractXmlApplicationContext ctx = null;
		try {
			writeLock.lock();
			if (this.context.get() != null) {
				throw new RuntimeException("Already started");
			}
			ctx = this.createSpringContext();
			
			final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setProperties(System.getProperties());
			ctx.addBeanFactoryPostProcessor(configurer);
			
			ctx.setConfigLocation(this.getSpringResource());
			ctx.refresh();
		} catch (Exception e) {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception e1) {
					this.logger.warn("Failed to close context", e1);
				}
				ctx = null;
			}
			throw new RuntimeException("Spring context failed", e);
		} finally {
			if (ctx != null) {
				this.context.set(ctx);
			}
			writeLock.unlock();
		}
		
		try {
			this.doAfterSpringStart();
		} catch (Exception e) {
			throw new RuntimeException("After spring failed", e);
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
	protected String getSpringResource() {
		return "spring/beans.xml";
	}
	
	public final void doStop() throws Exception {
		try {
			this.doBeforeSpringStop();
		} catch (Exception e) {
			throw new RuntimeException("Before spring stop failed", e);
		}
		Lock writeLock = this.rwLock.writeLock();
		try {
			writeLock.lock();
			if (this.context.get() == null) {
				throw new RuntimeException("Not yet started");
			}
			this.context.get().stop();
			this.context.get().close();
			this.context.set(null);
		} catch (Exception e) {
			throw new RuntimeException("spring stop failed", e);
		} finally {
			writeLock.unlock();
		}
		
		try {
			this.doAfterSpringStop();
		} catch (Exception e) {
			throw new RuntimeException("After spring stop failed", e);
		}
	}
	
}
