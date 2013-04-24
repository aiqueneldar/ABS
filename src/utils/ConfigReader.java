/**
 * 
 */
package utils;

/**
 * Import necesary libraries to read file and set properties
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Date;
import java.net.URISyntaxException;

/**
 * @author Calle "Aiquen" Lejdbrandt
 *
 */
public class ConfigReader {
	
	/**
	 * The properties read from configuration file
	 */
	private Properties properties;
	
	/**
	 * The filepath and name
	 */
	private String file;
	
	/**
	 * Default constructor 
	 * 
	 * Loads in the default config file
	 */
	public ConfigReader() {
		this.properties = this.parseProperties();
	}
	
	/**
	 * Constructor for specefied config
	 * 
	 * Loads in the specefied config file
	 * 
	 * @param fileName the custom config file
	 */
	public ConfigReader(String fileName) {
		this.properties = this.parseProperties(fileName);
	}
	
	/**
	 * Parses default properties file
	 * 
	 * Parses the default config file if none is given explicitly
	 * 
	 * @return a Properties object populated with different config parameters
	 */
	private Properties parseProperties() {
		Properties prop = new Properties();
		InputStream is = null;
		try {
			// get the path of the config file and append hardcoded config filename
			is = this.getClass().getClassLoader().getResourceAsStream("etc/server.cfg");
			this.file = "server.cfg";
			
			// load properties from file
			prop.load(is);
			
		} catch (FileNotFoundException e) {
			System.out.println("No configuration file found. Please create configuration file: " + this.file);
			System.out.println("Technical Error Messages: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException t) {
			System.out.println("Could not load configuration file. Please make sure the file is readable by java process");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {} //ignore
			}
		}
		
		return prop;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param fileName name of the configuration file if not-standard "server.cfg"
	 * @return a Properties object populated with different config parameters
	 */
	private Properties parseProperties(String fileName) {
		Properties prop = new Properties();
		InputStream is = null;
		try {
			this.file = "etc/" + fileName;
			// get the path of the config file and append hardcoded config filename
			is = new FileInputStream(this.file);
			
			// load properties from file
			prop.load(is);
			
		} catch (FileNotFoundException e) {
			System.out.println("No configuration file found. Please create configuration file: " + this.file);
			System.out.println("Technical Error Messages: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException t) {
			System.out.println("Could not load configuration file. Please make sure the file is readable by java process");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {} //ignore
			}
		}
		
		return prop;
	}
	
	/**
	 * Get the specefied property
	 * 
	 * Gets the specefied property from the config file. 
	 * Will always return String either with property value or 
	 * magic string <:PropertyNotFound:>
	 * 
	 * @param varName the property key wanted
	 * @return Returns string with the property value or <:PropertyNotFound:>
	 */
	public String getProperty(String varName) {
		return this.properties.getProperty(varName, "<:PropertyNotFound:>");
	}
	
	/**
	 * Sets a specifik property
	 * 
	 * Sets one property at a time. Takes the property to set and the value both as strings.
	 * 
	 * @param varName The proerty to set
	 * @param value The value to set the property to.
	 */
	public void setProperty(String varName, String value) {
		this.properties.setProperty(varName, value);
	}
	
	/**
	 * Save changes to config file
	 * 
	 * Saves the loaded properties back into the file they we're loaded from.
	 * 
	 * @return true on successfull filewrite, false otherwhise.
	 */
	public boolean saveProperties() {
		OutputStream os = null;
		boolean success = false;
		try {
			os = new FileOutputStream(new File(this.getClass().getClassLoader().getResource(this.file).toURI()));
			System.out.println(this.getClass().getClassLoader().getResource(this.file).toURI());
			Date today = new Date();
			this.properties.store(os, "Configure updated on: " + today.toString());
			success = true;
		} catch (FileNotFoundException e) {
			System.out.println("No configuration file found. Please make sure a file has been loaded");
		} catch (IOException e) {
			System.out.println("Coould not write to config file. Please make sure directory and file is writeable by java process");
		} catch (URISyntaxException e) {
			System.out.println("Something is wrong with the URI syntax");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {} //ignore
			}
		}
		
		return success;
	}
	
	/**
	 * Reload propertis from file
	 * 
	 * This class reloads properties from the file.
	 * WARNING: unsaved changes made to properties will not be saved to file!
	 */
	public void reloadProperties() {
		this.properties = this.parseProperties(this.file);
	}
}
