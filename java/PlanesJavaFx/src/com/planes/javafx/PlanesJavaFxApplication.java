package com.planes.javafx;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PlanesJavaFxApplication extends Application {

	class LeftPane extends TabPane 
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
	}
	
	class BoardPane extends Pane 
	{
		class PositionBoardPane {
			private int x = 0;
			private int y = 0;
			public PositionBoardPane(int i, int j) {
				x = i;
				y = j;
			}
			
			@Override
			public boolean equals(final Object other) {
				if (other == null)
					return false;
				if (this == other)
					return true;
				if (this.getClass() != other.getClass())
					return false;
				
				final PositionBoardPane pos = (PositionBoardPane)other;
				return this.x == pos.x && this.y == pos.y;
			}
			
			@Override
			public int hashCode() {
				return 100 * x + y;
			}
		}
		
		public void updateBoard() {
			System.out.println("Update board");
	        int gRows = m_PlaneRound.getRowNo();
	        int gCols = m_PlaneRound.getColNo();
			
			for (int i = 0; i < gRows + 2 * m_Padding; i++) {
				for (int j = 0; j < gCols + 2 * m_Padding; j++) {
					Canvas c = m_GridSquares.get(new PositionBoardPane(i, j));
					GraphicsContext gc = c.getGraphicsContext2D();
					 
					gc.setFill(Color.BLUE);
					gc.fillRect(0, 0, c.getWidth(), c.getHeight());
					
					System.out.println(c.getWidth()+ " " + c.getHeight());
				}
			}
			
		}
		
		public BoardPane() {
			final GridPane gridPane = new GridPane();
			
	        // In order to see the GridPane extends with the LeftPane, remove it further
	        gridPane.setGridLinesVisible(true);
	        // Those 2 following lines enable the gridpane to stretch/shrink according the LeftPane

	        gridPane.prefWidthProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()));
	        gridPane.prefHeightProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()));
	        gridPane.layoutXProperty().bind(Bindings.divide(Bindings.subtract(this.widthProperty(), Bindings.min(this.widthProperty(), this.heightProperty())), 2.0));
	        gridPane.layoutYProperty().bind(Bindings.divide(Bindings.subtract(this.heightProperty(), Bindings.min(this.widthProperty(), this.heightProperty())), 2.0));
	        
	        int gRows = m_PlaneRound.getRowNo();
	        int gCols = m_PlaneRound.getColNo();
	        
	        m_GridSquares = new HashMap<PositionBoardPane, Canvas>();
	        
			for (int i = 0; i < gRows + 2 * m_Padding; i++) {
				for (int j = 0; j < gCols + 2 * m_Padding; j++) {
					Canvas c = new Canvas();
				    c.widthProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()).divide(gCols + 2 * m_Padding));
				    c.heightProperty().bind(Bindings.min(this.widthProperty(), this.heightProperty()).divide(gRows + 2 * m_Padding));
					gridPane.add(c,  i,  j);
					m_GridSquares.put(new PositionBoardPane(i, j), c);
				}
			}
		    
			//AnchorPane.setTopAnchor(gridPane, 10.0);
			//AnchorPane.setLeftAnchor(gridPane, 10.0);
			
			this.getChildren().add(gridPane);
		}
		
		private Map<PositionBoardPane, Canvas> m_GridSquares;
		
	}	//BoardPane	
	
	class RightPane extends TabPane
	{
		public void updateBoards() {
			m_PlayerBoard.updateBoard();
			m_ComputerBoard.updateBoard();
		}
		
		public RightPane() {
		    Tab tab1 = new Tab();
		    tab1.setText("Player Board");
		    m_PlayerBoard = new BoardPane();
		    tab1.setContent(m_PlayerBoard);
		    Tab tab2 = new Tab();
		    tab2.setText("Computer Board");
		    m_ComputerBoard = new BoardPane();
		    tab2.setContent(m_ComputerBoard);		    
		    this.getTabs().addAll(tab1, tab2);
		}
		
		private BoardPane m_PlayerBoard;
		private BoardPane m_ComputerBoard;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub

		m_PlaneRound.createPlanesRound();
		
		final GridPane gridPane = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPrefWidth(300);
		col1.setMinWidth(200);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);
		col2.setMinWidth(200);
		gridPane.getColumnConstraints().addAll(col1, col2);
		
		m_LeftPane = new LeftPane();
		m_LeftPane.setStyle("-fx-border-color: red");
		m_RightPane = new RightPane();
		m_RightPane.setStyle("-fx-border-color: blue");
		
		gridPane.add(m_LeftPane, 0, 0);
		gridPane.add(m_RightPane,  1,  0);

		GridPane.setVgrow(m_LeftPane,  Priority.ALWAYS);
		GridPane.setVgrow(m_RightPane,  Priority.ALWAYS);
		
		//gridPane.setGridLinesVisible(true);
		
		stage.setScene(new Scene(gridPane, 1200, 800));
		stage.show();
	}
	
	private int m_Padding = 3;
	private PlaneRoundJavaFx m_PlaneRound = new PlaneRoundJavaFx();
	private LeftPane m_LeftPane;
	private RightPane m_RightPane;
}


