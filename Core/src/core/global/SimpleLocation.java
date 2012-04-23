package core.global;

import java.io.Serializable;

import core.support.Helper;

public class SimpleLocation implements Serializable {
	private static final long serialVersionUID = 7563623428616031795L;
	public double latitude;
	public double longitude;
	
	public SimpleLocation(double latitudeIn, double longitudeIn)
	{
		latitude = latitudeIn;
		longitude = longitudeIn;
	}
	
	public SimpleLocation(String location) throws Exception
	{
		String[] locations = location.split(",");
		if (locations.length != 2) {
			throw new Exception("Need 2 comma seperated floats.");
		}
		
		latitude = Double.parseDouble(locations[0]);
		longitude = Double.parseDouble(locations[1]);
	}
	
	/**
	 * Returns "latitude,longitude".
	 */
	public String toString()
	{
		String result = Double.toString(latitude) + "," + Double.toString(longitude);
		return result;
	}
	
	public boolean equals(Object object){
		if(this == object){
			return true;
		}
		if(object instanceof SimpleLocation){
			SimpleLocation test = (SimpleLocation)object;
			if(test.latitude == this.latitude &&
					test.longitude == this.longitude){
				return true;
			}
		}
		return false;
	}

	public boolean moreAccurate(SimpleLocation other) {
		return this.toString().length() > other.toString().length();
	}

	public String toStringShort() {
		String result = Double.toString(Helper.fourDecimals(latitude)) + "," 
			+ Double.toString(Helper.fourDecimals(longitude));
		return result;
	}
	
}
