package core.global;

import java.io.Serializable;
import java.util.List;

import core.support.Log;

public abstract class Place implements Serializable {

	private static final long serialVersionUID = 6826872648122499844L;
	
	public String Title = "";
	public double AvgRating = 0;
	public int NumReviews = 0;
	public String PhoneNumber = "";

	public String Address1 = "";
	public String Address2 = "";
	public String Address3 = "";
	public String PostCode = "";
	
	public String RatingImgUrl = null;
	public String ImageUrl = "";
	
	public String ID = "";
	
	public String PlaceUrl = "";
	
	public String DataSourceUrl = "";
	
	public SimpleLocation Location;
	
	public List<Review> Reviews;
		
	public String toString()
	{
		String result = String.format("%s %s/5 (%s ratings).",
				Title,
				AvgRating, 
				NumReviews);
		return result;
	}

	public boolean equals(Object other){
		if (this == other) {
			return true;
		}
		
		Place otherPlace = (Place)other;
		if (otherPlace.ID.equals(this.ID)) {
			return true;
		}
		
		return false;
	}
	
	public String getDataSource() throws Exception {
		if (this instanceof YelpPlace)
			return "Yelp";
		else if (this instanceof QypePlace)
			return "Qype";
		
		throw new Exception("not implemented");
	}
	
	public String getPoweredByImgURL() throws Exception {
		if (this instanceof YelpPlace)
			return "http://media3.px.yelpcdn.com/static/201012163983986833/img/developers/yelp_logo_100x50.png";
		else if (this instanceof QypePlace)
			return "http://assets2.qypecdn.net/images/logos/qype_logo_en.png";
		
		throw new Exception("not implemented");
	}
	
	public String getDataSourceBaseURL() throws Exception {
		if (this instanceof YelpPlace)
			return "http://yelp.com";
		else if (this instanceof QypePlace)
			return "http://qype.co.uk";
		
		throw new Exception("not implemented");
	}
	
	

	public abstract void downloadReviews(Log logger) throws Exception;
}
