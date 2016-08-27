package com.teasearch.animation.ingress.maxfielding;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.Parallel;
import com.teasearch.animation.Pause;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.Series;
import com.teasearch.animation.ingress.agent.Agent;
import com.teasearch.animation.ingress.agent.AgentState;
import com.teasearch.animation.ingress.agent.instructions.CapturePortal;
import com.teasearch.animation.ingress.agent.instructions.GoTo;
import com.teasearch.animation.ingress.agent.instructions.InstructionList;
import com.teasearch.animation.ingress.agent.instructions.LinkPortals;
import com.teasearch.animation.ingress.agent.instructions.Wait;
import com.teasearch.animation.ingress.clips.IngressAnimableConstantMU;
import com.teasearch.animation.ingress.fields.ActiveLink;
import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.animation.ingress.portals.PortalAnimation;
import com.teasearch.animation.ingress.portals.PortalHistory;
import com.teasearch.animation.regions.RegionOfControl;
import com.teasearch.utils.GeomUtils;
import com.teasearch.utils.MapUtils;

public class GeneralMaxfield implements Animable {

	private Animable delegate;
	private IngressAnimableConstantMU ingressPart;

	public GeneralMaxfield(final Portal[][] corners, Portal[] interior, double delayBetweenPortals, RegionOfControl control, final Agent agent[], double[][] rendezvousPoints, double expansionDuration, final double linkDelay) {
		this(corners, interior, delayBetweenPortals, control, agent, rendezvousPoints, expansionDuration, linkDelay, mixWithDarkGrey(agent[0].getColor()));
	}

	private static Color mixWithDarkGrey(Color c0) {
		Color c1 = Color.DARK_GRAY;
		int r0 = c0.getRed();
		int g0 = c0.getGreen();
		int b0 = c0.getBlue();
		int r1 = c1.getRed();
		int g1 = c1.getGreen();
		int b1 = c1.getBlue();
		int r = (r1*9+r0)/10;
		int g = (g1*9+g0)/10;
		int b = (b1*9+b0)/10;
		return new Color(r,g,b);
	}

	public GeneralMaxfield(final Portal[][] corners, Portal[] interior, double delayBetweenPortals, RegionOfControl control, final Agent[] agent, final double[][] rendezvousPoints, double expansionDuration, final double linkDelay, Color controlColor) {
		this(corners, interior, delayBetweenPortals, control, agent, rendezvousPoints, expansionDuration, linkDelay, controlColor, false);
	}
	
	public GeneralMaxfield(final Portal[][] corners, Portal[] interior, double delayBetweenPortals, RegionOfControl control, Agent[] agent, double[][] rendezvousPoints, double expansionDuration, double linkDelay, Color controlColor, boolean fieldOnly) {
		this(corners, new Portal[][] {interior}, delayBetweenPortals, control, new Agent[][] {agent}, new double[][][] {rendezvousPoints}, expansionDuration, linkDelay, controlColor, fieldOnly, null);
	}
	
