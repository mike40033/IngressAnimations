package com.teasearch.animation.ingress.agent.instructions;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.animation.ingress.agent.instructions.basic.RotateAgent;
import com.teasearch.animation.ingress.agent.instructions.basic.PauseAgent;
import com.teasearch.utils.MapUtils;

public class Wait implements Instruction{
	private double time;
	private boolean spin;
	private boolean timeIsDuration;

	private Wait(double time, boolean timeIsDuration, boolean spin) {
		this.time = time;
		this.timeIsDuration = timeIsDuration;
		this.spin = spin;
	}

	public static Wait _for(double duration, boolean spin) {
		return new Wait(duration, true, spin);
	}
	
	public static Wait _until(double time, boolean spin) {
		return new Wait(time, false, spin);
	}
	
	private static Random rr = new Random(23993);
	@Override
	public TreeMap<Double, List<BasicInstruction>> obey(AgentState agent) {
		TreeMap<Double, List<BasicInstruction>> rtn = new TreeMap<>();
		double startTime = agent.getTime();
		double endTime = timeIsDuration ? startTime+time : time;
		double duration = endTime - startTime;
		if (duration < 0) {
			return new TreeMap<>();
		}
		if (!spin) {
			BasicInstruction b = new PauseAgent(agent, duration);
			b.applyToState(agent);
			MapUtils.insert(rtn, startTime, b);
			return rtn;
		}
		int clockwise = rr.nextBoolean() ? 1 : -1;
		while (duration > 0) {
			double spinFor = rr.nextGaussian();
			spinFor *= spinFor;
			if (spinFor > duration) spinFor = duration;
			RotateAgent rotate = new RotateAgent(agent, agent.getDirection()+spinFor*agent.getRotationSpeed()*clockwise);
			MapUtils.insert(rtn, agent.getTime(), rotate);
			rotate.applyToState(agent);
			duration -= spinFor;
			clockwise *= -1;
		}
		return rtn;
	}
}
