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
	AudioFormat format;
	
	// Transmit Variables
	ArrayList<Node> linkedNodes;
	DatagramSocket  s;
	DatagramPacket  dp;
	TargetDataLine  tLine;
	int x, y;
	
	// Control Variables
	boolean running = true;
	byte[] buffer, packet;
	int numBytes;
	
	//Packet Header
	int sequenceNum, srcAddress, destAddress;
	
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
	public SocketSender(int nodeNum, int x, int y, ArrayList<Node> linkedNodes, int destNum) throws IOException, LineUnavailableException{
		this.linkedNodes = linkedNodes;
		
		s       = new DatagramSocket();
		format  = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
												16, 2, 4, 44100, false);
		DataLine.Info tLineInfo = new DataLine.Info(TargetDataLine.class, format);
		tLine   = (TargetDataLine)AudioSystem.getLine(tLineInfo);
		tLine.open(this.format);
		tLine.start();
		packet  = new byte[128];
		buffer  = new byte[120];
		
		sequenceNum      = 1;
		this.srcAddress  = nodeNum;
		this.destAddress = destNum;
		
		this.x = x;
		this.y = y;
		
	} // end SocketSender()
	
	/**
	 * Terminate can be called to terminate execution of the thread. A join should be
	 * called afterward in order to wait for the thread to finish.
	 */
	public void terminate(){
		running = false;
		
		InetAddress nextAddress = null;
		int nextPort;

		sequenceNum = 0;
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
		
		for(int i = 0; i < linkedNodes.size(); i++){
			
			try {
				nextAddress = InetAddress.getByName(linkedNodes.get(i).getAddress());
			}// end try
			
			catch (UnknownHostException e1) {
				// live on the edge
			}// end catch
			
			nextPort = linkedNodes.get(i).getPort();
			dp       = new DatagramPacket(packet, packet.length, nextAddress, nextPort);
			//if (!PacketDropRate.isPacketDropped(x, y, linkedNodes.get(i).getX(), linkedNodes.get(i).getY()))
			//{
				try{
					s.send(dp);
					System.out.println("Sending packet: " + (((dp.getData()[0] + 128) * 256) + dp.getData()[1] + 128) + "	" + (((dp.getData()[2] + 128) * 256) + dp.getData()[3] + 128) + "	" + (((dp.getData()[4] + 128) * 256) + dp.getData()[5] + 128) + "	" + (((dp.getData()[6] + 128) * 256) + dp.getData()[7] + 128));
				}// end try
				
				catch (IOException e){
					// empty sub-block
				}// end catch
			//}
			//else
			//{
				//numPacketsDropped++;
				//System.out.println("Packet Dropped For " + linkedNodes.get(i).getNumber());
			//}
		}// end for
	} // end terminate()
	
	
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
	 * Run command called automatically when the thread is started.
	 */
	@Override
	public void run(){	
		InetAddress nextAddress = null;
		int nextPort;
		//int numPacketsDropped = 0;
		
		while(running){			
			// Add sequence number to the packet
			if(sequenceNum < 65536){
				
				packet[0] = (byte)((sequenceNum/256)-128);
				packet[1] = (byte)((sequenceNum%256)-128);
			} // end if
			
			else{
				
				sequenceNum = 1;
				packet[0]   = (byte)((sequenceNum/256)-128);
				packet[1]   = (byte)((sequenceNum%256)-128);
			} // end else
			sequenceNum++;
			
			// Add source address to the packet
			packet[2] = (byte)((srcAddress/256)-128);
			packet[3] = (byte)((srcAddress%256)-128);
			
			// Add destination address to the packet
			packet[4] = (byte)((destAddress/256)-128);
			packet[5] = (byte)((destAddress%256)-128);
			
			// Add previous hop to the packet
			packet[6] = (byte)((srcAddress/256)-128);
			packet[7] = (byte)((srcAddress%256)-128);
			
			numBytes = tLine.read(buffer, 0, buffer.length);
			System.arraycopy(buffer, 0, packet, 8, buffer.length);
			
			for(int i = 0; i < linkedNodes.size(); i++){
				
				try {
					nextAddress = InetAddress.getByName(linkedNodes.get(i).getAddress());
				}// end try
				
				catch (UnknownHostException e1) {
					// live on the edge
				}// end catch
				
				nextPort = linkedNodes.get(i).getPort();
				dp       = new DatagramPacket(packet, packet.length, nextAddress, nextPort);
				//if (!PacketDropRate.isPacketDropped(x, y, linkedNodes.get(i).getX(), linkedNodes.get(i).getY()))
				//{
					try{
						s.send(dp);
						System.out.println("Sending packet: " + (((dp.getData()[0] + 128) * 256) + dp.getData()[1] + 128) + "	" + (((dp.getData()[2] + 128) * 256) + dp.getData()[3] + 128) + "	" + (((dp.getData()[4] + 128) * 256) + dp.getData()[5] + 128) + "	" + (((dp.getData()[6] + 128) * 256) + dp.getData()[7] + 128));
					}// end try
					
					catch (IOException e){
						// empty sub-block
					}// end catch
				//}
				//else
				//{
					//numPacketsDropped++;
					//System.out.println("Packet Dropped For " + linkedNodes.get(i).getNumber());
				//}
			}// end for
		}// end while
	} // end run()		
} // end SocketSender class
