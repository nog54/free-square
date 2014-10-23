package org.nognog.freeSquare.model.persist;

/**
 * @author goshi 2014/11/01
 */
public class LoadFailureException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	LoadFailureException(){
	}
	LoadFailureException(Throwable t) {
		super(t);
	}

}
