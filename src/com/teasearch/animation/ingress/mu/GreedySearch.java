package com.teasearch.animation.ingress.mu;

import java.util.LinkedList;
import java.util.List;

import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.utils.GeomUtils;

public class GreedySearch extends MUOptimiser {

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
				if (ExhaustiveSearch.contains(t1,q)) int1.add(q);
				else if (ExhaustiveSearch.contains(t2,q)) int2.add(q);
				else int3.add(q); // assumes all interior portals are in this triangle
			}
			List<Portal> thisSplit = new LinkedList<>();
			thisSplit.add(p);
			thisSplit.addAll(findBestSplittingOrder(t1, int1));
			thisSplit.addAll(findBestSplittingOrder(t2, int2));
			thisSplit.addAll(findBestSplittingOrder(t3, int3));
			double thisMU = getArea(triangle) + getArea(t1)*int1.size()+getArea(t2)*int2.size()+getArea(t3)*int3.size();
			if (bestSplit == null || mu < thisMU) {
				bestSplit = thisSplit;
				mu = thisMU;
			}
		}
		return bestSplit;
	}

	public static double getArea(Portal[] triangle) {
		return GeomUtils.getArea(triangle[0].getPt(), triangle[1].getPt(), triangle[2].getPt());
	}

}
