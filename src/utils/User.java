/**
 * 
 */
package utils;

/**
 * Class representing a user in the system
 * 
 * @author Calle "Aiquen" Lejdbrandt
 *
 */
public class User {
	
	/**
	 * The userid, unique for a user 
	 */
	private String id;
	
	/**
	 * The username, doesn't have to be unique but would be confusing if it weren't
	 */
	private String name;
	
	/**
	 * The email adress to the user
	 */
	private String email;
	
	/**
	 * Static method to create a new user in the system without a user id
	 * @param username the username of the new user
	 * @param email the email adress to the new user
	 */
	public static User createUser(String username, String email) {
		return new User(username, email);
	}
	
	/**
	 * Constructor for a user. Used to instanciate known users. To create new users, see static method {@link createUser}
	 * @param userid the user id of the user
	 * @param username the username, human readable for the user
	 * @param email the email adress to use for the user
	 */
	public User(String userid, String username, String email) {
		this.id = userid;
		this.name = username;
		this.email = email;
	}
	
	/**
	 * private constructor used by the static method to create new users without user id´s
	 * @param username the new users username
	 * @param email the new users email
	 */
	private User(String username, String email) {
		this.name = username;
		this.email = email;
	}
	
	/**
	 * Getter for the username
	 * @return Human readable username for the user
	 */
	public String getUsername() {
		return this.name;
	}
	
	/**
	 * Setter for the username
	 * @param newname The new username for the user
	 */
	public void setUsername(String newname) {
		this.name = newname;
	}
	
	/**
	 * Getter for the userid
	 * @return The unique userid
	 */
	public String getUserid() {
		return this.id;
	}
	
	/**
	 * Getter for the user email adress
	 * @return the email adress of the user
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * Setter for the email adress of the user
	 * @param newemail the new email adress for the user
	 */
	public void setEmail(String newemail) {
		this.email = newemail;
	}
	
}
