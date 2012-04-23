package core.datafilter;

import java.util.ArrayList;
import java.util.List;


import core.global.Place;
import core.global.Rectangle;
import core.global.SimpleLocation;
import core.support.DistancePoint;
import core.support.Helper;
import core.support.Log;

public class DataFilter {
	
	private static List<String> invalidWords;
	
	
	/**
	 * Filter the places down to the ones inside our crawl area
	 * We also filter out places with no title, id or location
	 * @param radiusKm 
	 * @return Filtered Places
	 */
	public static List<Place> filterPlaces(SimpleLocation startOfCrawl, SimpleLocation endOfCrawl, 
			List<Place> unfilteredPlaces, Double radiusKm, Log logger)
	{
		populateInvalidWords();
		
		List<Place> result = new ArrayList<Place>();
		int filteredPlaces = 0;
		for (Place place : unfilteredPlaces) {
			boolean addPlace = true;
			
			addPlace &= place != null;
			addPlace &= !Helper.nullOrEmpty(place.Title);
			addPlace &= place.Location != null;
			addPlace &= !Helper.nullOrEmpty(place.ID);
			addPlace &= placeIsWithinBounds(startOfCrawl, endOfCrawl, place, radiusKm);
			addPlace &= placeTitleValid(place.Title);
			
			if (addPlace) {
				result.add(place);
			} else {
				filteredPlaces++;
				logger.Debug("Filtering: " + place.toString());
			}
			
		}
		
		logger.Info("Filtered: " + filteredPlaces + " places");
		return result;
	}

	private static void populateInvalidWords() {
		// use lower case words here
		invalidWords = new ArrayList<String>();
		invalidWords.add("nando");
	}

	private static boolean placeTitleValid(String title) {
		boolean result = true;
		for (String word : invalidWords) {
			if (title.toLowerCase().contains(word)) {
				result = false;
				break;
			}
		}
		return result;
	}

	private static boolean placeIsWithinBounds(SimpleLocation startOfCrawl, SimpleLocation endOfCrawl, Place place, Double radiusKm) {
		boolean result = false;
			
		// calculate distance of point to line of pubcrawl.
		double d = Helper.distanceFromLine(place.Location, startOfCrawl, endOfCrawl);			
		result = (d <= radiusKm);		
		
		if (!result) {
			result = Helper.withinCircle(startOfCrawl, place.Location, radiusKm);			
		}
		
		if (!result) {
			result = Helper.withinCircle(endOfCrawl, place.Location, radiusKm);
		}
		
		return result;
	}
}
