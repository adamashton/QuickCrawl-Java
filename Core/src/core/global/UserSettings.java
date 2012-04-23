package core.global;

import java.util.ArrayList;
import java.util.List;

import core.dataobtainer.DataSource;
import core.dataobtainer.QypeDataSource;
import core.dataobtainer.YelpDataSource;
import core.support.Helper;
import core.support.Log;

/**
 * Holds all variables about the quick crawl.
 * @author adam
 *
 */
public class UserSettings implements java.io.Serializable {
	private static final long serialVersionUID = -4154240714341181746L;

	// how far is would you be prepared to walk between pubs.
	// calculated automatically based on distance between start and end
	public double MaxWalkingDistanceKm = -1;
	
	private SimpleLocation start;
	private SimpleLocation end;
	
	private String title; // the title of the crawl
	private String user; // the user who created the crawl
	
	
	public Category Category = core.global.Category.Pubs;
	
	//TODO: fix the long/lat converting to km problem.
	public Double DistanceFromLineKm = 0.6; // how far from the pubcrawl line do we look for pubs from
	
	private int NumberOfPlaces = 5; // number of places to find between start and end
	
	
	// static settings that have to be saved with the user settings.
	public double DefaultWalkingDistance = 0.5;

	// the average rating of the place
	public double AverageRatingWeight = 5; 

	// crawls where the overall distance is shorter are considered better than crawls with long walks
	// however we need to strike a balance between good pubs and walking...
	public double TotalDistanceWeighting = 10;
		
	public double DistanceDifferenceWighting = 5;
	
	public double EndPlaceClosenessWeighting = 5;
	
	// The max number of edges we traverse from a pub if there are no pubs
	// the actual number used is MaxEdgeNumber - numberOfPLaces
	// e.g. If I have 5 'stops' then the number of edges in the search would be: MaxEdgeNumber - 5
	// this prevents an 8 stop crawl taking forever, yet a 3 stop crawl could be improved with more pubs.
	public int MaxEdgeNumber = 14;

	// how far from the start (going in the opp direction to the finish) do we look for pubs outside our catchment area
	public double DistanceFromStartAndEnd = 0.3;
	
	public transient Log Logger;
	
	public UserSettings(Log logger) {
		this.Logger = logger;
	}


	public List<DataSource> getDataSources() {
		List<DataSource> result = new ArrayList<DataSource>();
		result.add(new QypeDataSource(this.Logger));
		result.add(new YelpDataSource(this.Logger));
		return result;
	}

	public boolean WalkInRightDirection = true;
	
	public SimpleLocation getEnd() {
		return end;
	}
	
	public SimpleLocation getStart() {
		return start;
	}
	
	public void setStartAndEnd(SimpleLocation start, SimpleLocation end) {
		this.start = start;
		this.end = end;
		
		recalcMaxWalkingDistance();
	}
	
	public int getNumberOfPlaces() {
		return NumberOfPlaces;
	}
	
	public void setNumberOfPlaces(int numberOfPlaces) {
		this.NumberOfPlaces = numberOfPlaces;
		recalcMaxWalkingDistance();
	}
	
	private void recalcMaxWalkingDistance() {
		// Auto decide a good distance, smaller pubs crawls need smaller distance etc.
		
		if (start != null && end != null) {
			if (isStartToEndTooShort()) {
				this.Logger.Info("Start to end is too short, default walking distance.");
				MaxWalkingDistanceKm = this.DefaultWalkingDistance;
			} else 
				MaxWalkingDistanceKm = Helper.distance(start, end) / (NumberOfPlaces - 1);
			this.Logger.Info("Max walking distance for crawl is calculated at " + MaxWalkingDistanceKm);
		}
	}
	

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return this.user;
	}
	
	
	/**
	 * Is this distance too short we have to go in a circle?
	 */
	public boolean isStartToEndTooShort() {
		return Helper.distance(getStart(), getEnd()) <= 1.0;
	}
}
