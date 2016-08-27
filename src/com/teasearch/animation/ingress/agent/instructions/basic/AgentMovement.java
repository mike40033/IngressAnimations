package com.teasearch.animation.ingress.agent.instructions.basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import com.teasearch.animation.AnimableItem;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ingress.agent.AgentState;

public class AgentMovement extends BasicInstruction{

	private double startX;
	private double startY;
	private double startDirecton;
	private double endX;
	private double endY;
	private double endDirection;

	public AgentMovement(double startTime, double duration, 
			double startX, double startY, double startDirecton,
			double endX, double endY, double endDirection) {
		super(startTime, duration);
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		this.startDirecton = startDirecton;
		this.endDirection = endDirection;
	}
	
	@Override
	public void applyToState(AgentState agent) {
		super.applyToState(agent);
		agent.setX(endX);
		agent.setY(endY);
		agent.setDirection(endDirection);
	}

	@Override
	public AnimableItem getAnimation(final Color c) {
		return new AnimableItem(duration) {
			
			@Override
			protected void drawIntermediate(Graphics gr, Scale scale, double u) {
				double x = getX(u);
				double y = getY(u);
				double dir = getDirection(u);
				double x1 = x+Math.cos(dir)*20;
				double y1 = y+Math.sin(dir)*20;
				double x2 = x+Math.cos(dir+Math.PI*3/4)*10;
				double y2 = y+Math.sin(dir+Math.PI*3/4)*10;
				double x3 = x+Math.cos(dir-Math.PI*3/4)*10;
				double y3 = y+Math.sin(dir-Math.PI*3/4)*10;
				double[] xx = {x1,x2,x3};
				double[] yy = {y1,y2,y3};
				int[] ix = new int[3];
				int[] iy = new int[3];
				for (int i=0; i<3; i++) {
					int[] gxy = scale.toGraphics(xx[i], yy[i]);
					ix[i] = gxy[0];
					iy[i] = gxy[1];
				}
				Polygon p = new Polygon(ix, iy, 3);
				gr.setColor(c);
				gr.fillPolygon(p);
			}
			
			@Override
			protected void drawInitial(Graphics gr, Scale scale) {
				
			}
			
			@Override
			protected void drawFinal(Graphics gr, Scale scale) {
				
			}
		};
	}
	
	public double getX(double u) {
		return interp(startX, endX, u);
	}

	public double getY(double u) {
		return interp(startY, endY, u);
	}

	private double getDirection(double u) {
		return interp(startDirecton, endDirection, u);
	}

	private double interp(double a, double b, double u) {
		return a*(1-u)+b*u;
	}
	


}
