package com.teasearch.animation.ingress.mu.movie;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import com.teasearch.animation.Animable;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.clips.Animate;
import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.mu.movie.MUMovieSceneD.SceneD;

public class MUMovieSceneC implements Animable {

	public static void main(String[] args) {
		
		int imageWidth = 2560; 
		int imageHeight = 1920; 
		double frameRate = 25;
		boolean scale = true;

//		int imageWidth = 256; 
//		int imageHeight = 192; 
//		double frameRate = 4;
//		boolean scale = false;

		double fieldCentreX=0;
		double fieldCentreY=0; 
		double fieldRadius=600; 

		String filePrefix = "sceneC"; 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/MU/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		MUMovieSceneC sceneC = new MUMovieSceneC();
		new Animate(sceneC, imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate, scale).makeClip();
	}

	public MUMovieSceneC() {
//		double duration1 = new SceneD(new Color(129,129,129)).getAnimation().getDuration();
//		double duration2 = new SceneD(new Color(129,129,129), true).getAnimation().getDuration();
//		this.duration1 = duration1 + 40 - 7.5;
	}
	@Override
	public double getDuration() {
		return 248;
	}

	Animable scene1 = new SceneD(new Color(129,129,129)).getAnimation(false);
	Animable scene2 = new SceneD(new Color(0xff69b4)).getAnimation(false);
	Animable scene3 = new SceneD(Faction.YOGURT.getColor()).getAnimation(false);
	Animable scene4 = new SceneD(Faction.CREME_TANGERINE.getColor()).getAnimation(false);
	Animable scene5 = new SceneD(Faction.CREME_TANGERINE.getColor(), true).getAnimation(false);
	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		if (t < 5.5) {
			scene1.draw(gr, scale, t);
		} else if (t < 7.5) {
			double u = t-5.5;
			scene1.draw(gr, scale, 5.5+(1-(u-2)*(u-2)/4));
		} else if (t < 12.5) {
			scene1.draw(gr, scale, 6.5);
		} else if (t < 13.5) {
			new SceneD(fade(new Color(129,129,129), new Color(0xff69b4), t-12.5)).getAnimation(false).draw(gr, scale, 6.5);
		} else if (t < 20.5) {
			scene2.draw(gr, scale, 6.5);
		} else if (t < 21.5) {
			new SceneD(fade(new Color(0xff69b4), Faction.YOGURT.getColor(),t-20.5)).getAnimation(false).draw(gr, scale, 6.5);
		} else if (t < 29.5) {
			scene3.draw(gr, scale, 6.5);
		} else if (t < 30.5) {
			new SceneD(fade(Faction.YOGURT.getColor(), Faction.CREME_TANGERINE.getColor(),t-29.5)).getAnimation(false).draw(gr, scale, 6.5);
		} else if (t < 38) {
			scene4.draw(gr, scale, 6.5);
		} else if (t < 40) {
			double u = t-38;
			scene4.draw(gr, scale, 6.5+u*u/4);
		} else if (t <= 87.5) {
			scene4.draw(gr, scale, t - 32.5);
		} else if (t <= 95.5) {
			double u = 89.5-t; 
			scene4.draw(gr, scale, 56-u*u/4);
		} else if (t <= 103) {
			scene4.draw(gr, scale, 324.5-3*t);
		} else if (t <= 111) {
			double u = 109-t;
			scene4.draw(gr, scale, 6.5+u*u/4);
		} else {
			scene5.draw(gr, scale, 7.5+(t-111));
		}
	}
	private Color fade(Color c1, Color c2, double t) {
		float u = (float) (t*t*(3-2*t));
		float r1 = c1.getRed()/255f;
		float g1 = c1.getGreen()/255f;
		float b1 = c1.getBlue()/255f;
		float r2 = c2.getRed()/255f;
		float g2 = c2.getGreen()/255f;
		float b2 = c2.getBlue()/255f;
		float r3 = r1*(1-u)+r2*u;
		float g3 = g1*(1-u)+g2*u;
		float b3 = b1*(1-u)+b2*u;
		return new Color(r3,g3,b3);
	}


}