	public GeneralMaxfield(final Portal[][] corners, Portal[][] interiors, double delayBetweenPortals, RegionOfControl control, final Agent[][] agentGroups, final double[][][] rendezvousPts, double expansionDuration, final double linkDelay, Color controlColor, boolean fieldOnly, double[][][] exitPts) {
		if (agentGroups.length > 1 || interiors.length > 1) {
			fieldOnly = true;
			if (agentGroups.length != interiors.length) {
				throw new IllegalArgumentException("incompatible array lengths");
			}
		}
		int numberOfAgentGroups = agentGroups.length;
		if (numberOfAgentGroups == 0) {
			throw new IllegalArgumentException("no agent groups passed in!");
		}
		if (fieldOnly) {
			expansionDuration = 1e-6;
		}
		final double[][][] rendezvousPoints = fromArrayOrAgents(agentGroups, rendezvousPts);
		final double[][][] exitPoints = fromArrayOrAgents(agentGroups, exitPts);
		List<Animable> portalAnimations = new LinkedList<>();
		for (Portal[] pp : corners) {
			for (Portal p : pp) {
				portalAnimations.add(new PortalAnimation(new PortalHistory(p)));
			}
		}
		final Portal[][] allPortalsInOrder = new Portal[numberOfAgentGroups][];
		List<Agent> allAgentList = new LinkedList<>();
		List<int[]> allAgentIndexList = new LinkedList<>();
		for (int a=0; a<numberOfAgentGroups; a++) {
			for (int b=0; b<agentGroups[a].length; b++) {
				allAgentIndexList.add(new int[] {a,b});
				allAgentList.add(agentGroups[a][b]);
			}
		}
		final Agent[] allAgents = allAgentList.toArray(new Agent[0]);
		final int[][] allAgentIndices = allAgentIndexList.toArray(new int[0][0]);
		double finalTimeSoFar = 0;
		TreeMap<Double, Double> finalTimeToLambdaMap = null;
		Series finalFirstPart = null;

		final ArrayList<int[][]> allLinksToMake = new ArrayList<>();
		final ArrayList<Wait[]> allWaits = new ArrayList<>();
		final ArrayList<Wait[]> allWaitUntils = new ArrayList<>();
		final ArrayList<Wait> allFirstWaitUntils = new ArrayList<>();
		final ArrayList<int[]> allNearestAgents = new ArrayList<>();
		final ArrayList<Double> allStartTimes = new ArrayList<>();
		allStartTimes.add(0.0);
		
		for (int a=0; a<numberOfAgentGroups; a++) {
			Portal[] interior = interiors[a];
			Agent[] agents = agentGroups[a];
			for (Portal p : interior) {
				portalAnimations.add(new PortalAnimation(new PortalHistory(p)));
			}

			List<Animable> linkAnimations = new LinkedList<>();
			double delaySoFar = delayBetweenPortals;
			for (Portal[] p : corners) {
				for (int i=0; i<3; i++) {
					linkAnimations.add(new ActiveLink(delaySoFar, p[i], p[(i+1)%3], p[i].getInitialColor()).getBaseAnimation());
				}
			}

			LinkedList<Portal[]> triangles = new LinkedList<>();
			LinkedList<Portal[]> allTriangles = new LinkedList<>();
			LinkedList<Portal[]> links = new LinkedList<>();
			triangles.addAll(Arrays.asList(corners));
			allTriangles.addAll(Arrays.asList(corners));
			for (Portal[] p : corners) {
				addToLinks(links,p[0], p[1]);
				addToLinks(links,p[1], p[2]);
				addToLinks(links,p[2], p[0]);
			}
			for (int i=0; i<interior.length; i++) {
				delaySoFar += delayBetweenPortals;
				Portal splitter = interior[i];
				// find which triangle it's splitting
				int which = -1;
				for (int j=0; which<0 && j<triangles.size(); j++) {
					Portal[] triangle = triangles.get(j);
					if (GeomUtils.pointInTriangle(splitter.getPt(), triangle[0].getPt(), triangle[1].getPt(), triangle[2].getPt())) {
						which = j;
					}
				}
				if (which < 0) throw new IllegalStateException("splitter " + i+" failed to split!");
				// remove that triangle from the list
				Portal[] toSplit = triangles.get(which);
				triangles.remove(which);
				// create animations
				for (int k=0; k<3; k++) {
					linkAnimations.add(new ActiveLink(delaySoFar, toSplit[k], splitter, splitter.getInitialColor()).getBaseAnimation());
				}
				// add new triangles to the list
				triangles.add(new Portal[] {splitter, toSplit[0], toSplit[1]});
				triangles.add(new Portal[] {splitter, toSplit[1], toSplit[2]});
				triangles.add(new Portal[] {splitter, toSplit[2], toSplit[0]});
				allTriangles.add(new Portal[] {splitter, toSplit[0], toSplit[1]});
				allTriangles.add(new Portal[] {splitter, toSplit[1], toSplit[2]});
				allTriangles.add(new Portal[] {splitter, toSplit[2], toSplit[0]});
				links.add(new Portal[] {splitter, toSplit[0]});
				links.add(new Portal[] {splitter, toSplit[1]});
				links.add(new Portal[] {splitter, toSplit[2]});
			}
			List<Animable> allAnimations = new LinkedList<>();
			allAnimations.addAll(linkAnimations);
			allAnimations.addAll(portalAnimations);
			Series firstPart = new Series(new Parallel(allAnimations.toArray(new Animable[0])), new Pause(delayBetweenPortals));
			LinkedHashSet<Portal> cornerPortals = new LinkedHashSet<>();
			for (Portal[] pp : corners) {
				for (Portal p : pp) {
					cornerPortals.add(p); 
				}
			}
			final Map<Double, Portal> portalOrder = new TreeMap<>();
			for (Portal p : cornerPortals) {
				portalOrder.put(control.getLambda(p.getPt())+jitter(), p);
			}
			for (Portal p : interior) {
				portalOrder.put(control.getLambda(p.getPt())+jitter(), p);
			}

			double timeSoFar = firstPart.getDuration();

			final Portal[] portalsInOrder = portalOrder.values().toArray(new Portal[0]);
			final Double[] lambdasInOrder = portalOrder.keySet().toArray(new Double[0]);
			double lambdaRange = lambdasInOrder[lambdasInOrder.length-1];
			double timePerUnitLambda = expansionDuration/lambdaRange;

			// find out the links that need to be made for each Portal.
			final int[][] linksToMake = new int[portalsInOrder.length][];
			for (int i=0; i<portalsInOrder.length; i++) {
				List<Integer> myLinks = new LinkedList<>();
				for (int j=0; j<i; j++) {
					if (contains(links, portalsInOrder[i], portalsInOrder[j])) {
						myLinks.add(j);
					}
				}
				// to determine link order, we need to know what triangles are completed
				List<int[]> myTriangles = new LinkedList<>();
				Portal pi = portalsInOrder[i];
				for (int j=0; j<i; j++) {
					Portal pj = portalsInOrder[j];
					for (int k=0; k<j; k++) {
						Portal pk = portalsInOrder[k];
						if (contains(allTriangles, pi, pj, pk)) {
							myTriangles.add(new int[] {i,j,k});
						}
					}
				}
				// we want to form the biggest triangles first.
				TreeMap<Double, List<int[]>> triangleSorter = new TreeMap<>();
				for (int[] tri : myTriangles) {
					double area = GeomUtils.getArea(portalsInOrder[tri[0]].getPt(), portalsInOrder[tri[1]].getPt(), portalsInOrder[tri[2]].getPt());
					MapUtils.insert(triangleSorter, -area, tri);
				}
				LinkedList<int[]> sortedTriangles = new LinkedList<>();
				for (List<int[]> tri : triangleSorter.values()) {
					sortedTriangles.addAll(tri);
				}

				// now note the link order.
				boolean[] made = new boolean[i];
				linksToMake[i] = new int[myLinks.size()];
				int linkIndex = 0;
				for (int[] tri : sortedTriangles) {
					// tri[0] is i.
					for (int k=1; k<tri.length; k++) {
						if (made[tri[k]]) {
							continue; // already made.
						}
						linksToMake[i][linkIndex++] = tri[k];
						made[tri[k]] = true;
					}
				}
				// next, all the links which haven't been made yet
				for (int remaining : myLinks) {
					if (!made[remaining]) {
						linksToMake[i][linkIndex++] = remaining;
					}
				}
				// all done!
				allPortalsInOrder[a] = portalsInOrder;
			}


			PortalHistory[] ph = new PortalHistory[portalsInOrder.length];
			for (int i=0; i<portalsInOrder.length; i++) {
				ph[i] = new PortalHistory(portalsInOrder[i]);
			}
			AgentState[] state = new AgentState[agents.length];
			double maxTime = 0;
			for (int j=0; j<state.length; j++) {
				state[j] = agents[j].getInitialState();
				Wait._until(allStartTimes.get(allStartTimes.size()-1), false).obey(state[j]);
				new GoTo(rendezvousPoints[a][j][0], rendezvousPoints[a][j][1], false).obey(state[j]);
				maxTime = Math.max(maxTime, state[j].getTime());
			}
			final Wait firstWaitUntil = Wait._until(maxTime, false);
			for (int j=0; j<state.length; j++) {
				firstWaitUntil.obey(state[j]);
			}
			final Wait[] waits = new Wait[lambdasInOrder.length];
			final int[] nearestAgents = new int[lambdasInOrder.length];
			final Wait[] waitUntils = new Wait[lambdasInOrder.length];
			TreeMap<Double, Double> timeToLambdaMap = new TreeMap<>();
			timeToLambdaMap.put(0.0, 0.0);
			double secondPartTime = maxTime;
			double myLambda = 0;
			timeToLambdaMap.put(maxTime, 0.0);

			// repeat: 
			for (int i=0; i<lambdasInOrder.length; i++) {
				//    expand the RegionOfControl (update the timeToLambdaMap) (add a 'wait' to the instructions)
				double expandFor = (lambdasInOrder[i] - myLambda)*timePerUnitLambda;
				secondPartTime += expandFor;
				Wait wait = Wait._for(expandFor, false);
				waits[i] = wait;
				//instructions.add(wait);
				for (int j=0; j<state.length; j++) {
					wait.obey(state[j]); // update agent state
				}
				timeToLambdaMap.put(secondPartTime, lambdasInOrder[i]);
				//    move the nearest agent to the relevant portal (add to instructions)
				CapturePortal capturePortal = new CapturePortal(ph[i]);
				int nearestJ=-1;
				double nearness = Double.POSITIVE_INFINITY;
				for (int j=0; j<state.length; j++) {
					double dx = state[j].getX() - ph[i].getX();
					double dy = state[j].getY() - ph[i].getY();
					if (dx*dx+dy*dy < nearness) {
						nearestJ = j;
						nearness = dx*dx+dy*dy;
					}
				}
				nearestAgents[i] = nearestJ;
				//    capture it and link.
				capturePortal.obey(state[nearestJ]); // update agent state

				if (linksToMake[i].length > 0) {
					// need to link some portals... but which?
					PortalHistory[] linkTargets = new PortalHistory[linksToMake[i].length];
					for (int k=0; k<linkTargets.length; k++) {
						linkTargets[k] = ph[linksToMake[i][k]];
					}
					LinkPortals linkPortals = new LinkPortals(ph[i], linkDelay, linkTargets);
					linkPortals.obey(state[nearestJ]);
				}
				double timeNow = state[nearestJ].getTime();
				// make the other agents wait
				Wait waitUntil = Wait._until(timeNow, false);
				waitUntils[i] = waitUntil;
				for (int j=0; j<state.length; j++) {
					if (j == nearestJ) continue;
					waitUntil.obey(state[j]);
				}
				secondPartTime = timeNow;

				//    check the time, to update the timeToLambdaMap again.
				timeToLambdaMap.put(secondPartTime, lambdasInOrder[i]);

				//instructions.add(capturePortal);

			} // until all portals are captured
			for (int j=0; j<agents.length; j++) {
				new GoTo(exitPoints[a][j][0], exitPoints[a][j][1], false).obey(state[j]);
			}
			double endTime = state[0].getTime();
			double finalElapsed = endTime - secondPartTime;
			double deltaLambda = finalElapsed * lambdasInOrder[lambdasInOrder.length-1]/secondPartTime;
			timeToLambdaMap.put(endTime, lambdasInOrder[lambdasInOrder.length-1]+deltaLambda);
			finalTimeToLambdaMap = timeToLambdaMap;
			finalTimeSoFar = timeSoFar;
			finalFirstPart = firstPart;
			
			allLinksToMake.add(linksToMake);
			allWaits.add(waits);
			allWaitUntils.add(waitUntils);
			allFirstWaitUntils.add(firstWaitUntil);
			allNearestAgents.add(nearestAgents);
			allStartTimes.add(endTime);
			
		}
		
		IngressAnimableConstantMU clip = new IngressAnimableConstantMU() {
			
			@Override
			protected Portal[] getPortals() {
				return allPortalsInOrder[0];
			}
			
			@Override
			protected double getAreaPerMU() {
				double area = 0;
				for (Portal[] pp : corners) {
					area += GeomUtils.getArea(pp[0].getPt(), pp[1].getPt(), pp[2].getPt());
				}
				return area/1000;
			}
			
			@Override
			protected Agent[] getAgents() {
				return allAgents;
			}
			
			@Override
			protected void addInstructions(InstructionList[] lists, PortalHistory[] histories) {
				for (int k=0; k<lists.length; k++) {
					int[] agentIndices = allAgentIndices[k];
					InstructionList list = lists[k];
					int a = agentIndices[0];
					int b = agentIndices[1];
					
					int[][] linksToMake = allLinksToMake.get(a);
					Wait[] waits = allWaits.get(a);
					Wait[] waitUntils = allWaitUntils.get(a);
					Wait firstWaitUntil = allFirstWaitUntils.get(a);
					int[] nearestAgents = allNearestAgents.get(a);
					
					lists[k].addInstruction(new GoTo(rendezvousPoints[a][b][0], rendezvousPoints[a][b][1], false));
					lists[k].addInstruction(firstWaitUntil);
					int[] indexOfHistory = new int[allPortalsInOrder[a].length];
					for (int i=0; i<allPortalsInOrder[a].length; i++) {
						indexOfHistory[i] = getIndexInHistories(allPortalsInOrder[a], histories, i);
					}
					for (int i=0; i<allPortalsInOrder[a].length; i++) {
						list.addInstruction(waits[i]);
						if (nearestAgents[i] != b) {
							// just wait
							list.addInstruction(waitUntils[i]);
							continue;
						} 
						PortalHistory thisPH = histories[indexOfHistory[i]];
						list.addInstructions(new CapturePortal(thisPH));
						if (linksToMake[i].length > 0) {
							PortalHistory[] linkTargets = new PortalHistory[linksToMake[i].length];
							for (int l=0; l<linksToMake[i].length; l++) {
								linkTargets[l] = histories[indexOfHistory[linksToMake[i][l]]];
							}
							list.addInstruction(new LinkPortals(thisPH, linkDelay, linkTargets));
						}
					}
					list.addInstruction(new GoTo(exitPoints[a][b][0], exitPoints[a][b][1], false));
				}
			}

			private int getIndexInHistories(final Portal[] portalsInOrder,
					PortalHistory[] histories, int i) {
				int index = -1;
				for (int j=0; j<histories.length;j++) {
					if (histories[j].getPortal().equals(portalsInOrder[i])) {
						index = j;
					}
				}
				return index;
			}
		};
		this.ingressPart = clip;
		Animable ingressAnimation = clip.getAnimation();
		if (fieldOnly) {
			this.delegate = ingressAnimation;
			return;
		}
		// delay the RoC and the IngressAnimable (below) with Pauses in series
		Animable controlAnimation = control.animateRegion(finalTimeToLambdaMap, controlColor);
		controlAnimation = new Series(new Pause(finalTimeSoFar), controlAnimation);
		ingressAnimation = new Series(new Pause(finalTimeSoFar), ingressAnimation);
		// run RoC, firstPart, IngressAnimable in parallel 
		Parallel fullAnimation = new Parallel(controlAnimation, finalFirstPart, ingressAnimation);
		this.delegate = fullAnimation;
		
	}

