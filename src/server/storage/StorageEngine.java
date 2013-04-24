package server.storage;

import utils.*;

/**
 * @author Calle "Aiquen" Lejdbrandt
 *
 */
public class StorageEngine {
	
	/**
	 * Holds the read config
	 */
	private ConfigReader config;
	
	/**
	 * The storage mechanism used to store values. Either database or flat file
	 */
	private Storage storage;
	
	public StorageEngine(ConfigReader config) throws Exception {
		this.config = config;
		if (this.config.getProperty("usedatabasestorage").equals("yes") || this.config.getProperty("usedatabasestorage").isEmpty()) {
			try {
				this.storage = new DatabaseStorage(this.config);
				System.out.println("Created database storage");
			} catch (Exception dbex) {
				try {
					this.storage = new FileStorage(this.config);
					System.out.println("Created flat file storage");
				} catch (Exception fiex) {
					System.out.println("Could not create any kind of storage");
					throw new Exception("Could not create any kind of storage");
				}
			}
		} else {
			try {
				this.storage = new FileStorage(this.config);
				System.out.println("Created flat file storage");
			} catch (Exception fiex) {
				System.out.println("Could not create a filestorage, and database storage is turned off");
				throw new Exception("Could not create file storage, and database storage is turned off");
			}
		}
	}
	
	public String store(User user) {
		return this.storage.store(user); 
	}
	
	public boolean store(Item item) {
		return this.storage.store(item);
	}
	
	public boolean store(Transaction transaction) {
		return this.storage.store(transaction);
	}
}
