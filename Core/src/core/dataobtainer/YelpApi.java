package core.dataobtainer;

import java.util.ArrayList;
import java.util.List;

import core.global.Place;
import core.global.SimpleLocation;
import core.global.YelpPlace;
import core.json.JSONArray;
import core.json.JSONObject;
import core.support.ConcurrentRestful;
import core.support.Log;
import core.support.Servers;

public class YelpApi {
	final static String key = "g0LDuG2LKD0-rd2CKqAEcg";
	final static String API_URL = "http://api.yelp.com";
	final static String BR_SEARCH = "business_review_search";
	
	private Log logger;
	
	public YelpApi(Log logger) {
		this.logger = logger;
	}

	public List<Place> searchMapBoundingBox(String searchTerm, Integer limit,
			SimpleLocation topLeft, SimpleLocation bottomRight) throws Exception {
		//TODO: Move to APIv2 when it comes out
		//TODO: add sort ordering.
		// Build url with required params
		String url = API_URL + "/" + BR_SEARCH + "?";
		url += "ywsid=" + key + "&";
		url += "tl_lat=" + topLeft.latitude + "&";
		url += "tl_long=" + topLeft.longitude + "&";
		url += "br_lat=" + bottomRight.latitude + "&";
		url += "br_long=" + bottomRight.longitude;
		// Add optional parameters
		String options = "";
		if (searchTerm != null)
			options += "&category=" + searchTerm;
		if (limit != null)
			options += "&limit=" + limit;
		url += options;
		
		// Send request
		JSONObject json = ConcurrentRestful.getJSONResult(url, Servers.Yelp, this.logger);
		return parseResponse(json);
	}
	
	public List<Place> searchRadius(String searchTerm, Integer limit, SimpleLocation centre, Double radius) throws Exception {
		// Build url with required params
		String url = API_URL + "/" + BR_SEARCH + "?";
		url += "ywsid=" + key + "&";
		url += "lat=" + centre.latitude + "&";
		url += "long=" + centre.longitude + "&";
		url += "radius=" + radius;
		// Add optional parameters
		String options = "";
		if (searchTerm != null)
			options += "&term=" + searchTerm;
		if (limit != null)
			options += "&limit=" + limit;
		url += options;
		// Send request
		
		//HttpURLConnection con = createConnection(url);
		//con.connect();
		
		JSONObject json = ConcurrentRestful.getJSONResult(url, Servers.Yelp, this.logger);
		List<Place> result = parseResponse(json);
		return result;
	}

	private List<Place> parseResponse(JSONObject response) throws Exception {
		// Get response string
		/*String payload = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				con.getInputStream(), "UTF-8"));
		payload = reader.readLine();
		
		// Parse JSON		
		JSONObject response = new JSONObject(payload);*/
			// Check for an error
			if (response.getJSONObject("message").getInt("code") != 0) {
				throw new Exception(response.toString(0));
			}
			// Get businesses
			JSONArray businesses = response.getJSONArray("businesses");
			int length = businesses.length();
			ArrayList<Place> places = new ArrayList<Place>();
			for (int i = 0; i < length; i++) {
				JSONObject b = businesses.getJSONObject(i);
				YelpPlace place = YelpPlace.create(b);
				if (place != null)
					places.add(place);
			}
		
		return places;
	}

	public Place searchID(String Id) {
		return YelpPlace.create(null);
	}
}

