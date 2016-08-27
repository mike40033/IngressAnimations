package com.teasearch.animation.ingress.agent.instructions.basic;

import com.teasearch.animation.ingress.agent.AgentState;


public class PauseAgent extends AgentMovement {

	public PauseAgent(double startTime, double duration, double ax, double ay, double direction) {
		super(startTime, duration, ax, ay, direction, ax, ay, direction);
	}

	public PauseAgent(AgentState agent, double d) {
		this(agent.getTime(), d, agent.getX(), agent.getY(), agent.getDirection());
	}

}
