package core.global;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import core.support.Helper;


public class QuickCrawl implements java.io.Serializable {
	private static final long serialVersionUID = 4602580469183514794L;
	private List<AggPlace> places;
	private Date created = new Date();
	
	public Date getCreated() {
		return created;
	}
	
	public QuickCrawl(List<AggPlace> places) {
		this.places = new ArrayList<AggPlace>(places);
	}
	
	public List<AggPlace> getPlaces() {
		return places;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("start ");
		for (AggPlace place : places) {
			sb.append(place.toString() + " -> ");
		}
		sb.append("end");
		return sb.toString();
	}
	
}
