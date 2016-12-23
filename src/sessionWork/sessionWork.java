package sessionWork;

import processing.core.*;

public class sessionWork extends PApplet {

	private String URL = "http://i.imgur.com/waHb3Ue.jpg";
	private PImage backgroundImg;
	public void setup (){
		size(200,200);
		
		backgroundImg = loadImage(URL);
	}
	public void draw(){
		backgroundImg.resize(0,height);
		image(backgroundImg,0,0);
		fill(255,209,0);
		ellipse(width/4,height/5,width/5,height/5);
	}
}
