package core.global;

public class Rectangle {
	public SimpleLocation topLeft;
	public SimpleLocation topRight;
	public SimpleLocation bottomLeft;
	public SimpleLocation bottomRight;
	
	private Rectangle() {}
	
	public static Rectangle ConstructTLBR(SimpleLocation topLeft, SimpleLocation bottomRight)
	{
		assert topLeft.longitude <= bottomRight.longitude;
		assert topLeft.latitude >= bottomRight.latitude;
		
		Rectangle result = new Rectangle();
		result.topLeft = topLeft;
		result.bottomRight = bottomRight;
		result.topRight = new SimpleLocation(topLeft.latitude, bottomRight.longitude);
		result.bottomLeft = new SimpleLocation(bottomRight.latitude, topLeft.longitude);
		
		return result;
	}
	
	public static Rectangle ConstructTRBL(SimpleLocation bottomLeft, SimpleLocation topRight)
	{
		assert topRight.longitude >= bottomLeft.longitude;
		assert topRight.latitude >= bottomLeft.latitude;
		
		Rectangle result = new Rectangle();
		result.topRight = topRight;
		result.bottomLeft = bottomLeft;
		result.topLeft = new SimpleLocation(topRight.latitude, bottomLeft.longitude);
		result.bottomRight = new SimpleLocation(bottomLeft.latitude, topRight.longitude);
		
		return result;
	}

	public double getArea() {
		double width = topRight.longitude - topLeft.longitude;
		double height = topRight.latitude - bottomRight.latitude;
		return width*height;
	}
}