	private double[][][] fromArrayOrAgents(final Agent[][] agentGroups,
			final double[][][] pts) {
		double[][][] points;
		if (pts == null) {
			points = new double[agentGroups.length][][];
		} else {
			points = pts;
		}
		for (int a=0; a<agentGroups.length; a++) {
			if (points[a] == null) {
				points[a] = new double[agentGroups[a].length][];
				for (int b=0; b<agentGroups[a].length; b++) {
					points[a][b] = new double[] {agentGroups[a][b].getStartX(), agentGroups[a][b].getStartY()};
				}
			}
		}
		return points;
	}
	
	private void addToLinks(LinkedList<Portal[]> links, Portal p, Portal q) {
		if (!contains(links, p, q)) {
			links.add(new Portal[] {p,q});
		}
	}

	private boolean contains(LinkedList<Portal[]> links, Portal p1,	Portal p2) {
		for (Portal[] pp : links) {
			if (pp[0].equals(p1) && pp[1].equals(p2)) {
				return true;
			}
			if (pp[0].equals(p2) && pp[1].equals(p1)) {
				return true;
			}
		}
		return false;
	}

	private boolean contains(LinkedList<Portal[]> triangles, Portal p1,	Portal p2, Portal p3) {
		for (Portal[] pp : triangles) {
			// ugly :(
			if (pp[0].equals(p1) && pp[1].equals(p2) && pp[2].equals(p3)) {
				return true;
			}
			if (pp[0].equals(p1) && pp[1].equals(p3) && pp[2].equals(p2)) {
				return true;
			}
			if (pp[0].equals(p2) && pp[1].equals(p1) && pp[2].equals(p3)) {
				return true;
			}
			if (pp[0].equals(p2) && pp[1].equals(p3) && pp[2].equals(p1)) {
				return true;
			}
			if (pp[0].equals(p3) && pp[1].equals(p2) && pp[2].equals(p1)) {
				return true;
			}
			if (pp[0].equals(p3) && pp[1].equals(p1) && pp[2].equals(p2)) {
				return true;
			}
		}
		return false;
	}


	private static Random jitterer = new Random(0x23984024);
	private static double jitter() {
		return jitterer.nextGaussian()*1e-8;
	}

	@Override
	public double getDuration() {
		return delegate.getDuration();
	}

	@Override
	public void draw(Graphics gr, ScaleAnimation scale, double t) {
		delegate.draw(gr, scale, t);
	}

	public IngressAnimableConstantMU getIngressPart() {
		return ingressPart;
	}

	
}
