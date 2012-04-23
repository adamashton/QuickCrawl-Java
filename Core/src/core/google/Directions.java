package core.google;

import java.util.ArrayList;
import java.util.List;

import core.global.AggPlace;
import core.global.SimpleLocation;
import core.json.JSONArray;
import core.json.JSONObject;
import core.support.Log;
import core.support.Servers;

public class Directions {
	/**
	 * Given a list of places it will use google directions to find the best route
	 * @return Best route for the places 
	 * @throws Exception 
	 */
	public static List<AggPlace> getOrderedPlaces(SimpleLocation start, SimpleLocation end, 
			List<AggPlace> places, Log log) throws Exception {
		
		String url = buildURL(start, end, places);
		JSONObject jsonResult = core.support.ConcurrentRestful.getJSONResult(url, Servers.Google, log);
		
		JSONObject wayPointOrder = jsonResult.getJSONArray("routes").getJSONObject(0);
		JSONArray waypointOrder = wayPointOrder.getJSONArray("waypoint_order");
		List<AggPlace> result = new ArrayList<AggPlace>();
		for (int i=0; i<waypointOrder.length(); i++) {
			AggPlace p = places.get(waypointOrder.getInt(i));
			result.add(p);
		}
				
		return result;
	}

	private static String buildURL(SimpleLocation start, SimpleLocation end,
			List<AggPlace> places) {
		StringBuilder url = new StringBuilder(); 
		url.append("http://maps.googleapis.com/maps/api/directions/json");
		
		url.append("?" + buildParameter("origin", start.toString()));
		url.append("&" + buildParameter("destination", end.toString()));
		
		StringBuilder waypoints = new StringBuilder();
		waypoints.append("optimize:true|");
		for (AggPlace p : places)
			waypoints.append(p.Location.toString() + "|");
		String waypointsStr = waypoints.substring(0, waypoints.length() - 1).toString();
		
		url.append("&" + buildParameter("waypoints", waypointsStr));
		url.append("&" + buildParameter("alternatives", "false"));
		url.append("&" + buildParameter("sensor", "false"));
		url.append("&" + buildParameter("mode", "walking"));
		
		return url.toString();
	}
	
	private static String buildParameter(String key, String value)
	{
		String result = key + "=" + value;
		return result;
	}
}
