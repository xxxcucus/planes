package com.planes.android;

import android.graphics.Color;

import org.junit.Test;

import static org.junit.Assert.*;

public class TopPane_VerticalTests {
	
	@Test
    public void TopPaneVertical_ComputeSquareBackgroundColor1() {
		PlaneRoundJavaFxMock planeRound = new PlaneRoundJavaFxMock();
		GameBoard tpv = new GameBoard(null);
		tpv.setPlaneRound(planeRound);

	    int i = -1;
	    int j = -1;

	    assertEquals("Color of board outside the game area", tpv.computeSquareBackgroundColor(-1, -1), Color.YELLOW);
    }
}