package com.vid.application;

import com.vid.controller.VideoPlayerController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class CustomVideoPlayer extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		new NativeDiscovery().discover();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VideoPlayer.fxml"));
		Parent root = (Parent) loader.load();
		VideoPlayerController controller = (VideoPlayerController) loader.getController();
		controller.setStageWindow(primaryStage); // or what you want
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
		Platform.setImplicitExit(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
