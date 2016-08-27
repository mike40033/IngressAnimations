package com.teasearch.animation.ingress.maxfielding;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teasearch.animation.clips.Animate;
import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.regions.ExpandingRectangle;
import com.teasearch.animation.regions.FibonacciRectangles;
import com.teasearch.animation.regions.RegionOfControl;
import com.teasearch.animation.regions.RegularPolygon;
import com.teasearch.animation.regions.SpinningRegularPolygon;
import com.teasearch.animation.regions.SweepingArc;
import com.teasearch.utils.GeomUtils;

public class MaxfieldMain {
	public static void main(String[] args) {
		final double frameRate= 10;
		final int imageWidth = 640;
		final int imageHeight = 480;
 		final boolean scale = true;
//		final double frameRate= 2;
//		final int imageWidth = 400;
//		final int imageHeight = 300;
// 		final boolean scale = false;
//		new Thread(){public void run() {	makeClip1(frameRate, imageWidth, imageHeight, Faction.ENL, scale);}}.start();
//		new Thread(){public void run() {	makeClip1(frameRate, imageWidth, imageHeight, Faction.RES, scale);}}.start();
//		new Thread(){public void run() {	makeClip1(frameRate, imageWidth, imageHeight, Faction.CREME_TANGERINE, scale);}}.start();
//		new Thread(){public void run() {	makeClip1(frameRate, imageWidth, imageHeight, Faction.MONTELIMAR, scale);}}.start();
//		new Thread(){public void run() {	makeClip1(frameRate, imageWidth, imageHeight, Faction.GINGER_SLING, scale);}}.start();
		new Thread(){public void run() {	makeClip1(frameRate, imageWidth, imageHeight, Faction.RED, scale);}}.start();
		
		
	}

	private static void makeClip1(double frameRate, int imageWidth,
			int imageHeight, Faction faction, boolean scale) {
		GeneralMaxfield generalMaxfield = getField1(faction);
		String filePrefix = faction.toString().substring(0,3); 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/General/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		new Animate(generalMaxfield, imageWidth, imageHeight, 0, 0, 600, folder, filePrefix, frameRate, scale).makeClip();
	}

	private static GeneralMaxfield getField1(Faction faction) {
		Portal corner0 = new Portal(-400, -410, Color.gray);
		Portal corner1 = new Portal(430, -400, Color.gray);
		Portal corner2 = new Portal(400, 420, Color.gray);
		Portal corner3 = new Portal(-300, 400, Color.gray);
		Portal[][] corners = !faction.equals(Faction.RES) ? 
				new Portal[][] {{corner0, corner1, corner2}, {corner0, corner3, corner2}} :
				new Portal[][] {{corner0, corner1, corner3}, {corner2, corner1, corner3}};
		
		Random r = new Random(829644);
		int N = 11;
		Portal[] splitters = new Portal[N];
		List<double[]> sofar = new ArrayList<>();
		sofar.add(new double[] {400,420});
		sofar.add(new double[] {430,-400});
		sofar.add(new double[] {-300,400});
		sofar.add(new double[] {-400,-410});
		double bound = 5000;
		for (int i=0; i<N; i++) {
			boolean okay;
			double x;
			double y;
			do {
				x = r.nextDouble();
				y = r.nextDouble();
				x = x*800 - 400;
				y = y*800 - 400;
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
		long seed = 509;
		if (faction.equals(Faction.ENL)) {
			seed = 393;
		} else if (faction.equals(Faction.RES)){
			seed = 939;
		}
		Random mixer = new Random(seed);
		for (int i=0; i<splitters.length; i++) {
			int j = mixer.nextInt(splitters.length);
			Portal tmp = splitters[i];
			splitters[i] = splitters[j];
			splitters[j] = tmp;
		}
		RegionOfControl polygon;
		switch (faction) {
		case ENL:
			polygon = new RegularPolygon(4, -1400.0, 0.0, -Math.PI*0.3333, 650.0*Math.sqrt(2));
			break;
		case RES:
		case RED:
			polygon = new ExpandingRectangle(new double[] {0,0}, new double[] {600,200}, new double[] {599,201});
			break;
		case CREME_TANGERINE:
			polygon = new SpinningRegularPolygon(5, 100., 150, Math.PI/14, 0);
			break;
		case MONTELIMAR:
			polygon = new ExpandingRectangle(new double[] {0,800}, new double[] {-200,-1000}, new double[] {400,-600});
			break;
		case GINGER_SLING:
			polygon = new SweepingArc();
			break;
		case PINEAPPLE_HEART:
			polygon = new FibonacciRectangles();
			break;
		default:
			polygon = null;
			break;
		}
		Agent agent1=new Agent(faction, -700,0.0,0.0);
		Agent agent2=new Agent(faction, -700,100.0,0.0);
		double factor;
		switch (faction) {
		case ENL:
			factor = 1.0;
			break;
		case RES:
		case RED:
			factor = 0.5;
			break;
		default:
			factor = 0.1667;
		}
		GeneralMaxfield generalMaxfield = new GeneralMaxfield(corners, 
				splitters, 
				 4*factor, 
				polygon, 
				new Agent[] {agent1, agent2}, 
				new double[][] {{0,0}, {10,60}},
				3.0 *factor, 
				1.5 *factor);
		return generalMaxfield;
	}
	

}
