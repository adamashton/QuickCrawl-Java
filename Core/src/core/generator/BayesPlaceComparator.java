package core.generator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import core.global.AggPlace;

public class BayesPlaceComparator implements Comparator<AggPlace> {
	private List<AggPlace> places;
	private HashMap<AggPlace, Double> ratings;
	
	// is the average number of ratings for all books shown (where RateCount > 0)
	private double avgNumberOfRatingsAll = 0.0;
	
	// is the average unweighted rating for all books shown (where RateCount > 0)
	private double avgRatingAll = 0.0;
	
	public BayesPlaceComparator(List<AggPlace> places) {
		this.places = places;
		calculateVariables();
	}
	
	private void calculateVariables() {
		ratings = new HashMap<AggPlace, Double>();
		
		avgNumberOfRatingsAll = 0.0;
		avgRatingAll = 0.0;
		
		for (AggPlace p : places) {
			if (p.NumberOfRatings > 0) {
				avgNumberOfRatingsAll += p.NumberOfRatings;
				avgRatingAll += p.AverageRating;
			}
		}
		
		avgNumberOfRatingsAll = avgNumberOfRatingsAll / places.size();
		avgRatingAll = avgRatingAll / places.size();
	}
	
	@Override
	public int compare(AggPlace a, AggPlace b) {
		double aRating = getRating(a);
		double bRating = getRating(b);
		
		if (aRating > bRating)
			return -1;
		else if (bRating > aRating)
			return 1;
		else
			return 0;
	}

	public double getRating(AggPlace place) {
		if (!ratings.containsKey(place)) {
			double rating = 0.0;
			
			if (place.NumberOfRatings > 0) {
				rating = (avgNumberOfRatingsAll * avgRatingAll) + (place.NumberOfRatings * place.AverageRating);
				rating = rating / (place.NumberOfRatings + avgNumberOfRatingsAll);
			}
			
			ratings.put(place, rating);
		}
		
		return ratings.get(place);
	}
}
