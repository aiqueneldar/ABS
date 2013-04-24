/**
 * The networking package that acts as server and handles clients and client commands
 */
package server.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import server.storage.StorageEngine;
import utils.*;

/**
 * @author Calle "Aiquen" Lejdbrandt
 *
 */
public class ClientProcesser implements Runnable {

	/**
	 * Holds the socket when used for connected client
	 */
	private Socket cliSocket;
	
	/**
	 * Holds the output stream for the transmit method to use
	 */
	private PrintWriter output;
	
	/**
	 * Holds the storage enginge used to store and retrive values.
	 */
	private StorageEngine storage;
	
	/**
	 * Holds the read config
	 */
	private ConfigReader config;
	
	/**
	 * Constructor that takes a client socket connection and processes client commands
	 * @param client A Socket to use for a connecting client
	 */
	public ClientProcesser(ConfigReader config, Socket client) {
		this.cliSocket = client;
		try {
			this.output = new PrintWriter(this.cliSocket.getOutputStream(), true);
		} catch (IOException ioerr) {
			System.out.println("Can't get output stream to client. Quitting.\nTechnical info:");
			System.out.println(ioerr.getMessage());
			ioerr.printStackTrace();
		}
		try {
			this.storage = new StorageEngine(config);
		} catch (Exception stex) {
			this.storage = null;
		}
		this.config = config;
		
	}
	/**s
	 * Thread start
	 * 
	 * First method run when new thread starts. Sets up an incoming stream and processes commands.
	 */
	public void run() {
		BufferedReader data;
		// Check if we have room for more connections before processing the first command. maxConnections of 0 means unlimited number of clients can connect
		System.out.println("Current connections: " + ServerEngine.currConnections + "/" + ServerEngine.maxConnections);
		if  ((ServerEngine.currConnections + 1) > ServerEngine.maxConnections && ServerEngine.maxConnections != 0) {
			this.transmit("FAIL;Too many clients connected to server. Disconnecting...");
		System.out.println("Too many connected clients. Disconnecting current client: " + this.cliSocket.getRemoteSocketAddress().toString());
			return;
		} else {
			++ServerEngine.currConnections;
		}
	
		try {
			data = new BufferedReader(new InputStreamReader(this.cliSocket.getInputStream()));
		} catch(IOException e) {
			System.out.println("Lost connection to client");
			return;
		}
	
		String command;
	
		try {
			command = data.readLine();
			System.out.println("Command: " + command);
		} catch (IOException e) {
			System.out.println("Couldn't get command string");
			return;
		}
		
		while(command != null) {
			if (!this.process(command)) {
				this.transmit("MSG;Disconnected");
				try {
					this.cliSocket.close();
				} catch (IOException ioerr) {
					System.out.println("Couldn't close connection to client.");
				}
				--ServerEngine.currConnections;
				break;
			}
			try {
				command = data.readLine();
			} catch (IOException e) {
				System.out.println("Couldn't get new command string");
				return;
			}
		}
	
	}

