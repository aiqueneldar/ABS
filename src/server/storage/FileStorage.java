/**
 * 
 */
package server.storage;

import utils.ConfigReader;
import utils.Item;
import utils.Transaction;
import utils.User;

/**
 * @author Calle "Aiquen" Lejdbrandt
 *
 */

public class FileStorage extends Storage {

	/**
	 * Holds the read config
	 */
	private ConfigReader config;
	
	/**
	 * 
	 */
	public FileStorage(ConfigReader config) {
		this.config = config;
	}
	
	public String store(User user) {
		return "Success";
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
