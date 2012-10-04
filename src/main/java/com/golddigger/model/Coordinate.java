package com.golddigger.model;
/**
 * Simple 2 Dimensional Vector class
 * Mainly used when both x and y coordinates are needed to be return.
 * @author Brett Wandel
 *
 */
public class Coordinate {
		public final int lat, lng;
		
		public Coordinate(int lat, int lng){
			this.lat = lat;
			this.lng = lng;
		}
		
		@Override
		public boolean equals(Object object){
			if (object instanceof Coordinate) {
				Coordinate tmp = (Coordinate) object;
				return this.lat == tmp.lat && this.lng == tmp.lng;
			} else {
				return false;
			}
		}
		
		@Override
		public String toString(){
			return "("+lat+","+lng+")";
		}

		public Coordinate add(Coordinate p) {
			return add(p.lat, p.lng);
		}
		
		public Coordinate add(int lat, int lng){
			return new Coordinate(this.lat + lat, this.lng + lng);
		}

		public Coordinate sub(Coordinate p) {
			return add(-p.lat, -p.lng);
		}
		
		public Coordinate sub(int lat, int lng){
			return add(-lat,-lng);
		}
		
		public Coordinate inverse(){
			return new Coordinate(-lat, -lng);
		}

		public int getLat() {
			return lat;
		}
		public int getLng() {
			return lng;
		}
	}
