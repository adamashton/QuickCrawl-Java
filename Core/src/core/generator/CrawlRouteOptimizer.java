package core.generator;

import java.util.List;

import core.global.AggPlace;
import core.global.QuickCrawl;
import core.global.UserSettings;
import core.google.Directions;
import core.support.Log;

public class CrawlRouteOptimizer  {

	public static void optimize(QuickCrawl crawl, UserSettings settings) throws Exception {
		Log logger = settings.Logger;
		logger.Info("Optimizing the crawl using google directions API");
		
		// use google directins to find the best walking order of the top x
		List<AggPlace> orderedPlaces = 
			Directions.getOrderedPlaces(settings.getStart(), 
					settings.getEnd(), 
					crawl.getPlaces(),
					logger);
		
		// make the new'n with new places
		crawl = new QuickCrawl(orderedPlaces);
	}

}
