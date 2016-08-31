package exceptions;

public class ParameterFormatException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2619067710743352671L;

	public ParameterFormatException(String errorMessage){
		super(errorMessage);
	}
}
