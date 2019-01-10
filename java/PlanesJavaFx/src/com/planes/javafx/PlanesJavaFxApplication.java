package com.planes.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

			gridPane.add(selectButton, 1, 0);
		    gridPane.add(rotateButton, 1, 1);
		    gridPane.add(upButton, 1, 2);
		    gridPane.add(leftButton, 0, 3);
		    gridPane.add(rightButton, 2, 3);
		    gridPane.add(downButton, 1, 4);
		    gridPane.add(doneButton, 1, 5);	
		        
		    //gridPane.prefWidth(300);
		    this.getChildren().add(gridPane);
		}
	}
	
	static class RightPane extends Pane 
	{
		public RightPane() {
			final HBox hbox = new HBox(5);
			hbox.getChildren().add(new Text("RIGHT"));
			this.getChildren().add(hbox);
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
		
		Pane leftPane = new LeftPane();
		leftPane.setStyle("-fx-border-color: red");
		Pane rightPane = new RightPane();
		rightPane.setStyle("-fx-border-color: blue");
		
		gridPane.add(leftPane, 0, 0);
		gridPane.add(rightPane,  1,  0);

		stage.setScene(new Scene(gridPane));
		stage.show();
	}
}


