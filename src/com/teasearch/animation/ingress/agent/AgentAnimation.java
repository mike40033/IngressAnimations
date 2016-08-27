package com.teasearch.animation.ingress.agent;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.AnimableItem;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.Series;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;

public class AgentAnimation implements Animable{

	private Series delegate;

	public AgentAnimation(AgentState state, TreeMap<Double, List<BasicInstruction>> actions) {
		List<AnimableItem> moves = new LinkedList<>();
		for (List<BasicInstruction> instructionList : actions.values()) {
			for (BasicInstruction instruction : instructionList) {
				moves.add(instruction.getAnimation(state.getColor()));
			}
		}
		Series delegate = new Series(moves.toArray(new AnimableItem[0]));
		this.delegate = delegate;
	}

	@Override
	public double getDuration() {
		return delegate.getDuration();
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		delegate.draw(gr, scale, t);
	}
	

}
