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
	 * Constructor
	 * 
	 * Constructor that starts a server listening for connecting clients
	 */
	public ServerEngine(ConfigReader config) {
		ServerSocket servSocket = null;
		Socket cliSocket = null;
		
		this.config = config;
		
		int port = 55228;
		int timeout = 0;
		
		try {
			port = Integer.parseInt(this.config.getProperty("port"));
		} catch (NumberFormatException e) {
			System.out.println("Port number given in config is not a number. Using default port 55228");
			port = 55228;
		}
		
		try {
			timeout = Integer.parseInt(this.config.getProperty("serverTimeout"));
		} catch (NumberFormatException e) {
			System.out.println("Timeout must be a positive integer or 0. Setting infinite timeout (0)");
			timeout = 0;
		}
		
		//Se if timout is a positive integer or 0 for inginite timeout
		if (timeout < 0) {
			System.out.println("Timeout must be _positive_ and in milliseconds, or 0 for infinite. Setting infinite timeout");
			timeout = 0;
		}
		
		try {
			servSocket = new ServerSocket(port);
			
		} catch (IOException e) {
			System.out.println("Could not connect on port: " + port + " quitting.");
			System.exit(1);
		}
		
		try {
			while(true) {
				cliSocket = servSocket.accept();
				cliSocket.setSoTimeout(timeout);
				
				ClientProcesser handler = new ClientProcesser(this.config, cliSocket);
				
				Thread tmpThread = new Thread(handler);
				tmpThread.start();
			
			}
		} catch (IOException e) {
			System.out.println("Could not establish connection with incomming client. Ignoring...");
		}
	}
	
	
}
