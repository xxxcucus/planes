package com.planes.javafx;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameStatsPane extends Pane {
	
	public void updateStats(int moves, int hits, int misses, int dead) {
		m_NoMovesValue.setText(Integer.toString(moves));
		m_NoMissesValue.setText(Integer.toString(misses));
		m_NoHitsValue.setText(Integer.toString(hits));
		m_NoGuessesValue.setText(Integer.toString(dead));
	}
	
	public GameStatsPane(final String title ) {
		GridPane gridPane = new GridPane();
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		gridPane.getColumnConstraints().addAll(col1, col2);
		
		Text noMovesText = new Text("Number of moves");
		Text noMissesText = new Text("Number of misses");
		Text noHitsText = new Text("Number of hits");
		Text noGuessesText = new Text("Planes guessed");
		Text titleText = new Text(title);
		Font titleFont = titleText.getFont();
		titleText.setFont(Font.font(titleFont.getName(), FontWeight.BOLD, titleFont.getSize()));
		
		m_NoMovesValue = new Text("0");
		m_NoMissesValue = new Text("0");
		m_NoHitsValue = new Text("0");
		m_NoGuessesValue = new Text("0");
		
		gridPane.add(titleText,  0, 0);
		gridPane.add(noMovesText, 0, 1);
		gridPane.add(m_NoMovesValue, 1, 1);
		gridPane.add(noMissesText,  0,  2);
		gridPane.add(m_NoMissesValue, 1, 2);
		gridPane.add(noHitsText, 0, 3);
		gridPane.add(m_NoHitsValue,  1,  3);
		gridPane.add(noGuessesText, 0, 4);
		gridPane.add(m_NoGuessesValue, 1, 4);
		
		gridPane.prefWidthProperty().bind(this.widthProperty());
        gridPane.prefHeightProperty().bind(this.heightProperty());
		
		this.getChildren().add(gridPane);
	}
	
	Text m_NoMovesValue;
	Text m_NoMissesValue;
	Text m_NoHitsValue;
	Text m_NoGuessesValue;
}
