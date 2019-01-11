package com.planes.javafx;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlanesJavaFxApplication extends Application {
	static class ToolbarPane extends Pane 
	{
		public ToolbarPane() {
			final HBox hbox = new HBox(5);
			hbox.getChildren().add(new Text("TOP"));
			this.getChildren().add(hbox);
		}
	}
	
	static class LeftPane extends Pane 
	{
		public LeftPane() {
			final GridPane gridPane = new GridPane();
			
			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(33);
			ColumnConstraints col2 = new ColumnConstraints();
			col2.setPercentWidth(33);
			ColumnConstraints col3 = new ColumnConstraints();
			col3.setPercentWidth(33);
			gridPane.getColumnConstraints().addAll(col1, col2, col3);
			
			Button selectButton = new Button("Select");
			Button rotateButton = new Button("Rotate");			
			Button upButton = new Button("Up");
			Button leftButton = new Button("Left");			
			Button rightButton = new Button("Right");
			Button downButton = new Button("Down");			
			Button doneButton = new Button("Done");	
			
	        // In order to see the GridPane extends with the LeftPane, remove it further
	        gridPane.setGridLinesVisible(true);
	        // Those 2 following lines enable the gridpane to stretch/shrink according the LeftPane
	        gridPane.prefWidthProperty().bind(this.widthProperty());
	        gridPane.prefHeightProperty().bind(this.heightProperty());

			gridPane.add(selectButton, 1, 0);
		    gridPane.add(rotateButton, 1, 1);
		    gridPane.add(upButton, 1, 2);
		    gridPane.add(leftButton, 0, 3);
		    gridPane.add(rightButton, 2, 3);
		    gridPane.add(downButton, 1, 4);
		    gridPane.add(doneButton, 1, 5);	
		    
			GridPane.setHalignment(selectButton, HPos.CENTER);
		    GridPane.setHalignment(rotateButton, HPos.CENTER);
		    GridPane.setHalignment(upButton, HPos.CENTER);
		    GridPane.setHalignment(leftButton, HPos.CENTER);
		    GridPane.setHalignment(rightButton, HPos.CENTER);
		    GridPane.setHalignment(downButton, HPos.CENTER);
		    GridPane.setHalignment(doneButton, HPos.CENTER);	
		    
		    //gridPane.prefWidth(300);
		    this.getChildren().add(gridPane);
		}
	}
	
	class RightPane extends Pane 
	{
		public RightPane() {
			final GridPane gridPane = new GridPane();
			
	        // In order to see the GridPane extends with the LeftPane, remove it further
	        gridPane.setGridLinesVisible(true);
	        // Those 2 following lines enable the gridpane to stretch/shrink according the LeftPane
	        gridPane.prefWidthProperty().bind(this.widthProperty());
	        gridPane.prefHeightProperty().bind(this.heightProperty());			
			
			for (int i = 0; i < m_GridSize; i++) {
				for (int j = 0; j < m_GridSize; j++) {
					Canvas c = new Canvas(m_CellSize, m_CellSize);
					gridPane.add(c,  i,  j);
				}
			}

		    
			this.getChildren().add(gridPane);
		}
	}		
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub

		final GridPane gridPane = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(40);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(60);
		gridPane.getColumnConstraints().addAll(col1, col2);
			
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(33);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(33);
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(33);		
		
		Region spacer1 = new Region();
		Region spacer2 = new Region();
		
		Pane leftPane = new LeftPane();
		leftPane.setStyle("-fx-border-color: red");
		Pane rightPane = new RightPane();
		rightPane.setStyle("-fx-border-color: blue");
		
		gridPane.add(spacer1,  0, 0);
		gridPane.add(leftPane, 0, 1);
		gridPane.add(spacer2,  0,  2);
		gridPane.add(rightPane,  1,  0, 1, 3);

		GridPane.setVgrow(spacer1,  Priority.ALWAYS);
		GridPane.setVgrow(spacer2,  Priority.ALWAYS);
		
		stage.setScene(new Scene(gridPane));
		stage.show();
	}
	
	private int m_GridSize = 10;
	private int m_CellSize = 30;
}


