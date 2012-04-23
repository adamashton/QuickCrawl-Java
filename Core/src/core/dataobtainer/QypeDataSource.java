package core.dataobtainer;

import java.util.ArrayList;
import java.util.List;

import core.global.Category;
import core.global.DataSourceType;
import core.global.Place;
import core.global.Rectangle;
import core.json.JSONObject;
import core.support.Helper;
import core.support.Log;

 public class QypeDataSource extends DataSource { 
	 
	private class QypeDataSourceGetter extends DataSource {
		public List<Place> Places;
		
		private Rectangle rectangle;
		private Category category;
		
		public boolean Success;
		public String ErrorMessage;
		
		public QypeDataSourceGetter(Rectangle rectangle, Category category, Log logger) {
			super(logger);
			
			this.rectangle = rectangle;
			this.category = category;
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
		
		private void getPlaces() throws Exception {
			String url = QypeApiUrlBuilder.getPlacesInRectangle(
					rectangle.bottomLeft.toString(), 
					rectangle.topRight.toString(), 
					QypeApiHelper.getCategoryCode(category));
			
			JSONObject startDocument = QypeApiHelper.getFirstDocument(url, logger);
			int totalEntries = QypeApiHelper.getTotalEntries(startDocument);
			
			ArrayList<Place> result = new ArrayList<Place>();
			
			if (totalEntries == 100) {
				// we're bound by qype's upper limit.
				// do it again with 2 smaller rectangles on diff threads
				Rectangle[] rectangles = Helper.SplitRectangle(rectangle);
				Thread[] threads = new Thread[2];
				
				// 1
				QypeDataSourceGetter first = new QypeDataSourceGetter(rectangles[0], category, logger);
				threads[0] = new Thread(first);
				threads[0].start();
				
				// 2
				QypeDataSourceGetter second = new QypeDataSourceGetter(rectangles[1], category, logger);
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
				
				Success = first.Success || second.Success;
			} else {
				result.addAll(actuallyDownloadPlaces(startDocument, totalEntries));
			}
			
			Places = result;
		}
		
		private List<Place> actuallyDownloadPlaces(JSONObject startDocument, int totalEntries) throws Exception {
			int count = 0;
			
			List<JSONObject> documents = new ArrayList<JSONObject>();
			documents.add(startDocument);
			documents.addAll(QypeApiHelper.getRestOfDocumentChain(startDocument, logger));

			List<Place> result = new ArrayList<Place>();
			for (JSONObject document : documents) {
				result.addAll(QypeApiHelper.getPlaces(document, logger));
			}

			if (totalEntries != result.size()) {
				logger.Warning("Number of entries in start doc differs from actually processed. " 
					+ totalEntries + "//" + count);
			}
			
			return result;
		}
	}
	
	 
	public QypeDataSource(Log logger) {
		super(logger);
	}
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		
		try {
			QypeDataSourceGetter getter = new QypeDataSourceGetter(rectangle, category, logger);
			getter.run(); // this one isn't spawned on a new thread
			Success = getter.Success;
			ErrorMessage = getter.ErrorMessage;
			if (Success) {
				Places = getter.Places;
			}
		} catch (Exception e) {
			Places = null;
			Success = false;
			ErrorMessage = e.toString();
		} finally {
			TimeTaken = System.currentTimeMillis() - start;
		}
	}

	@Override
	public DataSourceType getDataSourceType() {
		return DataSourceType.Qype;
	}
}
