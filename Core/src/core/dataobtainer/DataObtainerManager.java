package core.dataobtainer;

import java.util.ArrayList;
import java.util.List;

import core.global.Category;
import core.global.Place;
import core.global.Rectangle;
import core.support.Log;

/**
 * Main entry point to get data from different data sources.
 * @author adam
 */
public class DataObtainerManager {
	
	private Category category;
	private Rectangle rectangle;
	private Log logger;
	
	public DataObtainerManager(Rectangle rectangle, Category category, Log logger)
	{
		this.rectangle = rectangle;
		this.category = category;
		this.logger = logger;
	}
	
	private List<DataSource> dataSources = new ArrayList<DataSource>();
	
	public List<Place> downloadPlaces() throws Exception
	{
		if (dataSources.size() == 0) {
			throw new Exception("There are no data sources to download from.");
		}
		
		// using a diff threads for each data source
		int sourceCount = dataSources.size();
		Thread[] threads = new Thread[sourceCount];
		int i=0;
		for (DataSource source : dataSources) {
			threads[i] = new Thread(source);
			logger.Info("Obtaining data from " + source.getDataSourceType());
			threads[i].start();
			i++;
		}
		
		// wait for each thread to finish.
		for (i=0; i<sourceCount; i++) {
			threads[i].join();
		}
		
		boolean allFailed = true;
		ArrayList<Place> result = new ArrayList<Place>();
		for (DataSource source : dataSources) {
			if (source.Success) {
				logger.Info("Time taken for " + source.getDataSourceType() + ": " + source.TimeTaken + "ms");
				allFailed = false;
				List<Place> placeResults = source.Places;
				if (source.Places.size() == 0) {
					logger.Warning(source.getDataSourceType() + " returned 0 results.");
				}
				
				result.addAll(placeResults);				
			} else {
				logger.Error("Could not download from source: " + source.getDataSourceType());
				logger.Error("Message: " + source.ErrorMessage);
			}
		}
		
		if (allFailed) {
			String message = "All data sources failed to download place content.\n";
			for (DataSource d : dataSources) {
				message += d.getDataSourceType() + ": " + d.ErrorMessage + "\n";
			}
			throw new Exception(message);
		}
		
		logger.Info("Returned a total of " + result.size() + "places");
		return result;
	}
	
	public void addDataSource(DataSource ds) 
	{
		ds.initialise(rectangle, category);
		dataSources.add(ds);		
	}
	
	public List<DataSource> getDataSources() {
		return dataSources;
	}
}
