package com.example.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Subham Ashish (subham@sdrc.co.in)
 *
 */
@RestController
public class CustomErrorController implements ErrorController{

	@Autowired
	private ErrorAttributes errorAttributes;
	
	private static final String PATH ="/error";
	
	private boolean debug = true;
	
	@RequestMapping(value=PATH)
	public ErrorJson errorJson(HttpServletRequest request,HttpServletResponse response){
		
		return new ErrorJson(response.getStatus(),getErrorAttributes(request,debug));
		
	}
	
	private Map<String,Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
		
		  RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		
		return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
		
	}

	@Override
	public String getErrorPath() {
		
		return PATH;
	}
	
	

}
