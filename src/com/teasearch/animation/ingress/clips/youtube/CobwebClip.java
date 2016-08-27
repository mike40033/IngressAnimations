package com.teasearch.animation.ingress.clips.youtube;

import java.io.File;

import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.clips.IngressMovieClip;

public class CobwebClip extends IngressMovieClip {

	public CobwebClip(int imageWidth, int imageHeight, double fieldCentreX,
			double fieldCentreY, double fieldRadius, File folder,
			String filePrefix, double frameRate) {
		super(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder,
				filePrefix, frameRate);
	}

	@Override
	protected IngressAnimableConstantMU getIngressAnimation() {
		return new Cobweb();
	}

}
