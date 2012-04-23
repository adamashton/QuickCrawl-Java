package core.global;

import java.util.ArrayList;

import core.json.JSONArray;
import core.json.JSONException;
import core.json.JSONObject;
import core.support.Log;

@SuppressWarnings("serial")
public class YelpPlace extends Place {

	private YelpPlace() {}
	
	public static YelpPlace create(JSONObject jo) {
		try {
			YelpPlace place = new YelpPlace();
			if (jo.getBoolean("is_closed"))
				return null;
			
			place.ID = jo.getString("id");
			
			place.Title = jo.getString("name");
			place.PhoneNumber = jo.getString("phone");
			place.AvgRating = jo.getDouble("avg_rating");
			place.NumReviews = jo.getInt("review_count");
			place.Location = new SimpleLocation(jo.getDouble("latitude"), jo
					.getDouble("longitude"));

			place.Address1 = jo.getString("address1");
			place.Address2 = jo.getString("address2");
			place.Address3 = jo.getString("address3");
			place.PostCode = jo.getString("zip");
			
			place.ImageUrl = jo.getString("photo_url");
			
			place.RatingImgUrl = jo.getString("rating_img_url");
			
			place.DataSourceUrl = jo.getString("url");
			
			JSONArray reviews = jo.getJSONArray("reviews");
			place.Reviews = new ArrayList<Review>();
			for (int i=0; i<reviews.length(); i++) {
				Review toAdd = new Review();
				toAdd.Source = "Yelp";
				toAdd.Excerpt = "\"" + reviews.getJSONObject(i).getString("text_excerpt") + "\"";
				toAdd.Id = reviews.getJSONObject(i).getString("id");
				toAdd.Url = reviews.getJSONObject(i).getString("url");
				toAdd.RatingImg = reviews.getJSONObject(i).getString("rating_img_url");
				place.Reviews.add(toAdd);
			}

			
			return place;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void downloadReviews(Log logger) throws Exception {
		// we get reviews in the original place content, nothign to do here!		
	}

	
}
