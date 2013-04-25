//Package Declaration //
package Third_Project_Code;

//Java Package Support //
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
//import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;

//Internal Package Support //
// { Not Applicable }


/**
* 
* Project2/SocketReceiver.java
* 
* @author(s)	: Ian Middleton, Zach Ogle, Matthew J Swann
* @version  	: 1.0
* Last Update	: 2013-03-26
* Update By		: Ian Middleton
* 
* 
* Second_Project_Code PACKAGE :: Source code for Comp 6360: Wireless & Mobile Networks
* 	               Assignment 2 :: VOIP
* 
* This is source code for the SocketReceiver class. This class accepts a
* set of packets, unpacks the BYTES and plays back the sound.
* 
*/


public class SocketReceiver implements Runnable{
	
	// Audio Variables
	AudioFormat format;
	
	// Transmit Variables
	DatagramPacket dp;
	DatagramSocket s;
	SourceDataLine sLine;
	SocketSender sender;
	
	// Control Variables
	boolean running = true;
	byte[] buf;
	int number, port, x, y;
	String address;
	ArrayList<Node> nodes;
	ArrayList<int[]> cache = new ArrayList<int[]>();
	byte[] playbuf = new byte[120];
	
	/**
	 * Base constructor.
	 * 
	 * @throws IOException			: General IOException for package functions.
	 * @throws LineUnavailable		: General LineUnavailable for package 
	 * 										functions.
	 */
	public SocketReceiver(int nodeNum, String address, int port, ArrayList<Node> linkedNodes, int x, int y){
		this.buf    = new byte[128];
		
		try {
			this.s      = new DatagramSocket(port);
		} catch (SocketException e1) {
			System.out.println("SocketReciever: Socket Creation Problem");
		}
		
		this.dp     = new DatagramPacket(buf, buf.length);
		this.format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
												16, 2, 4, 44100, false);		
		DataLine.Info sLineInfo = new DataLine.Info(SourceDataLine.class, this.format);
		
		try {
			this.sLine   = (SourceDataLine)AudioSystem.getLine(sLineInfo);
		} catch (LineUnavailableException e) {
			System.out.println("SocketReceiver: Missing Data Line (SocketReceiver)");
		}
		
		this.nodes   = linkedNodes;
		this.number  = nodeNum;
		this.address = address;
		this.port    = port;
		this.x = x;
		this.y = y;
		
		sender = new SocketSender();
		
	} // end SocketReceiver()
	
	public Node getNode(int nodeNum){
		for (int count = 0; count < nodes.size(); count++)
		{
			if (nodes.get(count).getNumber() == nodeNum)
			{
				return nodes.get(count);
			}
		}
		return null;
	}
	
	public boolean checkTable(int sequence, int source, int destination){
		
		for (int count = 0; count < cache.size(); count++){
			if (cache.get(count)[0] == source && cache.get(count)[1] == destination){
				
				if (cache.get(count)[2] < sequence){
					cache.get(count)[2] = sequence;
					return false;
				}
				else if(sequence == 0){
					cache.get(count)[2] = 0;
					return true;
				}else{
					return true;
				}
			}
		}
		
		cache.add(new int[3]);
		cache.get(cache.size()-1)[0] = source;
		cache.get(cache.size()-1)[1] = destination;
		cache.get(cache.size()-1)[2] = sequence;
				
		return false;
	}
	
	public void playPacket(){
		System.arraycopy(this.dp.getData(), 8, playbuf, 0, playbuf.length);
		System.out.println("Playing packet: " + (((this.dp.getData()[0] + 128) * 256) + this.dp.getData()[1] + 128) + "	" + (((this.dp.getData()[2] + 128) * 256) + this.dp.getData()[3] + 128) + "	" + (((this.dp.getData()[4] + 128) * 256) + this.dp.getData()[5] + 128) + "	" + (((this.dp.getData()[6] + 128) * 256) + this.dp.getData()[7] + 128));
		this.sLine.write(playbuf, 0, playbuf.length);
	}
	
	public void forwardPacket(int prevHop){
		byte[] buffer = dp.getData();
		buffer[6] = (byte)((number / 256) - 128);
		buffer[7] = (byte)((number % 256) - 128);
	
		for (int count = 0; count < nodes.size(); count++){
			
			if (nodes.get(count).getNumber() != prevHop){
				sender.forward(nodes.get(count).getAddress(), nodes.get(count).getPort(), buffer);
			}
		}
	}
	
	/**
	 * Terminate can be called to terminate execution of the thread. A join should be
	 * called afterward in order to wait for the thread to finish.
	 */
	public void terminate(){
		
		byte[] buffer = new byte[8];
		
		for(int i = 0; i < 8; i++){
			buffer[i] = -128;
		}
		
		sender.forward(address, port, buffer);
		
	} // end terminate()
	
	/**
	 * Run command called automatically by Thread.start().
	 * 
	 * @throws IOException			: General IOException for package functions.
	 * @throws LineUnavailable		: General LineUnavailable for package 
	 * 										functions.
	 */
	@Override
	public void run(){
	
		boolean isTrash = false;
		
		try {
			this.sLine.open(this.format);
		}
		catch (LineUnavailableException e){
			System.out.println("SocketReceiver: Missing Data Line (run)");
		}
		
		this.sLine.start();
		
		// Continues until program is closed.
		while(running){
			
			try{
				this.s.receive(this.dp);
			}
			catch (IOException e){
				System.out.println("SocketReceiver: Bad packet reception");	
			}
			
			int sequence    = ((dp.getData()[0] + 128) * 256) + dp.getData()[1] + 128;
			int source      = ((dp.getData()[2] + 128) * 256) + dp.getData()[3] + 128;
			int destination = ((dp.getData()[4] + 128) * 256) + dp.getData()[5] + 128;
			int prevHop     = ((dp.getData()[6] + 128) * 256) + dp.getData()[7] + 128;
			
			Node prevNode = getNode(prevHop);
			if(prevNode == null){
				System.out.println("Invalid Previous Hop");
			}
			
			if (sequence == 0){
				if(source == 0){
					running = false;
				}
			}
			
			if (!PacketDropRate.isPacketDropped(x, y, prevNode.getX(), prevNode.getY()) && running)
			{
				
				isTrash = checkTable(sequence, source, destination);
			
				if (destination == number & !isTrash){
					playPacket();
				}
				else if (source != number && !isTrash){
					forwardPacket(prevHop);
				}
			}
			else
			{
				System.out.println("Packet Dropped For " + prevNode.getNumber());
			}
			isTrash = false;
		} // end while
		
		s.close();
	} // end SocketReceiver.run()

	
} // end SocketReceiver class
