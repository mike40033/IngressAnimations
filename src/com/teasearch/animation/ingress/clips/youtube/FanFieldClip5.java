package com.teasearch.animation.ingress.clips.youtube;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.Instruction;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.clips.IngressMovieClip;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.utils.GeomUtils;

public class FanFieldClip5 extends IngressMovieClip{

	public FanFieldClip5(int imageWidth, int imageHeight, double fieldCentreX,
			double fieldCentreY, double fieldRadius, File folder,
			String filePrefix, double frameRate) {
		super(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder,
				filePrefix, frameRate);
	}
	@Override
	protected IngressAnimableConstantMU getIngressAnimation() {
		return new FanField5();
	}

}
