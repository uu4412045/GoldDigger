package com.golddigger.model;
public class Point2D {
		public int x, y;
		public Point2D(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(Object object){
			if (object instanceof Point2D) {
				Point2D tmp = (Point2D) object;
				return this.x == tmp.x && this.y == tmp.y;
			} else {
				return false;
			}
		}
		
		@Override
		public String toString(){
			return "("+x+","+y+")";
		}
	}