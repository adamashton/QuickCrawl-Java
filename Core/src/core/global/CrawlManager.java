package core.global;

import java.io.Serializable;
import java.util.List;

import core.aggregator.DataAggregator;
import core.datafilter.DataFilter;
import core.datafilter.LocationCleaner;
import core.dataobtainer.DataObtainerManager;
import core.dataobtainer.DataSource;
import core.generator.Generator;
import core.generator.QuickCrawlGenerator;
import core.support.Helper;
import core.support.Log;

/**
 * Given a start and end point of a pubcrawl
 * It calculates the bounds to get the places and then
 * downloads, filter and aggregate the data.
 * @author adam
 *
 */
public class CrawlManager implements Serializable {
	
	private static final long serialVersionUID = -8134333299203580228L;

	private QuickCrawl crawl; // the crawl !
	
	transient private List<AggPlace> availablePlaces;
	
	private UserSettings savedSettings;

	public UserSettings getSavedSettings() {
		return savedSettings;
	}

	
	public List<AggPlace> getAvailablePlaces() {
		return availablePlaces;
	}
	
	public CrawlManager()
	{
		crawl = null;
		availablePlaces = null;
	}
	
	public QuickCrawl getCrawl() {
		return crawl;
	}
	
	/**
	 * Download, Filter, Aggregate & Generate :)
	 * @throws Exception 
	 */
	public void generate(UserSettings settings) throws Exception
	{
		if (availablePlaces == null || settingsUpdated(settings)) {
			// (re)download, filter, aggregate
			obtainPlaces(settings);
		}
				
		// generate the crawl
		Generator generator = new QuickCrawlGenerator(settings, availablePlaces);
		crawl = generator.generate();
		
		// fix any clashing locations - may edit some of the place locations if the clash
		LocationCleaner lc = new LocationCleaner(crawl.getPlaces());		
		lc.fixLocationClashes();
		
		this.savedSettings = settings; // save settings 
	}
	
	/**
	 * Updates to new settings. Call generate to generate the new pub crawl.
	 * If the locations or radius haven't changed the places are not redownloaded when you call generate!
	 * @param settings
	 */
	public boolean settingsUpdated(UserSettings settings) {
		boolean result = false;
		if (savedSettings.DistanceFromLineKm != settings.DistanceFromLineKm
			|| savedSettings.getStart() != settings.getStart()
			|| savedSettings.getEnd() != settings.getEnd()) {
			// set the place data to be redownloaded
			availablePlaces = null;
			result = true;
		}
		return result;
	}
	
	public void obtainPlaces(UserSettings settings) throws Exception {
		// Step 0 - Work out what area we are working with
		Rectangle largeRectangle = Helper.CreateRectangle(
				settings.getStart(), settings.getEnd(), settings.DistanceFromStartAndEnd);
		
		// Step 1 - Download the data
		DataObtainerManager dom = new DataObtainerManager(largeRectangle, settings.Category, settings.Logger);
		for (DataSource ds : settings.getDataSources()) {
			dom.addDataSource(ds);
		}		
		List<Place> places = dom.downloadPlaces();
		
		// Step 2 - Filter
		places = DataFilter.filterPlaces(settings.getStart(), 
				settings.getEnd(), 
				places, 
				settings.DistanceFromLineKm, 
				settings.Logger);
		
		// Step 3 - Aggregate
		DataAggregator aggregator = new DataAggregator(places, settings.Logger);
		aggregator.aggregatePlaces();
		availablePlaces = aggregator.getAggregatedPlaces();
		
		if (availablePlaces.size() < settings.getNumberOfPlaces()) {
			throw new Exception("The number of places returned (" 
					+ availablePlaces.size() + ") was not enough for the crawl ("
					+ settings.getNumberOfPlaces() + ").");
		}
	}	
	
	public void prepareForSerialization(UserSettings settings) {
		this.savedSettings = settings;
	}
	
	
}