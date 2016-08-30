package exceptions;

/**
 * Signals that a trial to insert a duplicate item in the database
 * @author Moustafa Shama
 *
 */
public class DuplicateItemException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1651206194147640085L;

	public DuplicateItemException(String errorMessage){
		super(errorMessage);
	}
}
