package com.example.utils;

import java.sql.Timestamp;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionListener implements HttpSessionListener {

	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		
		httpSessionEvent.getSession().setMaxInactiveInterval(5 * 60); 
		
		logger.info("session----created"+new Timestamp(new java.util.Date().getTime()));
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		
		logger.info("session----destroyed"+new Timestamp(new java.util.Date().getTime()));
		
	}

}
