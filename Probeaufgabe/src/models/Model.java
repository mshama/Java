package models;

import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.DuplicateItemException;
import exceptions.ItemNotSavedException;
import exceptions.NoItemWasFoundException;

/**
 * basic interface for database table models
 * @author Moustafa Shama
 *
 */
public interface Model {
	/**
	 * saves data into the database
	 */
	public void save() throws DuplicateItemException;
	
	public void delete() throws ItemNotSavedException, SQLException;
	
	/**
	 * selects data from the database based on the given parameters
	 * @param field is the field used to do the search
	 * @param value is the value we are searching for
	 * @return
	 */
	public static ArrayList<Model> select(String field, String value) throws NoItemWasFoundException{
		return null;
	}
	
	/**
	 * @return string array containing variable names to be used as 
	 * header for tables
	 */
	public String[] getHeader();
	
	/**
	 * @return value of variables as an array of Strings
	 */
	public String[] getData();
	
	/**
	 * used to set the values of the variables of the impelemented classes
	 * @param values a string array containing the values of the class variables
	 */
	public void setVariables(String[] values);
}
