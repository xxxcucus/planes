package com.planes.javafx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

class RightPane extends TabPane
{
	
	public void updateBoards() {
		m_PlayerBoard.updateBoard();
		m_ComputerBoard.updateBoard();
	}
	
	public RightPane(PlaneRoundJavaFx planeRound) {
	    m_PlaneRound = planeRound;
		
		Tab tab1 = new Tab();
	    tab1.setText("Player Board");
	    m_PlayerBoard = new BoardPane(m_PlaneRound, false);
	    tab1.setContent(m_PlayerBoard);
	    Tab tab2 = new Tab();
	    tab2.setText("Computer Board");
	    m_ComputerBoard = new BoardPane(m_PlaneRound, true);
	    tab2.setContent(m_ComputerBoard);		    
	    this.getTabs().addAll(tab1, tab2);
	}
	
	private BoardPane m_PlayerBoard;
	private BoardPane m_ComputerBoard;
	private PlaneRoundJavaFx m_PlaneRound;
}
