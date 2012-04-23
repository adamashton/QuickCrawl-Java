package core.dataobtainer;
import java.util.ArrayList;
import java.util.List;

import core.global.Category;
import core.global.Place;
import core.global.QypePlace;
import core.json.JSONArray;
import core.json.JSONException;
import core.json.JSONObject;
import core.support.ConcurrentRestful;
import core.support.Log;
import core.support.Servers;


 class QypeApiHelper {
	
	/**
	 * Downloads document chain from t'internet.
	 * @param url
	 * @return List of Documents from Qype API.
	 * @throws Exception
	 */
	 /*
	public static ArrayList<JSONObject> getDocumentChain(String url) throws Exception
	{
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
				
		String nextDocUrl = url;
		do {
			JSONObject document = Restful.getJSONResult(nextDocUrl);
			result.add(document);
			nextDocUrl = getNextURL(document);			
		} while (nextDocUrl != null && nextDocUrl != "");
		return result;
	}
	*/
	
	public static JSONObject getFirstDocument(String url, Log logger) throws Exception
	{
		JSONObject result = ConcurrentRestful.getJSONResult(url, Servers.Qype, logger);
		return result;
	}

	private static String getNextURL(JSONObject document) {
		String result = null;
		
		try {
			JSONArray links = document.getJSONArray("links");
			
			int linksLen = links.length();
			for (int i=0; i<linksLen; i++) {
				JSONObject link = links.getJSONObject(i);
				if (link.getString("rel").equals("next")) {
					result = link.getString("href");
				}
			}
		
		
		} catch (JSONException e) {
			System.err.println("Failed to get Next URL message was: " + e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}

	public static int getTotalEntries(JSONObject startDocument) {
		int result = -1;
		
		try {
			//JSONObject places = startDocument.getJSONObject("places");
			//JSONObject results = startDocument.getJSONObject("results");
			
			result = startDocument.getInt("total_entries");
		
		} catch (JSONException e) {
			System.err.println("Failed to get Next URL message was: " + e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}

	public static String getCategoryCode(Category category) {
		if (category == Category.Pubs) {
			return "21";
		} else if (category == Category.Bars) {
			return "22";
		} else if (category == Category.Clubs) {
			return "23";
		} else if (category == Category.Nightlife) {
			return "2";
		} else if (category == Category.Music) {
			return "303";
		} else {
			return "-1";
		}
	}

	public static List<JSONObject> getRestOfDocumentChain(JSONObject startDocument, Log logger) throws Exception {
		List<JSONObject> result = new ArrayList<JSONObject>();
		String nextDocUrl = getNextURL(startDocument);
		while (nextDocUrl != null && nextDocUrl != "") {
			JSONObject document = ConcurrentRestful.getJSONResult(nextDocUrl, Servers.Qype, logger);
			result.add(document);
			nextDocUrl = getNextURL(document);			
		}
		
		return result;
	}
 
	public static List<Place> getPlaces(JSONObject qypeDoc, Log logger) throws JSONException {
		List<Place> result = new ArrayList<Place>();
		JSONArray results = qypeDoc.getJSONArray("results");
		int len = results.length();
		for (int i=0; i<len; i++) {
			try {
				JSONObject object = results.getJSONObject(i);
				JSONObject place = object.getJSONObject("place");
				QypePlace qypePlace = QypePlace.create(place);
				if (qypePlace != null)
					result.add(qypePlace);
				else 
					logger.Info("A QypePlace was not valid.");
			} catch (Exception e) {
				String message = String.format("Error parsing a place. The message was: %s", e.getMessage());
				System.err.println(message);
				e.printStackTrace();
			}	
		}
		return result;
	}
	
	public static Place getPlace(String id, Log logger) throws Exception {
		if (id.startsWith("/places/")) {
			id.replace("/places/", "");
		}
		String url = QypeApiUrlBuilder.getPlaceById(id);
		JSONObject qypeDoc = ConcurrentRestful.getJSONResult(url, Servers.Qype, logger);
		JSONArray results = qypeDoc.getJSONArray("results");
		
		JSONObject object = results.getJSONObject(0);
		JSONObject place = object.getJSONObject("place");
		QypePlace qypePlace = QypePlace.create(place);
		return qypePlace;
	}
	
 }
