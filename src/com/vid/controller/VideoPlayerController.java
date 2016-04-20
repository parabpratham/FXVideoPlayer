package com.vid.controller;

import java.awt.Dimension;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.sun.jna.Memory;
import com.vid.comp.Jcomp.AbstractComp;
import com.vid.matroska.MatroskaContainer;
import com.vid.play.fx.overlay.DisplayOverlays;
import com.vid.play.fx.overlay.OverLayGenerator;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

public class VideoPlayerController implements Initializable {

	private Stage stageWindow;

	@FXML
	private AnchorPane playerHolder;

	// Playback buttons

	@FXML
	private Button openvideo_butt;

	@FXML
	private Button fforward_butt;

	@FXML
	private Button play_butt;

	@FXML
	private Button rewind_butt;

	@FXML
	private Button stop_butt;

	@FXML
	private Button next_butt;

	@FXML
	private Button prev_butt;

	@FXML
	private ToggleButton showtags_butt;

	@FXML
	private ToggleButton mute_butt;

	@FXML
	private ToggleButton toggleannotation_butt;

	@FXML
	private ImageView playbuttimg;

	@FXML
	private Slider timeSlider;

	@FXML
	private Slider volumeslider;

	@FXML
	private TextField currenttimeLabel;

	@FXML
	private TextField totaltime;

	@FXML
	private AnchorPane timelineholder;

	public static final int baseRectWidth = 800;
	public static final int baseRectHeight = 600;

	private static String PATH_TO_VIDEO = "I:\\workspace\\SpringWorkspace\\VideoEditor\\Sample\\out\\JaiMataDi_KingCircle.mp4.mkv";

	private ImageView imageView;

	private DirectMediaPlayerComponent mediaPlayerComponent;

	private WritableImage writableImage;

	private WritablePixelFormat<ByteBuffer> pixelFormat;

	private FloatProperty videoSourceRatioProperty;

	private Dimension videoDimension;

	private double wtFactor, htFactor;

	private static OverLayGenerator overLayGenerator;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mediaPlayerComponent = new CanvasPlayerComponent();
		videoSourceRatioProperty = new SimpleFloatProperty(0.4f);
		pixelFormat = PixelFormat.getByteBgraPreInstance();
		getPlayerHolder().setStyle("-fx-background-color: black;");

