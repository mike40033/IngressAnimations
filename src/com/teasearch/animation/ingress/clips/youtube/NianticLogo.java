package com.teasearch.animation.ingress.clips.youtube;

import java.awt.Color;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.teasearch.animation.ingress.Faction;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.ingress.stats.Corner;
import com.teasearch.utils.GeomUtils;

public class NianticLogo extends IngressAnimableConstantMU{


	@Override
	protected Agent[] getAgents() {
		return new Agent[] {
				new Agent(Faction.ENL, 50, 500, 0),
				new Agent(Faction.RES, -50, 500, 0)
				};
	}

	private	static Portal centre = new Portal(0, 0, Color.gray);
	
	private	static Portal core1 = new Portal(173, 100, Color.gray);
	private	static Portal core3 = new Portal(-173, 100, Color.gray);
	private	static Portal core5 = new Portal(0, -200, Color.gray);
	
	private	static Portal spike4 = new Portal(-130, -75, Color.gray);
	private	static Portal spike6 = new Portal(130, -75, Color.gray);
	private	static Portal spike2 = new Portal(0, 150, Color.gray);
	
	private	static Portal rim1 = new Portal(216, 128, Color.gray);
	private	static Portal rim2 = new Portal(0, 250, Color.gray);
	private	static Portal rim3 = new Portal(-216, 128, Color.gray);
	private	static Portal rim4 = new Portal(-216, -125, Color.gray);
	private	static Portal rim5 = new Portal(0, -250, Color.gray);
	private	static Portal rim6 = new Portal(216, -125, Color.gray);

	private	static Portal outer1 = new Portal(520, 300, Color.gray);
	private	static Portal outer3 = new Portal(-520, 300, Color.gray);
	private	static Portal outer5 = new Portal(0, -600, Color.gray);
	
	@Override
	protected Portal[] getPortals() {
		return new Portal[] {
				centre,
				core1, core3, core5,
				spike2, spike4, spike6,
				rim1, rim2, rim3, rim4, rim5, rim6,
				outer1, outer3, outer5
		};
	}

	@Override
	protected double getAreaPerMU() {
		return GeomUtils.getArea(new double[] {520,300}, new double[] {-520,300}, new double[] {0,-600})/1000;
	}

	@Override
	protected void addInstructions(InstructionList[] lists,
			PortalHistory[] histories) {
		Map<Portal, PortalHistory> m = new LinkedHashMap<>();
		for (PortalHistory p : histories) {
			m.put(p.getPortal(), p);
		}
		InstructionList l1 = lists[0];
		InstructionList l2 = lists[1];
		PortalHistory core1 = m.get(this.core1);
		PortalHistory core3 = m.get(this.core3);
		PortalHistory core5 = m.get(this.core5);
		PortalHistory outer1 = m.get(this.outer1);
		PortalHistory outer3 = m.get(this.outer3);
		PortalHistory outer5 = m.get(this.outer5);
		PortalHistory spike2 = m.get(this.spike2);
		PortalHistory spike4 = m.get(this.spike4);
		PortalHistory spike6 = m.get(this.spike6);
		PortalHistory rim1 = m.get(this.rim1);
		PortalHistory rim2 = m.get(this.rim2);
		PortalHistory rim3 = m.get(this.rim3);
		PortalHistory rim4 = m.get(this.rim4);
		PortalHistory rim5 = m.get(this.rim5);
		PortalHistory rim6 = m.get(this.rim6);
		PortalHistory centre = m.get(this.centre);
		l1.addInstructions(
				new CapturePortal(rim3),
				new CapturePortal(core3),
				new CapturePortal(centre),
				new LinkPortals(centre, 1, rim3, core3),
				new CapturePortal(core5),
				new LinkPortals(core5, 1, rim3, core3),
				new CapturePortal(core1),
				new LinkPortals(core1, 1, centre, core5),
				new CapturePortal(rim1),
				new LinkPortals(rim1, 1, centre, core5),
				new GoTo(centre),
				new LinkPortals(centre, 1, core5)
				);
		l2.addInstructions(
				Wait._for(26, true),
				new CapturePortal(rim3),
				new CapturePortal(rim1));
		l1.addInstructions(
				Wait._until(31.5, true),
				new CapturePortal(rim5),
				new LinkPortals(rim5, 1, core1, core3),
				new GoTo(core1),
				new LinkPortals(core1, 1, core3)
				);
		l2.addInstructions(
				Wait._until(40.5, true),
				new CapturePortal(rim5)
				);
		l1.addInstructions(
				Wait._until(43.6, true),
				new CapturePortal(rim1),
				new CapturePortal(spike2),
				new CapturePortal(rim2),
				new LinkPortals(rim2, 1, spike2, rim1),
				new CapturePortal(rim3),
				new LinkPortals(rim3, 1, rim2),

				new CapturePortal(spike4),
				new CapturePortal(rim4),
				new LinkPortals(rim4, 1, spike4, rim3),
				new CapturePortal(rim5),
				new LinkPortals(rim5, 1, rim4),

				new CapturePortal(spike6),
				new CapturePortal(rim6),
				new LinkPortals(rim6, 1, spike6, rim5, rim1),
				
				new GoTo(500,100, false),
				Wait._until(80, true),
				new GoTo(600,100, false)
				);
		l2.addInstructions(
				Wait._until(43.6, true),
				new CapturePortal(outer5),
				new CapturePortal(outer1),
				new CapturePortal(outer3),
				new GoTo(outer5),
				new LinkPortals(outer5, 1, outer3),
				new GoTo(outer1),
				Wait._until(73, true),
				new LinkPortals(outer1, 1, outer5, outer3),
				new GoTo(500,130, false),
				Wait._until(80, true),
				new GoTo(800,130, false)
				
				);
	}

	@Override
	protected Map<Color, Corner> getStatPanelColors(Agent[] agents) {
		Map<Color, Corner> rtn = new LinkedHashMap<>();
		rtn.put(Faction.ENL.getColor(), Corner.SW);
		rtn.put(Faction.RES.getColor(), Corner.SE);
		return rtn;
	}
	
}
