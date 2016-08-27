package com.teasearch.animation.ingress.mu.movie;

import java.awt.Color;
import java.io.File;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.clips.IngressMovieClip;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;

public class MUMovieSceneB extends IngressMovieClip {
	
	public MUMovieSceneB(int imageWidth, int imageHeight, double fieldCentreX,
			double fieldCentreY, double fieldRadius, File folder,
			String filePrefix, double frameRate, boolean scale) {
		super(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder,
				filePrefix, frameRate, scale);
	}

	public static void main(String[] args) {
	
		int imageWidth = 2560; 
		int imageHeight = 1920; 
		double frameRate = 25;
		boolean scale = true;

//		int imageWidth = 256; 
//		int imageHeight = 192; 
//		double frameRate = 1;
//		boolean scale = false;

		double fieldCentreX=0;
		double fieldCentreY=0; 
		double fieldRadius=600; 

		String filePrefix = "sceneB"; 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/MU/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		new MUMovieSceneB(imageWidth, imageHeight, fieldCentreX,
				fieldCentreY, fieldRadius, folder,
				filePrefix, frameRate, scale).makeClip();
	}

	@Override
	protected IngressAnimableConstantMU getIngressAnimation() {
		return new SceneB();
	}
	
	static class SceneB extends IngressAnimableConstantMU{

		@Override
		protected Agent[] getAgents() {
			return new Agent[] {
					new Agent(Faction.ENL, -650, 200, 0),
					new Agent(Faction.RES, -650, 100, 0),
					new Agent(Faction.YOGURT, -650, 0, 0),
					new Agent(Faction.LEMON, 0, 650, 0)
			};
		}

		@Override
		protected Portal[] getPortals() {
			return new Portal[] {
					new Portal(-400,0,Color.gray),
					new Portal(0,300,Color.gray),
					new Portal(-31,100,Color.gray),
					new Portal(0,-300,Color.gray),
					new Portal(400,0,Color.gray),
					new Portal(1300,-600,Faction.YOGURT.getColor()),
					new Portal(1800,2100,Faction.YOGURT.getColor()),
					new Portal(-2100,-1400,Faction.YOGURT.getColor()),
					new Portal(-1950,-1400,Faction.YOGURT.getColor())
			};
		}

		@Override
		protected double getAreaPerMU() {
			return 600*400/1000;
		}

		@Override
		protected void addInstructions(InstructionList[] lists,
				PortalHistory[] p) {
			lists[0].addInstruction(new CapturePortal(p[0]));
			lists[0].addInstruction(new CapturePortal(p[1]));
			lists[0].addInstruction(new LinkPortals(p[1], 0.5, p[0]));
			lists[0].addInstruction(new CapturePortal(p[2]));
			lists[0].addInstruction(new LinkPortals(p[2], 0.5, p[0], p[1]));
			lists[0].addInstruction(new CapturePortal(p[3]));
			lists[0].addInstruction(new LinkPortals(p[3], 0.5, p[0], p[2]));
			lists[0].addInstruction(new CapturePortal(p[4]));
			lists[0].addInstruction(new LinkPortals(p[4], 0.5, p[1], p[2], p[3]));
			lists[0].addInstruction(new GoTo(850,200,false));
			
			lists[1].addInstruction(Wait._until(24,false));
			lists[1].addInstruction(new CapturePortal(p[0]));
			lists[1].addInstruction(new CapturePortal(p[1]));
			lists[1].addInstruction(new LinkPortals(p[1], 0.5, p[0]));
			lists[1].addInstruction(new CapturePortal(p[2]));
			lists[1].addInstruction(new LinkPortals(p[2], 0.5, p[0], p[1]));
			lists[1].addInstruction(new CapturePortal(p[3]));
			lists[1].addInstruction(new LinkPortals(p[3], 0.5, p[0], p[1], p[2]));
			lists[1].addInstruction(new CapturePortal(p[4]));
			lists[1].addInstruction(new LinkPortals(p[4], 0.5, p[1], p[3]));
			lists[1].addInstruction(new GoTo(850,100,false));

			lists[2].addInstruction(Wait._until(48,false));
			lists[2].addInstruction(new CapturePortal(p[0]));
			lists[2].addInstruction(new CapturePortal(p[1]));
			lists[2].addInstruction(new LinkPortals(p[1], 0.5, p[0]));
			lists[2].addInstruction(new CapturePortal(p[2]));
			lists[2].addInstruction(new LinkPortals(p[2], 0.5, p[1], p[0]));
			lists[2].addInstruction(new CapturePortal(p[3]));
			lists[2].addInstruction(new LinkPortals(p[3], 0.5, p[1], p[2]));
			lists[2].addInstruction(Wait._for(4.0, true));
			lists[2].addInstruction(new LinkPortals(p[3], 0.5, p[0]));
			
			lists[3].addInstruction(Wait._until(65, false));
			lists[3].addInstruction(new CapturePortal(p[1]));
			lists[3].addInstruction(new GoTo(100,650,false));

			lists[2].addInstruction(new CapturePortal(p[1]));
			lists[2].addInstruction(new LinkPortals(p[1], 0.5, p[0]));
			lists[2].addInstruction(new CapturePortal(p[4]));
			lists[2].addInstruction(new LinkPortals(p[4], 0.5, p[1],p[6],p[5]));
			lists[2].addInstruction(new LinkPortals(p[6], 0.5, p[7], p[8]));
			lists[2].addInstruction(new GoTo(1200,650, false));

		}
		
	}
}
