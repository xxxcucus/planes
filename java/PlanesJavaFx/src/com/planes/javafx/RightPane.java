package com.planes.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

class RightPane extends TabPane
{
	
	public void selectPlane() {
		m_PlayerBoard.selectPlane();
	}
	
	public void rotatePlane() {
		m_PlayerBoard.rotatePlane();
	}	
	
	public void movePlaneLeft() {
		m_PlayerBoard.movePlaneLeft();
	}
	
	public void movePlaneRight() {
		m_PlayerBoard.movePlaneRight();
	}
	
	public void movePlaneDownwards() {
		m_PlayerBoard.movePlaneDownwards();
	}
	
	public void movePlaneUpwards() {
		m_PlayerBoard.movePlaneUpwards();
	}
	
	public void doneClicked() {
		SingleSelectionModel<Tab> selectionModel = this.getSelectionModel();	
		selectionModel.select(m_ComputerTab);
		m_PlayerBoard.doneClicked();
		m_ComputerBoard.doneClicked();
	}
	
	public void updateBoards() {
		m_PlayerBoard.updateBoard();
		m_ComputerBoard.updateBoard();
	}
	
	public void updateStats(int playerWins, int playerMoves, int playerHits, int playerMisses, int playerDead, 
			int computerWins, int computerMoves, int computerHits, int computerMisses, int computerDead) {
		m_LeftPane.updateStats(playerWins, playerMoves, playerHits, playerMisses, playerDead, 
    			computerWins, computerMoves, computerHits, computerMisses, computerDead);
	}
	
	public void setLeftPane(LeftPane lp) {
		m_LeftPane = lp;
	}
	
	public RightPane(PlaneRoundJavaFx planeRound) {
	    m_PlaneRound = planeRound;
		
		m_PlayerTab = new Tab();
	    m_PlayerTab.setText("Player Board");
	    m_PlayerBoard = new BoardPane(m_PlaneRound, this, false);
	    m_PlayerTab.setContent(m_PlayerBoard);
	    m_ComputerTab = new Tab();
	    m_ComputerTab.setText("Computer Board");
	    m_ComputerBoard = new BoardPane(m_PlaneRound, this, true);
	    m_ComputerTab.setContent(m_ComputerBoard);		    
	    this.getTabs().addAll(m_PlayerTab, m_ComputerTab);
	    
	    this.widthProperty().addListener((obs, oldVal, newVal) -> {
	        updateBoards();
	    });

	    this.heightProperty().addListener((obs, oldVal, newVal) -> {
	        updateBoards();
	    });	   
	    
	    this.getSelectionModel().selectedItemProperty().addListener(
	    	    new ChangeListener<Tab>() {
	    	        @Override
	    	        public void changed(ObservableValue<? extends Tab> ov, Tab oldValue, Tab newValue) {
	    	            
	    	        	if (newValue == m_PlayerTab)
	    	        		updateBoards();
	    	        }
	    	    }
	    	);
	}
	
	private BoardPane m_PlayerBoard;
	private BoardPane m_ComputerBoard;
	private PlaneRoundJavaFx m_PlaneRound;
	Tab m_PlayerTab;
	Tab m_ComputerTab;
	LeftPane m_LeftPane;
}
