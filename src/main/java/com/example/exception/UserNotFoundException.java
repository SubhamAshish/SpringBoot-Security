package com.example.exception;

/**
 * @author Subham Ashish(subham@sdrc.co.in)
 *
 */
public class UserNotFoundException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6918819129476664604L;

	public UserNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
