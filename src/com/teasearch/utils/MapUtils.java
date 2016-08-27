package com.teasearch.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teasearch.animation.ingress.agent.instructions.basic.BasicInstruction;

public class MapUtils {
	public static <U,V> void insert(Map<U,List<V>> map, U u, V v) {
		if (!map.containsKey(u)) {
			map.put(u,new LinkedList<V>());
		}
		map.get(u).add(v);
	}

	public static void insertAll(
			TreeMap<Double, List<BasicInstruction>> target,
			TreeMap<Double, List<BasicInstruction>> steps) {
		for (double d : steps.keySet()) {
			for (BasicInstruction b : steps.get(d)) {
				MapUtils.insert(target, d, b);
			}
		}
		// TODO Auto-generated method stub
		
	}
}
