package com.teasearch.animation.ingress.clips.youtube;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LabelPortal;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.FanFieldData;
import com.teasearch.animation.ingress.clips.FanType;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;
import com.teasearch.utils.GeomUtils;

public class FanField6 extends IngressAnimableConstantMU{

	@Override
	protected Agent[] getAgents() {
		class FastAgent extends Agent {

			public FastAgent(Color color, double startX, double startY,
					double startDirection) {
				super(color, startX, startY, startDirection);
			}

			@Override
			public double getRotationSpeed() {
				return super.getRotationSpeed()*2;
			}

			@Override
			public double getMovementSpeed() {
				return super.getMovementSpeed()*2;
			}
			
			
		}
		Agent[] agents = new Agent[] {
				new Agent(Faction.RED.getColor(), 800, -100, 0),
				new FastAgent(Faction.LEMON.getColor(), 800, 50, 0),
				new FastAgent(Faction.RES.getColor(), 800, -50, 0),
				new FastAgent(Faction.ENL.getColor(), 800, 0, 0),
		};
		return agents;
	}

	int[] anchorAngles = {0,1,5,2,4,3};

	@Override
	protected Portal[] getPortals() {
		Random rr = new Random(65536);
		int n = getNumberOfPortalsExcludingAnchor();
		LinkedList<Portal> pp = new LinkedList<>();
		Color initialColor = Faction.RED.getColor();
		double offsetAngle = -Math.PI/9;
		int majorAxis = 600;
		int minorAxis = 400;
		for (int i=0; i<6; i++) {
			double th1 = offsetAngle+i*Math.PI/3;
			double p2X = Math.cos(th1)*majorAxis;
			double p2Y = Math.sin(th1)*minorAxis;
			pp.add(new Portal(p2X,p2Y, initialColor));
		}
		for (int i=0; i<4; i++) {
			double th1 = offsetAngle+anchorAngles[i]*Math.PI/3;
			double thA = offsetAngle+anchorAngles[i+1]*Math.PI/3;
			double th2 = offsetAngle+anchorAngles[i+2]*Math.PI/3;
			double p1X = Math.cos(thA)*majorAxis;
			double p1Y = Math.sin(thA)*minorAxis;
			double p2X = Math.cos(th1)*majorAxis;
			double p2Y = Math.sin(th1)*minorAxis;
			double p3X = Math.cos(th2)*majorAxis;
			double p3Y = Math.sin(th2)*minorAxis;
			for (int j=0; j<n-2; j++) {
				double theta = Math.PI/2*(j+0.5)/(n-1);//+rr.nextGaussian()*0.02*Math.PI/2/n;
				double r = rr.nextDouble()*0.7+0.2;
				double a= r*Math.cos(theta);
				double b= r*Math.sin(theta);
				double x = p1X + (p2X-p1X)*a + (p3X-p1X)*b;
				double y = p1Y + (p2Y-p1Y)*a + (p3Y-p1Y)*b;
				while (!GeomUtils.pointInTriangle(new double[]{x,y}, new double[]{p1X,p1Y}, new double[]{p2X,p2Y}, new double[]{p3X,p3Y})) {
					r = rr.nextDouble();
					a= r*Math.cos(theta);
					b= r*Math.sin(theta);
					x = p1X + (p2X-p1X)*a + (p3X-p1X)*b;
					y = p1Y + (p2Y-p1Y)*a + (p3Y-p1Y)*b;
				}
				pp.add(new Portal(x, y, initialColor));
				
			}
			
		}
		return pp.toArray(new Portal[0]);
	}

	private int getNumberOfPortalsExcludingAnchor() {
		return 6;
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

		int n = getNumberOfPortalsExcludingAnchor();
		for (int k=0; k<4; k++) {
			List<PortalHistory> others = new LinkedList<>();
			others.add(histories[anchorAngles[k]]);
			others.add(histories[anchorAngles[k+2]]);
			for (int i=6+k*(n-2); i<6+(k+1)*(n-2); i++) {
				others.add(histories[i]);
			}
			FanFieldData fanFieldData = FanFieldData.createFanfieldData(
					histories[anchorAngles[k+1]], 
					!true, 
					others,
					!true,
					FanType.out,
					k % 2 == 0,
					true,
					0.05,
					0.25,
					0.25
					);
			l1.addInstructions(fanFieldData.getInstructions());
			for (int i=0; i<histories.length; i++) {
				l1.addInstruction(new LabelPortal(histories[i], "", Corner.NE));
			}
			
		}
		lists[1].addInstructions(Wait._for(45, false));
		lists[2].addInstructions(Wait._for(45.5, false));
		lists[3].addInstructions(Wait._for(46, false));
		double[][] positons = {
				null,
				{800,100},
				{800,-100},
				{800,0}
		};
		for (int j=0; j<15; j++) {
			for (int i=1; i<4; i++) {
				int index = getRandomPortalIndex(positons[i], histories);
				positons[i] = histories[index].getPt();
				lists[i].addInstruction(new CapturePortal(histories[index]));
				lists[i].addInstruction(new LinkPortals(histories[index], 0.05, randomised(histories,index)));
			}
		}
		l1.addInstruction(Wait._for(15, true));
		l1.addInstruction(new GoTo(-800, -200, false));
	}

	private PortalHistory[] randomised(PortalHistory[] histories, int index) {
		TreeMap<Double, PortalHistory> map = new TreeMap<>();
		for (int i=0; i<histories.length; i++) {
			if (i == index) continue;
			map.put(rrr.nextDouble(), histories[i]);
		}
		return map.values().toArray(new PortalHistory[0]);
	}

	private static Random rrr = new Random(239874);
	
	private int getRandomPortalIndex(double[] xy, PortalHistory[] histories) {
		double[] weights = new double[histories.length];
		double totalWeight = 0;
		for (int i=0; i<weights.length; i++) {
			weights[i] = GeomUtils.getDistance(histories[i].getPt(),xy);
			if (weights[i] != 0) {
				weights[i] = 1/weights[i];
			}
			totalWeight += weights[i];
		}
		for (int i=0; i<weights.length; i++) {
			weights[i] /= totalWeight;
		}
		double d= rrr.nextDouble();
		int rtn = 0;
		while (d > weights[rtn]) {
			d -= weights[rtn];
			rtn++;
		}
		return rtn;
	}

}
