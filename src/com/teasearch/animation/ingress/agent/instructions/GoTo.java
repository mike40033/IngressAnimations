package com.teasearch.animation.ingress.agent.instructions;

import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.animation.ingress.agent.instructions.basic.MoveAgent;
import com.teasearch.animation.ingress.agent.instructions.basic.RotateAgent;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.utils.MapUtils;

public class GoTo implements Instruction{
	
	private double px;
	private double py;
	private boolean buffer;

	public GoTo(double px, double py, boolean buffer) {
		this.px = px;
		this.py = py;
		this.buffer = buffer;
	}

	public GoTo(PortalHistory p) {
		this(p.getPortal());
	}

	public GoTo(Portal p) {
		this(p.getX(), p.getY(), true);
	}

	public GoTo(double x, double y) {
		this(x,y,false);
	}

	@Override
	public TreeMap<Double,List<BasicInstruction>>  obey(AgentState agent) {
		double ax = agent.getX();
		double ay = agent.getY();
		double adir = agent.getDirection();
		double pdir = Math.atan2(py-ay, px-ax);
		double distance = Math.sqrt((px-ax)*(px-ax) + (py-ay)*(py-ay));
		if (distance < (buffer ? agent.getActionRadius() : 0)) {
			return new TreeMap<>();
		}
		while (pdir>adir+Math.PI) {
			pdir -= Math.PI*2;
		}
		while (pdir<adir-Math.PI) {
			pdir += Math.PI*2;
		}
		TreeMap<Double, List<BasicInstruction>> rtn = new TreeMap<>();
		distance = distance - (buffer ? agent.getActionRadius() : 0);

		BasicInstruction rotate = new RotateAgent(agent, pdir);
		MapUtils.insert(rtn, agent.getTime(), rotate);
		rotate.applyToState(agent);

		BasicInstruction move = new MoveAgent(agent, ax+distance*Math.cos(pdir), ay+distance*Math.sin(pdir));
		MapUtils.insert(rtn, agent.getTime(), move);
		move.applyToState(agent);

		return rtn;
	}
}
