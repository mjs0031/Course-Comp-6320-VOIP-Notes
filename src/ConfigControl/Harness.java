//Package Declaration //
package ConfigControl;

//Java Package Support //
import java.io.IOException;

//Internal Package Support //
//{ Not Applicable }

/**
 * 
 * Project3/ConfigControl/Node.java
 * 
 * @author(s)	: Ian Middleton, Zach Ogle, Matthew J Swann
 * @version  	: 1.0
 * Last Update	: 2013-04-24
 * Update By	: Matthew J Swann
 * 
 * 
 * Third_Project_Code PACKAGE :: Source code for Comp 6360: Wireless & Mobile Networks
 * 	               Assignment 3 :: VOIP
 * 
 *  This code represents the running control for the Swarm and Node
 *  Classes.
 */

public class Harness {

	/**
	 * Runs the Swarm and Node classes.
	 * 
	 * @param args     : Command line arguments.
	 */
	public static void main(String[] args) throws IOException {
		try {
			Swarm s = new Swarm(args[0]);
		}
		catch (ArrayIndexOutOfBoundsException e){
			Swarm s = new Swarm(System.getProperty("user.dir")+"\\src\\ConfigControl\\config.txt");
		}

	} // end main()

} // end Harness Class
