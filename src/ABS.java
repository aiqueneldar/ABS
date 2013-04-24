import utils.*;
import server.networking.ServerEngine;

/**
 * @author Aiquen - <a href='mailto:aiqueneldar@gmail.com'>aiqueneldar@gmail.com</a>
 *
 */
public class ABS {

	/**
	 * @param args arguments. None used at the moment
	 */
	public static void main(String[] args) {
		System.out.print("Creating a config object...");
		ConfigReader config = new ConfigReader();
		System.out.println("Done");
		System.out.print("Creating server object...");
		ServerEngine server = new ServerEngine(config);
		System.out.println("Done");
		System.out.println("Starting server...");
		server.start();
	}

}
