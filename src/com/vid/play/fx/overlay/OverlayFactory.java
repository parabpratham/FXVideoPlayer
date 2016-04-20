package com.vid.play.fx.overlay;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

import com.vid.commons.Helper;
import com.vid.comp.Jcomp.AbstractComp;
import com.vid.matroska.MatroskaContainer;
import com.vid.play.fx.overlay.XMLJDomParser.Annotation;
import com.vid.play.fx.overlay.XMLJDomParser.AnnotationKey;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;

public class OverlayFactory implements Runnable {

	private XMLJDomParser xmlParser;

	private Map<AnnotationKey, Annotation> annotationMap;

	private static Map<Integer, CompNode> annotationNodeMap;

	private static Map<Integer, CustomOverlayMarker> overlayMarkerMap;

	private ArrayList<AnnotationKey> keyArrayList;

	private List<Integer> eventTimeList;

	private DirectMediaPlayerComponent mediaPlayerComponent;

	public OverlayFactory(MatroskaContainer container, DirectMediaPlayerComponent mediaPlayerComponent) {
		setContainer(container);
		xmlParser = new XMLJDomParser();
		keyArrayList = new ArrayList<AnnotationKey>();
		eventTimeList = new ArrayList<Integer>();
		overlayMarkerMap = new HashMap<>();
		annotationNodeMap = new HashMap<>();
		setupOverlays();
	}

	public OverlayFactory(MatroskaContainer container) {
		setContainer(container);
		xmlParser = new XMLJDomParser();
		keyArrayList = new ArrayList<AnnotationKey>();
		eventTimeList = new ArrayList<Integer>();
		overlayMarkerMap = new HashMap<>();
		setupOverlays();
	}

	@Override
	public void run() {
		setupOverlays();
	}

	public static MatroskaContainer container;

	public void setupOverlays() {

		annotationMap = xmlParser.xmlQuery(container);

		if (annotationMap != null) {
			keyArrayList.addAll(annotationMap.keySet());
			Collections.sort(keyArrayList);
			fillEventList();
			generateOveralysList();
		}

		setAnnotationComp();

	}

	private void setAnnotationComp() {
		Node node = null;
		for (AnnotationKey annotationKey : annotationMap.keySet()) {
			try {
				Annotation annotation = annotationMap.get(annotationKey);
				Map<String, Class<?>[]> addToMethodMap = Helper.addToMethodMap(annotation.getClassName());
				AbstractComp component = (AbstractComp) Class.forName(annotation.getClassName()).newInstance();
				for (Element parameter : annotation.getParameters().getChildren()) {
					// System.out.println("set" + parameter.getName());
					Method method = component.getClass().getMethod("set" + parameter.getName(),
							addToMethodMap.get("set" + parameter.getName()));
					Class<?>[] pType = method.getParameterTypes();
					for (int i = 0; i < pType.length; i++) {
						Object obj = Helper.parseParameter(pType[i], parameter);
						if (obj != null) {
							method.invoke(component, obj);
						}
					}
				}

				node = component.getAnnotatedNode();
				AnchorPane annHolder = new AnchorPane();
				GridPane gridPane = new GridPane();
				gridPane.setMinSize(component.getWidth() + 10, component.getHeight());
				ColumnConstraints column1 = new ColumnConstraints();
				column1.setMinWidth(component.getWidth());
				ColumnConstraints column2 = new ColumnConstraints();
				column2.setMinWidth(10);
				RowConstraints row1 = new RowConstraints();
				row1.setValignment(VPos.TOP);
				gridPane.getRowConstraints().add(row1);
				gridPane.getColumnConstraints().add(column1);
				gridPane.setPrefSize(component.getWidth(), component.getHeight());
				gridPane.getChildren().add(node);
				Button close = new Button();
				ImageView imageView = new ImageView(component.getDeleteGraphic());
				imageView.setFitWidth(10);
				imageView.setFitHeight(10);
				close.setGraphic(imageView);
				close.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				close.setMaxSize(10, 10);
				gridPane.add(close, 1, 0);
				annHolder.getChildren().add(gridPane);
				// System.out.println("Comp pos " + component.getStartX() + " "
				// + component.getStartY());
				annHolder.setLayoutX(component.getStartX());
				annHolder.setLayoutY(component.getStartY());
				annHolder.setPrefSize(component.getWidth() + 10, component.getHeight());
				close.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						annHolder.setVisible(false);
						annotation.setClosed(true);
					}
				});
				annotationNodeMap.put(annotation.getId(), new CompNode(annHolder, component));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class CompNode {
		private Node node;

		public Node getNode() {
			return node;
		}

		public void setNode(Node node) {
			this.node = node;
		}

		public AbstractComp getComp() {
			return comp;
		}

		public void setComp(AbstractComp comp) {
			this.comp = comp;
		}

		private AbstractComp comp;

		public CompNode(Node node, AbstractComp comp) {
			this.node = node;
			this.comp = comp;
		}

	}

