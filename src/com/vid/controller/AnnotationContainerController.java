package com.vid.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class AnnotationContainerController implements Initializable {

	@FXML
	private AnchorPane container;

	@FXML
	private AnchorPane window;

	@FXML
	private GridPane gridpane;

	@FXML
	private Button close;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {

		close.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				AnchorPane parent = (AnchorPane) container.getParent().getParent();
				parent.setVisible(false);
			}
		});

	}

	public AnchorPane getContainer() {
		return container;
	}

	public void setContainer(AnchorPane container) {
		this.container = container;
	}

	public AnchorPane getWindow() {
		return window;
	}

	public void setWindow(AnchorPane window) {
		this.window = window;
	}

	public GridPane getGridpane() {
		return gridpane;
	}

	public void setGridpane(GridPane gridpane) {
		this.gridpane = gridpane;
	}

	public Button getClose() {
		return close;
	}

	public void setClose(Button close) {
		this.close = close;
	}

}
