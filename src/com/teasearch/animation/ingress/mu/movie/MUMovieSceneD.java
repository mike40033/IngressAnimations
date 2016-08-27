package com.teasearch.animation.ingress.mu.movie;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LabelPortal;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.clips.IngressMovieClip;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;
import com.teasearch.utils.GeomUtils;

public class MUMovieSceneD extends IngressMovieClip{
	
	public MUMovieSceneD(int imageWidth, int imageHeight, double fieldCentreX,
			double fieldCentreY, double fieldRadius, File folder,
			String filePrefix, double frameRate, boolean scale) {
		super(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder,
				filePrefix, frameRate, scale);
	}

	public static void main(String[] args) {
	
//		int imageWidth = 2560; 
//		int imageHeight = 1920; 
//		double frameRate = 25;
//		boolean scale = true;

		int imageWidth = 256; 
		int imageHeight = 192; 
		double frameRate = 4;
		boolean scale = false;

		double fieldCentreX=0;
		double fieldCentreY=0; 
		double fieldRadius=600; 

		String filePrefix = "sceneD"; 
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/MU/" +
				filePrefix +
				"/smaller/");
		folder.mkdirs();
		new MUMovieSceneD(imageWidth, imageHeight, fieldCentreX,
				fieldCentreY, fieldRadius, folder,
				filePrefix, frameRate, scale).makeClip();
	}

	@Override
	protected IngressAnimableConstantMU getIngressAnimation() {
		return new SceneD(Faction.CREME_TANGERINE.getColor(), true);
	}
	
	public static class SceneD extends IngressAnimableConstantMU{

		private Color mainColor;
		private boolean withLabels;

		public SceneD(Color color) {
			this(color, false);
		}
		public SceneD(Color color, boolean withLabels) {
			this.mainColor = color;
			this.withLabels = withLabels;
		}

		@Override
		protected Agent[] getAgents() {
			Random rr = new Random(1306);
			Portal[] portals = getPortals();
			Agent[] agents = new Agent[portals.length+1];
			for (int i=0; i<portals.length+1; i++) {
				Color color = i < 3 || i == portals.length ? mainColor : (withLabels ? Faction.LEMON.getColor() : pickColor(rr));
				double rX = 700;
				double rY = 500;
				double t = rr.nextDouble()*(4*(rX+rY));
				double startX;
				double startY;
				if (t < rX*2) {
					startX = t - rX;
					startY = rY;
				} else if (t < 2*rX+2*rY) {
					startX = rX;
					startY = t - (2*rX+rY);
				} else if (t < 4*rX+2*rY) {
					startX = t - (3*rX+2*rY);
					startY = -rY;
				} else {
					startX = -rX;
					startY = t - (4*rX+3*rY);
				}
				agents[i] = new Agent(color, startX, startY, 0);
			}
			for (int i=3; i<agents.length; i++) {
				agents[i].changeMovementSpeed(3);
			}
			return agents;
		}
		private Color pickColor(Random rr) {
			Color c = new Color(rr.nextInt(256*256*256));
			float r = c.getRed()/255f;
			float g = c.getGreen()/255f;
			float b = c.getBlue()/255f;
			r = 4*r*(1-r);
			b = 4*b*(1-b);
			g = 4*g*(1-g);
			float rm = mainColor.getRed()/255f;
			float gm = mainColor.getGreen()/255f;
			float bm = mainColor.getBlue()/255f;
			float dm = (r-rm)*(r-rm)+(g-gm)*(g-gm)+(b-bm)*(b-bm);
			float dz = r*r+g*g+b*b;
			float eps = 0.5f;
			while (dm < eps*eps || dz < eps*eps) {
				r = 4*r*(1-r);
				b = 4*b*(1-b);
				g = 4*g*(1-g);
				dm = (r-rm)*(r-rm)+(g-gm)*(g-gm)+(b-bm)*(b-bm);
				dz = r*r+g*g+b*b;
			}
			return new Color(r,g,b);
		}

		@Override
		protected Portal[] getPortals() {
			int cx0 = -330;
			int cy0 = -360;
			int cx1 = -370;
			int cy1 = 410;
			int cx2 = 470;
			int cy2 = 120;
			Portal corner0 = new Portal(cx0, cy0, mainColor);
			Portal corner1 = new Portal(cx1, cy1, mainColor);
			Portal corner2 = new Portal(cx2, cy2, mainColor);
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
				splitters[i] = new Portal(x, y, mainColor);
			}
			// TODO Auto-generated method stub
			List<Portal> pp = new LinkedList<>();
			pp.add(corner0);
			pp.add(corner1);
			pp.add(corner2);
			for (Portal p : splitters) {
				pp.add(p);
			}
			return pp.toArray(new Portal[0]);
		}

		
		@Override
		protected Map<Color, Corner> getStatPanelColors(Agent[] agents) {
			Map<Color, Corner> map = new HashMap<Color, Corner>();
			map.put(mainColor, Corner.NW);
			return map;
		}

		@Override
		protected double getAreaPerMU() {
			Portal[] portals = getPortals();
			double area =GeomUtils.getArea(portals[0].getPt(), portals[1].getPt(), portals[2].getPt());
			return area/1000.1;
		}

		@Override
		protected void addInstructions(InstructionList[] lists,	PortalHistory[] histories) {
			Random rr = new Random(329048);
			Agent[] agents = getAgents();
			double[] wait1;
			double finalTime;
			if (withLabels){
				wait1 = new double[] {0,17.5,28.0,38.3,48.1,58.3,70.3,80.8,90.9,100.4,110.4,122.9};
				finalTime = 132.8;
			} else {
				wait1 = new double[] {0,10.8,14.6,17.6,21.9,25.9,29.4,32.2,35.8,39.5,42.2,47.0};
				finalTime = 50.6;
			}
			double[] wait2 = wait1.clone();
			wait2[0] = 8;
			for (int k=0; k<3; k++) {
				lists[k].addInstruction(new LinkPortals(histories[k], 0.1, histories[(k+1)%3]));
			}
			for (int i=0; i<wait1.length; i++) {
				for (int k=0; k<3; k++) {
					lists[k].addInstruction(Wait._until(wait1[i], false));
					lists[k].addInstruction(new LinkPortals(histories[k], 0.1, histories[3+i]));
				}
				if (i < wait2.length) {
					lists[3+i].addInstruction(Wait._until(wait2[i], false));
					lists[3+i].addInstructions(new GoTo(histories[3+i]));
					if (withLabels) {
						String fullLabel = getFullLabel(3+i);
						lists[3+i].addInstruction(new LabelPortal(histories[3+i], fullLabel.substring(0,7), Corner.NE));
						lists[3+i].addInstruction(Wait._for(1, false));
						for (int k=1; k<fullLabel.length()-6; k++) {
							lists[3+i].addInstruction(new LabelPortal(histories[3+i], fullLabel.substring(k,k+7), Corner.NE));
							lists[3+i].addInstruction(Wait._for(0.2, false));
						}
						lists[3+i].addInstruction(Wait._for(1, false));
					}
					lists[3+i].addInstruction(new CapturePortal(histories[3+i]));
					double finiX = agents[3+i].getStartX();
					double finiY = agents[3+i].getStartY();
					if (rr.nextBoolean()) finiX = -finiX;
					if (rr.nextBoolean()) finiY = -finiY;
					lists[3+i].addInstruction(new GoTo(finiX, finiY, false));
				}
			}
			if (withLabels) {
				lists[histories.length].addInstruction(Wait._until(finalTime, false)); 
				lists[histories.length].addInstruction(new CapturePortal(histories[6]));
				for (int p=3; p<histories.length; p++) {
					if (p == 6) continue;
					lists[histories.length].addInstruction(new LabelPortal(histories[p], " ", Corner.NW));
					lists[histories.length].addInstruction(Wait._for(0.1, false));
				}
				lists[histories.length].addInstruction(new GoTo(700,0));
				for (int i=0; i<3; i++) {
					lists[i].addInstruction(Wait._until(finalTime+4, false));
					lists[i].addInstruction(new LinkPortals(histories[i], 0.2, histories[6]));
				}
			}
			for (int i=0; i<(withLabels ? 3 : lists.length); i++) {
				lists[i].addInstruction(Wait._until(withLabels ? finalTime+5 : finalTime, false));
				lists[i].addInstruction(new GoTo(700,0));
			}
		}
		
		private String getFullLabel(int n) {
			Portal[] portals = getPortals();
			Portal me = portals[n];
			Portal[][] splits = {{portals[0],portals[1],me}, {portals[1],portals[2],me}, {portals[2],portals[0],me}};
			int[] mus = new int[3];
			int[] counts = new int[3];
			for (int k=3; k<portals.length; k++) {
				if (k == n) continue;
				for (int t=0; t<3; t++) {
					if (GeomUtils.pointInOrOnTriangle(portals[k].getPt(), splits[t][0].getPt(), splits[t][1].getPt(), splits[t][2].getPt())) {
						counts[t]++;
					}
				}
			}
			double areaPerMU = getAreaPerMU();
			for (int t=0; t<3; t++) {
				mus[t] = (int) Math.round(GeomUtils.getArea(splits[t][0].getPt(), splits[t][1].getPt(), splits[t][2].getPt())/areaPerMU);
			}
			String[] labelParts = new String[4];
			int sum = 0;
			for (int t=0; t<3; t++) {
				labelParts[t] = counts[t]+"x"+mus[t];
				sum += counts[t]*mus[t];
			}
			String string = labelParts[0] + " + " + labelParts[1] + " + " + labelParts[2] + " = " + sum + "  ";
			return string;
		}
		
	}
}
