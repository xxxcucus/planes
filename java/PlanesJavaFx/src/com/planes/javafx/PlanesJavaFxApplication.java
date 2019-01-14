package com.planes.javafx;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class PlanesJavaFxApplication extends Application {

	static class LeftPane extends TabPane 
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
		    //this.getChildren().add(gridPane);
		    
		    Tab tab = new Tab();
		    tab.setText("Board Editing");
		    tab.setContent(gridPane);
		    this.getTabs().add(tab);
		}
	}
	
	class BoardPane extends Pane 
	{
		
		public BoardPane() {
			final GridPane gridPane = new GridPane();
			
	        // In order to see the GridPane extends with the LeftPane, remove it further
	        gridPane.setGridLinesVisible(true);
	        // Those 2 following lines enable the gridpane to stretch/shrink according the LeftPane

	        gridPane.prefWidthProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()));
	        gridPane.prefHeightProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()));
	        gridPane.layoutXProperty().bind(Bindings.divide(Bindings.subtract(this.widthProperty(), Bindings.min(this.widthProperty(), this.heightProperty())), 2.0));
	        gridPane.layoutYProperty().bind(Bindings.divide(Bindings.subtract(this.heightProperty(), Bindings.min(this.widthProperty(), this.heightProperty())), 2.0));
	        
			for (int i = 0; i < m_GridSize; i++) {
				for (int j = 0; j < m_GridSize; j++) {
					Canvas c = new Canvas();
					GridPane.setHgrow(c, Priority.ALWAYS);
					GridPane.setVgrow(c, Priority.ALWAYS);
					gridPane.add(c,  i,  j);
				}
			}
		    
			//AnchorPane.setTopAnchor(gridPane, 10.0);
			//AnchorPane.setLeftAnchor(gridPane, 10.0);
			
			this.getChildren().add(gridPane);
		}
	}		
	
	class RightPane extends TabPane
	{
		public RightPane() {
		    Tab tab1 = new Tab();
		    tab1.setText("Player Board");
		    BoardPane pane1 = new BoardPane();
		    tab1.setContent(pane1);
		    Tab tab2 = new Tab();
		    tab2.setText("Computer Board");
		    BoardPane pane2 = new BoardPane();
		    tab2.setContent(pane2);		    
		    this.getTabs().addAll(tab1, tab2);
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
		col1.setPrefWidth(300);
		col1.setMinWidth(200);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);
		col2.setMinWidth(200);
		gridPane.getColumnConstraints().addAll(col1, col2);
		
		TabPane leftPane = new LeftPane();
		leftPane.setStyle("-fx-border-color: red");
		TabPane rightPane = new RightPane();
		rightPane.setStyle("-fx-border-color: blue");
		

		gridPane.add(leftPane, 0, 0);
		gridPane.add(rightPane,  1,  0);

		GridPane.setVgrow(leftPane,  Priority.ALWAYS);
		GridPane.setVgrow(rightPane,  Priority.ALWAYS);
		
		//gridPane.setGridLinesVisible(true);
		
		stage.setScene(new Scene(gridPane, 1200, 800));
		stage.show();
	}
	
	private int m_GridSize = 10;
	private int m_CellSize = 30;
}


