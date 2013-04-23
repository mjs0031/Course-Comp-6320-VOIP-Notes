//Package Declaration //
package Third_Project_Code;

//Java Package Support //
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.LineUnavailableException;

//Internal Package Support //
// { Not Applicable }

/**
 * 
 * Second_Project_Code/VOIP_Gui.java
 * 
 * @author(s)	: Ian Middleton, Zach Ogle, Matthew J Swann
 * @version  	: 1.0
 * Last Update	: 2013-03-28
 * Update By	: Ian R Middleton
 * 
 * Second_Project_Code PACKAGE :: Source code for Comp 6360: Wireless & Mobile Networks
 * 	               Assignment 2 :: VOIP
 * 
 * Source code for Comp 6360: Wireless & Mobile Networks. This code
 * encompasses the graphical user interface and runs the transmission
 * sender and receiver.
 * 
 */

public class VOIP_Gui extends JFrame{
	
	// Static Variables
	private static final long serialVersionUID = 1L;

	// GUI Variables
	private JButton button_one, button_two;
	private JLabel label_one, label_two, label_three, label_four, label_five;
	private JPanel panel_one, primary;
	private JTextField text_field_home_node, text_field_dest_node, text_field_config_file;
	
	private TransmitListener TransmitListener;
	private StartListener Starter;
	
	// Node variables
	private Node node = new Node();
	
	Object lock = new Object();
	
	Updater updater;
	Thread updaterThread;
	
	
	/**
     * Constructor for the GUI class.
     * 
	 * @param  args           : Not currently implemented
	 * @throws IOException    : General IOException for package functions.
	 * @throws LineUnavailable: General LineUnavailable for package
	 * 								functions. 
     */
	public VOIP_Gui() throws IOException, LineUnavailableException{
		
		// JButtons
		this.button_one = new JButton("Start");
		Starter 		= new StartListener();
		this.button_one.addActionListener(Starter);
		
		this.button_two = new JButton("Begin Sending");
		this.button_two.setFont(new Font("Helvetica", Font.BOLD, 12));
		this.button_two.setPreferredSize(new Dimension(100,50));
		TransmitListener = new TransmitListener();
		this.button_two.addActionListener(TransmitListener);
		
		// JLabels
		this.label_one   = new JLabel("Node ::");
		this.label_two   = new JLabel("Config File ::");
		this.label_three = new JLabel("Not Running");
		this.label_four  = new JLabel("Dest Node ::");
		this.label_five  = new JLabel("Not Sending");
		this.label_three.setForeground(Color.red);
		this.label_five.setForeground(Color.red);
		
		// JPanels
		this.panel_one = new JPanel();
		this.primary   = new JPanel();
				
		// JTextField
		this.text_field_home_node   = new JTextField(15);
		this.text_field_dest_node   = new JTextField(15);
		this.text_field_config_file = new JTextField(15);
		
		// Control Block
		this.compile_components();
	
	} // end VOIP_Gui()
	
	
    /**
     * Places components on the appropriate panels.
     */	
	public void compile_components(){
		
		this.getContentPane().add(primary);
		
		// Panel One additions
		this.panel_one.setLayout(new GridLayout(5, 2));
		this.panel_one.add(this.label_one);
		this.panel_one.add(this.text_field_home_node);
		this.panel_one.add(this.label_two);
		this.panel_one.add(this.text_field_config_file);
		this.panel_one.add(this.label_three);
		this.panel_one.add(this.button_one);
		this.panel_one.add(this.label_four);
		this.panel_one.add(this.text_field_dest_node);
		this.panel_one.add(this.label_five);
		this.panel_one.add(this.button_two);

		// Primary Panel additions
		this.primary.setPreferredSize(new Dimension(4*100,3*100+10));
		this.primary.add(this.panel_one);
	} // end VOIP_Gui.compile_components()

	
	/**
	 * Action listener for the start button.
	 * Attached to the start button.
	 */
	public class StartListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if(button_one.getText() == "Start"){
				try {
						node.setup(text_field_config_file.getText(), Integer.parseInt(text_field_home_node.getText()));
						node.startReceiving();
						
						updater = new Updater(node, lock);
						updaterThread = new Thread(updater);
						updaterThread.start();
						
						label_three.setText("Running");
						label_three.setForeground(Color.green);
						button_one.setText("Stop");
				} 
				
				catch (IOException e) {
					label_three.setText("File DNE");
				} 
				
				catch (NumberFormatException e){
					label_three.setText("Need Node Number");
				} 
				
				catch (LineUnavailableException e) {
					// Live on the edge.
				}
			}
			else{
				try {
					synchronized(lock){
						node.stopReceiving();
						updater.terminate();
						updaterThread.join();
					}
				} catch (InterruptedException e) {
					// Live on the edge.
				}
				label_three.setText("Not Running");
				label_three.setForeground(Color.red);
				button_one.setText("Start");
			}
		}// end StartListener.actionPerformed()
	}// end StartListener
	
	
	/**
	 * Action listener for the sending button.
	 * Attached to the send button.
	 */
	private class TransmitListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			if(button_two.getText() == "Begin Sending"){
				try {
					synchronized(lock){
						node.startSending(Integer.parseInt(text_field_dest_node.getText()));
						label_five.setText("Sending");
						label_five.setForeground(Color.green);
						button_two.setText("Stop Sending");
					}
				} 
				
				catch (NumberFormatException e) {
					// Live on the edge.
				} 
				
				catch (IOException e) {
					// Live on the edge.
				} 
				
				catch (LineUnavailableException e) {
					// Live on the edge.
				}
			}
			else{
				try {
					synchronized(lock){
						node.stopSending();
						label_five.setText("Not Sending");
						label_five.setForeground(Color.red);
						button_two.setText("Begin Sending");
					}
				} 
				
				catch (InterruptedException e) {
					// Live on the edge.
				}
			}
		} // end TransmitListener.actionPerformed()
	}// end TransmitListener()		
	
	
		/**
		 * Runs the Gui. Constructs the complementary variables, etc.
		 * Runs until the KillSwitch is engaged.
		 * 
		 * @param  args        : Not currently implemented
		 * @throws IOException : General IOException for package functions.
		 * 
		 */
	    public static void main(String[] args) throws IOException,
	    							IOException, LineUnavailableException{
			VOIP_Gui teh_gui = new VOIP_Gui();
			
			teh_gui.setTitle("VOIP to end all VOIP's");
			teh_gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			teh_gui.pack();
			teh_gui.setVisible(true);
		} // end main()	
	    
	} // end VOIP_Gui class
