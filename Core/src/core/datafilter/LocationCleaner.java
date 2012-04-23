package core.datafilter;

import java.util.HashMap;
import java.util.List;

import core.global.AggPlace;
import core.global.SimpleLocation;

public class LocationCleaner {
	private HashMap<SimpleLocation, AggPlace> placeToLocation;
	
	private List<AggPlace> places;
	
	private final double offset = 0.00000001;
	
	public LocationCleaner(List<AggPlace> places) {
		this.places = places;
	}
	
	public void fixLocationClashes() {
		
		placeToLocation = new HashMap<SimpleLocation, AggPlace>();
		
		for (AggPlace p : places) {
			
			while (placeToLocation.containsKey(p)) {
				// we have a clash
				p.Location.latitude += offset;
				p.Location.longitude += offset;
			}
			
			placeToLocation.put(p.Location, p);
		}
	}
}
