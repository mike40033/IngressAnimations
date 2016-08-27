package com.teasearch.animation.ingress.agent.instructions;

import java.awt.Color;
import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;
import com.teasearch.animation.ingress.agent.instructions.basic.PauseAgent;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;
import com.teasearch.utils.MapUtils;

public class LabelPortal implements Instruction {
	private PortalHistory portal;
	private String text;
	private Color color;
	private Corner corner;

	/** color may be null - then the label gets its color from the portal */
	public LabelPortal(PortalHistory portal, String text, Corner corner) {
		this(portal, text, corner, null);
	}

	/** color may be null - then the label gets its color from the portal */
	public LabelPortal(PortalHistory portal, String text, Corner corner, Color color) {
		this.portal = portal;
		this.text = text;
		this.corner = corner;
		this.color = color;
	}
	
	@Override
	public TreeMap<Double, List<BasicInstruction>> obey(AgentState agent) {
		TreeMap<Double,List<BasicInstruction>> nothing = new TreeMap<>();
		portal.setLabel(agent.getTime(), text, corner, color);
		return nothing;
	}

}
