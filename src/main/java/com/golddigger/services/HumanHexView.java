package com.golddigger.services;

/**
 * <p>HumanHexView allows players to see a view of the tiles surrounding their
 * digger that is more understandable to a human player when the map is made
 * up of hexagon tiles.</p>
 * 
 * <p>This view is only generated if a "human" qualifier is added to the view
 * command: digger_name/view/human</p>
 * 
 * <p>The main aim of this view is to show the tiles surrounding the digger
 * in the same directions that they are located relative to the digger. In
 * other words, if the player sees a tile north of its digger then if it went
 * north it would land on that tile.</p>
 * 
 * This is so called human view because it is easier to interpret by a human
 * but not particularly straight-forward to parse by a digger controller.
 * 
 * @author Mohammad Mehdi Bezyan
 */

public class HumanHexView {
	private static final char WRAPPER_TILE_SYMBOL = '-';
	
	/**
	 * This method take a view that has been parsed and occluded and adds
	 * spaces to make it human readable and in a shape that conforms to
	 * hexagon tiles.
	 * 
	 * @param view
	 * @param unit_lng
	 * @return the view with spaces to make it human-readable
	 */
	public static String convertToHumanReadable(char[][] view, int unit_lng) {
		String spaced_view = "";
		int ref_lng = 0;
		int offset = 1;
		
		if (view.length == 3) offset = 0; // for Line of sight == 1
		
		String view_line_even="", view_line_odd="";
		
		for (int lat = 0; lat < view.length; lat++) {			
			view_line_even = view_line_odd = "";			
			for (int lng = 0; lng < view[lat].length; lng++) {				
				ref_lng = unit_lng + lng + offset;								
				if ((ref_lng % 2) == 0) {
					view_line_odd += view[lat][lng];
					view_line_even += " ";					
				} else {					
					view_line_even += view[lat][lng];
					view_line_odd += " ";
				}
			}			
			spaced_view += view_line_even + "\n" + view_line_odd + "\n";			
		}		
		
		// deciding on the position of the wrapper row is only
		// dependent on the longitude of the unit (digger)
		if (unit_lng % 2 == 0) {
			spaced_view = constructWrapper(view[0].length) +  "\n" + spaced_view;						
		} else {
			spaced_view = spaced_view + constructWrapper(view[0].length);			
		}
		
		return spaced_view;
	}
	
	/**
	 * Given the horizontal length of the map, that is the number of columns,
	 * construct a row of wrappers using the wrapper tile symbol
	 * 
	 * @param length (horizontal length of the map)
	 * @return a string consisted of a single line of wrapper symbols whose
	 * length is equal to the length parameter
	 */
	private static String constructWrapper(int length) {
		String wrapper = "";
		for (int i=0; i < length; i++) {
			wrapper += WRAPPER_TILE_SYMBOL;
		}
		return wrapper;
	}

}
