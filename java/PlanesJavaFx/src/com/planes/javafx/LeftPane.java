package com.planes.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

class LeftPane extends TabPane 
{
	
public void setRightPane(RightPane rp) {
	m_RightPane = rp;
}
	
public void doneClicked() {
	activateGameTab();
}

public void activateGameTab() {
	SingleSelectionModel<Tab> selectionModel = this.getSelectionModel();	
	selectionModel.select(m_GameTab);
    m_EditorTab.setDisable(true);
    m_GameTab.setDisable(false);
    m_StartRoundTab.setDisable(false);
    m_ScorePane.deactivateStartRoundButton();
}

public void activateEditorTab() {
	SingleSelectionModel<Tab> selectionModel = this.getSelectionModel();	
	selectionModel.select(m_EditorTab);	
    m_EditorTab.setDisable(false);
    m_GameTab.setDisable(true);
    m_StartRoundTab.setDisable(false);
    m_ScorePane.deactivateStartRoundButton();
}

public void activateStartGameTab() {
	SingleSelectionModel<Tab> selectionModel = this.getSelectionModel();	
	selectionModel.select(m_StartRoundTab);		
    m_EditorTab.setDisable(true);
    m_GameTab.setDisable(true);
    m_StartRoundTab.setDisable(false);
    m_ScorePane.activateStartRoundButton();
}

public void updateStats(int playerWins, int playerMoves, int playerHits, int playerMisses, int playerDead, 
		int computerWins, int computerMoves, int computerHits, int computerMisses, int computerDead) {
	m_GspPlayer.updateStats(playerMoves, playerHits, playerMisses, playerDead);
	m_GspComputer.updateStats(computerMoves, computerHits, computerMisses, computerDead);
	m_ScorePane.updateStats(playerWins, computerWins);
}

public void roundEnds() {
	activateStartGameTab();
}

public void startNewRound() {
	m_PlaneRound.initRound();
	activateEditorTab();
	
	//update the statistics 
	int playerWins = m_PlaneRound.playerGuess_StatNoPlayerWins();
	int playerMoves = m_PlaneRound.playerGuess_StatNoPlayerMoves();
	int playerHits = m_PlaneRound.playerGuess_StatNoPlayerHits();
	int playerMisses = m_PlaneRound.playerGuess_StatNoPlayerMisses();
	int playerDead = m_PlaneRound.playerGuess_StatNoPlayerDead();
	int computerWins = m_PlaneRound.playerGuess_StatNoComputerWins();
	int computerMoves = m_PlaneRound.playerGuess_StatNoComputerMoves();
	int computerHits = m_PlaneRound.playerGuess_StatNoComputerHits();
	int computerMisses = m_PlaneRound.playerGuess_StatNoComputerMisses();
	int computerDead = m_PlaneRound.playerGuess_StatNoComputerDead();
	updateStats(playerWins, 0, 0, 0, 0, computerWins, 0, 0, 0, 0);
	
	m_RightPane.startNewRound();
}

public LeftPane(PlaneRoundJavaFx planeRound) {
	
	m_PlaneRound = planeRound;
	final GridPane gridPane = new GridPane();
	gridPane.setPadding(new Insets(10, 10, 10, 10));
	gridPane.setVgap(10);
	
	ColumnConstraints col1 = new ColumnConstraints();
	col1.setPercentWidth(33);
	ColumnConstraints col2 = new ColumnConstraints();
	col2.setPercentWidth(33);
	ColumnConstraints col3 = new ColumnConstraints();
	col3.setPercentWidth(33);
	gridPane.getColumnConstraints().addAll(col1, col2, col3);
	
	m_SelectButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        m_RightPane.selectPlane();
	        m_RightPane.updateBoards();	        
	    }		
	});
	m_RotateButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        boolean valid = m_RightPane.rotatePlane();
	        m_RightPane.updateBoards();	
	        m_DoneButton.setDisable(!valid);
	    }		
	});		
	m_UpButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        boolean valid = m_RightPane.movePlaneUpwards();
	        m_RightPane.updateBoards();	 
	        m_DoneButton.setDisable(!valid);
	    }		
	});
	m_LeftButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        boolean valid = m_RightPane.movePlaneLeft();
	        m_RightPane.updateBoards();	  
	        m_DoneButton.setDisable(!valid);
	    }		
	});				
	m_RightButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        boolean valid = m_RightPane.movePlaneRight();
	        m_RightPane.updateBoards();	
	        m_DoneButton.setDisable(!valid);
	    }		
	});
	m_DownButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        boolean valid = m_RightPane.movePlaneDownwards();
	        m_RightPane.updateBoards();	
	        m_DoneButton.setDisable(!valid);
	    }		
	});		
	m_DoneButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        m_RightPane.doneClicked();
	        m_RightPane.updateBoards();	
	        m_PlaneRound.doneClicked();
	        doneClicked();
	    }		
	});	
	
	
    // In order to see the GridPane extends with the LeftPane, remove it further
    gridPane.setGridLinesVisible(false);
    // Those 2 following lines enable the gridpane to stretch/shrink according the LeftPane
    gridPane.prefWidthProperty().bind(this.widthProperty());
    gridPane.prefHeightProperty().bind(this.heightProperty());

	gridPane.add(m_SelectButton, 1, 0);
    gridPane.add(m_RotateButton, 1, 1);
    gridPane.add(m_UpButton, 1, 2);
    gridPane.add(m_LeftButton, 0, 3);
    gridPane.add(m_RightButton, 2, 3);
    gridPane.add(m_DownButton, 1, 4);
    gridPane.add(m_DoneButton, 1, 5);	
    
	GridPane.setHalignment(m_SelectButton, HPos.CENTER);
    GridPane.setHalignment(m_RotateButton, HPos.CENTER);
    GridPane.setHalignment(m_UpButton, HPos.CENTER);
    GridPane.setHalignment(m_LeftButton, HPos.CENTER);
    GridPane.setHalignment(m_RightButton, HPos.CENTER);
    GridPane.setHalignment(m_DownButton, HPos.CENTER);
    GridPane.setHalignment(m_DoneButton, HPos.CENTER);	
    
    //gridPane.prefWidth(300);
    //this.getChildren().add(gridPane);
    
    m_EditorTab = new Tab();
    m_EditorTab.setText("Board Editing");
    m_EditorTab.setContent(gridPane);
    this.getTabs().add(m_EditorTab);

    m_GspPlayer = new GameStatsPane("Player");
    m_GspComputer = new GameStatsPane("Computer");
    
    VBox vbox1 = new VBox();
    vbox1.setPadding(new Insets(10, 10, 10, 10));
    vbox1.setSpacing(10);
    vbox1.getChildren().add(m_GspPlayer);
    vbox1.getChildren().add(m_GspComputer);
    
	m_GameTab = new Tab();
	m_GameTab.setText("Game");
	m_GameTab.setContent(vbox1);
    this.getTabs().add(m_GameTab);
    
    m_ScorePane = new ScorePane(this);
    
    VBox vbox2 = new VBox();
    vbox2.setPadding(new Insets(10, 10, 10, 10));
    vbox2.setSpacing(10);    
    vbox2.getChildren().add(m_ScorePane);
    
	m_StartRoundTab = new Tab();
	m_StartRoundTab.setText("Start Round");
	m_StartRoundTab.setContent(vbox2);
    this.getTabs().add(m_StartRoundTab);    
    
    activateEditorTab();
	}

	private RightPane m_RightPane;
	private PlaneRoundJavaFx m_PlaneRound;
	
	private GameStatsPane m_GspPlayer;
	private GameStatsPane m_GspComputer;
	private ScorePane m_ScorePane;
	
    private Tab m_EditorTab;
    private Tab m_GameTab;
    private Tab m_StartRoundTab;	
    
	private Button m_SelectButton = new Button("Select");
	private Button m_RotateButton = new Button("Rotate");			
	private Button m_UpButton = new Button("Up");
	private Button m_LeftButton = new Button("Left");			
	private Button m_RightButton = new Button("Right");
	private Button m_DownButton = new Button("Down");			
	private Button m_DoneButton = new Button("Done");	
}
