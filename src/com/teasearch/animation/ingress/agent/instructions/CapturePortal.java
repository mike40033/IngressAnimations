package com.teasearch.animation.ingress.agent.instructions;

import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.animation.ingress.agent.instructions.basic.PauseAgent;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.utils.MapUtils;

public class CapturePortal extends GoTo {

	private PortalHistory portal;

	public CapturePortal(PortalHistory portal) {
		super(portal.getX(), portal.getY(), true);
		this.portal = portal;
	}
	
	@Override
	public TreeMap<Double,List<BasicInstruction>> obey(AgentState agent) {
		TreeMap<Double,List<BasicInstruction>> move = super.obey(agent);
		portal.addDestroy(agent.getTime()+0.1, agent.getColor());
		portal.addCapture(agent.getTime()+0.4, agent.getColor());
		BasicInstruction b = new PauseAgent(agent, 0.5);
		MapUtils.insert(move, agent.getTime(), b);
		b.applyToState(agent);
		return move;
	}

}
