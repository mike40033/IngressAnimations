package com.teasearch.animation.ingress.mu.movie;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.teasearch.animation.clips.Animate;
import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.Instruction;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.ConcatenatedIngressAnimable;
import com.teasearch.animation.ingress.clips.FanFieldData;
import com.teasearch.animation.ingress.clips.FanType;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.maxfielding.GeneralMaxfield;
import com.teasearch.animation.ingress.mu.ExhaustiveSearch;
import com.teasearch.animation.ingress.mu.GreedySearch;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.regions.ExpandingRectangle;
import com.teasearch.animation.regions.FibonacciRectangles;
import com.teasearch.animation.regions.RegionOfControl;
import com.teasearch.animation.regions.RegularPolygon;
import com.teasearch.animation.regions.SpinningRegularPolygon;
import com.teasearch.animation.regions.SweepingArc;
import com.teasearch.utils.GeomUtils;

public class MUMovieSceneFGHI {
	public static void main(String[] args) {

		final double frameRate= 25;
		final int imageWidth = 2560;
		final int imageHeight = 1920;
 		final boolean scale = true;
//		final double frameRate= 1;
//		final int imageWidth =  256;
//		final int imageHeight = 192;
//		final boolean scale = false;

		boolean[] tf = {true
				//, false
				};
		for (final boolean f : tf) {
 			new Thread() {
 				public void run() {
 					makeClip2(frameRate, imageWidth, imageHeight, scale, f);
 				}
 			}.start();
 		}
	}

	private static void makeClip2(double frameRate, int imageWidth,
			int imageHeight, boolean scale, boolean f) {
		IngressAnimableConstantMU ingressPart1 = getField1(f).getIngressPart();
		IngressAnimableConstantMU ingressPart2 = new FanFieldSceneF(f);
		ConcatenatedIngressAnimable ingressPart = new ConcatenatedIngressAnimable(ingressPart1, ingressPart2);
		String filePrefix = "scene"+(f ? "H" : "I"); 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/MU/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		new Animate(ingressPart.getAnimation(), imageWidth, imageHeight, 0, 0, 600, folder, filePrefix, frameRate, scale).makeClip();
	}

	private static void makeClip1(double frameRate, int imageWidth,
			int imageHeight, boolean scale, boolean f) {
		GeneralMaxfield generalMaxfield = getField1(f);
		String filePrefix = "scene"+(f ? "F" : "G"); 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/MU/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		new Animate(generalMaxfield, imageWidth, imageHeight, 0, 0, 600, folder, filePrefix, frameRate, scale).makeClip();
	}

	public static class FanFieldSceneF extends IngressAnimableConstantMU{
		
		private boolean f;

		public FanFieldSceneF(boolean f) {
			this.f = f;
		}
		@Override
		protected Agent[] getAgents() {
			return new Agent[] {
					new Agent(Faction.RES, -700, -100, 0),
			};
		}

		@Override
		protected Portal[] getPortals() {
			int cx0 = f ? -330 : -340;
			int cy0 = -360;
			int cx1 = -370;
			int cy1 = f ? 410 : 400;
			int cx2 = 440;
			int cy2 = f ? 120 : -60;
			Portal corner0 = new Portal(cx0, cy0, Color.gray);
			Portal corner1 = new Portal(cx1, cy1, Color.gray);
			Portal corner2 = new Portal(cx2, cy2, Color.gray);
			Portal[][] corners = {{corner0, corner1, corner2}};
			Random r = new Random(f ? 18292644 : 29837472);
			int N = f ? 12 : 13;
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
			List<Portal> pp = new LinkedList<>();
			pp.add(corner0);
			pp.add(corner1);
			pp.add(corner2);
			for (Portal p : splitters) {
				pp.add(p);
			}
			pp.add(new Portal(cx2*1.1-cx1*0.05-cx0*0.05, cy2*1.1-cy1*0.05-cy0*0.05, Color.black));
			return pp.toArray(new Portal[0]);
		}

		@Override
		protected double getAreaPerMU() {
			Portal[] pp = getPortals();
			return GeomUtils.getArea(pp[0].getPt(), pp[1].getPt(), pp[2].getPt())/1000;
		}

		@Override
		protected void addInstructions(InstructionList[] lists,
				PortalHistory[] histories) {
			InstructionList l1 = lists[0];
			
			List<PortalHistory> others = new LinkedList<>();
			for (int i=1; i<histories.length; i++) {
				others.add(histories[i]);
			}
			FanFieldData fanFieldData = FanFieldData.createFanfieldData(
					histories[0], 
					true, 
					others,
					true,
					FanType.concurrent,
					true,
					true,
					0.1,
					0.1,
					0.1
					);
			List<Instruction> instructions = fanFieldData.getInstructions();
			l1.addInstructions(instructions.toArray(new Instruction[0]));
			l1.addInstruction(new GoTo(700,100));
		}

	}
	
	private static GeneralMaxfield getField1(boolean f) {
		int cx0 = f ? -330 : -340;
		int cy0 = -360;
		int cx1 = -370;
		int cy1 = f ? 410 : 400;
		int cx2 = 440;
		int cy2 = f ? 120 : -60;
		Portal corner0 = new Portal(cx0, cy0, Color.gray);
		Portal corner1 = new Portal(cx1, cy1, Color.gray);
		Portal corner2 = new Portal(cx2, cy2, Color.gray);
		Portal[][] corners = {{corner0, corner1, corner2}};
		Random r = new Random(f ? 18292644 : 29837472);
		int N = f ? 12 : 13;
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
		long seed = 540849;
		Portal[][] allSplitters = new Portal[3][];
		Random mixer = new Random(seed);
		for (int k=0; k<3; k++) {
			for (int i=0; i<splitters.length; i++) {
				int j = mixer.nextInt(splitters.length);
				Portal tmp = splitters[i];
				splitters[i] = splitters[j];
				splitters[j] = tmp;
			}
			Portal[] splitters1 = splitters.clone();
			allSplitters[k] = splitters1;
		}
		allSplitters[2] = new GreedySearch().findBestSplittingOrder(corners[0], Arrays.asList(splitters)).toArray(new Portal[0]);
		allSplitters[1] = new ExhaustiveSearch().findBestSplittingOrder(corners[0], Arrays.asList(splitters)).toArray(new Portal[0]);
		allSplitters[0] = new ExhaustiveSearch(false).findBestSplittingOrder(corners[0], Arrays.asList(splitters)).toArray(new Portal[0]);
		RegionOfControl polygon;
		polygon = new RegularPolygon(4, -1400.0, 0.0, -Math.PI*0.2486, 650.0*Math.sqrt(2));
		Agent[][] allAgents = new Agent[3][2];
		Faction[] factions = {Faction.LEMON, Faction.ENL, Faction.CREME_TANGERINE};
		double[][][] exitPoints = new double[3][2][];
		for (int i=0; i<3; i++) {
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
