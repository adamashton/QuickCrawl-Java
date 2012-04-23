package core.global;

import java.util.ArrayList;
import java.util.List;

import core.support.Helper;
import core.support.Log;


public class AggPlace implements java.io.Serializable {
	

	private static final long serialVersionUID = 5538031602450099979L;

	// a list of the places that this agg place represents, usually just 1
	private List<Place> places;
	
	public String Title;
	public SimpleLocation Location;
	public String PhoneNumber;
	public Double AverageRating;
	public int NumberOfRatings;
	public String OwnerDescription;
	public String imageUrl = null;
	public String Address1;
	public String Address2;
	public String Address3;
	public String PostCode;
	public String PlaceUrl;
	
	public List<Review> Reviews;

	// sum of all ratings given for this place
	public double TotalRating;
	
	public AggPlace() {
		places = new ArrayList<Place>();
		Reviews = new ArrayList<Review>();
	}
	
	public boolean hasOwnerDescription() {
		return OwnerDescription != null && !OwnerDescription.equals("");
	}
	
	public boolean addPlace(Place place) throws Exception{
		if(places.contains(place)){
			return false;
		}
		
		if (Helper.nullOrEmpty(this.Title) && !Helper.nullOrEmpty(place.Title)) {
			Title = place.Title;
		}
		
		if ((this.Location == null && place.Location != null) || place.Location.moreAccurate(this.Location)) {
			Location = place.Location;
		}
		
		if (Helper.nullOrEmpty(PhoneNumber) && !Helper.nullOrEmpty(place.PhoneNumber)) {
			PhoneNumber = place.PhoneNumber;
		}
		
		if (Helper.nullOrEmpty(this.imageUrl) && !Helper.nullOrEmpty(place.ImageUrl)) {
			imageUrl = place.ImageUrl;
		}
		
		if (place instanceof QypePlace) {
			if (Helper.nullOrEmpty(OwnerDescription))
				OwnerDescription = ((QypePlace)place).OwnerDescription;
		}
		
		if (Helper.nullOrEmpty(this.Address1) && !Helper.nullOrEmpty(place.Address1)) {
			Address1 = place.Address1;
			Address2 = place.Address2;
			Address3 = place.Address3;
		}
		if (Helper.nullOrEmpty(this.PostCode) && !Helper.nullOrEmpty(place.PostCode)) {
			PostCode = place.PostCode;
		}
		
		if (Helper.nullOrEmpty(this.PlaceUrl) && !Helper.nullOrEmpty(place.PlaceUrl)) {
			PlaceUrl = place.PlaceUrl;
		}
		
	
		//update the average ratings with new data from the new place
		//NumberOfRatings += place.NumReviews;
		
		boolean result = places.add(place);
		
		// re calc rating		
		NumberOfRatings = 0;
		AverageRating = 0.0;
		TotalRating = 0.0;
		
		double tempRating = 0.0;
		for (Place p : places) {
			NumberOfRatings += p.NumReviews;			
			assert p.AvgRating >= 0.0 && p.AvgRating <= 5.0 : p.AvgRating;			
			tempRating += p.NumReviews * (p.AvgRating / 5.0);
		}
		
		// the sum of ratings given to the place
		// we have to just take the avg and times by the num reviews to get closest as pos.
		TotalRating = tempRating;
		if (NumberOfRatings > 0) {
		this.AverageRating = tempRating / NumberOfRatings;
		} else {
			assert tempRating == (double) 0.0;
			AverageRating = 0.0;
		}
		
		assert AverageRating <= 1 & AverageRating >= 0 : AverageRating;
		return result;
	}
	
	public String toString() {
		String result = String.format("%s Avg:%s (%s ratings).",
				Title,
				AverageRating, 
				NumberOfRatings);
		result = result.replace("'", "\\'");
		return result;
	}
	
	public List<Place> getAggregatedPlaces() {
		return places;
	}

	public void downloadReviews(Log logger) throws Exception {
		for (Place p : places) {
			p.downloadReviews(logger);
			this.Reviews.addAll(p.Reviews);
		}
	}
	
}