package com.teasearch.animation.ingress.agent.instructions;

import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.animation.ingress.agent.instructions.basic.PauseAgent;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.utils.MapUtils;

public class LinkPortals extends GoTo{

	private double delayBetweenLinks;
	private PortalHistory[] linkTargets;
	private PortalHistory portal;

	public LinkPortals(PortalHistory portal, double delayBetweenPortals, PortalHistory...linkTargets) {
		super(portal.getX(), portal.getY(), true);
		this.portal = portal;
		this.delayBetweenLinks = delayBetweenPortals;
		this.linkTargets = linkTargets;
	}
	
	@Override
	public TreeMap<Double,List<BasicInstruction>> obey(AgentState agent) {
		TreeMap<Double,List<BasicInstruction>> move = super.obey(agent);
		BasicInstruction b = new PauseAgent(agent, (linkTargets.length+1)*delayBetweenLinks);
		double startTime = agent.getTime();
		MapUtils.insert(move, startTime, b);
		b.applyToState(agent);
		for (int i=0; i<linkTargets.length; i++) {
			portal.addLinkTo(startTime+(i+1)*delayBetweenLinks, agent.getColor(), linkTargets[i]);
		}
		return move;
	}
}
