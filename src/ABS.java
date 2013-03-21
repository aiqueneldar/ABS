import utils.ConfigReader;
/**
 * @author Aiquen - <a href='mailto:aiqueneldar@gmail.com'>aiqueneldar@gmail.com</a>
 *
 */
public class ABS {

	/**
	 * @param args arguments. None used at the moment
	 */
	public static void main(String[] args) {
		ConfigReader config = new ConfigReader();
		System.out.println("Created a ConfigReader object");
		System.out.print("Trying to print test property: ");
		System.out.println(config.getProperty("test"));
		System.out.println("Done");
		System.out.print("Changing 'test' property...");
		config.setProperty("test", "other");
		System.out.println("done");
		System.out.println("New value: " + config.getProperty("test"));
		System.out.println("Done");
		System.out.print("Saving new value to file...");
		config.saveProperties();
		System.out.println("done");
		System.out.println("Done");
	}

}
