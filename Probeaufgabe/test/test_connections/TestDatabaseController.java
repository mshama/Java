package test_connections;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import connections.DatabaseController;

public class TestDatabaseController {

	@Test
	public void testGetInstance() {
		DatabaseController dbController = DatabaseController.getInstance();
		assert(dbController.equals(DatabaseController.getInstance()));
	}

	@Test
	public void testExecute_sql() {
		DatabaseController dbController = DatabaseController.getInstance();
		ResultSet rs = dbController.execute_sql("SELECT date('now') AS currentDate;");
		try{
			rs.next();
			String currentDate = rs.getString("currentDate");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			assert(dateFormat.format(date).equals(currentDate));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
