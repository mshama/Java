package exceptions;

/**
 * Signals that a trial to insert a duplicate item in the database
 * @author Moustafa Shama
 *
 */
public class DuplicateItemException extends Exception {
	
	public DuplicateItemException(String errorMessage){
		super(errorMessage);
	}
}
