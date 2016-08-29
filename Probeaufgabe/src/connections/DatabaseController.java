package connections;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * this is a singlton class responsible for creating connection with the
 * database and handle the execution of sql statments
 * 
 * @author Moustafa Shama
 *
 */
public class DatabaseController {
	private static final DatabaseController dbController = new DatabaseController();
	private static Connection connection;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading JDBC driver");
			e.printStackTrace();
		}
	}

	private DatabaseController() {
		initDBConnection();
	}

	/**
	 * @return an instance of the class
	 */
	public static DatabaseController getInstance() {
		return dbController;
	}
	
	/**
	 * this function handles the initialization of the database connection
	 */
	private void initDBConnection() { 
        try { 
            if (connection != null) 
                return; 
            System.out.println("Creating Connection to Database...");
            String DB_PATH = System.getProperty("user.dir") + "/res/db/database.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH); 
            if (!connection.isClosed()) 
                System.out.println("...Connection established"); 
        } catch (SQLException e) { 
            throw new RuntimeException(e); 
        } 
        
        // closes the connection when program ends
        Runtime.getRuntime().addShutdownHook(new Thread() { 
            public void run() { 
                try { 
                    if (!connection.isClosed() && connection != null) { 
                        connection.close(); 
                        if (connection.isClosed()) 
                            System.out.println("Connection to Database closed"); 
                    } 
                } catch (SQLException e) { 
                    e.printStackTrace(); 
                } 
            } 
        }); 
    }
	
	/**
	 * this function executes the given SQL statement.
	 * @param sqlStmt
	 * @return the result set in case of a select statement
	 * or null in case of insert, update or delete statements.
	 */
	public ResultSet execute_sql(String sqlStmt){
		try{
			Statement stmt = connection.createStatement();
			return stmt.executeQuery(sqlStmt);
		} catch (Exception e){
			return null;
		}
	}
}