	private void fillEventList() {

		for (AnnotationKey key : getKeyArrayList()) {
			if (!eventTimeList.contains(key.getStartTime()))
				eventTimeList.add(key.getStartTime());
			if (!eventTimeList.contains(key.getEndTime()))
				eventTimeList.add(key.getEndTime());
		}
		Collections.sort(eventTimeList);
	}

	private void generateOveralysList() {
		System.out.println("generateOveralysList");
		for (int overlayGenerationTime : eventTimeList) {
			System.out.println(overlayGenerationTime);
			for (AnnotationKey key : keyArrayList) {
				if (key.getEndTime() <= overlayGenerationTime)
					key.setChecked(true);
				if (key.getStartTime() > overlayGenerationTime)
					break;
				else if (key.getStartTime() <= overlayGenerationTime && key.getEndTime() > overlayGenerationTime) {
					CustomOverlayMarker marker;
					if (overlayMarkerMap.get(overlayGenerationTime) == null) {
						marker = new CustomOverlayMarker();
						marker.setStartTime(key.getStartTime());
						marker.setEndTime(key.getEndTime());
					} else
						marker = overlayMarkerMap.get(overlayGenerationTime);
					marker.addOverlayMarker(overlayGenerationTime, annotationMap.get(key));
					System.out.println(
							"overlayGenerationTime " + overlayGenerationTime + " " + marker.getAnnotations().size());
					overlayMarkerMap.put(overlayGenerationTime, marker);
				}
			}
		}

	}

	public ArrayList<AnnotationKey> getKeyArrayList() {
		return keyArrayList;
	}

	public class CustomOverlayMarker {

		int id;

		int startTime;

		int endTime;

		List<Annotation> annotations;

		public int getStartTime() {
			return startTime;
		}

		public void setStartTime(int startTime) {
			this.startTime = startTime;
		}

		public int getEndTime() {
			return endTime;
		}

		public void setEndTime(int endTime) {
			this.endTime = endTime;
		}

		public List<Annotation> getAnnotations() {
			return annotations;
		}

		public void setAnnotations(List<Annotation> annotations) {
			this.annotations = annotations;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void addOverlayMarker(int id, Annotation annotation) {
			if (annotations == null)
				annotations = new ArrayList<>();
			annotations.add(annotation);
		}

	}

	public static Map<Integer, CustomOverlayMarker> getOverlayMarkerMap() {
		return overlayMarkerMap;
	}

	public static void setOverlayMarkerMap(Map<Integer, CustomOverlayMarker> overlayMarkerMap) {
		OverlayFactory.overlayMarkerMap = overlayMarkerMap;
	}

	public static MatroskaContainer getContainer() {
		return container;
	}

	public static void setContainer(MatroskaContainer container) {
		OverlayFactory.container = container;
	}

	public DirectMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}

	public void setMediaPlayerComponent(DirectMediaPlayerComponent mediaPlayerComponent) {
		this.mediaPlayerComponent = mediaPlayerComponent;
	}

	public static Map<Integer, CompNode> getAnnotationNodeMap() {
		return annotationNodeMap;
	}

}
