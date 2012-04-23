package web;

import javax.servlet.http.HttpServletRequest;

import core.global.SimpleLocation;


public class WebSupport {
	public static String getURLWithContextPath(HttpServletRequest request)
	{
		String port = "";
		if (request.getServerPort() != 80) {
			port = ":" + request.getServerPort();
		}
	   return request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
	}
	
	public static String getGoogleDirectionsURL(SimpleLocation aLoc, String aDesc, SimpleLocation bLoc, String bDesc)
	{
		String result = "http://maps.google.com/"
			+ "?saddr=" + aLoc.toStringShort() + "+(" + aDesc + ")"
			+ "&daddr=" + bLoc.toStringShort() + "+(" + bDesc + ")"
			+ "&dirflg=w&doflg=ptk";
		
		return result;
	}
	
	public static String getPoweredByImgURL(String source) throws Exception {
		if (source.equals("Yelp"))
			return "http://media3.px.yelpcdn.com/static/201012163983986833/img/developers/yelp_logo_100x50.png";
		else if (source.equals("Qype"))
			return "http://assets2.qypecdn.net/images/logos/qype_logo_en.png";
		
		throw new Exception("not implemented");
	}
	
	public static String eJS(String text) {
		return text.replace("\'", "\\'");
	}
	
}
