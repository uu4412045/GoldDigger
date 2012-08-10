package com.golddigger.services;

import org.junit.Test;

import static com.golddigger.services.HexViewService.mask;

public class HexViewServiceTest {

	@Test
	public void testMark() {
		int[][] mask = mask(3);
		for(int[] is : mask){
			for (int i : is){
				System.out.print(i);
			}
			System.out.println();
		}
	}

}
