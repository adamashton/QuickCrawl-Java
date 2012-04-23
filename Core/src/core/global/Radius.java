package core.global;

public class Radius {
	
	/**
	 * If radius is less than 2 we need to do our own filtering.
	 */
	public boolean requirePostProcessing = false;
	
	public Radius(double valueIn) throws IndexOutOfBoundsException
	{
		setValue(valueIn);
	}
	
	private double value;
	
	public double getValue()
	{
		return value;
	}
	
	public void setValue(double valueIn) throws IndexOutOfBoundsException
	{
		if (valueIn < 2) {
			// mixing in_cat and radius less than 2 doesn't work.
			requirePostProcessing  = true;			
		} else if (valueIn > 5) {
			throw new IndexOutOfBoundsException("Qype defaults to 5.");
		}
		
		value = valueIn;
	}
}
