package junk;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Numbers {
	public static String munge(String digits) {
		if (digits.length() != 10) return null;
		int[] histogram = new int[10];
		for (int i=0; i<digits.length(); i++) {
			char ch = digits.charAt(i);
			if (ch < '0' || ch > '9') return null;
			histogram[ch-'0']++;
		}
		String rtn = "";
		for (int i : histogram) {
			rtn += i;
		}
		return rtn;
	}
	
	public static List<String> loop(String s) {
		String fast = s;
		String slow = s;
		do {
			fast = munge(fast);
			if (fast == null) return null;
			fast = munge(fast);
			if (fast == null) return null;
			slow = munge(slow);
		} while (!slow.equals(fast));
		
		List<String> rtn = new LinkedList<>();
		do {
			fast = munge(fast);
			rtn.add(fast);
		} while (!slow.equals(fast));
		return rtn;
	}
	
	public static void main(String[] args) {
		HashSet<List<String>> all = new HashSet<>();
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		for (long i=0; i<1000000000L; i++) {
			String s = ""+i;
			while (s.length() < 10) {
				s = "0"+s;
			}
			List<String> t = loop(s);
			if (t == null) {
				count1++;;
			} else if (t.size() == 1) {
				count2++;
			} else if (t.size() == 2) {
				count3++;
			}
			if (i % 100000 == 0) {
				System.out.println(count1+","+count2+","+count3);
			}
			if (!all.contains(t)) {
				all.add(t);
				System.out.println(t);
			}
		}
	}
}
