package web;

import java.text.DecimalFormat;
import java.util.List;

import core.db.db;
import core.global.AggPlace;
import core.global.CrawlManager;
import core.global.QuickCrawl;
import core.global.SimpleLocation;
import core.global.UserSettings;
import core.support.Helper;
import core.support.Log;
import core.support.LogLevel;
import core.support.LogMsg;

public class QuickCrawlWebManager {
	private UserSettings settings;
	private CrawlManager manager;
	
	private SimpleLocation start;
	private SimpleLocation end;
	private String crawlId;
	
	private Log logger;
	
	public QuickCrawlWebManager() throws Exception {
		this.logger = new Log();		
		settings = new UserSettings(this.logger);
		manager = new CrawlManager();
	}
	
	public UserSettings getSettings() {
		return settings;
	}
	
	public String getCrawlId() {
		return crawlId;
	}
	
	public boolean isCrawlLoaded() {
		return manager.getCrawl() != null 
			&& getStart() != null
			&& this.settings.getStart() != null
			&& this.manager.getSavedSettings().getStart() != null;
	}
	
	public AggPlace getPlace(int pos) throws Exception {
		return manager.getCrawl().getPlaces().get(pos);
	}
	
	public List<String> getAllGeneratedCrawls() throws Exception {
		return db.getGeneratedCrawls();
	}
	
	public String persist(String user) throws Exception {
		this.settings.setStartAndEnd(start, end);
		manager.prepareForSerialization(this.settings);
		String id = db.Add(manager, user);
		this.logger.Info("Saved crawl to db. ID = " + id);
		
		this.crawlId = id;
		return this.crawlId;
	}
	
	public void load(String id) throws Exception {
		this.logger.Info("Loading crawl from db with id: " + id);
		manager = db.getCrawlManager(id);
		settings = manager.getSavedSettings();
		start = settings.getStart();
		end = settings.getEnd();
		this.logger = manager.getSavedSettings().Logger;
		crawlId = id;
	}
	
	public QuickCrawl getCrawl() throws Exception {
		return manager.getCrawl();
	}
	
	public String getDistance(SimpleLocation a, SimpleLocation b) {
		double result = Helper.distance(a, b);
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(result);
	}
	
	public void generate() throws Exception {
		settings.setStartAndEnd(start, end);
		manager.generate(settings);
	}
	
	public String generateWayPtsJavaScript() throws Exception {
		QuickCrawl crawl = manager.getCrawl();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < crawl.getPlaces().size(); i++) {
		    result.append("wayPts.push({ location:\"" 
		    		+ crawl.getPlaces().get(i).Location 
		    		+ "\", stopover:true }); \r\n" );
		}
	    return result.toString();
	}
	
	
	
	public void setStart(String start) throws Exception {
		this.logger.Info("Setting start to: " + start);
		// remove brackets
		start = start.replace("(", "").replace(")", "");
		
		this.start = new SimpleLocation(start);
	}
	
	public String getStart() {
		return this.start.toString();
	}
	
	public void setEnd(String end) throws Exception {
		this.logger.Info("Setting end to: " + end);
		// remove brackets
		end = end.replace("(", "").replace(")", "");
		
		this.end = new SimpleLocation(end);
	}
	
	public String getEnd() {
		return this.end.toString();
	}
	
	public void setNumberOfPlaces(String numStr) {
		settings.setNumberOfPlaces(Integer.parseInt(numStr));
	}
	
	public void setTitle(String title) {
		this.settings.setTitle(title);
	}
	
	public void setUser(String user) {
		this.settings.setUser(user);
	}
	
	public double getDistance(String a, String b) throws Exception {
		SimpleLocation aL = new SimpleLocation(a.replace("(", "").replace(")", ""));
		SimpleLocation bL = new SimpleLocation(b.replace("(", "").replace(")", ""));
		return Helper.distance(aL, bL);
	}
	
	public String getCenterOfCrawl() {
		//TODO
		return getStart().toString();
	}
	
	public String getFullLog() {
		StringBuilder result = new StringBuilder();
		if (this.logger != null) {
			for (String message : this.logger.getFullLog(LogLevel.Info)) {
				result.append(message + "<br />");
			}
		}
		return result.toString();
	}
	
}
