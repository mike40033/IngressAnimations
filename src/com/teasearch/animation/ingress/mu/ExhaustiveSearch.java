package com.teasearch.animation.ingress.mu;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.text.rtf.RTFEditorKit;

import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.utils.GeomUtils;

public class ExhaustiveSearch extends MUOptimiser {

	private boolean best;

	public ExhaustiveSearch() {
		this(true);
	}
	
	public ExhaustiveSearch(boolean best) {
		this.best = best;
	}
	
	@Override
	public List<Portal> findBestSplittingOrder(Portal[] triangle, List<Portal> interior) {
		if (interior.size() <= 1) {
			return interior;
		}
		LinkedList<Portal[]> triangles = new LinkedList<>();
		triangles.add(triangle);
		LinkedList<List<Portal>> interiors = new LinkedList<>();
		interiors.add(interior);
		List<Portal> bestSplit = null;
		double mu = -1;
		for (Portal p : interior) {
			Portal[] t1 = {triangle[0],triangle[1],p};
			Portal[] t2 = {triangle[1],triangle[2],p};
			Portal[] t3 = {triangle[2],triangle[0],p};
			LinkedList<Portal> int1 = new LinkedList<>();
			LinkedList<Portal> int2 = new LinkedList<>();
			LinkedList<Portal> int3 = new LinkedList<>();
			for (Portal q : interior) {
				if (q.equals(p)) continue;
				if (contains(t1,q)) int1.add(q);
				else if (contains(t2,q)) int2.add(q);
				else int3.add(q); // assumes all interior portals are in this triangle
			}
			List<Portal> thisSplit = new LinkedList<>();
			thisSplit.add(p);
			thisSplit.addAll(findBestSplittingOrder(t1, int1));
			thisSplit.addAll(findBestSplittingOrder(t2, int2));
			thisSplit.addAll(findBestSplittingOrder(t3, int3));
			double thisMU = MUCalculator.calculateMU(1.0, new Portal[][] {triangle}, thisSplit.toArray(new Portal[0]));
			if (bestSplit == null || best == mu < thisMU) {
				bestSplit = thisSplit;
				mu = thisMU;
			}
		}
		return bestSplit;
	}

	public static boolean contains(Portal[] t, Portal q) {
		return GeomUtils.pointInOrOnTriangle(q.getPt(), t[0].getPt(), t[1].getPt(), t[2].getPt());
	}
	
	public TreeMap<Double,Integer> getMUHistogram(Portal[] triangle, List<Portal> interior, double areaPerMU) {
		double area = GreedySearch.getArea(triangle);
		TreeMap<Double, Integer> histogram = new TreeMap<>();
		if (interior.size() <= 1) {
			histogram.put((interior.size()+1)*area/areaPerMU, 1);
			return histogram;
		}
		LinkedList<Portal[]> triangles = new LinkedList<>();
		triangles.add(triangle);
		LinkedList<List<Portal>> interiors = new LinkedList<>();
		interiors.add(interior);
		double mu = area/areaPerMU;
		TreeMap<Double, Integer> rtn = new TreeMap<>();
		for (Portal p : interior) {
			Portal[] t1 = {triangle[0],triangle[1],p};
			Portal[] t2 = {triangle[1],triangle[2],p};
			Portal[] t3 = {triangle[2],triangle[0],p};
			LinkedList<Portal> int1 = new LinkedList<>();
			LinkedList<Portal> int2 = new LinkedList<>();
			LinkedList<Portal> int3 = new LinkedList<>();
			for (Portal q : interior) {
				if (q.equals(p)) continue;
				if (contains(t1,q)) int1.add(q);
				else if (contains(t2,q)) int2.add(q);
				else int3.add(q); // assumes all interior portals are in this triangle
			}
			TreeMap<Double, Integer> h1 = getMUHistogram(t1, int1, areaPerMU);
			TreeMap<Double, Integer> h2 = getMUHistogram(t2, int2, areaPerMU);
			TreeMap<Double, Integer> h3 = getMUHistogram(t3, int3, areaPerMU);
			for (Double d1 : h1.keySet()) {
				for (Double d2 : h2.keySet()) {
					for (Double d3 : h3.keySet()) {
						double d = mu + d1 + d2 + d3;
						int  i = h1.get(d1)+h2.get(d2)+h3.get(d3);
						if (rtn.containsKey(d)) {
							rtn.put(d, rtn.get(d)+i);
						} else {
							rtn.put(d, i);
						}
					}
				}
			}
		}
		return rtn;
	}


}
