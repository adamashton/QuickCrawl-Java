package core.dataobtainer;

import java.util.ArrayList;
import java.util.List;


import core.global.Category;
import core.global.DataSourceType;
import core.global.Place;
import core.global.Rectangle;
import core.support.Helper;
import core.support.Log;

public class YelpDataSource extends DataSource {

	private final int max = 20;
	private String searchTerm;

	private class YelpDataSourceGetter extends DataSource {
		private int max;
		private String searchTerm;
		private Rectangle rectangle;
		
		public boolean Success;
		public String ErrorMsg;
		
		public List<Place> Places;
		
		public YelpDataSourceGetter(String searchTerm, int max, Rectangle rectangle, Log logger) {
			super(logger);
			
			this.searchTerm = searchTerm;
			this.max = max;
			this.rectangle = rectangle;
		}
		
		private void getPlaces() throws Exception
		{
			List<Place> result = new ArrayList<Place>();
			
			YelpApi yelpDataSource = new YelpApi(this.logger);
			
			List<Place> origResult = yelpDataSource.searchMapBoundingBox(
					searchTerm, 
					max, 
					rectangle.topLeft, 
					rectangle.bottomRight);
					
			if (origResult.size() == max) {
				Rectangle[] rectangles = Helper.SplitRectangle(rectangle);
				
				// spawn this on two threads
				Thread[] threads = new Thread[2];
				
				// thread 1
				YelpDataSourceGetter first = new YelpDataSourceGetter(searchTerm, max, rectangles[0], this.logger);
				threads[0] = new Thread(first);
				threads[0].start();
				
				// 2
				YelpDataSourceGetter second = new YelpDataSourceGetter(searchTerm, max, rectangles[1], this.logger);
				threads[1] = new Thread(second);
				threads[1].start();
				
				// wait for them to finish
				threads[0].join();
				threads[1].join();
				
				result = new ArrayList<Place>();
				if (first.Success) {
					result.addAll(first.Places);
				} else {
					ErrorMessage = first.ErrorMessage;
				}
				
				if (second.Success) {
					result.addAll(second.Places);
				} else {
					ErrorMessage = second.ErrorMessage;
				}
				// consider a success if either one of them gets some data
				Success = first.Success || second.Success;
			} else {
				result = origResult;
			}
			
			Places = result;
		}
		
		@Override
		public void run() {
			try {
				getPlaces();
				Success = true;
			} catch (Exception e) {
				ErrorMessage = e.toString();
				Success = false;
			}
		}
		
	}
	
	public YelpDataSource(Log logger) {
		super(logger);
	}
	
	@Override
	public DataSourceType getDataSourceType() {
		return DataSourceType.Yelp;
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			searchTerm = getSearchTerm(category);
			YelpDataSourceGetter getter = new YelpDataSourceGetter(searchTerm, max, rectangle, logger);
			// the first one isn't spawned on a new thread.
			getter.run();
			Success = getter.Success;
			ErrorMessage = getter.ErrorMessage;
			if (Success) {
				Places = getter.Places;
			}
		} catch (Exception e) {
			Success = false;
			ErrorMessage = e.toString();
		} finally {
			TimeTaken = System.currentTimeMillis() - start;
		}
	}
	
	private String getSearchTerm(Category cat) throws Exception {
		if (cat == Category.Pubs)
			return "pubs";
		else if (cat == Category.Bars)
			return "bars";
		else if (cat == Category.Clubs)
			return "nightlife";
		else
			throw new Exception("not implemented");
	}
}
