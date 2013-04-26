package Third_Project_Code;

import java.util.ArrayList;

public class TCRow
{
	private int nodeNumber, sequenceNumber;
	private ArrayList<Integer> mPRSelectors = new ArrayList<Integer>();
	
	public TCRow(int nodeNumber, int sequenceNumber, ArrayList<Integer> mPRSelectors)
	{
		this.nodeNumber = nodeNumber;
		this.sequenceNumber = sequenceNumber;
		for (int i = 0; i < mPRSelectors.size(); i++)
		{
			this.mPRSelectors.add(mPRSelectors.get(i));
		}
	}
	
	public int getNodeNumber()
	{
		return nodeNumber;
	}
	
	public void setNodeNumber(int newNodeNumber)
	{
		nodeNumber = newNodeNumber;
	}
	
	public int getSequenceNumber()
	{
		return sequenceNumber;
	}
	
	public void setSequenceNumber(int newSequenceNumber)
	{
		sequenceNumber = newSequenceNumber;
	}
	
	public ArrayList<Integer> getMPRSelectors()
	{
		return mPRSelectors;
	}
	
	public void setMPRSelectors(ArrayList<Integer> newMPRSelectors)
	{
		mPRSelectors.clear();
		for (int i = 0; i < newMPRSelectors.size(); i++)
		{
			mPRSelectors.add(newMPRSelectors.get(i));
		}
	}
}