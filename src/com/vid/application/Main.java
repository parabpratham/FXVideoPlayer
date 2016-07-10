package com.vid.application;

import java.lang.reflect.Method;
import java.util.Map;

import org.jdom2.Element;

import com.vid.commons.Helper;
import com.vid.comp.Jcomp.AbstractComp;
import com.vid.comp.Jcomp.StaticComponent;
import com.vid.play.fx.overlay.XMLJDomParser;
import com.vid.play.fx.overlay.XMLJDomParser.Annotation;
import com.vid.play.fx.overlay.XMLJDomParser.AnnotationKey;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = new AnchorPane();
			Scene scene = new Scene(root, 800, 600);
			setAnnotationComp1(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAnnotationComp1(AnchorPane root) {
		Node node = null;
		Map<AnnotationKey, Annotation> xmlQuery = new XMLJDomParser()
				.xmlQuery("I:/workspace/VideoPlayerWorkSpace/SampleInOut/Out/Blank Large.mp4.xml");
		for (AnnotationKey key : xmlQuery.keySet()) {
			try {
				Annotation annotation = xmlQuery.get(key);
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

				if (component instanceof StaticComponent) {
					annHolder.setLayoutX(component.getStartX());
					annHolder.setLayoutY(component.getStartY());
					annHolder.setPrefSize(component.getWidth(), component.getHeight());
					annHolder.getChildren().add(node);
				} else {
					GridPane gridPane = new GridPane();
					gridPane.setMinSize(component.getWidth() + 5, component.getHeight());
					gridPane.setPrefSize(component.getWidth() + 5, component.getHeight());
					ColumnConstraints column1 = new ColumnConstraints();
					column1.setMinWidth(component.getWidth());
					ColumnConstraints column2 = new ColumnConstraints();
					column2.setMinWidth(5);
					RowConstraints row1 = new RowConstraints();
					row1.setValignment(VPos.TOP);
					gridPane.getRowConstraints().add(row1);
					gridPane.getColumnConstraints().add(column1);
					gridPane.getChildren().add(node);
					Button close = new Button();
					ImageView imageView = new ImageView(component.getDeleteGraphic());
					imageView.setFitWidth(10);
					imageView.setFitHeight(10);
					close.setGraphic(imageView);
					close.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					close.setMaxSize(5, 5);
					close.setPrefSize(5, 5);
					gridPane.add(close, 1, 0);
					annHolder.setLayoutX(component.getStartX());
					annHolder.setLayoutY(component.getStartY());
					annHolder.setPrefSize(component.getWidth() + 5, component.getHeight());
					close.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent arg0) {
							annHolder.setVisible(false);
							annotation.setClosed(true);
						}
					});
					annHolder.getChildren().add(gridPane);
				}
				root.getChildren().add(annHolder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setAnnotationComp(AnchorPane root) {
		Node node = null;
		Map<AnnotationKey, Annotation> xmlQuery = new XMLJDomParser()
				.xmlQuery("I:/workspace/SpringWorkspace/VideoEditor/Sample/out/JaiMataDi_KingCircle.mp4.xml");
		for (AnnotationKey key : xmlQuery.keySet()) {
			try {
				Annotation annotation = xmlQuery.get(key);
				Map<String, Class<?>[]> addToMethodMap = Helper.addToMethodMap(annotation.getClassName());
				AbstractComp component = (AbstractComp) Class.forName(annotation.getClassName()).newInstance();
				for (Element parameter : annotation.getParameters().getChildren()) {
					System.out.println("set" + parameter.getName());
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
				// FXMLLoader loader = new
				// FXMLLoader(getClass().getResource("/fxml/AnnotationContainer.fxml"));
				// AnnotationContainerController controller =
				// loader.<AnnotationContainerController> getController();
				AnchorPane annHolder = new AnchorPane();
				GridPane gridPane = new GridPane();
				gridPane.setMinSize(component.getWidth() + 15, component.getHeight());
				ColumnConstraints column1 = new ColumnConstraints();
				column1.setMinWidth(component.getWidth());
				ColumnConstraints column2 = new ColumnConstraints();
				column2.setMinWidth(15);
				RowConstraints row1 = new RowConstraints();
				row1.setValignment(VPos.TOP);
				gridPane.getRowConstraints().add(row1);
				gridPane.getColumnConstraints().add(column1);
				gridPane.setPrefSize(component.getWidth(), component.getHeight());
				gridPane.getChildren().add(node);
				Button close = new Button();
				close.setGraphic(new ImageView(
						new Image("file:I:\\workspace\\SpringWorkspace\\FXVideoPlayer\\src\\resources\\icons")));
				close.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				close.setMaxSize(15, 15);
				gridPane.add(close, 1, 0);
				annHolder.setLayoutX(component.getStartX());
				annHolder.setLayoutY(component.getStartY());
				annHolder.setPrefSize(component.getWidth() + 15, component.getHeight());

				close.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						annHolder.setVisible(false);
					}
				});

				annHolder.getChildren().add(gridPane);
				root.getChildren().add(annHolder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
