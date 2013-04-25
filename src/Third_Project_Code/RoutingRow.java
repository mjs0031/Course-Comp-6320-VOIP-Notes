package Third_Project_Code;

public class RoutingRow
{
	private int destAddress, nextHopAddress, distance;
	
	public RoutingRow(int destAddress, int nextHopAddress, int distance)
	{
		this.destAddress = destAddress;
		this.nextHopAddress = nextHopAddress;
		this.distance = distance;
	}
	
	public int getDestAddress()
	{
		return destAddress;
	}
	
	public void setDestAddress(int newDestAddress)
	{
		destAddress = newDestAddress;
	}
	
	public int getNextHopAddress()
	{
		return nextHopAddress;
	}
	
	public void setNextHopAddress(int newNextHopAddress)
	{
		nextHopAddress = newNextHopAddress;
	}
	
	public int getDistance()
	{
		return distance;
	}
	
	public void setDistance(int newDistance)
	{
		distance = newDistance;
	}
}