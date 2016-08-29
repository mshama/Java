package models;

import java.util.ArrayList;

/**
 * basic interface for database table models
 * @author Moustafa Shama
 *
 */
public interface Model {
	/**
	 * saves data into the database
	 */
	public void save();
	
	/**
	 * selects data from the database based on the given parameters
	 * @param field is the field used to do the search
	 * @param value is the value we are searching for
	 * @return
	 */
	public static ArrayList<Model> select(String field, String value){
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
}
