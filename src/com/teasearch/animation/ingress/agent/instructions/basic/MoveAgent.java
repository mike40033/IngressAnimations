package com.teasearch.animation.ingress.agent.instructions.basic;

import com.teasearch.animation.ingress.agent.AgentState;


public class MoveAgent extends AgentMovement {

	public MoveAgent(double startTime, double ax, double ay, double bx, double by, double direction, double duration) {
		super(startTime, duration, ax, ay, direction, bx, by, direction);
	}

	public MoveAgent(AgentState agent, double bx, double by) {
		this(agent.getTime(), agent.getX(), agent.getY(), bx, by, agent.getDirection(), dist(agent.getX()-bx, agent.getY()-by)/agent.getMovementSpeed());
	}

	private static double dist(double dx, double dy) {
		return Math.sqrt(dx*dx+dy*dy);
	}

}
