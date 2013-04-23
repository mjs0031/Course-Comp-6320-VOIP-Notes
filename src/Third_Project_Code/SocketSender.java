//Package Declaration //
package Third_Project_Code;

//Java Package Support //
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

//Internal Package Support //
// { Not Applicable }

/**
* 
* Project2/SocketSender.java
* 
* @author(s)	: Ian Middleton, Zach Ogle, Matthew J Swann
* @version  	: 2.0
* Last Update	: 2013-03-26
* Update By		: Ian R Middleton
* 
* 
* Second_Project_Code PACKAGE :: Source code for Comp 6360: Wireless & Mobile Networks
* 	               Assignment 2 :: VOIP
* 
* This is source code for the SocketSender class. This class records
* 	sound input, packs the sound in BYTE packets, and forwards the data
* 	to the appropriate IP Address(es) based on the Nodes linked to the 
* 	Node calling this thread.
* 
*/


public class SocketSender implements Runnable{
	
	// Audio Variables
	private AudioFormat format;
	
	// Transmit Variables
	private ArrayList<Node> linkedNodes;
	private DatagramSocket  s;
	private DatagramPacket  dp;
	private TargetDataLine  tLine;
	private InetAddress nextAddress;
	private int nextPort;
	
	// Control Variables
	private boolean running = true;
	private Object lock = new Object();
	
	//Packet Header
	private int sequenceNum, srcAddress, destAddress;
	
	public SocketSender() throws SocketException{
		s       = new DatagramSocket();
	}
	
	/**
	 * Base constructor.
	 * 
	 * @param nodes					: ArrayList of Node objects that are linked to the sending Node.
	 * @param srcAddress			: Number designating the source Node of this message.
	 * @param destAddress			: Number designating the destination Node of this message.
	 * @throws IOException			: General IOException for package functions.
	 * @throws LineUnavailable		: General LineUnavailable for package 
	 * 										functions.
	 */
	public SocketSender(int nodeNum, ArrayList<Node> linkedNodes, int destNum) throws IOException, LineUnavailableException{
		this.linkedNodes = linkedNodes;
		
		s       = new DatagramSocket();
		
		format  = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
												16, 2, 4, 44100, false);
		DataLine.Info tLineInfo = new DataLine.Info(TargetDataLine.class, format);
		tLine   = (TargetDataLine)AudioSystem.getLine(tLineInfo);
		tLine.open(this.format);
		tLine.start();
		
		sequenceNum      = 1;
		this.srcAddress  = nodeNum;
		this.destAddress = destNum;		
	} // end SocketSender()
	
	
	private byte[] createHeader(byte[] packet){
		packet[0]   = (byte)((sequenceNum/256)-128);
		packet[1]   = (byte)((sequenceNum%256)-128);
		
		// Add source address to the packet
		packet[2] = (byte)((srcAddress/256)-128);
		packet[3] = (byte)((srcAddress%256)-128);
		
		// Add destination address to the packet
		packet[4] = (byte)((destAddress/256)-128);
		packet[5] = (byte)((destAddress%256)-128);
		
		// Add previous hop to the packet
		packet[6] = (byte)((srcAddress/256)-128);
		packet[7] = (byte)((srcAddress%256)-128);
		
		return packet;
	}
	
	private void sendPacket(Node nextNode, byte[] packet){
		// Get the IP address.
		try {
			nextAddress = InetAddress.getByName(nextNode.getAddress());
		}// end try
		catch (UnknownHostException e1) {
			nextAddress = null;
		}// end catch
		
		// Get the Port Number.
		nextPort = nextNode.getPort();
		
		// Create a Datagram Packet with IP address and Port Number.
		dp       = new DatagramPacket(packet, packet.length, nextAddress, nextPort);
		
		// Send Datagram Packet from the Datagram Socket.
		try{
			s.send(dp);
			System.out.println("Sending packet: " + (((dp.getData()[0] + 128) * 256) + dp.getData()[1] + 128) + "	" + (((dp.getData()[2] + 128) * 256) + dp.getData()[3] + 128) + "	" + (((dp.getData()[4] + 128) * 256) + dp.getData()[5] + 128) + "	" + (((dp.getData()[6] + 128) * 256) + dp.getData()[7] + 128));
		}// end try
		catch (IOException e){
			// empty sub-block
		}// end catch
	}
	
	/**
	 * Static method to be used for forwarding packets.
	 * 
	 * @param address		: String of the address to be sent to.
	 * @param port			: Integer of the port number to be sent to.
	 * @param packet		: The packet to be sent.
	 * @throws IOException	: General IOException.
	 */
	public void forward(String address, int port, byte[] packet) throws IOException{
		InetAddress fwdAddress = InetAddress.getByName(address);
		dp      = new DatagramPacket(packet, packet.length, fwdAddress, port);
		s.send(dp);
		System.out.println("Forwarding packet: " + (((dp.getData()[0] + 128) * 256) + dp.getData()[1] + 128) + "	" + (((dp.getData()[2] + 128) * 256) + dp.getData()[3] + 128) + "	" + (((dp.getData()[4] + 128) * 256) + dp.getData()[5] + 128) + "	" + (((dp.getData()[6] + 128) * 256) + dp.getData()[7] + 128));
	} // end forward()
	
	/**
	 * Terminate can be called to terminate execution of the thread. A join should be
	 * called afterward in order to wait for the thread to finish.
	 */
	public void terminate(){
		synchronized(lock){
			
			running = false;
	
			byte[] packet = new byte[128];
			
			sequenceNum = 0;
			packet = createHeader(packet);
			
			for(int i = 0; i < linkedNodes.size(); i++){
				sendPacket(linkedNodes.get(i), packet);
			}// end for
		}
	} // end terminate()	
	
	/**
	 * Run command called automatically when the thread is started.
	 */
	@Override
	public void run(){	
		byte[] packet = new byte[128];
		byte[] buffer = new byte[120];
		
		while(running){			
			synchronized(lock){
				if(sequenceNum != 0){
					// Check for sequence number overflow.
					if(!(sequenceNum < 65536)){
						sequenceNum = 1;
					} // end if
					
					// Create packet header and increment sequence number.
					packet = createHeader(packet);
					sequenceNum++;
					
					// Read sound data off the line and copy it into the packet after the header.
					tLine.read(buffer, 0, buffer.length);
					System.arraycopy(buffer, 0, packet, 8, buffer.length);
					
					// Send packet to all connected nodes.
					for(int i = 0; i < linkedNodes.size(); i++){
						sendPacket(linkedNodes.get(i), packet);
					}// end for
				}// end if
			}// end synchronized
		}// end while
	} // end run()		
} // end SocketSender class
