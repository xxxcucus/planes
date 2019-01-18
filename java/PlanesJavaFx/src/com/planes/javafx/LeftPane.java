package com.planes.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

class LeftPane extends TabPane 
{
	
public void setRightPane(RightPane rp) {
	m_RightPane = rp;
}
	
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
	
	selectButton.setOnAction(new EventHandler<ActionEvent>() {
	    @Override 
	    public void handle(ActionEvent e) {
	        m_RightPane.updateBoards();
	    }
	});
	
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

	RightPane m_RightPane;
}
