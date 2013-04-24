//Package Declaration //
package ConfigControl;

//Java Package Support //
import java.util.Random;

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
 *  This code represents the abstraction of a node within the emulated environment. 
 */

public class Node {
	
	// Variables
	private String node_address;
	private String[] input_array = new String[0];
	private int[] link_array;
	private int node_id, port_id, x_coord, y_coord;
	private final int DATA_DESIGNATIONS = 7, SEVEN = 7;
	private final float BOT = (float)0.25, MID = (float)0.50, TOP = (float)0.75;

	
	/*
	 * Constructor for the Node class.
	 * 
	 * @param input_line: Raw string input from configuration file.
	 */
	public Node(String input_line){
		try{
			input_array = input_line.split("\\s+");
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		// Skips 'NODE' designation on '0' index.
		
		// Sets node id based on '1' index.
		this.node_id = Integer.parseInt(input_array[1]);
		
		// Sets node address based on '2' index. Cleans ',' characters.
		if (input_array[2].charAt(input_array[2].length()-1) == ','){
			this.node_address = input_array[2].substring(0, (input_array[2].length()-1));
		} else {
			this.node_address = input_array[2];
		}
		
		// Sets port id based on '3' index.
		this.port_id = Integer.parseInt(input_array[3]);
		
		// Sets 'x' coord based on '4' index.
		this.x_coord = Integer.parseInt(input_array[4]);
		
		// Sets 'y' coord based on '5' index.
		this.y_coord = Integer.parseInt(input_array[5]);
		
		// Skips 'LINKS; designation on '6' index.
		
		// Established link array based on remaining index values.
		this.link_array = new int[input_array.length - DATA_DESIGNATIONS];
		for(int i = DATA_DESIGNATIONS; i < input_array.length; i++){
			this.link_array[i-DATA_DESIGNATIONS] = Integer.parseInt(input_array[i]);
		}// endfor	
		
	}// end Node()
	
	
	/* Mutates the node data based on random number generation. This code
	 * emulates a person moving in a simulated wireless network
	 */
	public void mutate(){
		Random rand = new Random();
		
		float direction = rand.nextFloat();
		float movement  = SEVEN*rand.nextFloat();
		
		// Directional movement based of randomization.
		if(direction < BOT){
			this.x_coord -= Math.round(movement);
		}
		else if (direction < MID){
			this.y_coord -= Math.round(movement);
		}
		else if (direction < TOP){
			this.x_coord += Math.round(movement);
		} 
		else {
			this.y_coord += Math.round(movement);
		}	
	} // end mutate()
	
	
	/**
	 * Returns node info report for tracking/testing purposes.
	 * 
	 * @return Node information report as a String.
	 */
	public String node_to_string(){
		String output = "--------------------------\n"+
					    "ID  : "+this.get_node_id()+"\n"+
					    "Addr: "+this.get_node_address()+"\n"+
					    "Port: "+this.get_port_id()+"\n"+
					    "X-Co: "+this.get_x_coord()+"\n"+
					    "Y-Co: "+this.get_y_coord()+"\n";
		for(int i = 0; i < this.link_array.length; i++){
			output += "    link #"+(i+1)+": "+link_array[i]+"\n";
		}
		return output;
	} // end node_to_string()
	
	
	/**
	 * Converts a node to a String line such that it can be returned to the
	 * configuration file.
	 * 
	 * @return output	: String representation of a line.
	 */
	public String node_to_line(){
		String output = "Node "+this.node_id+" "+this.node_address+", "+
						this.port_id+" "+this.x_coord+" "+this.y_coord+" "+
						"links";
		for(int i=0; i<this.link_array.length; i++){
			output += " "+this.link_array[i];
		}
		output += "\n";
		return output;
	}
	
	
	/** 
	 * Returns the node's identification number.
	 * 
	 * @return Unique ID number for the node.
	 */
	public int get_node_id(){
		return this.node_id;
	}
	
	
	/** 
	 * Sets the node's identification number.
	 * 
	 * @param new_id: The new ID number for the node.
	 */
	public void set_node_id(int new_id){
		this.node_id = new_id;
	}
	
	
	/** 
	 * Returns the node's port number.
	 * 
	 * @return Unique port number for the node.
	 */
	public int get_port_id(){
		return this.port_id;
	}
	
	
	/** 
	 * Sets the node's port number.
	 * 
	 * @param new_id: The new port number for the node.
	 */
	public void set_port_id(int new_id){
		this.port_id = new_id;
	}
	
	
	/** 
	 * Returns the node's current link array.
	 * 
	 * @return The current link array for the node.
	 */
	public int[] get_link_array(){
		return this.link_array;
	}
	
	
	/** 
	 * Sets the current node's link array.
	 * 
	 * @param new_links: New set of links for the node.
	 */
	public void set_link_array(int[] new_links){
		this.link_array = new_links;
	}
	
	
	/** 
	 * Returns the node's 'x' coordinate.
	 * 
	 * @return 'X' coord for the node.
	 */
	public int get_x_coord(){
		return this.x_coord;
	}
	
	
	/** 
	 * Sets the node's 'x' coord to new value.
	 * 
	 * @param new_coord: New 'x' coord.
	 */
	public void set_x_coord(int new_coord){
		this.x_coord = new_coord;
	}
	
	
	/** 
	 * Returns the node's 'y' coordinate.
	 * 
	 * @return 'Y' coord for the node.
	 */
	public int get_y_coord(){
		return this.y_coord;
	}
	

	/** 
	 * Sets the node's 'y' coord to new value.
	 * 
	 * @param new_coord: New 'y' coord.
	 */
	public void set_y_coord(int new_coord){
		this.y_coord = new_coord;
	}
	
	
	/** 
	 * Returns the node's known address.
	 * 
	 * @return The node's currently known address.
	 */
	public String get_node_address(){
		return this.node_address;
	}
	
	
	/** 
	 * Sets the node's address to a new value.
	 * 
	 * @param new_address: New address for the node.
	 */
	public void set_node_address(String new_address){
		this.node_address = new_address;
	}

} // end Node Class
