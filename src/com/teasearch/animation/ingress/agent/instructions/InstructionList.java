package com.teasearch.animation.ingress.agent.instructions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.utils.MapUtils;

public class InstructionList implements Instruction {
	private LinkedList<Instruction> list;

	public InstructionList() {
		this.list = new LinkedList<>();
	}
	
	public void addInstruction(Instruction next) {
		list.add(next);
	}

	@Override
	public TreeMap<Double, List<BasicInstruction>> obey(AgentState agent) {
		TreeMap<Double, List<BasicInstruction>> compiled = new TreeMap<>();
		for (Instruction instruction : list) {
			TreeMap<Double, List<BasicInstruction>> steps = instruction.obey(agent);
			MapUtils.insertAll(compiled,steps);
		}
		return compiled;
	}

	public void addInstructions(Instruction... instructions) {
		addInstructions(Arrays.asList(instructions));
	}

	public void addInstructions(List<Instruction> instructions) {
		for (Instruction i : instructions) {
			addInstruction(i);
		}
	}
}
