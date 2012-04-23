package core.generator;

import java.util.List;

import core.global.AggPlace;
import core.global.QuickCrawl;
import core.global.UserSettings;
import core.support.Log;

public abstract class Generator {
	UserSettings settings;
	List<AggPlace> places;
	Log logger;
	
	public Generator(UserSettings settings, List<AggPlace> places) {
		this.settings = settings;
		this.places = places;
		this.logger = settings.Logger;
	}
	
	public abstract QuickCrawl generate() throws Exception;
}
