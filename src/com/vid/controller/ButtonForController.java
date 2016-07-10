package com.vid.controller;

import java.io.File;
import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

public class ButtonForController {

	private static final int SKIP_TIME_MS = 10 * 1000;

	public static void initializePlayPauseButton(VideoPlayerController VideoPlayerController, Button play_butt,
			DirectMediaPlayer mediaPlayer) {

		play_butt.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				URL resource = null;
				getClass().getClassLoader().getResource("icons/glyphicons-17-bin.png");
				if (mediaPlayer.isPlaying()) {
					// code for icon
					mediaPlayer.pause();
					resource = getClass().getClassLoader().getResource("icons/control_play_blue.png");
				} else {
					mediaPlayer.play();
					resource = getClass().getClassLoader().getResource("icons/control_pause_blue.png");
				}
				Image image = new Image("file:" + resource.getPath());
				VideoPlayerController.getPlaybuttimg().setImage(image);
			}
		});
	}

	public static void initializeStopButton(VideoPlayerController VideoPlayerController, Button stop_butt,
			DirectMediaPlayer mediaPlayer) {

		stop_butt.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mediaPlayer.stop();
				URL resource = getClass().getClassLoader().getResource("icons/control_play_blue.png");
				Image image = new Image("file:" + resource.getPath());
				VideoPlayerController.getPlaybuttimg().setImage(image);
				VideoPlayerController.getDisplayOverlays().stopAnnotationComp();
			}
		});
	}

	public static void initializeRewindButton(VideoPlayerController VideoPlayerController, Button rewind_butt,
			DirectMediaPlayer mediaPlayer) {

		rewind_butt.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (mediaPlayer.getLength() > 0) {
					mediaPlayer.skip(SKIP_TIME_MS);
				}
			}
		});
	}

	public static void initializeForwardButton(VideoPlayerController VideoPlayerController, Button fforward_butt,
			DirectMediaPlayer mediaPlayer) {
		fforward_butt.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (mediaPlayer.getLength() > 0) {
					mediaPlayer.skip(-SKIP_TIME_MS);
				}
			}
		});
	}

	public static void initializePrevButton(VideoPlayerController videoPlayerController, Button prev_butt,
			DirectMediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	public static void initializeNextButton(VideoPlayerController videoPlayerController, Button next_butt,
			DirectMediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub

	}

	public static void initializeMuteButton(VideoPlayerController videoPlayerController, ToggleButton mute_butt,
			DirectMediaPlayer mediaPlayer) {
		mute_butt.selectedProperty().addListener((ChangeListener<? super Boolean>) (observable, oldValue, newValue) -> {
			mediaPlayer.mute(newValue);
		});

	}

	public static void initializeNewVideoButton(VideoPlayerController videoPlayerController, Button openvideo_butt,
			DirectMediaPlayer mediaPlayer) {
		openvideo_butt.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				fileChooser.setInitialDirectory(new File("K:\\Test\\Out"));
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MKV", "*.mkv"));
				try {
					File file = fileChooser.showOpenDialog(videoPlayerController.getStageWindow());
					VideoPlayerController.setPATH_TO_VIDEO(file.getAbsolutePath());
					videoPlayerController.initializeMediaPlayer();
					Thread.sleep(500);
					mediaPlayer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	public static void initializeShowTagsbuttButton(VideoPlayerController videoPlayerController,
			ToggleButton showtags_butt, DirectMediaPlayer mediaPlayer) {

	}

}
