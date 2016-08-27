package com.teasearch.animation.clips;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.teasearch.animation.Animable;
import com.teasearch.animation.AxisAlignedScale;
import com.teasearch.animation.Scale;
import com.teasearch.animation.ScaleAnimation;

public abstract class MovieClip {

	private File folder;
	private String filePrefix;
	private double frameRate;
	private boolean scaleOutput;
	private ScaleAnimation imageScale;

	public MovieClip(
			int imageWidth, 
			int imageHeight, 
			double fieldCentreX, 
			double fieldCentreY, 
			double fieldRadius,
			File folder,
			String filePrefix,
			double frameRate,
			boolean scaleOutput) {
		this(new AxisAlignedScale(imageWidth, imageHeight, fieldCentreX, fieldCentreY, fieldRadius),
				folder, 
				filePrefix,
				frameRate,
				scaleOutput);
	}
	public MovieClip(
			ScaleAnimation imageScale,
			File folder,
			String filePrefix,
			double frameRate,
			boolean scaleOutput) {
				this.imageScale = imageScale;
				this.folder = folder;
				this.filePrefix = filePrefix;
				this.frameRate = frameRate;
				this.scaleOutput = scaleOutput;
	}
	

	public File getFolder() {
		return folder;
	}



	public String getFilePrefix() {
		return filePrefix;
	}



	public double getFrameRate() {
		return frameRate;
	}

	public void makeClip() {
		
		Animable parallel = getAnimation();
		
		// grab images and output. (!! except for the file/folder names)
		
		ScaleAnimation scale = getScaleAnimation();
		for (int i=1; i<=parallel.getDuration()*getFrameRate()+1; i++) {
			if (i == 297) {
				System.out.println("MovieClip.makeClip()");
			}
			BufferedImage im = new BufferedImage(scale.getWidth(), scale.getHeight(), BufferedImage.TYPE_INT_RGB);
			double t = ((double)i)/getFrameRate();
			parallel.draw(im.getGraphics(), scale, t);
			if (this.scaleOutput) {
				im = scale(im);
			}
			try {
				ImageIO.write(im, "png",new File(getFolder(), getFilePrefix()+formatFrameNumber(i,parallel.getDuration()*getFrameRate()+1)+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	private ScaleAnimation getScaleAnimation() {
		return imageScale;
	}

	private BufferedImage scale(BufferedImage im) {
		int w = im.getWidth()/2;
		int h = im.getHeight()/2;
		BufferedImage rtn= new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				int rgb1 = im.getRGB(2*i, 2*j);
				int rgb2 = im.getRGB(2*i, 2*j+1);
				int rgb3 = im.getRGB(2*i+1, 2*j);
				int rgb4 = im.getRGB(2*i+1, 2*j+1);
				int rgb = merge(new int[] {rgb1, rgb2, rgb3, rgb4});
				rtn.setRGB(i, j, rgb);
			}
		}
		return rtn;
	}



	public static int merge(int[] rgbs) {
		int r = 0;
		int g = 0;
		int b = 0;
		for (int rgb : rgbs) {
			Color color = new Color(rgb);
			r += color.getRed();
			g += color.getGreen();
			b += color.getBlue();
		}
		float n = rgbs.length;
		return new Color(Math.round(r/n),Math.round(g/n),Math.round(b/n)).getRGB();
	}



	public abstract Animable getAnimation();

	private static String formatFrameNumber(int i, double d) {
		return formatFrameNumber(i, (int)Math.ceil(d));
	}

	private static String formatFrameNumber(int i, int max) {
		String rtn = "";
		while (max > 0) {
			rtn = (i%10)+rtn;
			i /= 10;
			max /= 10;
		}
		return rtn;
	}

}	
