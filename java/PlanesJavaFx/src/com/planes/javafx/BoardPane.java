package com.planes.javafx;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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
	
	public BoardPane(PlaneRoundJavaFx planeRound) {
		m_PlaneRound = planeRound;
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
	private PlaneRoundJavaFx m_PlaneRound;
	private int m_Padding = 3;
	
}	//BoardPane	
