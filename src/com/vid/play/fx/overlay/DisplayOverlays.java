package com.vid.play.fx.overlay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vid.matroska.MatroskaContainer;
import com.vid.play.fx.overlay.OverlayFactory.CompNode;
import com.vid.play.fx.overlay.OverlayFactory.CustomOverlayMarker;
import com.vid.play.fx.overlay.XMLJDomParser.Annotation;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class DisplayOverlays implements Runnable {

	public final static Object overlay = new Object();

	private int nextTime = 0;

	private List<Integer> keySet = new ArrayList<Integer>();

	private Map<Integer, CustomOverlayMarker> overlays;

	private MatroskaContainer container;

	private DirectMediaPlayerComponent mediaPlayerComponent;

	private AnchorPane playerHolder;

	public DisplayOverlays(MatroskaContainer container2, DirectMediaPlayerComponent mediaPlayerComponent,
			AnchorPane playerHolder) {
		setContainer(container);
		setMediaPlayerComponent(mediaPlayerComponent);
		setPlayerHolder(playerHolder);
	}

	public DisplayOverlays(MatroskaContainer container) {
		setContainer(container);
	}

	@Override
	public void run() {
		overlays = new HashMap<>(OverLayGenerator.getOverlays());
		if (getOverlays() != null) {
			keySet.addAll(OverLayGenerator.getOverlays().keySet());
			java.util.Collections.sort(keySet);
		}
		registerListeners();

		if (keySet.get(0) != null)
			nextTime = keySet.get(0);

		// displayOverlayTimeLine();
	}

	private List<Node> prevAnnoCount;
	private int overlayIndex = 0;

	private void registerListeners() {
		getMediaPlayerComponent().getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

			@Override
			public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
				super.mediaChanged(mediaPlayer, media, mrl);
				overlays = new HashMap<>(OverLayGenerator.getOverlays());
				if (getOverlays() != null) {
					keySet.addAll(OverLayGenerator.getOverlays().keySet());
					java.util.Collections.sort(keySet);
				}
				// nextTime = 0;
			}

			@Override
			public void timeChanged(uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer, long newTime) {
				super.timeChanged(mediaPlayer, newTime);
				if (newTime >= nextTime) {
					//System.out.println("Display overlay Event called " + newTime + " " + nextTime);
					if (prevAnnoCount != null) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								//System.out.println("removeAnnotationComp " + Calendar.getInstance().getTimeInMillis());
								removeAnnotationComp();
							}
						});
					}
					for (; overlayIndex < keySet.size() - 1; overlayIndex++) {
						boolean isTrue = (newTime >= keySet.get(overlayIndex)
								&& newTime < keySet.get(overlayIndex + 1));
						System.out.println(overlayIndex + " " + newTime + ">=" + keySet.get(overlayIndex) + " -- "
								+ newTime + " < " + keySet.get(overlayIndex + 1) + " " + isTrue);
						if (isTrue) {
							try {
								System.out.println("Display overlay " + overlayIndex + " " + keySet.get(overlayIndex)
										+ " " + newTime + " " + keySet.get(overlayIndex + 1));
								// mediaPlayer.pause();
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										System.out.println(
												"setAnnotationComp " + Calendar.getInstance().getTimeInMillis());
										setAnnotationComp(getOverlays().get(keySet.get(overlayIndex)));
									}
								});
							} catch (Exception e) {
								e.printStackTrace();
							}
							nextTime = keySet.get(overlayIndex + 1);
							overlayIndex = 0;
							break;
						}
					}

					if (overlayIndex == keySet.size()) {
						nextTime = Integer.MAX_VALUE;
					}
				}
			}

		});

	}

	public void stopAnnotationComp() {
		removeAnnotationComp();
		nextTime = 0;
	}

	private void removeAnnotationComp() {
		if (prevAnnoCount == null)
			return;
		for (Node node : prevAnnoCount) {
			try {
				//System.out.println("Remove " + node + " ");
				getPlayerHolder().getChildren().remove(node);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		prevAnnoCount = null;
	}

	private void setAnnotationComp(CustomOverlayMarker customOverlayMarker) {
		prevAnnoCount = new ArrayList<>();
		System.out.println("customOverlayMarker.getAnnotations() -- " + customOverlayMarker.getAnnotations().size());
		try {
			for (Annotation annotation : customOverlayMarker.getAnnotations()) {
				if (!annotation.isClosed()) {
					try {
						System.out.println("Add -- " + annotation.getId() + " " + annotation.getStartTime() + " "
								+ annotation.getEndTime());
						CompNode compNode = OverlayFactory.getAnnotationNodeMap().get(annotation.getId());
						AnchorPane node = (AnchorPane) compNode.getNode();
						node.setLayoutX(compNode.getComp().getStartX());
						node.setLayoutY(compNode.getComp().getStartY());
						getPlayerHolder().getChildren().add(node);
						prevAnnoCount.add(node);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * private void setAnnotationComp(CustomOverlayMarker customOverlayMarker) {
	 * Node node = null; AnchorPane container = getPlayerHolder();
	 * List<Annotation> annotations = customOverlayMarker.getAnnotations(); for
	 * (Annotation annotation : annotations) { try { Map<String, Class<?>[]>
	 * addToMethodMap = Helper.addToMethodMap(annotation.getClassName());
	 * AbstractComp component = (AbstractComp)
	 * Class.forName(annotation.getClassName()).newInstance(); for (Element
	 * parameter : annotation.getParameters().getChildren()) {
	 * System.out.println("set" + parameter.getName()); Method method =
	 * component.getClass().getMethod("set" + parameter.getName(),
	 * addToMethodMap.get("set" + parameter.getName())); Class<?>[] pType =
	 * method.getParameterTypes(); for (int i = 0; i < pType.length; i++) {
	 * Object obj = Helper.parseParameter(pType[i], parameter); if (obj != null)
	 * { method.invoke(component, obj); } } } AnchorPane annHolder = new
	 * AnchorPane(); GridPane gridPane = new GridPane();
	 * gridPane.setMinSize(component.getWidth() + 15, component.getHeight());
	 * ColumnConstraints column1 = new ColumnConstraints();
	 * column1.setMinWidth(component.getWidth()); ColumnConstraints column2 =
	 * new ColumnConstraints(); column2.setMinWidth(15); RowConstraints row1 =
	 * new RowConstraints(); row1.setValignment(VPos.TOP);
	 * gridPane.getRowConstraints().add(row1);
	 * gridPane.getColumnConstraints().add(column1);
	 * gridPane.setPrefSize(component.getWidth(), component.getHeight());
	 * gridPane.getChildren().add(node); gridPane.add(new Button(), 1, 0);
	 * annHolder.setLayoutX(component.getStartX());
	 * annHolder.setLayoutY(component.getStartY());
	 * annHolder.setPrefSize(component.getWidth() + 15, component.getHeight());
	 * 
	 * annHolder.getChildren().add(gridPane);
	 * container.getChildren().add(annHolder); } catch (Exception e) {
	 * e.printStackTrace(); } } }
	 */
	public static Object getOverlay() {
		return overlay;
	}

	public Map<Integer, CustomOverlayMarker> getOverlays() {
		return overlays;
	}

	public void setOverlays(Map<Integer, CustomOverlayMarker> overlays) {
		this.overlays = overlays;
	}

	public MatroskaContainer getContainer() {
		return container;
	}

	public void setContainer(MatroskaContainer container) {
		this.container = container;
	}

	public DirectMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}

	public void setMediaPlayerComponent(DirectMediaPlayerComponent mediaPlayerComponent) {
		this.mediaPlayerComponent = mediaPlayerComponent;
	}

	public AnchorPane getPlayerHolder() {
		return playerHolder;
	}

	public void setPlayerHolder(AnchorPane playerHolder) {
		this.playerHolder = playerHolder;
	}

}
