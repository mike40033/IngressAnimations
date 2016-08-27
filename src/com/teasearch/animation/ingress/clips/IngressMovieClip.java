package com.teasearch.animation.ingress.clips;

import java.io.File;

import com.teasearch.animation.Animable;
import com.teasearch.animation.clips.MovieClip;

public abstract class IngressMovieClip extends MovieClip {

	public IngressMovieClip(
			int imageWidth, 
			int imageHeight, 
			double fieldCentreX, 
			double fieldCentreY, 
			double fieldRadius,
			File folder,
			String filePrefix,
			double frameRate) {
		this(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate, false);
	}

	public IngressMovieClip(
			int imageWidth, 
			int imageHeight, 
			double fieldCentreX, 
			double fieldCentreY, 
			double fieldRadius,
			File folder,
			String filePrefix,
			double frameRate,
			boolean scale) {
		super(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate, scale);
	}
	
	protected abstract IngressAnimableConstantMU getIngressAnimation();

	@Override
	public Animable getAnimation() {
		return getIngressAnimation().getAnimation();
	}
	

}	
