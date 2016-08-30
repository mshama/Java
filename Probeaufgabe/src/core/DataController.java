package core;

import java.util.ArrayList;

import exceptions.DuplicateItemException;
import io.DataReader;
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
	 * @param modelName
	 * @param filename
	 * @return
	 */
	public ArrayList<String[]> readData(String modelName, String filename){
		this.currentModel = modelName;
		if(filename.matches("(.*).csv") || filename.matches("(.*).CSV")){
			ArrayList<String[]> fileContent = DataReader.read_CSV(filename, ",");
			
			for(int i=1/*skip header*/; i<fileContent.size(); i++){
				try {
					this.models.add( (Model) Class.forName(this.currentModel).newInstance());
					this.models.get(i-1).setVariables(fileContent.get(i));
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
			result.add(this.models.get(0).getData());
		}
		
		return result;
	}
	
	/**
	 * adds new data to models array list and add the index to modified index list
	 * @param newData
	 */
	public void addData(String[] newData){
		try {
			this.models.add((Model) Class.forName(this.currentModel).newInstance());
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
	 * @param modifiedData
	 * @param index
	 */
	public void updateData(String[] modifiedData, int index){
		this.models.get(index).setVariables(modifiedData);
		this.modifiedIndices.add(index);
		this.isModified = true;
	}
	
	public void saveData(){
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
	
	public boolean isModified(){
		return this.isModified;
	}
	
}
