/**
 * 
 */
package server.storage;

import utils.Item;
import utils.User;
import utils.Transaction;

/**
 * This class defines the different storage enginges API
 * @author Calle "Aiquen" Lejdbrandt
 *
 */
public abstract class Storage {

	public abstract boolean store(User user);
	
	public abstract boolean store(Item item);
	
	public abstract boolean store(Transaction transaction);
	
	public abstract Object extract(String key);
	
}
