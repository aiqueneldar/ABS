package server.networking;

import java.net.*;
import java.io.*;
import utils.ConfigReader;

/**
 * Handles connectiosn
 * 
 * Handles all connections, both as a multithreaded server and handles every opened connection to a client
 * 
 * @author cale02
 *
 */
public class ServerEngine {
	
	/**
	 * Amount of simultanious connections allowed from all hosts. Specefied in the config file
	 */
	public static int maxConnections;
	
	/**
	 * Amount of current simultanious connections from one host
	 */
	public static int currConnections;
	
	/**
	 * Holds the read config
	 */
	private ConfigReader config;
	
	/**
	 * port used for this server
	 */
	private int port;
	
	/**
	 * timeout of a connecting client in seconds
	 */
	private int timeout;
	
	/**
	 * Constructor
	 * 
	 * Constructor that starts a server listening for connecting clients
	 */
	public ServerEngine(ConfigReader config) {
		this.config = config;
		
		this.port = 55228;
		this.timeout = 0;
		
		try {
			this.port = Integer.parseInt(this.config.getProperty("port"));
		} catch (NumberFormatException e) {
			System.out.println("Port number given in config is not a number. Using default port 55228");
			this.port = 55228;
		}
		
		try {
			this.timeout = Integer.parseInt(this.config.getProperty("servertimeout"));
		} catch (NumberFormatException e) {
			System.out.println("Timeout must be a positive integer or 0. Setting infinite timeout (0)");
			this.timeout = 0;
		}
		
		//Se if timout is a positive integer or 0 for inginite timeout
		if (this.timeout < 0) {
			System.out.println("Timeout must be _positive_ and in milliseconds, or 0 for infinite. Setting infinite timeout");
			this.timeout = 0;
		}
		
		//Set max connections
		try {
			ServerEngine.maxConnections = Integer.parseInt(this.config.getProperty("maxclients"));
		} catch (NumberFormatException e) {
			System.out.println("Max clients must be a positive integer or 0. Setting infinite amount of clients (0)");
			ServerEngine.maxConnections = 0;
		}
	}
	
	/**
	 * Sartup method
	 * 
	 * This method starts upp the server after it has been initialized by the {@link Constructor}
	 */
	public void start() {
		ServerSocket servSocket = null;
		Socket cliSocket = null;
		
		try {
			servSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			System.out.println("Could not connect on port: " + this.port + " quitting.");
			System.exit(1);
		}
		
		try {
			while(true) {
				cliSocket = servSocket.accept();
				cliSocket.setSoTimeout(this.timeout);
				
				System.out.println("New client Connected");
				
				new PrintWriter(cliSocket.getOutputStream(), true).println("Welcome to the server: " + this.config.getProperty("servername"));
				
				ClientProcesser handler = new ClientProcesser(this.config, cliSocket);
				
				Thread tmpThread = new Thread(handler);
				tmpThread.start();
			
			}
		} catch (IOException e) {
			System.out.println("Could not establish connection with incomming client. Ignoring...");
		}
	}
	
	
}
