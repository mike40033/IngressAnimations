package com.teasearch.animation.ingress.stats;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.EnumMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.teasearch.animation.Animable;
import com.teasearch.animation.ScaleAnimation;
import com.teasearch.animation.ingress.mu.methods.MUMethod;
import com.teasearch.animation.ingress.portals.Portal;

public class StatHistory {
	
	Map<StatType, Map<Double, Integer>> events = new EnumMap<>(StatType.class);
	Map<StatType, TreeMap<Double, Integer>> history;
	private Color color;
	private MUMethod areaPerMU;

	public StatHistory(Color color, MUMethod areaPerMU) {
		this.color = color;
		this.areaPerMU = areaPerMU;
		for (StatType type : StatType.values()) {
			events.put(type, new TreeMap<Double, Integer>());
		}
	}
	
	private void compileHistory() {
		history = new EnumMap<>(StatType.class);
		for (StatType type : StatType.values()) {
			history.put(type, new TreeMap<Double, Integer>());
			int stat = 0;
			for (Double time : events.get(type).keySet()) {
				stat += events.get(type).get(time);
				history.get(type).put(time, stat);
			}
		}
	}
	
	public void addStatChange(double time, StatType type, int changeAmount) {
		history = null;
		Map<Double, Integer> map = events.get(type);
		map.put(time, map.containsKey(time) ? map.get(time)+changeAmount : changeAmount);
	}
	
	public void destroyPortal(double time) {
		addStatChange(time, StatType.AP, 75*8);
	}

	public void capturePortal(double time) {
		addStatChange(time, StatType.AP, 125*8+250+500);
		//addStatChange(time, StatType.Captures, 1);
	}
	
	public void destroyFields(int number, double time) {
		addStatChange(time, StatType.AP, number*750);
	}
	public void destroyLinks(int number, double time) {
		addStatChange(time, StatType.AP, 187*number);
	}
	public void createField(double time, double area, Portal src, Portal dest, Portal other) {
		addStatChange(time, StatType.AP, 1250);
		addStatChange(time, StatType.Fields, 1);
		addStatChange(time, StatType.MU, areaPerMU.calcMU(area, src, dest, other));
	}
	public void createLink(double time) {
		addStatChange(time, StatType.AP, 313);
		addStatChange(time, StatType.Links, 1);
	}

	public static void merge(Map<Color, StatHistory> base,	Map<Color, StatHistory> toAdd) {
		for (Color c : toAdd.keySet()) {
			if (!base.containsKey(c)) {
				base.put(c, toAdd.get(c));
			} else {
				base.get(c).mergeWith(toAdd.get(c));
			}
		}
	}

	private void mergeWith(StatHistory other) {
		for (StatType type : StatType.values()) {
			if (!other.events.containsKey(type)) {
				continue;
			}
			for (Double time : other.events.get(type).keySet()) {
				addStatChange(time, type, other.events.get(type).get(time));
			}
		}
	}

	public Animable animatePanel(final double duration, final Corner corner) {
		return new Animable() {
			
			@Override
			public double getDuration() {
				return duration;
			}
			
			@Override
			public void draw(Graphics gr, ScaleAnimation scale, double t) {
				int width = scale.getWidth();
				int height = scale.getHeight();
				gr.setFont(gr.getFont().deriveFont(height/25f));
				gr.setColor(color);
				Map<StatType, Integer> stats = getStatsAt(t);
				boolean allZero = true;
				for (int stat : stats.values()) {
					if (stat != 0) allZero = false;
				}
				if (allZero) return;
				int number = stats.size();
				double[] textBottom = new double[number];
				double[] textLeft = new double[number];
				String[] texts = new String[number];
				int index = 0;
				for (StatType type : stats.keySet()) {
					String text = type + ": " + stats.get(type);
					Rectangle2D stringBounds = gr.getFontMetrics().getStringBounds(text, gr);
					double textWidth = stringBounds.getWidth();
					double textHeight = stringBounds.getHeight()+width/100;
					textBottom[index] = index == 0 ? textHeight : textBottom[index-1]+textHeight;
					textLeft[index] = corner.isLeftAligned() ? width/100 : width*99/100-textWidth;
					texts[index] = text;
					index++;
				}
				double panelHeight = textBottom[number-1];
				for (int i=0; i<number; i++) {
					if (corner.isTopAligned()) {
						textBottom[i] += height/100;
					} else {
						textBottom[i] += height*99/100-panelHeight;
					}
				}
				for (int i=0; i<number; i++) {
					gr.drawString(texts[i], (int)Math.round(textLeft[i]), (int)Math.round(textBottom[i]));
				}
			}
		};
	}

	protected Map<StatType, Integer> getStatsAt(double t) {
		compileHistory();
		Map<StatType, Integer> rtn = new EnumMap<>(StatType.class);
		for (StatType type : StatType.values()) {
			SortedMap<Double, Integer> headMap = history.get(type).headMap(t);
			if (headMap.isEmpty()) {
				rtn.put(type, 0);
			} else {
				rtn.put(type, headMap.get(headMap.lastKey()));
			}
		}
		return rtn;
	}
}
