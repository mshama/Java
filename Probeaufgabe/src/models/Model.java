package models;

import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.DependencyException;
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
	 * saves current object into the database
	 * @throws DuplicateItemException, DependencyException 
	 */
	public void save() throws DuplicateItemException, DependencyException;
	
	/**
	 * deletes current model from the database
	 * @throws ItemNotSavedException
	 * @throws SQLException
	 * @throws DependencyException
	 */
	public void delete() throws ItemNotSavedException, SQLException, DependencyException;
	
	/**
	 * selects data from the database based on the given parameters
	 * @param field is the field used to do the search
	 * @param value is the value we are searching for
	 * @return ArrayList of Models that were returned from the query
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
	
	/**
	 * @return if the object corresponds to a database records
	 */
	public boolean isDatabaseRecord();
	
	
	
	/**
	 * @param check value used to set the flag of isDBRecord
	 */
	public void setIsDBRecord(boolean check);

	/**
	 * selects all data for this model from the database
	 * @return all data for the given model as an ArrayList<String[]>
	 * @throws NoItemWasFoundException 
	 */
	public ArrayList<String[]> selectAll() throws NoItemWasFoundException;
}
