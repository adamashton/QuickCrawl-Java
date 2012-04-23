package core.generator;

import java.util.Comparator;
import java.util.HashMap;

import core.global.AggPlace;
import core.global.QuickCrawl;
import core.global.SimpleLocation;
import core.global.UserSettings;
import core.support.Helper;

public class QuickCrawlComparator implements Comparator<QuickCrawl> {

	private HashMap<QuickCrawl, Double> savedCrawlRatings;
	
	private BayesPlaceComparator placeComparator;
	
	private UserSettings settings;
	
	public QuickCrawlComparator(BayesPlaceComparator placeComparator, UserSettings settings) {
		savedCrawlRatings = new HashMap<QuickCrawl, Double>();
		this.placeComparator = placeComparator;
		this.settings = settings;
	}
	
	@Override
	public int compare(QuickCrawl a, QuickCrawl b) {
		
		assert a.getPlaces().size() == b.getPlaces().size();
		
		double aD = getRating(a);
		double bD = getRating(b);
		
		if (aD < bD) {
			return 1;
		} else if (bD < aD) {
			return -1;
		} else {
			return 0;
		}
	}
	
	private double getRating(QuickCrawl crawl) {
		if (!savedCrawlRatings.containsKey(crawl)) {
			savedCrawlRatings.put(crawl, getCrawlRating(crawl));
		}
		
		return savedCrawlRatings.get(crawl);
	}
	
	private double getCrawlRating(QuickCrawl crawl) {
		// place rating
		double crawlRating = getCrawlRatingRating(crawl);
		
		// total distance rating
		double totalDistanceRating = getTotalDistanceRating(crawl);
		
		// place 1 closeness to start
		double startFirstPlaceClosenessRating = getFirstPlaceClosenessRating(crawl);
		
		// place n closeness to end
		double lastPlaceEndClosenessRating = getLastPlaceEndClosenessRating(crawl); 
		
		double averageDistanceRating = getAverageDistanceRating(crawl);
		
		/*System.out.println(Helper.twoDecimals(crawlRating) 
				+ "/" + Helper.twoDecimals(totalDistanceRating) 
				+ "/" + Helper.twoDecimals(startFirstPlaceClosenessRating)
				+ "/" + Helper.twoDecimals(lastPlaceEndClosenessRating)
				+ "/" + Helper.twoDecimals(averageDistanceRating));*/

		double result = crawlRating 
			+ totalDistanceRating 
			+ startFirstPlaceClosenessRating 
			+ lastPlaceEndClosenessRating
			+ averageDistanceRating;
		
		return result;
	}
	
	private double getLastPlaceEndClosenessRating(QuickCrawl crawl) {
		SimpleLocation last = crawl.getPlaces().get(crawl.getPlaces().size() - 1).Location;
		double rating = Helper.distance(last, this.settings.getEnd());
		rating = this.settings.MaxWalkingDistanceKm - rating;
		assert rating > 0 : rating;
		rating = rating * this.settings.EndPlaceClosenessWeighting;
		return rating;
	}

	private double getFirstPlaceClosenessRating(QuickCrawl crawl) {
		double rating = Helper.distance(this.settings.getStart(), crawl.getPlaces().get(0).Location);		
		// less is more ;-)
		rating = this.settings.MaxWalkingDistanceKm - rating;
		assert rating > 0 : rating;
		rating = rating * this.settings.EndPlaceClosenessWeighting;		
		return rating;
	}
	
	/**
	 * A crawl is considered good if there is a nice even spacing between all the pubs :-)
	 */
	private double getAverageDistanceRating(QuickCrawl crawl) {
		HashMap<Integer, Double> distanceBetween = new HashMap<Integer, Double>(); 
		
		// get distance for each place
		int size = crawl.getPlaces().size();
		double totalDistance = 0.0;
		for (int i=0; i<=size; i++) {
			double distance;
			
			if (i == 0) {
				distance = Helper.distance(this.settings.getStart(), crawl.getPlaces().get(0).Location);
			} else if (i < size) {
				distance = Helper.distance(
						crawl.getPlaces().get(i - 1).Location, 
						crawl.getPlaces().get(i).Location);
			} else {
				assert i == size;
				distance = Helper.distance(
						crawl.getPlaces().get(i - 1).Location,
						this.settings.getEnd());
			}

			distanceBetween.put(i, distance);
			
			totalDistance += distance;
		}
		
		double avgDistance = totalDistance / size;
		
		// calculate differences from avg.
		double totalDifference = 0.0;
		
		for (int i=0; i<=size; i++) {
			double diff = avgDistance - distanceBetween.get(i);
			if (diff < 0) {
				diff = diff * -1;
			}
			
			totalDifference += diff;
		}
		
		// now this totalDiff, a high total diff is bad so lets flip it.
		double result = totalDistance - totalDifference;
		result = result * this.settings.DistanceDifferenceWighting;
		return result;
	}

	/**
	 * Get our rating based on the place ratings for this crawl.
	 */
	private double getCrawlRatingRating(QuickCrawl crawl) {
		double result = 0.0;
		for (AggPlace p : crawl.getPlaces()) {
			// get place rating
			result += getPlaceRating(p);			
		}
		return result * this.settings.AverageRatingWeight;
	}
	
	
	/**
	 * Get a rating based on how far you have to walk.
	 * @param crawl
	 * @return
	 */
	private double getTotalDistanceRating(QuickCrawl crawl) {
		
		// calculate rating for start - first place
		double result = Helper.distance(this.settings.getStart(), crawl.getPlaces().get(0).Location);
		result = this.settings.MaxWalkingDistanceKm - result;
		assert result >= 0;
		int size = crawl.getPlaces().size();
		
		for (int i=0; i<size; i++) {
			double distanceD;
			AggPlace p = crawl.getPlaces().get(i);
			
			// calc distance rating
			if (i >= 0 && i < (size - 1)) {
				distanceD = Helper.distance(p.Location, crawl.getPlaces().get(i+1).Location);				
			} else {
				assert i == size - 1;
				distanceD = Helper.distance(p.Location, this.settings.getEnd());
			} 
			
			// smaller the distance the better...
			distanceD = this.settings.MaxWalkingDistanceKm - distanceD;
			assert distanceD >= 0;
			result += distanceD;
		}
		
		return result * this.settings.TotalDistanceWeighting;
	}
	

	private double getPlaceRating(AggPlace place) {
		return placeComparator.getRating(place);
	}
}
