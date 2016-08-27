package com.teasearch.animation.ingress.portals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.teasearch.animation.ingress.stats.Corner;

public class Label {

	private String text;
	private Corner corner;
	private Color color;

	public Label(String text, Corner corner, Color color) {
		this.text = text;
		this.corner = corner;
		this.color = color;
	}

	public void draw(Graphics gr, int[] topLeft, int[] botRigt, Color portalColor) {
		gr.setColor(color == null ? portalColor : color);
		double leftX = topLeft[0];
		double rigtX = botRigt[0];
		double w = rigtX-leftX;
		double topY = topLeft[1];
		double botY = botRigt[1];
		double h = botY-topY;
		leftX -= w/10.0;
		rigtX += w/10.0;
		topY -= h/10.0;
		botY += h/10.0;
		Rectangle2D stringBounds = gr.getFontMetrics().getStringBounds(text, gr);
		double textWidth = stringBounds.getWidth();
		double textHeight = stringBounds.getHeight();
		int textX, textY;
		switch (corner) {
		case NE:
			textX = (int)Math.round(rigtX);
			textY = (int)Math.round(topY);
			break;
		case NW:
			textX = (int)Math.round(leftX-textWidth);
			textY = (int)Math.round(topY);
			break;
		case SE:
			textX = (int)Math.round(rigtX);
			textY = (int)Math.round(botY+textHeight);
			break;
		case SW:
			textX = (int)Math.round(leftX-textWidth);
			textY = (int)Math.round(botY+textHeight);
			break;
		default:
			throw new IllegalArgumentException("unknown corner "+corner);
		}
		gr.drawString(text, textX, textY);
	}
}
