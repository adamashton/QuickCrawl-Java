package core.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import core.global.AggPlace;
import core.global.QuickCrawl;
import core.global.SimpleLocation;
import core.global.UserSettings;
import core.support.Helper;

class CrawlsGenerator {
	private UserSettings settings;
	private List<AggPlace> places;
	private List<QuickCrawl> potentialCrawls;
	
	// each place has a number of edges, i.e. the other places that place can reach
	// given the max walking distance
	private HashMap<AggPlace, List<AggPlace>> edges;
	
	// we save the comparator because it can cache some calculations :)
	private BayesPlaceComparator comparator;
	
	private Stack<AggPlace> stack; //the stack used when traversing the nodes
	
	public CrawlsGenerator(UserSettings settings, List<AggPlace> places, BayesPlaceComparator placeComparator) {
		this.settings = settings;
		this.places = places;
		edges = new HashMap<AggPlace, List<AggPlace>>();
		potentialCrawls = new ArrayList<QuickCrawl>();
		comparator = placeComparator;
	}
	
	public List<QuickCrawl> getCrawls() {
		return potentialCrawls;
	}
	
	public void generate() throws Exception {
		// perform a DFS from the start node trying to reach the end node
		// this creates a number of crawls
		// only the crawls that successfully get from start to end nodes are added.
		List<AggPlace> startNodes = getStartingEdges();
		stack = new Stack<AggPlace>();
		int count = 0;
		for (AggPlace start : startNodes) {
			assert Helper.distance(start.Location, settings.getStart()) < settings.MaxWalkingDistanceKm;
			count++;
			visit(start);
		}
	}
	
	private List<AggPlace> getStartingEdges() {
		List<AggPlace> result = new ArrayList<AggPlace>();
		for (AggPlace place : places) {
			if (withinWalkingDistance(settings.getStart(), place.Location)) {
				result.add(place);
			}
		}
		
		return MaxEdges(result);
	}

	/**
	 * Takes the top x places based on the number of edges we can allow for the search.
	 */
	private List<AggPlace> MaxEdges(List<AggPlace> places) {
		// TODO: remove this and sort the whole of places once. Ensure the get Edges methods take from the top first.
		Collections.sort(places, comparator);
		int edgeNumber = settings.MaxEdgeNumber - settings.getNumberOfPlaces();
		if (places.size() <= edgeNumber) {
			return places;
		} else {
			return places.subList(0, edgeNumber);
		}
	}

	private void visit(AggPlace toVisit) {		
		if (stack.contains(toVisit))
			return;
		
		stack.push(toVisit);
		
		int size = stack.size();
		if (size == settings.getNumberOfPlaces() && withinWalkingDistance(toVisit.Location, settings.getEnd())) {
			// we have found a crawl :)
			List<AggPlace> places = new ArrayList<AggPlace>();
			for (int i = 0; i < size; i++) {
				places.add(stack.elementAt(i));
			}
		
			QuickCrawl crawl = new QuickCrawl(places);
			potentialCrawls.add(crawl);
		} else if (size < settings.getNumberOfPlaces()) {
			// keep looking if we haven't gone over the size
			for (AggPlace node : getEdges(toVisit)) {
				visit(node);
			}
		}
		
		stack.pop();
	}
	
	private List<AggPlace> getEdges(AggPlace node) {
		if (!edges.containsKey(node)) {
			// find the edges
			List<AggPlace> nodeEdges = new ArrayList<AggPlace>();
			
			for (AggPlace place : places) {
				// nodes can't visit themselves
				if (!node.equals(place)
						&& withinWalkingDistance(node.Location, place.Location)
						&& walkingInTheRightDirection(node.Location, place.Location)) {
					nodeEdges.add(place);
				}
			}
			
			edges.put(node, MaxEdges(nodeEdges));
		}
		
		return edges.get(node);
	}
	
	private boolean walkingInTheRightDirection(SimpleLocation a,
			SimpleLocation b) {
		// TODO Using the start and end nodes
		// is the walk from a -> b in the right direction 
		// some maths here about finding gradient and direction of travel
		
		// we only care about if we're walking inthe right direction if it is a straight line crawl
		boolean result = true;
		if (settings.WalkInRightDirection || !settings.isStartToEndTooShort()) {
		// in the time being let's do that b must be closer to end than a
			result = Helper.distance(b, settings.getEnd()) <= Helper.distance(a, settings.getEnd());
		}
		return result;
	}

	private boolean withinWalkingDistance(SimpleLocation a, SimpleLocation b) {
		double distanceKm = Helper.distance(a, b);
		return distanceKm <= settings.MaxWalkingDistanceKm;
	}
	
}
