package core.support;

import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import core.global.Rectangle;
import core.global.SimpleLocation;

public class Helper {

	public static Rectangle CreateRectangle(SimpleLocation startOfCrawl,
			SimpleLocation endOfCrawl, double radiusKm) throws Exception {
		
		assert radiusKm >= 0.0;
		
		Rectangle result = getRectangle(startOfCrawl, endOfCrawl);
		double radius = radiusKm / 98;
		// now add the radius
		SimpleLocation topLeftWRadius = new SimpleLocation(result.topLeft.latitude + radius, 
				result.topLeft.longitude - radius);
		SimpleLocation bottomRightWRadius = new SimpleLocation(result.bottomRight.latitude - radius,
				result.bottomRight.longitude + radius);
		result = Rectangle.ConstructTLBR(topLeftWRadius, bottomRightWRadius);

		return result;
	}
	
	public static double twoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
	}
	
	public static double fourDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.####");
    	return Double.valueOf(twoDForm.format(d));
	}

	public static Rectangle getRectangle(SimpleLocation startOfCrawl,
			SimpleLocation endOfCrawl) throws Exception {
		double top, bottom;
		if (startOfCrawl.latitude > endOfCrawl.latitude) {
			top = startOfCrawl.latitude;
			bottom = endOfCrawl.latitude;
		} else {
			bottom = startOfCrawl.latitude;
			top = endOfCrawl.latitude;
		}
		
		double left, right;
		if (startOfCrawl.longitude < endOfCrawl.longitude) {
			left = startOfCrawl.longitude;
			right = endOfCrawl.longitude;
		} else {
			right = startOfCrawl.longitude;
			left = endOfCrawl.longitude;
		}
		
		/// sanity check
		assert top >= bottom; 
		assert left <= right;
		
		SimpleLocation topLeft = new SimpleLocation(top, left);
		SimpleLocation bottomRight = new SimpleLocation(bottom, right);		
		Rectangle result = Rectangle.ConstructTLBR(topLeft, bottomRight);
		return result;
	}

	public static Rectangle[] SplitRectangle(Rectangle rectangle) {
		Double width = rectangle.bottomRight.longitude - rectangle.bottomLeft.longitude;
		Double height = rectangle.topLeft.latitude - rectangle.bottomLeft.latitude;
		
		Rectangle[] result = new Rectangle[2];
		
		if (Math.abs(width) > Math.abs(height)) {
			// split rectangle vertically
			Double newLongitude = rectangle.topLeft.longitude + (width / 2);
			
			result[0] = Rectangle.ConstructTLBR(
					rectangle.topLeft, 
					new SimpleLocation(rectangle.bottomRight.latitude, newLongitude));
			
			result[1] = Rectangle.ConstructTLBR(
					new SimpleLocation(rectangle.topLeft.latitude, newLongitude), 
					rectangle.bottomRight);
			
		} else {
			// split horizontally
			Double newLatitude = rectangle.bottomLeft.latitude + (height / 2);
			
			result[0] = Rectangle.ConstructTLBR(
					rectangle.topLeft, 
					new SimpleLocation(newLatitude, rectangle.bottomRight.longitude));
			
			result[1] = Rectangle.ConstructTLBR(
					new SimpleLocation(newLatitude, rectangle.bottomLeft.longitude), 
					rectangle.bottomRight);
		}
		
		return result;
	}

	public static boolean withinCircle(SimpleLocation centreOfCircle,
			SimpleLocation point, Double radiusKm) {
		double distance = distance(centreOfCircle, point);
		return distance < radiusKm;
	}

	/**
	 * Returns distance in km.
	 */
	public static double distance(SimpleLocation a,
			SimpleLocation b) {
		double result = distance(a.latitude, a.longitude, b.latitude, b.longitude);
		return result;
	}

	/**
	 * Returns distance in KM
	 */
	public static double distanceFromLine(SimpleLocation point,
			SimpleLocation a, SimpleLocation b) {
		
		double result = Line2D.ptLineDist(a.longitude, a.latitude, 
				b.longitude, b.latitude, 
				point.longitude, point.latitude);
		
		// TODO: this doesn't really work properly
		// not taking into account curvature of earth, need to find new 
		// impl. that does this
		return result * 98;
		/*return DistancePoint.distanceToSegment(point.longitude, point.latitude, 
				a.longitude, a.latitude,
				b.longitude, b.longitude);*/
	}
	
	public static double getGradient(SimpleLocation newA, SimpleLocation newB) {
		double result = (newB.latitude - newA.latitude) / (newB.longitude - newA.longitude);
		return result;
	}
	
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two ZIP Codes or Postal Codes using our           :*/
	/*::  ZIPCodeWorld(TM) and PostalCodeWorld(TM) products.                     :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::  Hexa Software Development Center © All Rights Reserved 2004            :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
	  double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;
	  
	  dist = dist * 1.609344; // convert to km
	  
	  return dist;
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
	  return (rad * 180.0 / Math.PI);
	}

	public static boolean nullOrEmpty(String s) {
		return s == null || s.equals("");
	}

	public static boolean nullOrEmpty(StringBuilder sb) {
		return sb == null || sb.toString() == null || sb.toString().equals("");
	}
}
