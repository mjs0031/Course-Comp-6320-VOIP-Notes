//Package Declaration //
package Third_Project_Code;

//Java Package Support //
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

//Internal Package Support //
// { Not Applicable }

/**
* 
* Second_Project_Code/Updater.java
* 
* @author(s)	: Ian Middleton, Zach Ogle, Matthew J Swann
* @version  	: 2.0
* Last Update	: 2013-03-28
* Update By	: Ian R Middleton
* 
* 
* Second_Project_Code PACKAGE :: Source code for Comp 6360: Wireless & Mobile Networks
* 	               Assignment 2 :: VOIP
* 
* This is source code for the Updater class.
* 
*/


public class Updater implements Runnable{
	Node node;
	Object lock;
	boolean running = true;
	String configFileLoc;
	int nodeNum;
	
	/**
	 * Base constructor.
	 * 
	 */
	public Updater(Node node, Object lock){
		this.node = node;
		this.lock = lock;
	} // end Updater()
	
	/**
	 * Ends the runnable.
	 */
	public void terminate(){
		running = false;
	}

	/**
	 * Run command called automatically when the thread is started.
	 */
	@Override
	public void run(){	
		while(running){
			synchronized(lock){
				try {
					if(node.checkForUpdate()==true){
						boolean wasSending = false;
						if(node.isSending()){
							wasSending = true;
							node.stopSending();
						}
						node.stopReceiving();
						node.setup(node.getConfigFileLoc(), node.getNumber());
						System.out.println("setup done");
						node.startReceiving();
						if(wasSending){
							node.startSending(node.getSendDest());
						}
						System.out.println("Updated!");
					}
				} catch (IOException e) {
					// Live on the edge.
				} catch (InterruptedException e){

				} catch (LineUnavailableException e){

				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Live on the edge.
			}
		}
	} // end run()		
} // end Updater class
