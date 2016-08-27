package com.teasearch.animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Mian {
	public static void main(String[] args) throws IOException {
		Background bg = new Background(Color.yellow, Color.green, 3);
		Line l1 = new Line(Color.black, new BasicStroke(2f), -1, 0, 1, 0, 1);
		Line l2 = new Line(Color.black, new BasicStroke(2f), 1, 0, 0, Math.sqrt(3), 1);
		Line l3 = new Line(Color.black, new BasicStroke(2f), 0, Math.sqrt(3), -1, 0, 1);
		Series series = new Series(l1,l2,l3);
		Easing easing = new Easing(series, true, false);
		Parallel parallel = new Parallel(bg, easing);
		Scale scale = new AxisAlignedScale(400, 400, 0.0, 0.0, 2.0);
		for (int i=0; i<=75; i++) {
			BufferedImage im = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
			parallel.draw(im.getGraphics(), scale, i/25.0);
			ImageIO.write(im, "png",new File("/home/hartleym/Desktop/Animation/triangle"+i+".png"));
		}
		
	}
}
