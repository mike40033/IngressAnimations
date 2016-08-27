package com.teasearch.animation.ingress.agent;

import java.awt.Color;

import com.teasearch.animation.ingress.Faction;

public class Agent {
	private Color color;
	private double startX;
	private double startY;
	private double startDirection;
	private double movementSpeed;

	public Agent(Color color, double startX, double startY, double startDirection) {
		this.color = color;
		this.startX = startX;
		this.startY = startY;
		this.startDirection = startDirection;
		this.movementSpeed = 200;
	}
	
	public Agent(Faction faction, double startX, double startY, double startDirection) {
		this(faction.getColor(),startX, startY, startDirection);
	}

	public AgentState getInitialState() {
		return new AgentState(this, startX, startY, startDirection, 0.0);
	}

	public double getRotationSpeed() {
		return Math.PI;
	}

	public double getActionRadius() {
		return 40;
	}

	public double getMovementSpeed() {
		return movementSpeed;
	}

	public void changeMovementSpeed(double factor) {
		movementSpeed *= factor;
	}

	public Color getColor() {
		return color;
	}

	public double getStartX() {
		return startX;
	}

	public double getStartY() {
		return startY;
	}

}
