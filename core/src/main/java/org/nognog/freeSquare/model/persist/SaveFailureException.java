package org.nognog.freeSquare.model.persist;

/**
 * @author goshi 2014/11/01
 */
public class SaveFailureException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	SaveFailureException(){
	}
	SaveFailureException(Throwable t) {
		super(t);
	}
	SaveFailureException(String string) {
		super(string);
	}
	
	
}
