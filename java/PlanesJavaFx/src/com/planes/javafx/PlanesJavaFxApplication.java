package com.planes.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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
			
			gridPane.add(new Button("Select"), 1, 0);
		    gridPane.add(new Button("Rotate"), 1, 1);
		    gridPane.add(new Button("Up"), 1, 2);
		    gridPane.add(new Button("Left"), 0, 3);
		    gridPane.add(new Button("Right"), 2, 3);
		    gridPane.add(new Button("Down"), 1, 4);
		    gridPane.add(new Button("Done"), 1, 5);	
		    
		    gridPane.prefWidth(300);
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
	
	private Pane createToolbarPane() {
		return new ToolbarPane();
	}
	
	private Pane createLeftPane() {
		return new LeftPane();
	}
	
	private Pane createRightPane() {
		return new RightPane();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub

		final BorderPane borderPane = new BorderPane();
		//borderPane.setTop(createToolbarPane());
		borderPane.setCenter(createRightPane());
		borderPane.setLeft(createLeftPane());
		
		stage.setScene(new Scene(borderPane, 600, 400));
		stage.show();
	}
}


