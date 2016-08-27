package com.teasearch.animation.ingress.agent.instructions.basic;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.AnimableItem;
import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.Instruction;

public abstract class BasicInstruction implements Instruction {

	protected double startTime;
	protected double duration;

	public BasicInstruction(double time, double duration) {
		this.startTime = time;
		this.duration = duration;
	}
	
	@Override
	public TreeMap<Double, List<BasicInstruction>> obey(AgentState agent) {
		TreeMap<Double, List<BasicInstruction>> rtn = new TreeMap<>();
		rtn.put(this.startTime,  Arrays.asList(this));
		return rtn;
	}

	public void applyToState(AgentState agent) {
		agent.addTime(duration);
	}

	public abstract AnimableItem getAnimation(Color c);

}
