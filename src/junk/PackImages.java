package junk;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.teasearch.animation.clips.MovieClip;

public class PackImages {
	public static void main(String[] args) throws IOException {
		String[] pre = {"CRE","MON","GIN","PIP"};
		int[] lastFrame = {2089,2047,1926,1724};
		for (int frame = 205; frame < 2090; frame++) {
			int[] myFrames = new int[4];
			if (frame <= 210) {
				String[] myPre = new String[4];
				Arrays.fill(myPre,"PIN");
				Arrays.fill(myFrames, frame);
				montage(myPre, myFrames, frame);
				continue;
			}
			for (int i=0; i<4; i++) {
				int thisFrame = frame - 2089 + lastFrame[i];
				if (thisFrame < 210) thisFrame = 210;
				myFrames[i] = thisFrame;
			}
			montage(pre, myFrames, frame);
		}
	}

	private static void montage(String[] myPre, int[] myFrames, int frame) throws IOException {
		System.out.println(Arrays.toString(myPre)+"@"+Arrays.toString(myFrames)+" --> "+frame);
		File[] f = new File[4];
		for (int i=0; i<4; i++) {
			f[i] = buildFile(myPre[i], myFrames[i]);
		}
		File output = buildFile("FOUR",frame);
		BufferedImage bi[] = new BufferedImage[4];
		for (int i=0; i<4; i++) {
			try {
				bi[i] = ImageIO.read(f[i]);
			} catch (IOException ioe) {
				System.out.println("failed to load "+f[i].getName());
				throw ioe;
			}
		}
		int w = bi[0].getWidth();
		int h = bi[0].getHeight();
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int i=0; i<w; i++) {
			for (int j=0; j<h; j++) {
				int k = i*2%w;
				int l = j*2%h;
				int m = i*2/w + j*2/h*2;
				int[] rgbs = {bi[m].getRGB(k, l),bi[m].getRGB(k+1, l),bi[m].getRGB(k, l+1),bi[m].getRGB(k+1, l+1)};
				int rgb = MovieClip.merge(rgbs);
				out.setRGB(i, j, rgb);
			}
		}
		ImageIO.write(out,"png",output);
	}

	private static File buildFile(String pre, int frame) {
		String number = to4digits(frame);
		File f = new File("/home/hartleym/Desktop/Animation/Ingress/General/"+pre+"/smaller/");
		f.mkdirs();
		f = new File(f,(pre.equals("PIP") ? "PIN" : pre)+number+".png");
		return f;
	}

	private static String to4digits(int frame) {
		String rtn = ""+frame;
		while (rtn.length()<4) {
			rtn = "0"+rtn;
		}
		return rtn;
	}
}
