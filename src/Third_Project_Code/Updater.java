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
	Object lock = new Object();
	boolean running = true;
	String configFileLoc;
	int nodeNum;
	
	/**
	 * Base constructor.
	 * 
	 */
	public Updater(Node node){
		this.node = node;
	} // end Updater()
	
	/**
	 * Ends the runnable.
	 */
	public void terminate(){
		synchronized(lock){
			running = false;
		}
	}

	/**
	 * Run command called automatically when the thread is started.
	 */
	@Override
	public void run(){	
		while(true){
			synchronized(lock){
				if(!running){
					break;
				}
			}
				try {
					if(node.checkForUpdate()==true){
						boolean wasSending = false;
						if(node.isSending()){
							wasSending = true;
							node.stopSending();
							System.out.println("stopped sending");
						}
						node.stopReceiving();
						System.out.println("stopped receiving");
						node.setup(node.getConfigFileLoc(), node.getNumber());
						System.out.println("setup done");
						node.startReceiving();
						if(wasSending){
							System.out.println("stopped sending");		node.startSending(node.getSendDest());
						}
						System.out.println("Updated!");
					}
				} catch (IOException e) {
					// Live on the edge.
				} catch (InterruptedException e){

				} catch (LineUnavailableException e){

				}
			}
			System.out.println("Checked");
			for(int i = 0; i < 2; i++){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// Live on the edge.
				}
				
				synchronized(lock){
					if(!running){
						break;
					}
				}
			}
	} // end run()		
} // end Updater class
