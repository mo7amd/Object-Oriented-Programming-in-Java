package module6;

import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development MOOC team
 * @author mohamed khaled khalil
 */
public class AirportMap extends PApplet {
	
	private static final long serialVersionUID = 1L;
	UnfoldingMap map;
	private List<Marker> airportList;
	private List<Marker> routeList;
	
	private HashMap<Integer, AirportMarker> airportsRoutes;
	private HashMap<Integer, Location> airports;
	
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {
		// setting up PAppler
		size(1250,850, OPENGL);
		// setting up map and default events
		map = new UnfoldingMap(this, 250, 20, 850, 650, new Microsoft.RoadProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		// get features from airport data
		List<PointFeature> AirportsFeatures = ParseFeed.parseAirports(this, "/home/khalil/eclipse/UCSDUnfoldingMaps/data/airports.dat");
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "/home/khalil/eclipse/UCSDUnfoldingMaps/data/routes.dat");
		// list for markers, hashmap for quicker access when matching with routes
		airports = new HashMap<Integer, Location>();
		airportsRoutes = new HashMap<Integer, AirportMarker>();
		routeList = new ArrayList<Marker>();
		airportList = new ArrayList<Marker>();
		//********************************** create markers from features *******
		for(PointFeature feature : AirportsFeatures) {
//			create marker to each airport on the data file
			AirportMarker marker = new AirportMarker(feature);
			marker.setRadius(3);
			airportList.add(marker);
//			*********************************************************************
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			airportsRoutes.put(Integer.parseInt(feature.getId()), marker);
		}
		
		for(ShapeFeature route : routes) {
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			SimpleLinesMarker simpleLine = new SimpleLinesMarker(route.getLocations(), route.getProperties());
//			System.out.println(route.getProperties());
			simpleLine.setHidden(true);
			routeList.add(simpleLine);
			if (airportsRoutes.containsKey(source) && airportsRoutes.containsKey(dest)) {
				airportsRoutes.get(source).addRoute(simpleLine);
				airportsRoutes.get(dest).addRoute(simpleLine);
			}
		}
//****************** sort the airports according the highest number of routes to them **************
		sortAndPrint(10);
//******************************* add all markers to the map **************************************
		map.addMarkers(routeList);
		map.addMarkers(airportList);
		
	}
	
	public void draw() {
		background(0,255,255);
		map.draw();
		addKey();
		
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null)	return;
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			setInitialHiddenMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkMarkersForClick();
		}
	}
	
	private void setInitialHiddenMarkers() {
		for (Marker marker : airportList) {
			marker.setHidden(false);
		}
		
		for (Marker marker : routeList) {
			marker.setHidden(true);
		}
	}
	
	private void checkMarkersForClick() {
		if (lastClicked != null) return;
		for (Marker marker : airportList) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)marker;
				for (Marker airport : airportList) {
					if (airport != lastClicked  ) {
						airport.setHidden(true);
					}
				}
				for (Marker route : ((AirportMarker)lastClicked).routes) {
					route.setHidden(false);
					int source = Integer.parseInt((String)route.getProperty("source"));
//					int dest = Integer.parseInt((String)route.getProperty("destination"));
//					System.out.println(airportsRoutes.get(source).getCode());
					for(Marker airport : airportList){
						if(airport.getProperty("code") == airportsRoutes.get(source).getCode()){
							airport.setHidden(false);
						}
					}
				}
				return;
			}
		}
	}
	
	private void sortAndPrint(int numToPrint) {
		AirportMarker[] markerArray = airportList.toArray(new AirportMarker[airportList.size()]);
		Arrays.sort(markerArray);
		int ActualNumToPrint = numToPrint >= markerArray.length ? markerArray.length : numToPrint;
		for (int i=0;i<ActualNumToPrint;i++){
			System.out.println((i+1) +" ) " + markerArray[i].toString());
		}
	}
	
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255,255,255);
		
		int xbase = 20;
		int ybase = 20;
		
		rect(xbase, ybase, 150, 200);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(14);
		text("Airport Key",xbase+13, ybase+13);
		
		fill(0, 255, 0);
		stroke(204, 255, 255);
		ellipse(xbase+8,ybase+52,10,10);
		
		fill(0, 0, 255);
		stroke(0, 153, 153);
		ellipse(xbase+8,ybase+82,10,10);
		
		fill(255,0,0);
		stroke(0, 51, 51);
		ellipse(xbase+8,ybase+112,10,10);

		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("less than 10 routes", xbase+20, ybase+50);
		text("less than 30 routes", xbase+20, ybase+80);
		text("more than30 routes", xbase+20, ybase+110);
		
	}

}