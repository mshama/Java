package exceptions;

/**
 * Signals that the item is not saved and the operation insert, update,
 * delete cannot be performed
 * @author Moustafa Shama
 *
 */
public class ItemNotSavedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8146260255529458982L;

	public ItemNotSavedException(String errorMessage){
		super(errorMessage);
	}
}
