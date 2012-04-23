package core.global;

import java.io.Serializable;

public class Review implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4426400566588522246L;
	public String RatingImg;
	public String Id;
	public String Excerpt;
	
	// of the review
	public String Url;
	
	// yelp or qype
	public String Source;
	
	public String getPoweredByImgURL() throws Exception {
		if (this.Source.equals("Yelp"))
			return "http://media3.px.yelpcdn.com/static/201012163983986833/img/developers/yelp_logo_100x50.png";
		else if (this.Source.equals("Qype"))
			return "http://assets2.qypecdn.net/images/logos/qype_logo_en.png";
		
		throw new Exception("not implemented");
	}
}
