// Package Declaration //
package Third_Project_Code;

// Java Package Support //
import java.util.Random;

// Internal Package Support //
// { Not Applicable }

/**
 * 
 * Second_Project_Code/PacketDropRate.java
 * 
 * @author(s)	: Ian Middleton, Zach Ogle, Matthew J Swann
 * @version  	: 1.0
 * Last Update	: 2013-03-28
 * Update By	: Ian R Middleton
 * 
 * 
 * Second_Project_Code PACKAGE :: Source code for Comp 6360: Wireless & Mobile Networks
 * 	               Assignment 2 :: VOIP
 * 
 * Source code for Comp 6360: Wireless & Mobile Networks. This code
 * encompasses the packet drop rate checker which exists to check
 * whether a packet should be dropped due to distance.
 *  
 */

public class PacketDropRate
{
	
	/**
	 * Checks whether a packet should be dropped due to the distance between the two
	 * sets of x and y coordinates.
	 * 
	 * @param x1	: The x coordinate of the first node.
	 * @param y1	: The y coordinate of the first node.
	 * @param x2	: The x coordinate of the second node.
	 * @param y2	: The y coordinate of the second node.
	 * @return		: True if the packet should be dropped, false if it should not be.
	 */
	public static boolean isPacketDropped(int x1, int y1, int x2, int y2)
	{
		double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		int probability = new Random().nextInt(100);
		if (distance <= 80)
		{
			return false;
		}
		else if (distance <= 90)
		{
			if (probability < 20)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (distance <= 100)
		{
			if (probability < 40)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (distance <= 110)
		{
			if (probability < 60)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (distance < 120)
		{
			if (probability < 80)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
}
