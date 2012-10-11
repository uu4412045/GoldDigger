package com.golddigger.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import com.golddigger.model.Coordinate;

/**
 * Simple hexagon to help with the the calculation of the occlusion masks for
 * hexagon tiled maps.
 * @author Brett Wandel
 */
/*
 * The Hexagon is offset according to the standard Hexagon Map offset.
 * The even column(latitude) tiles are always height than the odd column.
 */
public class Hexagon {
	public static final double PRECISION = 0.0000001;
	public static final double RADIUS = 1.0;
	public static final double HEIGHT = Math.sqrt(3.0);
	private static final double HALF_HEIGHT = HEIGHT/2;

	private static final Point2D E = new Point2D.Double(0, RADIUS-PRECISION);
	private static final Point2D W = new Point2D.Double(0,-RADIUS+PRECISION);
	private static final Point2D NE = new Point2D.Double(-HALF_HEIGHT+PRECISION, (RADIUS/2)-PRECISION);
	private static final Point2D SE = new Point2D.Double(HALF_HEIGHT-PRECISION, (RADIUS/2)-PRECISION);
	private static final Point2D NW = new Point2D.Double(-HALF_HEIGHT+PRECISION, -(RADIUS/2)+PRECISION);
	private static final Point2D SW = new Point2D.Double(HALF_HEIGHT-PRECISION, -(RADIUS/2)+PRECISION);
	
	private Point2D center;
	Line2D e_w, ne_sw, se_nw;

	/**
	 * Create Hexagon at the given coordinate.
	 * @param position the position of the Hexagon.
	 */
	public Hexagon(Coordinate position){
		this(position.lat, position.lng);
	}
	
	public Hexagon(int lat, int lng){
		double latitude = lat*HEIGHT;
		double longitude = lng*RADIUS*1.5;
		if (lng % 2 == 1){
			latitude += HALF_HEIGHT;
		}
		
		center = new Point2D.Double(latitude, longitude);
		e_w = getLineBetween(E, W);
		ne_sw = getLineBetween(NE, SW);
		se_nw = getLineBetween(SE, NW);
	}

	/**
	 * Get the centre point of the hexagon.
	 * Used for drawing "lines" between two hexagons for intersection tests.
	 * @return The centre.
	 */
	public Point2D getCenter(){
		return center;
	}
	
	/**
	 * Check to see if the line intersects the Hexagon.
	 * only guarentees correctness if the line passes <b>through</b> the
	 * hexagon completely.
	 * @param line The line to test against.
	 * @return true if the line intersects, false otherwise.
	 */
	public boolean intersects(Line2D line){
		if (line.intersectsLine(e_w) || line.intersectsLine(ne_sw) || line.intersectsLine(se_nw)){
			return true;
		}
		return false;
	}
	
	/**
	 * Used to generate the lines between the corners of the hexagon,
	 * used for the intersection tests.
	 * @param p1 start of the line.
	 * @param p2 end of the line.
	 * @return the actual line.
	 */
	private Line2D getLineBetween(Point2D p1, Point2D p2){
		return new Line2D.Double(offset(p1), offset(p2));
	}

	/**
	 * offset the point from the centre, used to offset the generic corners.
	 * @param point the point to add to the center.
	 * @return a <b>new<b> Point2D object with the result.
	 */
	private Point2D offset(Point2D point){
		return this.add(point, center);
	}
	
	private Point2D add(Point2D p1, Point2D p2) {
		return new Point2D.Double(p1.getX()+p2.getX(), p1.getY()+p2.getY());
	}
}
