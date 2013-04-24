package server.storage;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import utils.ConfigReader;
import utils.Item;
import utils.User;
import utils.Transaction;

/**
 * @author Calle "Aiquen" Lejdbrandt
 *
 */
public class DatabaseStorage extends Storage {
	
	/**
	 * Holds the read config
	 */
	private ConfigReader config;
	
	/**
	 * The connection to the database
	 */
	private Connection dbconn;
	
	/**
	 * Hostname to the database
	 */
	private String dbhost;
	
	/**
	 * Port for the database
	 */
	private int dbport;
	
	/**
	 * Username for connecting to the database
	 */
	private String dbuser;
	
	/**
	 * Password for connecting to the database
	 */
	private String dbpasswd;
	
	/**
	 * Database name
	 */
	private String dbname;
	
	/**
	 * Prefix to table names
	 */
	private String tblprefix;
	
	/**
	 * The prepaired statement to store users in database
	 */
	private PreparedStatement addUserStmnt;
	
	
	public DatabaseStorage(ConfigReader config) {
		this.config = config;
		
		this.dbhost = this.config.getProperty("dbhost");
		if (this.dbhost.isEmpty()) {
			System.out.println("Using localhost for database connetion");
			this.dbhost = "localhost";
		}
		
		try {
			this.dbport = Integer.parseInt(this.config.getProperty("dbport"));
		} catch (NumberFormatException e) {
			System.out.println("Configure port number to database is not a number. Using default number 3306");
			this.dbport = 3306;
		}
		
		this.dbname = this.config.getProperty("dbname");
		this.tblprefix = this.config.getProperty("tblprefix");
		this.dbuser = this.config.getProperty("dbuser");
		this.dbpasswd = this.config.getProperty("dbpasswd");
		
		// Connect to the database upon creation
		try {
			this.connect();
		} catch (SQLException sqlerr) {
			System.out.println("Could not connect to the database. SQLException thrown!");
			System.exit(3);
		}
		
		try {
			this.addUserStmnt = this.dbconn.prepareStatement("INSERT INTO " + this.tblprefix + "user (username, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException sqlerr) {
			System.out.println("Could not create prepared statement for inserting new user");
			System.exit(3);
		}
	}
	
	public void connect() throws SQLException {
		StringBuffer tmpurl = new StringBuffer("jdbc:mysql://");
		tmpurl.append(this.dbhost);
		tmpurl.append(":");
		tmpurl.append(this.dbport);
		tmpurl.append("/");
		tmpurl.append(this.dbname);
		String url = tmpurl.toString();
		
		try {
			this.dbconn = DriverManager.getConnection(url, this.dbuser, this.dbpasswd);
		} catch (SQLException sqlex) {
			System.out.println("Could not connect to the database");
			throw sqlex;
		}
	}
	
	public String store(User user) {
		try {
			this.addUserStmnt.setString(1, user.getUsername());
		} catch (SQLException sqlerr) {
			System.out.println("Could not set username in prepared statement");
		}
		
		try {
			this.addUserStmnt.setString(2, user.getEmail());
		} catch (SQLException sqlerr) {
			System.out.println("Could not set email in prepared statement");
		}
		
		ResultSet uid = null;

		try {
			int affectedRows = this.addUserStmnt.executeUpdate();
			if (affectedRows == 0) {
				System.out.println("No rows were affected.");
			}
			uid = this.addUserStmnt.getGeneratedKeys();
			if (! uid.next())
				System.out.println("No ID returned");
			
		} catch (SQLException sqlerr) {
			//Check if user already exists
			if (sqlerr.getErrorCode() == 1062) {
				return "Duplicate entry: " + sqlerr.getMessage();
			} else {
				System.out.println("Could not execute statement and get generated keys");
				System.out.println("Technical info:");
				System.out.println(sqlerr.getErrorCode());
				System.out.println(sqlerr.getSQLState());
				System.out.println(sqlerr.getMessage());
			}
		}
		
		User newUser = null;
		
		try {
			if (uid != null) {
				newUser = new User(uid.getString(1), user.getUsername(), user.getEmail());
			} else {
				throw new Exception("Could not create new user.");
			}
		} catch (SQLException sqlerr) {
			System.out.println("Could not create a new user object with the aquired data");
			System.out.println("Technical info:");
			System.out.println(sqlerr.getErrorCode());
			System.out.println(sqlerr.getSQLState());
			System.out.println(sqlerr.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		if (newUser != null)
			return "Success";
		else
			return "Fail";
		
	}
	
	public boolean store(Item item) {
		return true;
	}
	
	public boolean store(Transaction transaction) {
		return true;
	}
	
	public Object extract(String key) {
		return new Object();
	}

}
