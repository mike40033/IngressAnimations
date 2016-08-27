package com.teasearch.animation.ingress.mu.movie;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teasearch.animation.clips.Animate;
import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.maxfielding.GeneralMaxfield;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.regions.ExpandingRectangle;
import com.teasearch.animation.regions.FibonacciRectangles;
import com.teasearch.animation.regions.RegionOfControl;
import com.teasearch.animation.regions.RegularPolygon;
import com.teasearch.animation.regions.SpinningRegularPolygon;
import com.teasearch.animation.regions.SweepingArc;
import com.teasearch.utils.GeomUtils;

public class MUMovieSceneA {
	public static void main(String[] args) {
		final double frameRate= 10;
		final int imageWidth = 2560;
		final int imageHeight = 1920;
 		final boolean scale = true;
//		final double frameRate= 5;
//		final int imageWidth = 400;
//		final int imageHeight = 300;
// 		final boolean scale = false;
 		
		makeClip1(frameRate, imageWidth, imageHeight, scale);
	}
	
	private static void makeClip1(double frameRate, int imageWidth,
			int imageHeight, boolean scale) {
		GeneralMaxfield generalMaxfield = getField1();
		String filePrefix = "sceneA"; 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/MU/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		new Animate(generalMaxfield, imageWidth, imageHeight, 0, 0, 600, folder, filePrefix, frameRate, scale).makeClip();
	}

	private static GeneralMaxfield getField1() {
		int cx0 = -330;
		int cy0 = -360;
		int cx1 = -370;
		int cy1 = 410;
		int cx2 = 470;
		int cy2 = 120;
		Portal corner0 = new Portal(cx0, cy0, Color.gray);
		Portal corner1 = new Portal(cx1, cy1, Color.gray);
		Portal corner2 = new Portal(cx2, cy2, Color.gray);
		Portal[][] corners = {{corner0, corner1, corner2}};
		Random r = new Random(18292644);
		int N = 12;
		Portal[] splitters = new Portal[N];
		List<double[]> sofar = new ArrayList<>();
		sofar.add(new double[] {cx0,cy0});
		sofar.add(new double[] {cx1,cy1});
		sofar.add(new double[] {cx2,cy2});
		double bound = 5000;
		for (int i=0; i<N; i++) {
			boolean okay = false;
			double x=0;
			double y=0;
			do {
				double u = r.nextDouble();
				double v = r.nextDouble();
				if (u+v > 1) continue;
				x = u*(cx1-cx0) + v*(cx2-cx0) + cx0;
				y = u*(cy1-cy0) + v*(cy2-cy0) + cy0;
				okay = false;
				for (Portal[] pp : corners) {
					if (GeomUtils.pointInOrOnTriangle(new double[] {x,y}, pp[0].getPt(), pp[1].getPt(), pp[2].getPt())) {
						okay = true;
					}
				}
				if (okay) {
					for (int j=0; j<sofar.size(); j++) {
						for (int k=0; k<j; k++) {
							double area = GeomUtils.getArea(new double[] {x,y}, sofar.get(j), sofar.get(k));
							if (area < bound) {
								okay = false;
							}
						}
					}
					if (!okay) {
						bound--;
					}
				}
			} while (!okay);
			sofar.add(new double[] {x,y});
			splitters[i] = new Portal(x, y, Color.gray);
		}
		long seed = 5409;
		Portal[][] allSplitters = new Portal[4][];
		Random mixer = new Random(seed);
		for (int k=0; k<4; k++) {
			for (int i=0; i<splitters.length; i++) {
				int j = mixer.nextInt(splitters.length);
				Portal tmp = splitters[i];
				splitters[i] = splitters[j];
				splitters[j] = tmp;
			}
			Portal[] splitters1 = splitters.clone();
			allSplitters[k] = splitters1;
		}
		RegionOfControl polygon;
		polygon = new RegularPolygon(4, -1400.0, 0.0, -Math.PI*0.2486, 650.0*Math.sqrt(2));
		Agent[][] allAgents = new Agent[4][2];
		Faction[] factions = {Faction.RED, Faction.ENL, Faction.RES, Faction.LEMON};
		double[][][] exitPoints = new double[4][2][];
		for (int i=0; i<4; i++) {
			for (int j=0; j<2; j++) {
				allAgents[i][j] = new Agent(factions[i], -650, 600*j-300, 0);
				exitPoints[i][j] = new double[] {650,600*j-300};
			}
		}
		GeneralMaxfield generalMaxfield = new GeneralMaxfield(corners, 
				allSplitters, 
				1, 
				polygon, 
				allAgents, 
				null,
				1, 
				1,
				Color.gray,
				true,
				exitPoints
				);
		return generalMaxfield;
	}

}
