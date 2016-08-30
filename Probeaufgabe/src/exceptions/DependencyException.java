package exceptions;

public class DependencyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7486039482632702342L;

	public DependencyException(String errorMessage){
		super(errorMessage);
	}
}
