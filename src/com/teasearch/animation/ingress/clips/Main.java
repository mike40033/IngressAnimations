package com.teasearch.animation.ingress.clips;

import java.io.File;

import com.teasearch.animation.ingress.clips.youtube.CobwebClip;
import com.teasearch.animation.ingress.clips.youtube.FanFieldClip1;
import com.teasearch.animation.ingress.clips.youtube.FanFieldClip2;
import com.teasearch.animation.ingress.clips.youtube.FanFieldClip3;
import com.teasearch.animation.ingress.clips.youtube.FanFieldClip4;
import com.teasearch.animation.ingress.clips.youtube.FanFieldClip5;
import com.teasearch.animation.ingress.clips.youtube.FanFieldClip6;
import com.teasearch.animation.ingress.clips.youtube.HerringboneClip;
import com.teasearch.animation.ingress.clips.youtube.NianticLogoClip;

public class Main {
	
//	private static int imageWidth = 400;
//	private static  int imageHeight = 300;
//	private static double frameRate = 10;
	private static int imageWidth = 2560;
	private static int imageHeight = 1920;
	private static double frameRate = 25;

	public static void main(String[] args) {
//		new Thread() {public void run() {makeFanField6();}}.start();
		new Thread() {public void run() {makeFanField5();}}.start();
//		new Thread() {public void run() {makeFanField3();}}.start();
//		new Thread() {public void run() {makeFanField4();}}.start();
//		new Thread() {public void run() {makeNianticLogo();}}.start();
//		new Thread() {public void run() {makeHerringbone();}}.start();
//		new Thread() {public void run() {makeC2obweb();}}.start();
	}
	
	public static void makeHerringbone() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/Herringbone");
		folder.mkdirs();
		String filePrefix = "herring"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 800;
		HerringboneClip herringbone = new HerringboneClip(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		herringbone.makeClip();
	}
	
	public static void makeCobweb() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/Cobweb");
		folder.mkdirs();
		String filePrefix = "cobweb"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 800;
		CobwebClip cobweb = new CobwebClip(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		cobweb.makeClip();
	}
	
	public static void makeNianticLogo() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/NianticLogo");
		folder.mkdirs();
		String filePrefix = "niantic"; 
		double fieldCentreX = 0;
		double fieldCentreY = -125;
		double fieldRadius = 700;
		NianticLogoClip logo = new NianticLogoClip(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		logo.makeClip();
	}
	
	public static void makeFanField1() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/FanField1");
		folder.mkdirs();
		String filePrefix = "ff"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 700;
		FanFieldClip1 ff = new FanFieldClip1(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		ff.makeClip();
	}
	
	public static void makeFanField2() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/FanField2");
		folder.mkdirs();
		String filePrefix = "ff"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 700;
		FanFieldClip2 ff = new FanFieldClip2(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		ff.makeClip();
	}

	public static void makeFanField3() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/FanField3");
		folder.mkdirs();
		String filePrefix = "ff"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 700;
		FanFieldClip3 ff = new FanFieldClip3(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		ff.makeClip();
	}


	public static void makeFanField4() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/FanField4");
		folder.mkdirs();
		String filePrefix = "ff"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 700;
		FanFieldClip4 ff = new FanFieldClip4(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		ff.makeClip();
	}

	public static void makeFanField5() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/FanField5");
		folder.mkdirs();
		String filePrefix = "ff"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 700;
		FanFieldClip5 ff = new FanFieldClip5(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		ff.makeClip();
	}

	public static void makeFanField6() {
		File folder = new File("/home/hartleym/Desktop/Animation/Ingress/FanField6");
		folder.mkdirs();
		String filePrefix = "ff"; 
		double fieldCentreX = 0;
		double fieldCentreY = 0;
		double fieldRadius = 700;
		FanFieldClip6 ff = new FanFieldClip6(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius, folder, filePrefix, frameRate);
		ff.makeClip();
	}

	
}
