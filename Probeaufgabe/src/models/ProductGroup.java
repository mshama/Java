package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connections.DatabaseController;
import exceptions.DependencyException;
import exceptions.DuplicateItemException;
import exceptions.ItemNotSavedException;
import exceptions.NoItemWasFoundException;

public class ProductGroup implements Model {
	private Integer productGroupID = null;
	private Integer upperProductGroupID = null;
	private String description = "";
	private boolean isDBRecord = false;
	
	public ProductGroup(){
	}
	
	public ProductGroup(int upperProductGroupID, String description){
		this.upperProductGroupID = upperProductGroupID;
		this.description = description;
	}
	
	public ProductGroup(int productGroupID, int upperProductGroupID, String description){
		this.productGroupID = productGroupID;
		this.upperProductGroupID = upperProductGroupID;
		this.description = description;
	}

	/**
	 * selects data from the database based on the given parameters
	 * @param field is the field used to do the search
	 * @param value is the value we are searching for
	 * @return ArrayList of Models that were returned from the query
	 */
	public static ArrayList<ProductGroup> select(String field, String value) throws NoItemWasFoundException{
		DatabaseController dbController = DatabaseController.getInstance();
		
		ArrayList<ProductGroup> result = new ArrayList<ProductGroup>();
		
		String sqlStmt = "SELECT * FROM 'ProductGroup'" +
						 "WHERE " + field + " = '" + value + "';";
		try{
			ResultSet rs = dbController.execute_sql(sqlStmt);
			while(rs.next()){
				ProductGroup temp = new ProductGroup(
														rs.getInt("ProductGroupID"),
														rs.getInt("UpperProductGroupID"),
														rs.getString("Description")
													);
				temp.setIsDBRecord(true);
				result.add(temp);
			}
		} catch(SQLException | DependencyException e){
			throw(new NoItemWasFoundException("no items corresponds to the selection criteria"));
		}
		
		return result;
		
	}
	
	@Override
	public void save() throws DuplicateItemException, DependencyException {
		if(isDBRecord){
			update();
		} else {
			insert();
		}
	}
	
	/**
	 * inserts a new record in the database
	 * @throws DuplicateItemException
	 */
	private void insert() throws DuplicateItemException{
		DatabaseController dbController = DatabaseController.getInstance();
		
		String sqlStmt = "INSERT INTO 'ProductGroup' ";
		
		String valueofUpperProductGroupID;
		try{
			valueofUpperProductGroupID = Integer.toString(this.upperProductGroupID);
		} catch(Exception e){
			valueofUpperProductGroupID = "NULL";
		}
		
		if(this.productGroupID == null){
		 	sqlStmt = sqlStmt + "(UpperProductGroupID, Description)" +
		 						"VALUES(" + valueofUpperProductGroupID + ",'" + this.description + "');";
		} else{
			sqlStmt = sqlStmt + "(ProductGroupID, UpperProductGroupID, Description)" +
								"VALUES(" + Integer.toString(this.productGroupID) + 
								        "," + valueofUpperProductGroupID + 
								        ",'" + this.description + "');";
		}
		
		ResultSet rs;
		try {
			rs = dbController.execute_sql(sqlStmt);
		} catch (SQLException | DependencyException e1) {
			throw (new DuplicateItemException("Item already Exists in the database"));
		}
		// if the product group id is empty then select the latest one from the database
		if(this.productGroupID == null){
			sqlStmt = "SELECT seq FROM sqlite_sequence where name='ProductGroup'";
			try {
				rs = dbController.execute_sql(sqlStmt);
				rs.next();
				this.productGroupID = rs.getInt("seq");
				isDBRecord = true;
			} catch (SQLException | DependencyException e1) {
				e1.printStackTrace();
			}
			
		} else{
			isDBRecord = true;
		}
		
		
	}
	
	/**
	 * updates database record of the current item
	 */
	private void update() throws DependencyException{
		DatabaseController dbController = DatabaseController.getInstance();
		
		String valueofUpperProductGroupID;
		try{
			valueofUpperProductGroupID = Integer.toString(this.upperProductGroupID);
		} catch(Exception e){
			valueofUpperProductGroupID = "NULL";
		}
		
		String sqlStmt = "UPDATE 'ProductGroup' SET" +
						 "'UpperProductGroupID' = " + valueofUpperProductGroupID + "," +
						 "'Description' = '" + this.description + "'" + 
				 		 "WHERE ProductGroupID = '" + Integer.toString(this.productGroupID) + "';";
		
		try {
			dbController.execute_sql(sqlStmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void delete() throws ItemNotSavedException, SQLException, DependencyException{
		if(!isDBRecord){
			throw(new ItemNotSavedException("Record does not exist in the database. cannot delete"));
		}
		
		DatabaseController dbController = DatabaseController.getInstance();
		
		String sqlStmt = "DELETE FROM 'ProductGroup'" +
				 		 "WHERE ProductGroupID = '" + Integer.toString(this.productGroupID) + "';";
		
		dbController.execute_sql(sqlStmt);
		
		this.isDBRecord = false; // this object does not correspond any more to a database record
	}

	@Override
	public String[] getHeader() {
		return new String[] {"Product Group ID", "Upper Product Group ID", "Description"};
	}

	@Override
	public String[] getData() {
		String valueofUpperProductGroupID;
		try{
			valueofUpperProductGroupID = Integer.toString(this.upperProductGroupID);
		} catch(Exception e){
			valueofUpperProductGroupID = "";
		}
		String valueofProductGroupID;
		try{
			valueofProductGroupID = Integer.toString(this.productGroupID);
		} catch(Exception e){
			valueofProductGroupID = "";
		}
		return new String[] {	
								valueofProductGroupID,
								valueofUpperProductGroupID,
								description
							};
	}

	@Override
	public void setVariables(String[] values) {
		if(values.length == 2){
			try{
				// check in case empty string is sent
				this.upperProductGroupID = Integer.valueOf(values[0]);
			} catch(Exception e){
				this.upperProductGroupID = null;
			}
			this.description = values[1];
		} else if(values.length == 3){
			try{
				this.productGroupID = Integer.valueOf(values[0]);
			} catch(Exception e){
				this.productGroupID = null;
			}
			try{
				// check in case empty string is sent
				this.upperProductGroupID = Integer.valueOf(values[1]);
			} catch(Exception e){
				this.upperProductGroupID = null;
			}
			this.description = values[2];
		}
		
	}

	public Integer getUpperProductGroupID() {
		return upperProductGroupID;
	}

	public void setUpperProductGroupID(Integer upperProductGroupID) {
		this.upperProductGroupID = upperProductGroupID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProductGroupID() {
		return productGroupID;
	}

	@Override
	public boolean isDatabaseRecord() {
		return isDBRecord;
	}
	
	@Override
	public void setIsDBRecord(boolean check){
		this.isDBRecord = check;
	}

	@Override
	public ArrayList<String[]> selectAll() throws NoItemWasFoundException {
		DatabaseController dbController = DatabaseController.getInstance();
		String sqlStmt = "SELECT * FROM ProductGroup";
		try{
			ResultSet rs = dbController.execute_sql(sqlStmt);
			ArrayList<String[]> data = new ArrayList<String[]>();
			data.add(this.getHeader());
			while(rs.next()){
				data.add(new String[]{Integer.toString(rs.getInt("ProductGroupID")),Integer.toString(rs.getInt("UpperProductGroupID")),rs.getString("Description")});
			}
			return data;
		} catch(SQLException | DependencyException e){
			throw(new NoItemWasFoundException("no items corresponds to the selection criteria"));
		}
	}

}
