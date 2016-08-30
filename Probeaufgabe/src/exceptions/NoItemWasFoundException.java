package exceptions;

/**
 * Signals that no item was found in the database
 * @author Moustafa Shama
 *
 */
public class NoItemWasFoundException extends Exception {

	/**
	 * Signals that no item was found in the database
	 */
	private static final long serialVersionUID = -1625962113274314618L;
	
	public NoItemWasFoundException(String errorMessage){
		super(errorMessage);
	}
}
