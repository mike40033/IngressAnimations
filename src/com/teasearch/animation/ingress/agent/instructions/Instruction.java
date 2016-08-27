package com.teasearch.animation.ingress.agent.instructions;

import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;

public interface Instruction {
	public TreeMap<Double, List<BasicInstruction>> obey(AgentState agent);
}
