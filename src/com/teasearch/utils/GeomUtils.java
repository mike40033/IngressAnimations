package com.teasearch.utils;

public class GeomUtils {
	public static boolean sameSide(double[] p1, double[] p2, double[] a, double[] b) {
		double cp1 = crossProduct(new double[] {b[0]-a[0],b[1]-a[1]}, new double[] {p1[0]-a[0],p1[1]-a[1]});
		double cp2 = crossProduct(new double[] {b[0]-a[0],b[1]-a[1]}, new double[] {p2[0]-a[0],p2[1]-a[1]});
		return (cp1*cp2 >= 0);
	}

	public static  double crossProduct(double[] a, double[] b) {
		return a[1]*b[0] - a[0]*b[1];
	}

	/** checks if a point is in a triangle */
	public  static boolean pointInTriangle(double[] p, double[] a,double[] b,double[]c) {
		return sameSide(p,a, b,c) && sameSide(p,b, a,c) && sameSide(p,c, a,b);
	}

	/** checks if a point is in a triangle */
	public  static boolean pointInOrOnTriangle(double[] p, double[] a,double[] b,double[]c) {
		// method: use cramer's rule to try to write p as a convex combination of a, b and c. 
		double[][] lhs = {{a[0],b[0],c[0]},{a[1],b[1],c[1]},{1,1,1}};
		double d0 = det3x3(lhs);
		
		double[][] mx = {{p[0],b[0],c[0]},{p[1],b[1],c[1]},{1,1,1}};
		double dx = det3x3(mx); 
		double x = dx/d0;
		if (Double.isNaN(x) || x < 0 || x > 1) return false;
		
		double[][] my = {{a[0],p[0],c[0]},{a[1],p[1],c[1]},{1,1,1}};
		double dy = det3x3(my); 
		double y = dy/d0;
		if (Double.isNaN(y) || y < 0 || y > 1) return false;
		
		double[][] mz = {{a[0],b[0],p[0]},{a[1],b[1],p[1]},{1,1,1}};
		double dz = det3x3(mz);
		double z = dz/d0;
		if (Double.isNaN(z) || z < 0 || z > 1) return false;
		
		return true;
	}

	private static double det3x3(double[][] m) {
		double det = m[0][0]*m[1][1]*m[2][2] + m[0][1]*m[1][2]*m[2][0] + m[0][2]*m[1][0]*m[2][1];
		det -= m[0][0]*m[2][1]*m[1][2] + m[0][1]*m[2][2]*m[1][0] + m[0][2]*m[2][0]*m[1][1];
		return det;
	}

	/** checks if line segments p1p2 and q1q2 cross */
	public static boolean lineSegmentsCross(double[] p1, double[] p2,
			double[] q1, double[] q2) {
		double a = p2[0]-p1[0];
		double b = q2[0]-q1[0];
		double c = p2[1]-p1[1];
		double d = q2[1]-q1[1];
		double e = q2[0]-p1[0];
		double f = q2[1]-p1[1];
		double det = a*d-b*c;
		if (det == 0) return false;
		double t= (d*e-b*f)/det;
		double u = (a*f-e*c)/det;
		return t > 0 && t < 1 && u > 0 && u < 1;
	}

	public static double getArea(double[] p, double[] q, double[] r) {
		double a = getDistance(p,q);
		double b = getDistance(q,r);
		double c = getDistance(r,p);
		double s = (a+b+c)/2;
		return Math.sqrt(s*(s-a)*(s-b)*(s-c));
		
	}

	public static double getDistance(double[] p, double[] q) {
		double dx = q[0]-p[0];		
		double dy = q[1]-p[1];
		return Math.sqrt(dx*dx+dy*dy);
	}

	public static boolean pointInConvexPolygon(double[] pt, double[][] polygon) {
		// the polygon is convex, so we can test the membership of the pt in a triangulation of the polygon.
		double[] x = polygon[0];
		for (int i=1; i<x.length-1; i++) {
			double[] y = polygon[1];
			if (pointInOrOnTriangle(pt, new double[] {x[0],  y[0]}, new double[] {x[i],  y[i]}, new double[] {x[i+1],  y[i+1]})) {
				return true;
			}
		}
		// if it's not in any of the triangles, it's not in the polygon
		return false;
	}
}
