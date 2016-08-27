package com.teasearch.animation.ingress.maxfielding;

import java.io.File;

import com.teasearch.animation.clips.Animate;
import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.clips.youtube.Cobweb;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.regions.ExpandingPolygon;
import com.teasearch.animation.regions.ExpandingRectangle;
import com.teasearch.animation.regions.RegionOfControl;
import com.teasearch.animation.regions.RegularPolygon;

public class CobwebMain {
	public static void main(String[] args) {
		final double frameRate= 25;
		final int imageWidth = 2560;
		final int imageHeight = 1920;
 		final boolean scale = true;
//		final double frameRate= 1;
//		final int imageWidth = 400;
//		final int imageHeight = 300;
// 		final boolean scale = false;
 		final int kase = 4;
 		
 		final Faction[] faction = {Faction.ENL, Faction.RES, Faction.LEMON, Faction.ENL};
		new Thread(){public void run() {	
		makeClip1(frameRate, imageWidth, imageHeight, faction[kase], scale, kase);}}.start();
		
		
	}

	private static void makeClip1(double frameRate, int imageWidth,
			int imageHeight, Faction faction, boolean scale, int kase) {
		GeneralMaxfield generalMaxfield = getField1(faction, kase);
		String filePrefix = faction.toString().substring(0,3); 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/General/Cobweb/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		new Animate(generalMaxfield, imageWidth, imageHeight, 0, 0, 600, folder, filePrefix, frameRate, scale).makeClip();
	}

	private static GeneralMaxfield getField1(Faction faction, int kase) {
		Cobweb cobweb = new Cobweb();
		Portal[] portals = cobweb.getPortals();
		int n = cobweb.getNumberOfPortalsPerBranch();
		Portal corner0 = portals[n];
		Portal corner1 = portals[2*n];
		Portal corner2 = portals[3*n];
		Portal[][] corners = new Portal[][] {{corner0, corner1, corner2}};
		Portal[] splitters = kase == 1 ? new Portal[portals.length-3] : new Portal[3*n-2];
		for (int j=n-1,k=0; j>=1; j--) {
			for (int i=0; i<3; i++,k++) {
				splitters[k] = portals[i*n+j];
			}
		}
		splitters[3*n-3] = portals[0];
		if (kase == 1) {
			for (int k=3*n+1; k<portals.length; k++) {
				splitters[k-3] = portals[k];
			}
		}
		RegionOfControl polygon;
		if (kase == 2) {
			double[] centre = {2000*-0.5, 2000*-Math.sqrt(3)/2};
			double[] v1 = {0.515,Math.sqrt(3)/2} ;
			double[] v2 = {0.0,Math.sqrt(3)/2} ;
			double[] v3 = {-0.485,-Math.sqrt(3)/2} ;
			polygon = new ExpandingPolygon(centre, v1, v2, v3);
		} else {
			polygon = new RegionOfControl() {

				@Override
				public double[][] getPolygon(double lambda) {
					double r0 = lambda-30;
					double r1 = lambda-15;
					double r2 = lambda;
					if (r0 < 0) r0 = 0;
					if (r1 < 0) r1 = 0;
					if (r2 < 0) r2 = 0;
					double flange = Math.max(Math.min(lambda-30,20),0);
					double[] x = {r0,r0,-r1/2+flange*Math.sqrt(3)/2,-r1/2-flange*Math.sqrt(3)/2,-r2/2-flange*Math.sqrt(3)/2,-r2/2+flange*Math.sqrt(3)/2};
					double[] y = {-flange, flange, r1*Math.sqrt(3)/2+flange/2, r1*Math.sqrt(3)/2-flange/2,-r2*Math.sqrt(3)/2+flange/2, -r2*Math.sqrt(3)/2-flange/2};
					return new double[][] {x,y};
				}
			};
		}
		Agent agent1=new Agent(faction, 700,0.0,0.0);
		Agent agent2=new Agent(faction, 700,100.0,0.0);
		Agent agent3=new Agent(faction, 700,200.0,0.0);
		double factor = 1.0;
		Agent[] agents = kase == 2 ? new Agent[] {agent1} : new Agent[] {agent1, agent2, agent3};
		GeneralMaxfield generalMaxfield = new GeneralMaxfield(corners, 
				splitters, 
				 4*factor, 
				polygon, 
				agents, 
				new double[][] {{60,10}, {-35,30}, {-30,-38}},
				3.0 *factor, 
				1.5 *factor);
		return generalMaxfield;
	}
	

}
