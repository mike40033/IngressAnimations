package com.teasearch.animation.ingress.clips;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Parallel;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.AgentAnimation;
import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.animation.ingress.fields.FieldHistory;
import com.teasearch.animation.ingress.mu.methods.ConstantPerArea;
import com.teasearch.animation.ingress.mu.methods.MUMethod;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;

public abstract class IngressAnimable {

	public Animable getAnimation() {
		return getAnimation(true);
	}
	public Animable getAnimation(boolean debug) {
		// define portals
		Portal[] portals = getPortals();
			
		// get portal histories from portals 
		PortalHistory portalHistories[] = new PortalHistory[portals.length];
		for (int i=0; i<portals.length; i++) {
			portalHistories[i] = new PortalHistory(portals[i]);
		}
		
		// define agents
		Agent[] agents = getAgents();
		
		// get agent states from agents
		// define instruction lists for each agent
		AgentState states[] = new AgentState[agents.length];
		InstructionList[] lists = new InstructionList[agents.length];
		for (int i=0; i<agents.length; i++) {
			states[i] = agents[i].getInitialState();
			lists[i] = new InstructionList();
		}
		
		// add instructions to instruction lists
		addInstructions(lists, portalHistories);
		
		// convert instruction lists to maps of BasicInstructions via 'obey'
		TreeMap<Double, List<BasicInstruction>> actions[] = new TreeMap[agents.length];
		for (int i=0; i<agents.length; i++) {
			actions[i] = lists[i].obey(states[i]);
			if (debug)System.out.println("Agent "+i+" finishes at "+states[i].getTime());
		}
		
		Map<Color, Corner> statPanel = getStatPanelColors(agents);
		
		// create a FieldState from the PortalHistories and get the animation
		List<Animable> allAnimations = new LinkedList<>();
		Animable portalAnim = new FieldHistory(portalHistories).getAnimation(getMUCalculationMethod(), statPanel);
		allAnimations.add(portalAnim);
		
		// add the animations from each agent (!!)
		for (int i=0; i<agents.length; i++) {
			allAnimations.add(new AgentAnimation(states[i], actions[i]));
		}
		
		// create a parallel (!!)
		Parallel parallel = new Parallel(allAnimations.toArray(new Animable[0]));
		return parallel;
	}

	protected Map<Color, Corner> getStatPanelColors(Agent[] agents) {
		// define stat panel corners (!! (almost))
		LinkedHashSet<Color> allColors = new LinkedHashSet<>();
		for (Agent a : agents) {
			allColors.add(a.getColor());
		}
		Color[] colors = allColors.toArray(new Color[0]);
		Map<Color, Corner> statPanel = new LinkedHashMap<>();
		Corner[] cc = Corner.values();
		for (int i=0; i<colors.length && i < cc.length; i++) {
			statPanel.put(colors[i], cc[i]);
		}
		return statPanel;
	}

	protected abstract Agent[] getAgents();

	protected abstract Portal[] getPortals();

	protected abstract MUMethod getMUCalculationMethod();
	
	protected abstract void addInstructions(InstructionList[] lists,	PortalHistory[] histories);

}	
