package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;
	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	// The map
	private UnfoldingMap map;
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
//	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/quakeml.php";
	// colors : 
	private final int white  = color(255,255,255);
	private final int yellow = color(255, 255, 0);
	private final int red    = color(255,0,0);
	private final int blue   = color(0,0,255);
    private final int black  = color(0,0,0);
    private final int orange = color(204, 102, 0);
	
    public void setup() {
		size(1350, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 900, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 1150, 500, new Microsoft.RoadProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
	 // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    
	    //TODO: Add code here as appropriate
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    for (PointFeature earthquake : earthquakes){
	    	SimplePointMarker earthquakeMarker = createMarker(earthquake);
	    	Object magObj = earthquake.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	if(mag<4){
	    		earthquakeMarker.setRadius(8);
	    		earthquakeMarker.setColor(blue);
	    	}
	    	else if(mag>=4 && mag<=4.9){
	    		earthquakeMarker.setRadius(10);
	    		earthquakeMarker.setColor(yellow);
	    	}
	    	else if(mag>4.9 && mag<=5.9){
	    		earthquakeMarker.setRadius(12);
	    		earthquakeMarker.setColor(orange);
	    	}
	    	else if(mag>5.9 && mag<=6.9){
	    		earthquakeMarker.setRadius(14);
	    		earthquakeMarker.setColor(red);
	    	}
	    	else if(mag>6.9){
	    		earthquakeMarker.setRadius(14);
	    		earthquakeMarker.setColor(black);
	    	}
	    	markers.add(earthquakeMarker);
	    }
	    // These print statements show you 
        // (1) all of the relevant properties in the features
        // (2) how to get one property and use it
//	    if (earthquakes.size() > 0) {
//	    	PointFeature f = earthquakes.get(0);
//	    	System.out.println(f.getProperties());
//	    	Object magObj = f.getProperty("magnitude");
//	    	float mag = Float.parseFloat(magObj.toString());
//	    	// PointFeatures also have a getLocation method
//	    }
	    map.addMarkers(markers);
	    
	    
	    
	}
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{	
		
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation());
	}
	
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}
	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey()
	{	fill(white);
		rect(25,50,180,400);
		fill(50);
		text("Earthquake Key", 50, 70);
		
		fill(black);
		ellipse(45,110,14,14);
		fill(50);
		text("7.0+ magnitude",60,115);
		
		fill(red);
		ellipse(45,170,14,14);
		fill(50);
		text("6.0+ Magnitude",60,175);
		
		fill(orange);
		ellipse(45,230,12,12);
		fill(50);
		text("5.0+ Magnitude",60,235);
		
		fill(yellow);
		ellipse(45,290,10,10);
		fill(50);
		text("4.0+ Magnitude",60,295);
		
		fill(blue);
		ellipse(45,350,8,8);
		fill(50);
		text("Below 4.0",60,355);
		// Remember you can use Processing's graphics methods here
	}
	
}











