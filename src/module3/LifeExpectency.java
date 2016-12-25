package module3;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;
import java.util.Map;
//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;



public class LifeExpectency extends PApplet{
	
	UnfoldingMap map;
	Map<String,Float> lifeExpByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;
	
	public void setup () {
		size(800,600, OPENGL);
		map = new UnfoldingMap(this,50,50,700,500, new Microsoft.RoadProvider());
		MapUtils.createDefaultEventDispatcher(this,map);
		lifeExpByCountry = loadLifeExpectancyFromCSV("/home/khalil/eclipse/UCSDUnfoldingMaps/data/LifeExpectancyWorldBank.csv");
		countries = GeoJSONReader.loadData(this,"/home/khalil/eclipse/UCSDUnfoldingMaps/data/countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		shadeCountries();
	}
	public void draw(){
		map.draw();
	}


private Map<String,Float> loadLifeExpectancyFromCSV(String fileName){
Map<String,Float> lifeExpMap = new HashMap<String,Float>();
String[] rows = loadStrings(fileName);
for (String row : rows){
	String[] columns = row.split(",");
	String data = columns[5];
//	System.out.println(isNumeric(data));
//	if(isNumeric(data)){
	if(data.length()>4 && !data.equals("..")){
		System.out.println(data);
	float value = Float.parseFloat(columns[5]);
	lifeExpMap.put(columns[4], value);
	}
	
}
return lifeExpMap;	
}

private void shadeCountries () {
	for (Marker marker : countryMarkers){
		String countryId = marker.getId();
		if(lifeExpByCountry.containsKey(countryId)){
			float lifeExp = lifeExpByCountry.get(countryId);
			int colorLevel = (int) map(lifeExp,40,90,10,255);
			marker.setColor(color(255-colorLevel,100,colorLevel));
		}
		else{
			marker.setColor(color(150,150,150));
		}
	}
}

}


