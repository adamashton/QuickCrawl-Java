package core.generator;

import java.util.Collections;
import java.util.List;

import core.global.AggPlace;
import core.global.QuickCrawl;
import core.global.UserSettings;

public class QuickCrawlGenerator extends Generator {
	
	public QuickCrawlGenerator(UserSettings settings,
			List<AggPlace> places) {
		super(settings, places);
	}

	@Override
	public QuickCrawl generate() throws Exception {
		logger.Info("Generating the potential crawls");
		int iteration = 0;
		QuickCrawl result = generateCrawl(settings, places);
		
		while (result == null && iteration < 5) {
			logger.Warning("Could not generate pub crawl with current settings so increasing the max walking distance.");
			// try again, this time without the restriction of walking in the right direction
			settings.WalkInRightDirection = false;
			// and increasing the walking distance
			settings.MaxWalkingDistanceKm *= 1.1;
			result = generateCrawl(settings, places);
			iteration++;
			
			if (iteration == 5 && result == null && settings.getNumberOfPlaces() > 1) {
				// if we have reached max iteration
				// try again with 1 less pub until we get down to 2 pubs then give up
				logger.Warning("Still struggling to generate! reducing the number of places and trying again...");
				settings.setNumberOfPlaces(settings.getNumberOfPlaces() - 1);
				iteration = 0;
			}
		}
		
		if (result == null) {
			throw new Exception("Could not generate a pub crawl based on your settings.");
		}
		
		return result;
	}

	private QuickCrawl generateCrawl(UserSettings settings, List<AggPlace> places) throws Exception {

		// Step 1 - Construct all potential crawls
		BayesPlaceComparator placeComparator = new BayesPlaceComparator(places);
		CrawlsGenerator generator = new CrawlsGenerator(settings, places, placeComparator);
		generator.generate();
		List<QuickCrawl> crawls = generator.getCrawls(); 
		// we should have some crawls
		if (crawls.size() == 0) {
			//uh-oh...  increase the walking distance and stuff
			return null;
		}
		logger.Info("Generated " + crawls.size() + " potential crawls, finding best one.");
		
		// Step 2 - Sort them real good :)
		Collections.sort(crawls, new QuickCrawlComparator(placeComparator, settings));
		
		// Step 3 - your pub crawl, sir!
		QuickCrawl result = crawls.get(0);
		
		// Step 4 - Use google maps API to optimize the route ordering
		// TODO: bit buggy, can sometimes move starting pub miles away
		//CrawlRouteOptimizer.optimize(result);
		
		// Step 5 - Obtain reviews for the places
		logger.Info("Downloading reviews for selected places.");
		for (AggPlace p : result.getPlaces()) {
			p.downloadReviews(logger);
		}
		
		logger.Info("Fin: " + result.toString());
		
		return result;
	}
}
