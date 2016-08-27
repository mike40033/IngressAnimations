package com.teasearch.animation.ingress;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.teasearch.animation.Animable;
import com.teasearch.animation.AxisAlignedScale;
import com.teasearch.animation.Parallel;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.AgentAnimation;
import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LabelPortal;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.animation.ingress.fields.FieldHistory;
import com.teasearch.animation.ingress.mu.methods.ConstantPerArea;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;

public class Main {

	public static void main(String[] args) {
		Random rr = new Random(2881282);
		Portal pp[] = new Portal[24];
		for (int i=0; i<pp.length; i++) {
			pp[i] = new Portal(rr.nextGaussian()*150, rr.nextGaussian()*150, Color.gray);
		}
		PortalHistory ph[] = new PortalHistory[pp.length];
		for (int i=0; i<pp.length; i++) {
			ph[i] = new PortalHistory(pp[i]);
		}
		Agent[] agents = new Agent[] {
				new Agent(Faction.RES, -400, 0, 0),
				new Agent(Faction.ENL, 400, 0, 0),
//				new Agent(Color.red, 0, 400, 0),
//				new Agent(Color.yellow, 0, -400, 0),
		};
		AgentState states[] = new AgentState[agents.length];
		InstructionList[] lists = new InstructionList[agents.length];
		for (int i=0; i<agents.length; i++) {
			states[i] = agents[i].getInitialState();
			lists[i] = new InstructionList();
		}
		for (int i=0; i<50; i++) {
			for (int j=0; j<agents.length; j++) {
				PortalHistory portal = ph[rr.nextInt(ph.length)];
				lists[j].addInstruction(new CapturePortal(portal));
				lists[j].addInstruction(new LinkPortals(portal, 0.1, ph));
				if (i % 5 == 0) {
					lists[j].addInstruction(new LabelPortal(portal, ""+i+":"+Corner.values()[i%4], Corner.values()[i%4]));
				}
			}
		}
		TreeMap<Double, List<BasicInstruction>> actions[] = new TreeMap[agents.length];
		for (int i=0; i<agents.length; i++) {
			actions[i] = lists[i].obey(states[i]);
		}
		// TODO : turn agent actions into a Series
		List<Animable> allAnimations = new LinkedList<>();
		Map<Color, Corner> statPanel = new LinkedHashMap<>();
		statPanel.put(Faction.RES.getColor(), Corner.NE);
		statPanel.put(Faction.ENL.getColor(), Corner.NW);
		statPanel.put(Color.red, Corner.SE);
		statPanel.put(Color.yellow, Corner.SW);
		Animable portalAnim = new FieldHistory(ph).getAnimation(new ConstantPerArea(400.0), statPanel);
		allAnimations.add(portalAnim);
		for (int i=0; i<agents.length; i++) {
			allAnimations.add(new AgentAnimation(states[i], actions[i]));
		}
		Parallel parallel = new Parallel(allAnimations.toArray(new Animable[0]));
		
		Scale scale = new AxisAlignedScale(800, 800, 0.0, 0.0, 500);
		for (int i=0; i<=parallel.getDuration()*5+1; i++) {
			BufferedImage im = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
			parallel.draw(im.getGraphics(), scale, i/5.0);
			try {
				ImageIO.write(im, "png",new File("/home/hartleym/Desktop/Animation/agent"+i+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}	
