//Package Declaration //
package ConfigControl;

//Java Package Support //
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//Internal Package Support //
//{ Not Applicable }

/**
 * 
 * Project3/ConfigControl/Swarm.java
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
 *  This code represents the abstraction of a node collection within the 
 *  emulated environment. 
 */

public class Swarm {
	
	// Variables
	private boolean running = true;
	private String config_file;
	private ArrayList<Node> the_swarm = new ArrayList<Node>();
	private final int FIVE = 5;
	
	
	/** 
	 * Constructor for the Swarm class.
	 * 
	 * @param config_file_name      : File name for the initial configuration
	 *                                text.
	 * @throws IOException 		    : IOException is thrown if file does not exist.
	 * @throws FileNotFoundException: If file cannot be found.
	 */
	public Swarm(String config_file_name) throws FileNotFoundException, IOException{
		this.config_file = config_file_name;
		
		while(running == true){
			this.setup();
		
			for(int i = 0; i < the_swarm.size(); i++){
				this.the_swarm.get(i).mutate();
				System.out.println(the_swarm.get(i).node_to_string());
			}
		
			this.tear_down();
			
			this.system_pause();
		} // endwhile
	} // end Swarm()
	
	
	/**
	 * Establishes the node swarm emulating a wireless environment.
	 * Called at the beginning of each run_command().
	 * 
	 * @throws FileNotFoundException: If file cannot be found.
	 */
	public void setup() throws FileNotFoundException{
		try{
			File file_read = new File(this.config_file);
			Scanner scan = new Scanner(file_read);
			
			while(scan.hasNext()){		
				this.the_swarm.add(new Node(scan.nextLine()));
			}// endwhile
			
			scan.close();
		} 
		catch (FileNotFoundException e){
			System.out.println(e.getMessage());
		}		
	} // end setup()
	
	
	/**
	 * Removes all items from the swarm of nodes.
	 * Translates nodes within swarm to String format.
	 */
	private void tear_down() throws IOException{
		try{
			FileWriter file_write = new FileWriter(System.getProperty("user.dir")+"\\src\\ConfigControl\\config.txt");
			
			// Non-iterating for-loops FTW!
			for(int i = 0; i < the_swarm.size();){
				file_write.write(this.the_swarm.remove(i).node_to_line());
			} // endfor
		
			file_write.flush();
			file_write.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	} // end tear_down()
	
	
	/**
	 * Pauses the system before continuing node mutation.
	 */
	private void system_pause(){
		try{
			Thread.sleep(FIVE*1000);
		}
		catch(InterruptedException e){
			System.out.println(e.getMessage());			
		}
	} // end system_pause()
} // end Swarm Class
