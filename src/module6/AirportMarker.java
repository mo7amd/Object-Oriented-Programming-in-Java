package module6;

import java.util.List;
import java.util.ArrayList;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development MOOC team
 * @author mohamed khaled khalil
 */
public class AirportMarker extends CommonMarker implements Comparable<AirportMarker>{
	public List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		routes = new ArrayList<SimpleLinesMarker>();
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		int outRoutes = routesNum();
		if (outRoutes < 10) {
			pg.fill(0, 255, 0);
			pg.stroke(204, 255, 255);
		} else if (outRoutes > 30) {
			pg.fill(0, 0, 255);
			pg.stroke(0, 51, 51);
		} else {
			pg.fill(255,0, 0);
			pg.stroke(0, 153, 153);
		}
		pg.ellipse(x, y, 8, 8);
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		// show rectangle with title
		String title = getName() + " " + getCode() + " " + getCity() + " " + getCountry();
		
		pg.pushStyle();
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);
		
		
		pg.popStyle();
		// show routes	
	}
	
	public int compareTo(AirportMarker marker) 
	{return this.routesNum()>marker.routesNum() ? -1 : this.routesNum() == marker.routesNum() ? 0 : 1;}
	
	public String toString() {
	
		String title = getName() + " " + getCode() + " " + getCity() + " " + getCountry() + " Has " + routesNum() + " routes.";
		return title;
	}
	
//	*************************** getters **********************************
	public void addRoute(SimpleLinesMarker simpleLine) {
		routes.add(simpleLine);
	}
	
	public String getName() {
		return getProperty("name").toString();
	}
	
	public String getCode() {
		return getProperty("code").toString();
	}
	
	public String getCity() {
		return getProperty("city").toString();
	}
	
	public String getCountry() {
		return getProperty("country").toString();
	}
	
	private int routesNum() {
		return routes.size();
	}
}