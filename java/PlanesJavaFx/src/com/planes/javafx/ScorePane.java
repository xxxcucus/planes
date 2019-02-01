package com.planes.javafx;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScorePane extends Pane {
	
	void updateStats(int playerWins, int computerWins) {
		m_ScorePlayerValue.setText(Integer.toString(playerWins));
		m_ScoreComputerValue.setText(Integer.toString(computerWins));
	}
	
	void activateStartRoundButton() {
		m_StartNewRoundButton.setDisable(false);
	}
	
	void deactivateStartRoundButton() {
		m_StartNewRoundButton.setDisable(true);
	}
	
	public ScorePane(LeftPane leftPane) {
		m_LeftPane = leftPane;
		GridPane gridPane = new GridPane();
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		gridPane.getColumnConstraints().addAll(col1, col2);
		
		Text scorePlayerText = new Text("Player");
		Text scoreComputerText = new Text("Computer");
		Text titleText = new Text("General Score");
		Font titleFont = titleText.getFont();
		titleText.setFont(Font.font(titleFont.getName(), FontWeight.BOLD, titleFont.getSize()));
		
		m_ScorePlayerValue = new Text("0");
		m_ScoreComputerValue = new Text("0");
		
    	m_StartRoundHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent e) { 
            	m_LeftPane.startNewRound();
            }
        };
        m_StartNewRoundButton = new Button("Start New Round");
        m_StartNewRoundButton.setOnMouseClicked(m_StartRoundHandler);
		
		gridPane.add(titleText,  0, 0);
		gridPane.add(scoreComputerText, 0, 1);
		gridPane.add(m_ScoreComputerValue, 1, 1);
		gridPane.add(scorePlayerText,  0,  2);
		gridPane.add(m_ScorePlayerValue, 1, 2);
		gridPane.add(m_StartNewRoundButton, 0, 3);
		
		gridPane.prefWidthProperty().bind(this.widthProperty());
        gridPane.prefHeightProperty().bind(this.heightProperty());
		
		this.getChildren().add(gridPane);
	}
	
	private Text m_ScorePlayerValue;
	private Text m_ScoreComputerValue;
	private LeftPane m_LeftPane;
	private EventHandler<MouseEvent> m_StartRoundHandler;
	private Button m_StartNewRoundButton;
}
