package core.dataobtainer;

import java.util.List;

import core.global.Category;
import core.global.DataSourceType;
import core.global.Place;
import core.global.Rectangle;
import core.support.Log;

public abstract class DataSource implements Runnable {
	public List<Place> Places;
	public boolean Success;
	public String ErrorMessage;
	public long TimeTaken;
	
	protected Rectangle rectangle;
	protected Category category;
	
	protected Log logger;
	
	public DataSource(Log logger)
	{
		this.logger = logger;
	}
	
	public void initialise(Rectangle rectangle,
			Category category) {
		this.rectangle = rectangle;
		this.category = category;
	}
	
	protected DataSourceType dataSourceType;
	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}
}
