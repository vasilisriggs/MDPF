package ds.bplus.object;

public class KeyValuesObject {
	private long key;
	private double valueX;
	private double valueY;
	
	public KeyValuesObject(long key, double valueX, double valueY) {
		this.key = key;
		this.valueX = valueX;
		this.valueY = valueY;
	}
	
	public long getKey() 
	{return key;}
	
	public double getValueX() 
	{return valueX;}
	
	public double getValueY() 
	{return valueY;}
}
