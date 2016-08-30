package core;

import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.DependencyException;
import exceptions.DuplicateItemException;
import exceptions.ItemNotSavedException;
import exceptions.NoItemWasFoundException;
import io.DataReader;
import io.DataWriter;
import models.Model;

/**
 * this class is the link between the ui and the rest of the classes
 * @author Moustafa Shama
 *
 */
public class DataController {
	private ArrayList<Model> models;
	private ArrayList<Integer> modifiedIndices;
	
	private String currentModel;
	
	private boolean isModified;
	
	public DataController(){
		models = new ArrayList<Model>();
		modifiedIndices = new ArrayList<Integer>();
		isModified = false;
	}
	
	/**
	 * this function reads data from given file and create models based on
	 * the modelName
	 * @param modelName name of the concrete model that we want to view and modify
	 * @param filename name of the file that we want to read from
	 * @return list of String[] where each element represents the data of the objects in the model array list
	 */
	public ArrayList<String[]> readDataFile(String modelName, String filename){
		this.currentModel = modelName;
		if(filename.matches("(.*).csv") || filename.matches("(.*).CSV")){
			ArrayList<String[]> fileContent = DataReader.read_CSV(filename, ",");
			
			for(int i=1/*skip header*/; i<fileContent.size(); i++){
				try {
					this.models.add( (Model) Class.forName("models." + this.currentModel).newInstance());
					this.models.get(i-1).setVariables(fileContent.get(i));
					modifiedIndices.add(i-1);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		ArrayList<String[]> result = new ArrayList<String[]>();
		// add table header
		result.add(this.models.get(0).getHeader());
		// fill data
		for(int i=0; i<this.models.size(); i++){
			result.add(this.models.get(i).getData());
		}

		this.isModified = true;
		
		return result;
	}
	
	/**
	 * @param modelName name of the concrete model that we want to view and modify
	 * @return list of String[] where each element represents the data of the objects in the model array list
	 * @throws NoItemWasFoundException if there are no data found in the database
	 */
	public ArrayList<String[]> readDataDB(String modelName) throws NoItemWasFoundException{
		this.currentModel = modelName;
		ArrayList<String[]> data = null;
		try {
			this.models.add( (Model) Class.forName("models." + this.currentModel).newInstance());
			data = this.models.get(0).selectAll();
			this.models = new ArrayList<Model>();
			for(int i=1/*skip header*/; i<data.size(); i++){
				try {
					this.models.add( (Model) Class.forName("models." + this.currentModel).newInstance());
					this.models.get(i-1).setVariables(data.get(i));
					this.models.get(i-1).setIsDBRecord(true);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			if(data.size() > 1){
				isModified = false;
				return data;
			} else{
				throw(new NoItemWasFoundException("No records were found in the database"));
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param filename name of the file where we are going to save our data
	 */
	public void extractData(String filename){
		ArrayList<String[]> result = new ArrayList<String[]>();
		// add table header
		result.add(this.models.get(0).getHeader());
		// fill data
		for(int i=0; i<this.models.size(); i++){
			result.add(this.models.get(i).getData());
		}
		
		DataWriter.write_CSV(result, filename, ",");
	}
	
	/**
	 * returns data stored in the model
	 * @return ArrayList of String array representation of all the objects in our models ArrayList
	 */
	public ArrayList<String[]> getData(){
		ArrayList<String[]> result = new ArrayList<String[]>();
		// add table header
		result.add(this.models.get(0).getHeader());
		// fill data
		for(int i=0; i<this.models.size(); i++){
			result.add(this.models.get(i).getData());
		}
		
		return result;
	}
	
	/**
	 * adds new data to models array list and add the index to modified index list
	 * @param newData that we want to insert
	 */
	public void addData(String[] newData){
		try {
			this.models.add((Model) Class.forName("models." + this.currentModel).newInstance());
			this.models.get(models.size()-1).setVariables(newData);
			this.modifiedIndices.add(models.size()-1);
			this.isModified = true;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * updates an existing record with new data at a certain index
	 * @param modifiedData the new data that we need to update our object with
	 * @param index of the object that needs to be updated
	 */
	public void updateData(String[] modifiedData, int index){
		this.models.get(index).setVariables(modifiedData);
		this.modifiedIndices.add(index);
		this.isModified = true;
	}
	
	/**
	 * @param index of the object that needs to be deleted
	 * @throws DependencyException if other records depends on the deleted record (foreign key)
	 */
	public void deleteData(int index) throws DependencyException{
		if(this.models.get(index).isDatabaseRecord()){
			try {
				this.models.get(index).delete();
				this.models.remove(index);
			} catch (ItemNotSavedException e) {
			} catch (SQLException e) {
			} catch (DependencyException e) {
				throw(e);
			}
		} else{
			this.models.remove(index);
		}
	}
	
	/**
	 * saves data for the modified model objects
	 * @throws DependencyException if trying to save data using a foreign key that does not exist
	 */
	public void saveData() throws DependencyException{
		for(int i=0; i<this.modifiedIndices.size(); i++){
			try {
				this.models.get(this.modifiedIndices.get(i)).save();
			} catch (DuplicateItemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.isModified = false;
		this.modifiedIndices = new ArrayList<Integer>();
	}
	
	/**
	 * @return boolean that identify if there were modifications made to the models in the controller
	 */
	public boolean isModified(){
		return this.isModified;
	}
	
}
