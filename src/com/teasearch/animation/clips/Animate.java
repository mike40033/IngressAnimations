package com.teasearch.animation.clips;

import java.io.File;

import com.teasearch.animation.Animable;
import com.teasearch.animation.AxisAlignedScale;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;

public class Animate extends MovieClip {

	Animable animation;
	
	public Animate(Animable animation, int imageWidth, int imageHeight, double fieldCentreX,
			double fieldCentreY, double fieldRadius, File folder,
			String filePrefix, double frameRate, boolean scale) {
		this(animation, 
				new AxisAlignedScale(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius), 
				folder, filePrefix, frameRate, scale);
	}

	public Animate(Animable animation, ScaleAnimation zoomingScale, File folder,
			String filePrefix, double frameRate, boolean scaleOutput) {
		super(zoomingScale, folder, filePrefix, frameRate, scaleOutput);
		this.animation = animation;
	}


	@Override
	public Animable getAnimation() {
		return animation;
	}

}
