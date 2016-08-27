package com.teasearch.animation.ingress.agent.instructions.basic;

import com.teasearch.animation.ingress.agent.AgentState;


public class RotateAgent extends AgentMovement {

	public RotateAgent(double startTime,double ax, double ay, double startAngle, double endAngle, double duration) {
		super(startTime, duration, ax, ay, startAngle, ax, ay, endAngle);
	}

	public RotateAgent(AgentState agent, double pdir) {
		this(agent.getTime(), agent.getX(), agent.getY(), agent.getDirection(), pdir, Math.abs(pdir-agent.getDirection())/agent.getRotationSpeed());
	}

}
