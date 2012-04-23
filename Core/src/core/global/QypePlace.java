package core.global;

import java.util.ArrayList;

import core.dataobtainer.QypeApiUrlBuilder;
import core.json.JSONArray;
import core.json.JSONException;
import core.json.JSONObject;
import core.support.ConcurrentRestful;
import core.support.Log;
import core.support.Servers;

public class QypePlace extends Place {
	
	public String OwnerDescription = "";
	public String OpeningHours = "";
	
	public static QypePlace create(JSONObject place) throws Exception {
		QypePlace p = new QypePlace();
		if (place.getBoolean("closed")) {
			return null;	
		}
		
		p.RatingImgUrl = "http://cdn.businessservicereviews.com/wp-content/themes/wpremix2_3/images/icons/small-3-and-one-half-star-rating.png";
		
		try {
			p.Title = place.getString("title");		
			p.ID = place.getString("id");
			// the id is the number after the last '/'.
			String[] t = p.ID.split("/");
			p.ID = t[t.length - 1]; 
			p.Location = new SimpleLocation(place.getString("point"));
		} catch (JSONException e) {
			return null;
		}
		
		try {
			p.AvgRating = Double.parseDouble(place.getString("average_rating"));
		} catch (NumberFormatException e) {}
		
		try {
			p.PhoneNumber = place.getString("phone");
		} catch (JSONException e) {}
		
		try {
			JSONArray links = place.getJSONArray("links");
			int linksLen = links.length();
			
			for (int i=0; i<linksLen; i++) {
				JSONObject link = links.getJSONObject(i);
				String rel = link.getString("rel");
			
				if (rel != null	&& rel.equalsIgnoreCase("http://schemas.qype.com/reviews")) {
					p.NumReviews += link.getInt("count");				
				}
			}
		} catch (JSONException e) {}
		
		try {
			p.OwnerDescription = place.getString("owner_description_text");
		} catch (JSONException e) {}
		
		try {
			p.OpeningHours = place.getString("opening_hours");
		} catch (JSONException e) {}
		
		try {
			p.PlaceUrl = place.getString("url");
		} catch (JSONException e) {}
		
		try {
			JSONObject images = place.getJSONObject("image");
			p.ImageUrl = images.getString("medium");
		} catch (JSONException e) {}
		
		try {
			JSONArray links = place.getJSONArray("links");
			for (int i=0; i<links.length(); i++)
			{
				JSONObject link = links.getJSONObject(i);
				if (link.getString("rel").equals("alternate")) {
					p.DataSourceUrl = link.getString("href");
					break;
				}
			}
		} catch (JSONException e) {}
		
		p.Reviews = new ArrayList<Review>();
		
		return p;
		
		//TODO: The rest of the qype information.

	}

	@Override
	public void downloadReviews(Log logger) throws Exception {
		if (Reviews == null || Reviews.size() == 0) {
			String url = QypeApiUrlBuilder.getReviews(this.ID);
			JSONObject result = ConcurrentRestful.getJSONResult(url, Servers.Qype, logger);
			
			Reviews = new ArrayList<Review>();
			
			//JSONObject reviews = result.getJSONObject("reviews");
			JSONArray reviewsJSON = result.getJSONArray("results");
			for (int i=0; i<reviewsJSON.length(); i++) {
				JSONObject reviewJSON = reviewsJSON.getJSONObject(i);
				reviewJSON = reviewJSON.getJSONObject("review");
				
				Review toAdd = new Review();
				toAdd.Excerpt = reviewJSON.getString("summary");
				toAdd.RatingImg = "http://cdn.businessservicereviews.com/wp-content/themes/wpremix2_3/images/icons/small-3-and-one-half-star-rating.png";
				toAdd.Id = reviewJSON.getString("id");
				toAdd.Source = "Qype";
				toAdd.Url = PlaceUrl;
				
				Reviews.add(toAdd);
			}
		}
	}
	
}
