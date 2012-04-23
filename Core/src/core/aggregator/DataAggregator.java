package core.aggregator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import core.global.AggPlace;
import core.global.Place;
import core.support.Helper;
import core.support.Log;

/**
 * Static manager for the aggregator.
 * @author adam
 *
 */
public class DataAggregator {
	
	private List<String> uneccessaryWords;
	
	private HashMap<String, AggPlace> searchTermToPlace;
	
	private List<Place> places;	
	private List<AggPlace> aggregatedPlaces;
	
	public List<AggPlace> getAggregatedPlaces() {
		return aggregatedPlaces;
	}
	
	private Log logger;

	public DataAggregator(List<Place> places, Log logger) {
		this.logger = logger;
		this.places = places;
		this.aggregatedPlaces = new ArrayList<AggPlace>();
		searchTermToPlace = new HashMap<String, AggPlace>();

		uneccessaryWords = new ArrayList<String>();
		uneccessaryWords.add("the");
		uneccessaryWords.add("bar");
		uneccessaryWords.add("club");
		uneccessaryWords.add("pub");
		uneccessaryWords.add("public");
		uneccessaryWords.add("house");
		uneccessaryWords.add("and");
		uneccessaryWords.add("&");
	}
	
	public void aggregatePlaces() throws Exception {
		this.logger.Info("Aggregating Places");
		for (Place p : places) {
			// Step 1 - search
			String phoneSearchTerm = getPhoneNumberSearchTerm(p);
			String titleSearchTerm = getTitleSearchStr(p);
			
			AggPlace aggregatedPlace = null;
			if (!Helper.nullOrEmpty(phoneSearchTerm) && searchTermToPlace.containsKey(phoneSearchTerm)) {
				aggregatedPlace = searchTermToPlace.get(phoneSearchTerm);
				
			} else if (searchTermToPlace.containsKey(titleSearchTerm)) {
				aggregatedPlace = searchTermToPlace.get(titleSearchTerm);
			}
			
			if (aggregatedPlace != null 
					&& Helper.distance(p.Location, aggregatedPlace.Location) <= 0.5) {
				// Step 2a - aggregate 
				this.logger.Debug("Aggregating " + p + " into: " + aggregatedPlace);
				aggregatedPlace.addPlace(p);
			} else {
				// Step 2b - create new'n
				aggregatedPlace = new AggPlace();
				aggregatedPlace.addPlace(p);
				aggregatedPlaces.add(aggregatedPlace);
				
				// add to maps for quick searching
				searchTermToPlace.put(titleSearchTerm, aggregatedPlace);
				if (!Helper.nullOrEmpty(phoneSearchTerm)) {
					searchTermToPlace.put(phoneSearchTerm, aggregatedPlace);
				}
			}
		}
	}

	private String getTitleSearchStr(Place p) {
		String result = p.Title;
		
		result = replacePunctutationWithSpaces(result);
		
		// to lower
		result = result.toLowerCase();
		
		// remove '
		result = result.replace("'", "");
		
		// split by any number of space, tab, etc.
		List<String> searchWords = Arrays.asList(result.split("\\s+"));
		
		searchWords = removeUneccessaryWords(searchWords);
		searchWords = expandNumbers(searchWords);
		
		if (searchWords.size() > 0) {
			StringBuilder searchStr = new StringBuilder();
			
			// sort list into alphabetical order
			Collections.sort(searchWords);
			
			for (String word : searchWords) {
				searchStr.append(word);
			}
			
			result = searchStr.toString();
		}
		
		return result;
	}

	

	private String replacePunctutationWithSpaces(String search) {
		String result = search
		.replace(",", " ")
		.replace(".", " ")
		.replace("!", " ")
		.replace("\"", " ")
		.replace("£", " ")
		.replace("$", " ")
		.replace("%", " ")
		.replace("^", " ")
		.replace("&", " ")
		.replace("*", " ")
		.replace("(", " ")
		.replace(")", " ")
		.replace("-", " ")
		.replace("_", " ")
		.replace("=", " ")
		.replace("+", " ")
		.replace(";", " ")
		.replace(":", " ")
		//.replace("'", " ") special case
		.replace("@", " ")
		.replace("#", " ")
		.replace("~", " ")
		.replace("/", " ")
		.replace("\\", " ");
		return result;
	}

	private List<String> removeUneccessaryWords(List<String> searchWords) {
		List<String> result = new ArrayList<String>();
		
		for (String word : searchWords) {
			if (!uneccessaryWords.contains(word)) {
				result.add(word);
			}
		}
		
		return result;
	}
	
	private List<String> expandNumbers(List<String> searchWords) {
		List<String> result = new ArrayList<String>();

		for (String word : searchWords) {
			if (word.equals("1")) {
				result.add("one");
			} else if (word.equals("2")) {
				result.add("two");
			} else if (word.equals("3")) {
				result.add("three");
			} else if (word.equals("4")) {
				result.add("four");
			} else if (word.equals("5")) {
				result.add("five");
			} else if (word.equals("6")) {
				result.add("siz");
			} else if (word.equals("7")) {
				result.add("seven");
			} else if (word.equals("8")) {
				result.add("eight");
			} else if (word.equals("9")) {
				result.add("nine");
			} else 
				result.add(word);
		}
		
		return result;
	}

	private String getPhoneNumberSearchTerm(Place p) {
		String result = p.PhoneNumber;
		
		if (!Helper.nullOrEmpty(result)) {
			// remove all whitespace
			result = result.replace(" ", "").trim();
		}
		return result;
	}
}
