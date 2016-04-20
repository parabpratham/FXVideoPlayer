package com.vid.play.fx.overlay;

import java.util.Calendar;
import java.util.Map;

import com.vid.matroska.MatroskaContainer;
import com.vid.play.fx.overlay.OverlayFactory.CustomOverlayMarker;

import javafx.scene.layout.AnchorPane;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class OverLayGenerator {

	private static Map<Integer, CustomOverlayMarker> overlays;

	private boolean isTransperantWindowSupport;

	private static ThreadGroup tg;
	private static ThreadGroup dg;

	private MatroskaContainer container;

	private DirectMediaPlayerComponent mediaPlayerComponent;

	private AnchorPane playerHolder;

	public OverLayGenerator(DirectMediaPlayerComponent component, MatroskaContainer container,
			AnchorPane playerHolder) {

		setPlayerHolder(playerHolder);
		setContainer(container);

		setMediaPlayerComponent(component);

		// Start if the media is pre_loaded
		startNewGeneratorFactory(container);

		// Add listeners to comp
		registerListeners();
	}

	/**
	 * To read the annotations from the xml file and generate overlays to be
	 * displayed
	 * 
	 * called when <br>
	 * 1> Media is loaded for the first time <br>
	 * 2> Media changes
	 * 
	 * @param container
	 * 
	 */

	private void startNewGeneratorFactory(MatroskaContainer container) {
		setContainer(container);
		new OverlayFactory(container, getComponent());
		overlays = OverlayFactory.getOverlayMarkerMap();
		if (overlays != null) {
			dg = new ThreadGroup("Overlays Threads");
			// Platform.runLater(new DisplayOverlays(container,
			// mediaPlayerComponent, getPlayerHolder()));
			// new DisplayOverlays(container, mediaPlayerComponent,
			// getPlayerHolder()).run();
			//
			// new Thread(dg, new DisplayOverlays(container,
			// mediaPlayerComponent, getPlayerHolder()), "Display_Overlay")
			// .start();
			// System.out.println(dg.activeCount() + " threads in thread
			// group.");
		}
	}

	@SuppressWarnings("deprecation")
	private static void resetOverlays() {
		try {
			Thread thrds[] = new Thread[dg.activeCount()];
			dg.enumerate(thrds);
			for (Thread t : thrds) {
				System.out.println(t.getName() + " Stopping " + t.getName());
				if (t.isAlive())
					t.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void stopVideoOerlays() {
		if (tg != null) {
			try {
				System.out.println("StopVideo Called " + Calendar.getInstance().getTimeInMillis());
				Thread thrds[] = new Thread[tg.activeCount()];
				tg.enumerate(thrds);
				for (Thread t : thrds) {
					System.out.println(t.getName() + " Stopping " + t.getName());
					if (t.isAlive())
						t.stop();
				}
			} catch (Exception e) {
			}
		}
		resetOverlays();
	}

	static int i = 0;

	private void registerListeners() {
		getComponent().getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void playing(MediaPlayer mediaPlayer) {
			}

			@Override
			public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
				mediaPlayer.setVolume(mediaPlayer.getVolume());
			}

			@Override
			public void stopped(MediaPlayer mediaPlayer) {
				super.stopped(mediaPlayer);
				stopVideoOerlays();
			}

			@Override
			public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
				super.timeChanged(mediaPlayer, newTime);
			}

			@Override
			public void newMedia(MediaPlayer mediaPlayer) {
				super.newMedia(mediaPlayer);
				System.out.println("-- new media ----" + mediaPlayer.getTitle());
			}

			@Override
			public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
				super.mediaChanged(mediaPlayer, media, mrl);
				System.out.println("-- media changed ----" + mrl);
				// if (mrl.equals("0")) {
				// stopVideoOerlays();
				startNewGeneratorFactory(getContainer());
				// }
			}

		});

		/*
		 * getMediaListPlayer().addMediaListPlayerEventListener(new
		 * MediaListPlayerEventAdapter() {
		 * 
		 * @Override public void stopped(MediaListPlayer mediaListPlayer) {
		 * super.stopped(mediaListPlayer); enableOverlay(false);
		 * stopVideoOerlays(); }
		 * 
		 * @Override public void nextItem(MediaListPlayer mediaListPlayer,
		 * libvlc_media_t item, String itemMrl) { enableOverlay(false);
		 * stopVideoOerlays(); super.nextItem(mediaListPlayer, item, itemMrl);
		 * // System.out.println("Start nwe generator factory");
		 * System.out.println("Start nwe generator factory");
		 * startNewGeneratorFactory(); enableOverlay(true); }
		 * 
		 * @Override public void mediaStateChanged(MediaListPlayer
		 * mediaListPlayer, int newState) { // TODO Auto-generated method stub
		 * System.out.println("Media state changed " + newState);
		 * super.mediaStateChanged(mediaListPlayer, newState); } });
		 */
	}

	public static Map<Integer, CustomOverlayMarker> getOverlays() {
		return overlays;
	}

	public static void setOverlays(Map<Integer, CustomOverlayMarker> overlays) {
		OverLayGenerator.overlays = overlays;
	}

	// TODO
	public final static Object obj = new Object();

	public static void NotifyObj() {
		synchronized (obj) {
			obj.notifyAll();
		}
	}

	public boolean isTransperantWindowSupport() {
		return isTransperantWindowSupport;
	}

	public void setTransperantWindowSupport(boolean isTransperantWindowSupport) {
		this.isTransperantWindowSupport = isTransperantWindowSupport;
	}

	public static void main(String[] args) {
		// OverLayGenerator overLayGenerator = new OverLayGenerator();
	}

	public DirectMediaPlayerComponent getComponent() {
		return mediaPlayerComponent;
	}

	public void setComponent(DirectMediaPlayerComponent component) {
		this.mediaPlayerComponent = component;
	}

	public DirectMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}

	public void setMediaPlayerComponent(DirectMediaPlayerComponent mediaPlayerComponent) {
		this.mediaPlayerComponent = mediaPlayerComponent;
	}

	public MatroskaContainer getContainer() {
		return container;
	}

	public void setContainer(MatroskaContainer container) {
		this.container = container;
	}

	public AnchorPane getPlayerHolder() {
		return playerHolder;
	}

	public void setPlayerHolder(AnchorPane playerHolder) {
		this.playerHolder = playerHolder;
	}

}
