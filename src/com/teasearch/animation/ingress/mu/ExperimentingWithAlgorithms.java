package com.teasearch.animation.ingress.mu;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.teasearch.animation.ingress.portals.Portal;

public class ExperimentingWithAlgorithms {

	private static final class PDFEstimator  {
		private final int m;

		private PDFEstimator(int m) {
			this.m = m;
		}

		Portal t1 = new Portal(0, 0, Color.gray);
		Portal t2 = new Portal(100, 0, Color.gray);
		Portal t3 = new Portal(0, 100, Color.gray);
		ExhaustiveSearch opt1 = new ExhaustiveSearch();
		GreedySearch opt2 = new GreedySearch();
		Portal[] triangle= {t1,t2,t3};
		TreeMap<Double, Integer> pdf = new TreeMap<>();
		int count = 0;

		public void runBlock() {
			for (int blockCount =0; blockCount<1000; blockCount++) {
				List<Portal> interior= new LinkedList<>();
				while (interior.size() < m) {
					double x = Math.random();
					double y = Math.random();
					if (x+y >= 100) continue;
					interior.add(new Portal(x, y, Color.gray));
				}
				double areaPerMU = 100*100/2.0/1000;
				TreeMap<Double, Integer> order1 = opt1.getMUHistogram(triangle, interior, areaPerMU);
				List<Portal> greedyBest = opt2.findBestSplittingOrder(triangle, interior);
				double mu = MUCalculator.calculateMU(areaPerMU, new Portal[][] {triangle}, greedyBest.toArray(new Portal[0]));
				Double[] muList = order1.keySet().toArray(new Double[0]);
				int rank = 1;
				for (int i=muList.length-rank; i>=0; i--,rank++) {
					if (Math.abs(muList[i]-mu) < 1e-3) {
						break;
					}
				}
				if (rank > muList.length) {
					continue;
				}
				count++;
				insert(pdf, Math.floor((rank-1.0)/muList.length*20)*5);
			}
			System.out.println(m+" : "+count+": "+pdf);
		}
	}

	public static void main(String[] args) {
		List<PDFEstimator> est = new LinkedList<>();
		for (int n = 3; n<=12; n++) {
			est.add(new PDFEstimator(n));
		}
		while (true) {
			for (PDFEstimator e : est) {
				e.runBlock();
			}
		}
	}

	private static void insert(TreeMap<Double, Integer> pdf, double rank) {
		if (pdf.containsKey(rank)) {
			pdf.put(rank, pdf.get(rank)+1);
		} else {
			pdf.put(rank, 1);
		}
	}

	public static void mai1n(String[] args) {
		int n = 3;
		Portal t1 = new Portal(0, 0, Color.gray);
		Portal t2 = new Portal(100, 0, Color.gray);
		Portal t3 = new Portal(0, 100, Color.gray);
		MUOptimiser opt1 = new ExhaustiveSearch();
		MUOptimiser opt2 = new GreedySearch();
		Portal[] triangle= {t1,t2,t3};
		while (true) {
			List<Portal> interior= new LinkedList<>();
			while (interior.size() < n) {
				double x = Math.floor(Math.random()*99+1);
				double y = Math.floor(Math.random()*99+1);
				if (x+y >= 100) continue;
				interior.add(new Portal(x, y, Color.gray));
			}
			List<Portal> order1 = opt1.findBestSplittingOrder(triangle, interior);
			List<Portal> order2 = opt2.findBestSplittingOrder(triangle, interior);
			double mu1 = MUCalculator.calculateMU(100*100/2.0/1000,new Portal[][] {triangle}, order1.toArray(new Portal[0]));
			double mu2 = MUCalculator.calculateMU(100*100/2.0/1000,new Portal[][] {triangle}, order2.toArray(new Portal[0]));
			if (mu2 != mu1) {
				System.out.println(mu1+" : "+points(order1));
				System.out.println(mu2+" : "+points(order2));
				break;
			}
		}
	}

	private static String points(List<Portal> order1) {
		String s = "";
		for (Portal p : order1) {
			if (s.length() == 0) {
				s += ", ";
			}
			s += Arrays.toString(p.getPt());
		}
		return s;
	}
}
