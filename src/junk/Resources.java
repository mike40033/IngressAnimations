package junk;

import java.util.Arrays;
import java.util.Random;

public class Resources {
	public static void main(String[] args) {
		int[][] upgrades = {
				{135,7,3,1},
//				{22,19,33,1},
				{135,2,8,0},
//				{34,2,42,0},
				{135,7,3,0},
//				{22,19,33,0},
//				{135,2,0,0},
//				{34,2,0,0},
//			
		};
		double current = 445641.03;
		double target = 500000 - current;
		int[][] bases = {
				{1264,1},
				{1176,1},
				{1136,2},
				{1088,2},
				{1048,4},
				{1000,1},
				{960,4},
				{912,5},
				{872,1},
				{824,2},
				{784,10},
				{736,2},
				{696,5},
				{648,6},
				{608,7},
				{568,12},
				{520,1},
				{480,4},
				{432,1}
		};
		int n = 0;
		for (int i=0; i<bases.length; i++) {
			n += bases[i][1];
		}
		int[] mines = new int[n];
		for (int i=0, j=0; j<n; i++) {
			for (int k=0; k<bases[i][1]; k++,j++) {
				mines[j] = bases[i][0];
			}
		}
		double[] multipliers = new double[upgrades.length];
		for (int i=0; i<multipliers.length; i++) {
			multipliers[i] = Math.pow(1.01, upgrades[i][0])*Math.pow(1.02, upgrades[i][1])*Math.pow(1.03, upgrades[i][2])*Math.pow(1.05, upgrades[i][3])-1;
		}
		int[] best = null;
		double bestValue= 0;
		Random rr = new Random(474);
		while (true) {
			int[] mult = new int[mines.length];
			Arrays.fill(mult, -1);
			double additional = 0;
			while (additional < target) {
				int next = rr.nextInt(mines.length);
				if (mult[next] >= 0) continue;
				mult[next] = rr.nextInt(upgrades.length);
				additional += mines[next]*multipliers[mult[next]];
				if (Math.abs(additional-target) >= Math.abs(bestValue-target)) {
					continue;
				}
				bestValue = additional;
				best = mult.clone();
				System.out.println("==== new best: "+(bestValue+current)+" ====");
				for (int i=0; i<mult.length; i++) {
					if (mult[i] >= 0) {
						System.out.println(mines[i]+" : "+Arrays.toString(upgrades[mult[i]]));
					}
				}
			}
		}
	}
}
