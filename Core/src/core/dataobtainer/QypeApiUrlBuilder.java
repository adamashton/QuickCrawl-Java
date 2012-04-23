package core.dataobtainer;

public class QypeApiUrlBuilder {
	// resource strings
	private static String key = "YkBaByL17rcUc7Lo0zt4w";	
	private static String baseUrl = "http://api.qype.com/v1/";
	
	private static String positions = "positions";
	private static String places = "places";
	
	private static String categoryParam = "in_category";
	private static String radiusParam = "radius";
	private static String keyParam = "consumer_key";
	private static String pageParam = "per_page";
	
	private static String boundingBoxes = "bounding_boxes";
	
	public static String getPlacesInCircle(String location, String category, String radius)
	{
		// returns all places that are within the radius in km of the location
		// and inside the category
		// places can be in more than 1 category
		// but categories are hierarchical (e.g. Pubs is in Nightlife)
		// So a places with category Pubs appears in Nightlife and Pubs.
		String result = baseUrl;
		result += positions + "/" + location + "/";
		result += places;
		result += ".json";
		
		result += "?" + buildParameter(categoryParam, category);
		result += "&" + buildParameter(radiusParam, radius);
		result += "&" + buildParameter(pageParam, "100");
		result += "&" + buildParameter(keyParam, key);
		
		return result;
	}
	
	static String getPlacesInRectangle(String bottomLeft, String topRight, String category)
	{
		//http://api.qype.com/v1/bounding_boxes/{sw_lat},{sw_lng},{ne_lat},{ne_lng}/places
		String result = baseUrl;
		result += boundingBoxes + "/" + bottomLeft + "," + topRight + "/";
		result += places;
		result += ".json";
		
		result += "?" + buildParameter(categoryParam, category);
		result += "&" + buildParameter(pageParam, "100");
		result += "&" + buildParameter(keyParam, key);
		return result;
	}
	
	static String getPlaceById(String id) {
		String result = baseUrl + "places/" + id;
		result += "?" + buildParameter(keyParam, key);
		return result;
	}
	
	public static String getReviews(String id) {
		String result = baseUrl + "places/" + id + "/reviews.json?";
		result += buildParameter(keyParam, key);
		return result;
	}
	
	static String buildParameter(String key, String value)
	{
		String result = key + "=" + value;
		return result;
	}
}