	/**
	 * Process a command
	 * 
	 * Takes a String command and process it
	 * 
	 * @param command the command to process
	 * @return True if client is recognized and command is valid, False otherwise
	 */
	protected boolean process(String data) {
		String[] parts = data.split(";", 3);
		if (! this.authClient(parts[0])) {
			this.transmit("FAIL;Not authorized");
			return false;
		}
		
		//Prepair a returnstring to send back to the client.
		//The string will always have format <SUCCESS|FAIL>;<Human readable message>|<payload data>
		String returnstring = "FAIL;Not a recognized command!";
		
		// Rename the part with command data for better readability
		String payload = "";
		if (parts.length >= 3)
			payload = parts[2];
		
		// Switch the commands. This function looks for valid commands. The function executing the command valuates the payload data for the command
		switch (parts[1]) {
		case "adduser": 
			String result = this.addUser(payload);
			if (result.equals("Success")) {
				returnstring = "SUCCESS;User added";
			} else {
				returnstring = "FAIL;" + result;
			}
			
			break;
			
		case "removeuser":
			if (this.removeUser(payload)) {
				returnstring = "SUCCSS;User removed";
			} else {
				returnstring = "FAIL;User not removed";
			}
			
			break;
			
		case "getuserinfofromid":
			int uid = -1;
			try {
				uid = Integer.parseInt(payload);
			} catch (NumberFormatException nan) {
				System.out.println("getUserInfo id is not a number [" + payload + "]");
				returnstring = "FAIL;ID is not a number";
				break;
			}
			String userinfo = this.getUserInfo(uid);
			if (userinfo.isEmpty()) {
				returnstring = "FAIL;No user registered with ID " + payload;
			} else {
				returnstring = "SUCCESS;" + userinfo;
			}
			
			break;
			
		case "getuserinfofromname":
			userinfo = this.getUserInfo(payload);
			if (userinfo.isEmpty()) {
				returnstring = "FAIL;No user registered with name " + payload;
			} else {
				returnstring = "SUCCESS;" + userinfo;
			}
			
			break;
			
		case "changeuser":
			if (this.changeUser(payload)) {
				returnstring = "SUCCESS;User info updated";
			} else {
				returnstring = "FAIL;Could not update information";
			}
			
		case "getallusers":
			String userids = this.getAllUsers();
			if (userids.isEmpty()) {
				returnstring = "FAIL;No users registered with server!";
			} else {
				returnstring = "SUCCESS;" + userids;
			}
			
			break;
			
		case "additem":
			if (this.addItem(payload)) {
				returnstring = "SUCCESS;Item added";
			} else {
				returnstring ="FAIL;Couldn't add item";
			}
			
			break;
			
		case "removeitem":
			if (this.removeItem(payload)) {
				returnstring = "SUCCESS;Item removed";
			} else {
				returnstring = "FAIL;Couldn't remove item";
			}
			
			break;
			
		case "getiteminfo":
			String info = this.getItemInfo(payload);
			if (info.isEmpty()) {
				returnstring = "FAIL;Item not found";
			} else {
				returnstring = "SUCCESS;" + info;
			}
			
			break;
		
		case "getallitems":
			info = this.getAllItems();
			if (info.isEmpty()) {
				returnstring = "FAIL;No items recorded";
			} else {
				returnstring = "SUCCESS;" + info;
			}
			
			break;
			
		case "changeitem":
			if (this.changeItem(payload)) {
				returnstring = "SUCCESS;Item changed";
			} else {
				returnstring = "FAIL;Couldn't change item";
			}
			
			break;
		
		case "addtransaction":
			if (this.addTransaction(payload)) {
				returnstring = "SUCCESS;Transaction added";
			} else {
				returnstring = "FAIL;Transaction could not be completed";
			}
			
			break;
			
		case "removetransaction":
			if (this.removeTransaction(payload)) {
				returnstring ="SUCCESS;Transaction removed";
			} else {
				returnstring = "FAIL;Transaction could not be removed";
			}
			
			break;
			
		case "changetransaction":
			if (this.changeTransaction(payload)) {
				returnstring = "SUCCESS;Transaction changed";
			} else {
				returnstring = "FAIL;Transaction could not be updated";
			}
			
			break;
			
		case "gettransaction":
			info = this.getTransaction(payload);
			if (info.isEmpty()) {
				returnstring = "FAIL;No transaction found";
			} else {
				returnstring = "SUCCESS;" + info;
			}
			
			break;
			
		case "getalltransactions":
			info = this.getAllTransactions();
			if (info.isEmpty()) {
				returnstring = "FAIL;No transactions found";
			} else {
				returnstring = "SUCCESS;" + info;
			}
			
		case "search":
			result = this.search(payload);
			if (result.isEmpty()) {
				returnstring = "FAIL;No results found";
			} else {
				returnstring = "SUCCESS;" + result;
			}
			
			break;
		
		case "quit":
			this.transmit("SUCCESS;Goodbye");
			return false;
			
		}
	
		return this.transmit(returnstring);

	}

	private boolean authClient(String client) {
		String clients = this.config.getProperty("allowedclients");
		String[] clientList = clients.split(";");
		if (Arrays.asList(clientList).contains(client)) {
			return true;
		} else {
			return false;
		}
	}

	private String addUser(String payload) {
		String[] data = payload.split(";");
		if (data.length != 2) {
			return "Fail";
		}
		
		// This is needed because new users don't have user ids yet.
		User newUser = User.createUser(data[0], data[1]);
		
		return this.storage.store(newUser);
	}

	private boolean removeUser(String payload) {
		return true;
	}

	private String getUserInfo(String payload) {
		return "";
	}

	private String getUserInfo(int payload) {
		return "";
	}

	private boolean changeUser(String payload) {
		return true;
	}

	private String getAllUsers() {
		return "";
	}

	private boolean addItem(String payload) {
		return true;
	}
	
	private boolean removeItem(String payload) {
		return true;
	}
	
	private String getItemInfo(String payload) {
		return "";
	}
	
	private String getAllItems() {
		return "";
	}
	
	private boolean changeItem(String payload) {
		return true;
	}
	
	private boolean addTransaction(String payload) {
		return true;
	}
	
	private boolean removeTransaction(String payload) {
		return true;
	}
	
	private boolean changeTransaction(String payload) {
		return true;
	}
	
	private String getTransaction(String payload) {
		return "";
	}
	
	private String getAllTransactions() {
		return "";
	}
	
	protected String search(String payload) {
		return "";
	}
	
	/**
	 * transmit a message to the client
	 * @param data message to be sent to client. Must be String
	 * @return Returns true if message was sent, false other whise
	 */
	public boolean transmit(String data) {
		this.output.println(data);	
		return true;
	}
}