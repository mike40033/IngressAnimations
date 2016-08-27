package com.teasearch.animation.ingress.agent;

import java.awt.Color;

public class AgentState {

	private double x;
	private double y;
	private double direction;
	private Agent agent;
	private double time;

	public AgentState(Agent agent, double x, double y, double direction, double time) {
		this.agent = agent;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.time = time;
	}

	public double getDirection() {
		return direction;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	public double getRotationSpeed() {
		return agent.getRotationSpeed();
	}

	public double getActionRadius() {
		return agent.getActionRadius();
	}

	public double getMovementSpeed() {
		return agent.getMovementSpeed();
	}

	public void addTime(double duration) {
		this.time += duration;
	}

	public double getTime() {
		return this.time;
	}

	public Color getColor() {
		return agent.getColor();
	}

	public Agent getAgent() {
		return agent;
	}
	
}
