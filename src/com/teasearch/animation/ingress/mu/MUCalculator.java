package com.teasearch.animation.ingress.mu;

import java.util.Arrays;
import java.util.LinkedList;

import com.teasearch.animation.ingress.portals.Portal;
import com.teasearch.utils.GeomUtils;

public class MUCalculator {
	
	public static double calculateMU(double areaPerMU, Portal[][] corners, Portal[] interior) {
		double area = 0;
		for (Portal[] p : corners) {
			area += GeomUtils.getArea(p[0].getPt(), p[1].getPt(), p[2].getPt());
		}
		LinkedList<Portal[]> triangles = new LinkedList<>();
		triangles.addAll(Arrays.asList(corners));
		for (int i=0; i<interior.length; i++) {
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
			area += GeomUtils.getArea(toSplit[0].getPt(), toSplit[1].getPt(), toSplit[2].getPt());
			triangles.remove(which);
			// create animations
			// add new triangles to the list
			triangles.add(new Portal[] {splitter, toSplit[0], toSplit[1]});
			triangles.add(new Portal[] {splitter, toSplit[1], toSplit[2]});
			triangles.add(new Portal[] {splitter, toSplit[2], toSplit[0]});
		}
		return area / areaPerMU;
	}

}
