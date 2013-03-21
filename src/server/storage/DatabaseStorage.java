package server.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	
	public boolean store(User user) {
		return true;
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
