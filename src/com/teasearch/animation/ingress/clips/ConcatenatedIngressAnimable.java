package com.teasearch.animation.ingress.clips;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.mu.methods.MUMethod;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;

public class ConcatenatedIngressAnimable extends IngressAnimable {
	
	private Portal[] portals;
	private Agent[] agents;
	private MUMethod areaPerMU;
	private IngressAnimableConstantMU[] delegates;
	private int[] agentOffsets;
	private int[] agentCounts;

	public ConcatenatedIngressAnimable(IngressAnimableConstantMU... delegates) {

		this.delegates = delegates;
		List<Agent> allAgents = new LinkedList<>();
		int[] agentOffsets = new int[delegates.length];
		int[] agentCounts = new int[delegates.length];
		for (int i=0; i<delegates.length; i++){
			IngressAnimableConstantMU delegate = delegates[i];
			Agent[] myAgents = delegate.getAgents();
			allAgents.addAll(Arrays.asList(myAgents));
			agentCounts[i] = myAgents.length;
			if (i+1 < delegates.length) {
				agentOffsets[i+1] = agentOffsets[i] + agentCounts[i];
			}
		}
		this.agentCounts = agentCounts;
		this.agentOffsets = agentOffsets;
		this.agents = allAgents.toArray(new Agent[0]);

		Set<Portal> allPortals = new HashSet<>();
		for (IngressAnimableConstantMU delegate : delegates) {
			allPortals.addAll(Arrays.asList(delegate.getPortals()));
		}
		this.portals = allPortals.toArray(new Portal[0]);
		
		double sum = 0;
		for (IngressAnimableConstantMU delegate : delegates) {
			this.areaPerMU = delegate.getMUCalculationMethod();
		}
		
	}

	@Override
	protected Agent[] getAgents() {
		return agents;
	}

	@Override
	protected Portal[] getPortals() {
		return portals;
	}

	@Override
	protected MUMethod getMUCalculationMethod() {
		return areaPerMU;
	}

	@Override
	protected void addInstructions(InstructionList[] lists,	PortalHistory[] histories) {
		double timeSoFar = 0;
		// for each delegate:
		for (int k=0; k<delegates.length; k++) {
			IngressAnimableConstantMU delegate = delegates[k];
			// * identify the relevant portalhistories
			Portal[] portals = delegate.getPortals();
			List<PortalHistory> myHistories = new LinkedList<>();
			for (Portal p : portals) {
				for (PortalHistory h : histories) {
					if (p.equals(h.getPortal())) {
						myHistories.add(h);
						break;
					}
				}
			}
			PortalHistory[] delegateHistories = myHistories.toArray(new PortalHistory[0]);
			// * identify the relevant lists
			InstructionList[] myList = new InstructionList[agentCounts[k]];
			for (int i=0; i<myList.length; i++) {
				myList[i] = lists[i+agentOffsets[k]];
			}
			// * pass these to the delegate
			for (int i=0; i<myList.length; i++) {
				myList[i].addInstruction(Wait._until(timeSoFar, false));
			}
			delegate.addInstructions(myList, delegateHistories);
			// * get the amount of time for the next lot to wait.
			timeSoFar += delegate.getAnimation().getDuration();
		}
		for (InstructionList list : lists) {
			list.addInstruction(Wait._until(timeSoFar, false));
		}
	}

}
