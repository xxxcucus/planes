package com.planes.javafx;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class PlanesJavaFxApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub

		m_PlaneRound = new PlaneRoundJavaFx();
		m_PlaneRound.createPlanesRound();
		
		final GridPane gridPane = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPrefWidth(300);
		col1.setMinWidth(200);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);
		col2.setMinWidth(200);
		gridPane.getColumnConstraints().addAll(col1, col2);
		
		m_LeftPane = new LeftPane(m_PlaneRound);
		m_LeftPane.setStyle("-fx-border-color: red");
		m_RightPane = new RightPane(m_PlaneRound);
		m_RightPane.setStyle("-fx-border-color: blue");
		m_LeftPane.setRightPane(m_RightPane);
		m_RightPane.setLeftPane(m_LeftPane);
		
		gridPane.add(m_LeftPane, 0, 0);
		gridPane.add(m_RightPane,  1,  0);

		GridPane.setVgrow(m_LeftPane,  Priority.ALWAYS);
		GridPane.setVgrow(m_RightPane,  Priority.ALWAYS);
		
		//gridPane.setGridLinesVisible(true);
		
		stage.setScene(new Scene(gridPane, 1200, 800));
		stage.show();
		
		m_RightPane.updateBoards();
	}
	
	private PlaneRoundJavaFx m_PlaneRound;
	private LeftPane m_LeftPane;
	private RightPane m_RightPane;
}