		initializeImageView();
		initializeSliders();
		initializeButtons();
		// initializeMediaPlayer();
		initializeMultiplires();
	}

	private void initializeMultiplires() {

		// Set height,width adjustment for the abstractcomp
		AbstractComp.setWidthMul(playerHolder.getMaxWidth() / 600);
		AbstractComp.setHeightMul(playerHolder.getMaxHeight() / 400);

		// Set startX,StartY adjustment for the abstractcomp
	}

	private void intinializeAnnotationEngine() {
		MatroskaContainer matroskaContainer = new MatroskaContainer(PATH_TO_VIDEO);
		setOverLayGenerator(new OverLayGenerator(getMediaPlayerComponent(), matroskaContainer, getPlayerHolder()));
		setDisplayOverlays(new DisplayOverlays(matroskaContainer, getMediaPlayerComponent(), getPlayerHolder()));
		Platform.runLater(getDisplayOverlays());
	}

	private DisplayOverlays displayOverlays;

	public void initializeMediaPlayer() {
		mediaPlayerComponent.getMediaPlayer().prepareMedia(PATH_TO_VIDEO);
		// mediaPlayerComponent.getMediaPlayer().mute();
		intinializeAnnotationEngine();
	}

	private void initializeButtons() {
		// play,pause buttons
		ButtonForController.initializePlayPauseButton(this, play_butt, mediaPlayerComponent.getMediaPlayer());
		ButtonForController.initializeStopButton(this, stop_butt, mediaPlayerComponent.getMediaPlayer());
		ButtonForController.initializeRewindButton(this, rewind_butt, mediaPlayerComponent.getMediaPlayer());
		ButtonForController.initializeForwardButton(this, fforward_butt, mediaPlayerComponent.getMediaPlayer());
		ButtonForController.initializeMuteButton(this, mute_butt, mediaPlayerComponent.getMediaPlayer());
		ButtonForController.initializeNextButton(this, next_butt, mediaPlayerComponent.getMediaPlayer());
		ButtonForController.initializePrevButton(this, prev_butt, mediaPlayerComponent.getMediaPlayer());
		ButtonForController.initializeNewVideoButton(this, openvideo_butt, mediaPlayerComponent.getMediaPlayer());
		// TODO
		// code for toggle button of tags and annotations

	}

	private void initializeImageView() {

		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		writableImage = new WritableImage((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
		setImageView(new ImageView(writableImage));
		playerHolder.getChildren().add(getImageView());
		fitImageViewSizeForMe((float) playerHolder.getMaxWidth(), (float) playerHolder.getMaxHeight());

	}

	// timeline related changes
	private void initializeSliders() {
		timeSlider.setMinWidth(50);
		timeSlider.setMaxWidth(Double.MAX_VALUE);
		timeSlider.setMajorTickUnit(10.0);
		timeSlider.setShowTickMarks(true);
		timeSlider.setMax(100);
		timeSlider.setMin(0);
		timeSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				try {
					{
						// multiply duration by percentage calculated by slider
						// position
						if (timeSlider.isValueChanging()) {
							Duration duration1 = new Duration(mediaPlayerComponent.getMediaPlayer().getLength());
							int newTime = (int) duration1.multiply(timeSlider.getValue() / 100.0).toMillis();
							System.out.println(" timeSlider.valueProperty " + timeSlider.getValue() / 100.0 + " "
									+ duration1.multiply(timeSlider.getValue() / 100.0));
							// mediaPlayerComponent.getMediaPlayer().stop();
							mediaPlayerComponent.getMediaPlayer().setTime(newTime);
							// mediaPlayerComponent.getMediaPlayer().play();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		IntegerProperty volume = new SimpleIntegerProperty(100);
		volumeslider.setValue(100);
		volumeslider.setMin(0);
		volumeslider.setMax(200);

		volumeslider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				// TODO Auto-generated method stub
				try {
					if (volumeslider.isValueChanging())
						mediaPlayerComponent.getMediaPlayer().setVolume((int) volumeslider.getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
				super.timeChanged(mediaPlayer, newTime);
				try {
					timeSlider.setValue(Math.round(mediaPlayerComponent.getMediaPlayer().getPosition() * 100));
					String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(newTime),
							TimeUnit.MILLISECONDS.toMinutes(newTime)
									- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(newTime)),
							TimeUnit.MILLISECONDS.toSeconds(newTime)
									- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(newTime)));
					// System.out.println(position + " " + newTime + " " + s);
					getCurrenttimeLabel().setText(s);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
				super.videoOutput(mediaPlayer, newCount);
				long newTime = mediaPlayer.getLength();
				String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(newTime),
						TimeUnit.MILLISECONDS.toMinutes(newTime)
								- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(newTime)),
						TimeUnit.MILLISECONDS.toSeconds(newTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(newTime)));
				// System.out.println("mediaChanged " + s);
				getTotaltime().setText(s);

			}

			@Override
			public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
				super.mediaChanged(mediaPlayer, media, mrl);
			}
		});
	}

	private void fitImageViewSizeForMe(float width, float height) {
		Platform.runLater(() -> {
			getImageView().setFitHeight(height - 10);
			getImageView().setFitWidth(width - 10);
			getImageView().setX(5);
			getImageView().setY(5);
		});
	}

	private class CanvasPlayerComponent extends DirectMediaPlayerComponent {

		public CanvasPlayerComponent() {
			super(new CanvasBufferFormatCallback());
		}

		PixelWriter pixelWriter = null;

		private PixelWriter getPW() {
			if (pixelWriter == null) {
				pixelWriter = writableImage.getPixelWriter();
			}
			return pixelWriter;
		}

		@Override
		public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
			//System.out.println("timeChanged : " + newTime);
			super.timeChanged(mediaPlayer, newTime);
		}

		@Override
		public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
			super.mediaChanged(mediaPlayer, media, mrl);
		}

		@Override
		public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
			if (writableImage == null || mediaPlayer == null) {
				return;
			}
			Platform.runLater(() -> {
				try {
					Memory nativeBuffer = mediaPlayer.lock()[0];
					ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
					getPW().setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer,
							bufferFormat.getPitches()[0]);
					videoDimension = mediaPlayer.getVideoDimension();
					wtFactor = videoDimension.getWidth() / (playerHolder.getMaxWidth() - 10) * 1.0;
					htFactor = videoDimension.getHeight() / (playerHolder.getMaxHeight() - 10) * 1.0;
					// System.out.println(videoDimension + " " + wtFactor + " "
					// + htFactor);
				} catch (Exception e) {
					return;
				} finally {
					mediaPlayer.unlock();
				}
			});
		}
	}

	private class CanvasBufferFormatCallback implements BufferFormatCallback {
		@Override
		public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
			Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
			Platform.runLater(() -> videoSourceRatioProperty.set((float) sourceHeight / (float) sourceWidth));
			return new RV32BufferFormat((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
		}
	}

	public AnchorPane getPlayerHolder() {
		return playerHolder;
	}

	public Button getOpenvideo_butt() {
		return openvideo_butt;
	}

	public void setOpenvideo_butt(Button openvideo_butt) {
		this.openvideo_butt = openvideo_butt;
	}

	public Button getFforward_butt() {
		return fforward_butt;
	}

	public void setFforward_butt(Button fforward_butt) {
		this.fforward_butt = fforward_butt;
	}

	public Button getPlay_butt() {
		return play_butt;
	}

	public void setPlay_butt(Button play_butt) {
		this.play_butt = play_butt;
	}

	public Button getRewind_butt() {
		return rewind_butt;
	}

	public void setRewind_butt(Button rewind_butt) {
		this.rewind_butt = rewind_butt;
	}

	public Button getStop_butt() {
		return stop_butt;
	}

	public void setStop_butt(Button stop_butt) {
		this.stop_butt = stop_butt;
	}

	public Button getNext_butt() {
		return next_butt;
	}

	public void setNext_butt(Button next_butt) {
		this.next_butt = next_butt;
	}

	public Button getPrev_butt() {
		return prev_butt;
	}

	public void setPrev_butt(Button prev_butt) {
		this.prev_butt = prev_butt;
	}

	public ToggleButton getShowtags_butt() {
		return showtags_butt;
	}

	public void setShowtags_butt(ToggleButton showtags_butt) {
		this.showtags_butt = showtags_butt;
	}

	public ToggleButton getMute_butt() {
		return mute_butt;
	}

	public void setMute_butt(ToggleButton mute_butt) {
		this.mute_butt = mute_butt;
	}

	public ToggleButton getToggleannotation_butt() {
		return toggleannotation_butt;
	}

	public void setToggleannotation_butt(ToggleButton toggleannotation_butt) {
		this.toggleannotation_butt = toggleannotation_butt;
	}

	public ImageView getPlaybuttimg() {
		return playbuttimg;
	}

	public void setPlaybuttimg(ImageView playbuttimg) {
		this.playbuttimg = playbuttimg;
	}

	public Slider getTimeSlider() {
		return timeSlider;
	}

	public void setTimeSlider(Slider timeSlider) {
		this.timeSlider = timeSlider;
	}

	public Slider getVolumeslider() {
		return volumeslider;
	}

	public void setVolumeslider(Slider volumeslider) {
		this.volumeslider = volumeslider;
	}

	public TextField getCurrenttimeLabel() {
		return currenttimeLabel;
	}

	public void setCurrenttimeLabel(TextField currenttimeLabel) {
		this.currenttimeLabel = currenttimeLabel;
	}

	public TextField getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(TextField totaltime) {
		this.totaltime = totaltime;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public DirectMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}

	public void setMediaPlayerComponent(DirectMediaPlayerComponent mediaPlayerComponent) {
		this.mediaPlayerComponent = mediaPlayerComponent;
	}

	public WritableImage getWritableImage() {
		return writableImage;
	}

	public void setWritableImage(WritableImage writableImage) {
		this.writableImage = writableImage;
	}

	public WritablePixelFormat<ByteBuffer> getPixelFormat() {
		return pixelFormat;
	}

	public void setPixelFormat(WritablePixelFormat<ByteBuffer> pixelFormat) {
		this.pixelFormat = pixelFormat;
	}

	public FloatProperty getVideoSourceRatioProperty() {
		return videoSourceRatioProperty;
	}

	public void setVideoSourceRatioProperty(FloatProperty videoSourceRatioProperty) {
		this.videoSourceRatioProperty = videoSourceRatioProperty;
	}

	public Dimension getVideoDimension() {
		return videoDimension;
	}

	public void setVideoDimension(Dimension videoDimension) {
		this.videoDimension = videoDimension;
	}

	public double getWtFactor() {
		return wtFactor;
	}

	public void setWtFactor(double wtFactor) {
		this.wtFactor = wtFactor;
	}

	public double getHtFactor() {
		return htFactor;
	}

	public void setHtFactor(double htFactor) {
		this.htFactor = htFactor;
	}

	public static int getBaserectwidth() {
		return baseRectWidth;
	}

	public static int getBaserectheight() {
		return baseRectHeight;
	}

	public static String getPathToVideo() {
		return PATH_TO_VIDEO;
	}

	public static String getPATH_TO_VIDEO() {
		return PATH_TO_VIDEO;
	}

	public static void setPATH_TO_VIDEO(String pATH_TO_VIDEO) {
		PATH_TO_VIDEO = pATH_TO_VIDEO;
	}

	public static OverLayGenerator getOverLayGenerator() {
		return overLayGenerator;
	}

	public static void setOverLayGenerator(OverLayGenerator overLayGenerator) {
		VideoPlayerController.overLayGenerator = overLayGenerator;
	}

	public Stage getStageWindow() {
		return stageWindow;
	}

	public void setStageWindow(Stage stageWindow) {
		this.stageWindow = stageWindow;
	}

	public void setPlayerHolder(AnchorPane playerHolder) {
		this.playerHolder = playerHolder;
	}

	public DisplayOverlays getDisplayOverlays() {
		return displayOverlays;
	}

	public void setDisplayOverlays(DisplayOverlays displayOverlays) {
		this.displayOverlays = displayOverlays;
	}

}
