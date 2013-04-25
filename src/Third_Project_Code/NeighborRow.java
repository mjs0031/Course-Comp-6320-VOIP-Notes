package Third_Project_Code;

import java.util.ArrayList;

public class NeighborRow
{
	private int nodeNumber, linkStatus, numHops, holdingTime, sequenceNumber;
	private boolean isMPRSelector;
	private ArrayList<Integer> twoHopNeighbors = new ArrayList<Integer>();
	
	public NeighborRow(int nodeNum, int status, int numHops, ArrayList<Integer> twoHopNeighbors, int holdTime, int sequenceNum, boolean isMPR)
	{
		nodeNumber = nodeNum;
		linkStatus = status;
		this.numHops = numHops;
		for (int i = 0; i < twoHopNeighbors.size(); i++)
		{
			this.twoHopNeighbors.add(twoHopNeighbors.get(i));
		}
		holdingTime = holdTime;
		sequenceNumber = sequenceNum;
		isMPRSelector = isMPR;
	}
	
	public int getNodeNumber()
	{
		return nodeNumber;
	}
	
	public void setNodeNumber(int newNodeNumber)
	{
		nodeNumber = newNodeNumber;
	}
	
	public int getLinkStatus()
	{
		return linkStatus;
	}
	
	public void setLinkStatus(int newLinkStatus)
	{
		linkStatus = newLinkStatus;
	}
	
	public int getNumHops()
	{
		return numHops;
	}
	
	public void setNumHops(int newNumHops)
	{
		numHops = newNumHops;
	}
	
	public ArrayList<Integer> getTwoHopNeighbors()
	{
		return twoHopNeighbors;
	}
	
	public void setTwoHopNeighbors(ArrayList<Integer> newTwoHopNeighbors)
	{
		twoHopNeighbors.clear();
		for (int i = 0; i < newTwoHopNeighbors.size(); i++)
		{
			twoHopNeighbors.add(newTwoHopNeighbors.get(i));
		}
	}
	
	public int getHoldingTime()
	{
		return holdingTime;
	}
	
	public void setHoldingTime(int newHoldingTime)
	{
		holdingTime = newHoldingTime;
	}
	
	public int getSequenceNumber()
	{
		return sequenceNumber;
	}
	
	public void setSequenceNumber(int newSequenceNumber)
	{
		sequenceNumber = newSequenceNumber;
	}
	
	public boolean getIsMPRSelector()
	{
		return isMPRSelector;
	}
	
	public void setIsMPRSelector(boolean newIsMPRSelector)
	{
		isMPRSelector = newIsMPRSelector;
	}
}