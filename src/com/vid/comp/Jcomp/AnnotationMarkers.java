package com.vid.comp.Jcomp;

import java.net.URL;

import org.controlsfx.control.PopOver;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class AnnotationMarkers extends AbstractComp {

	public AnnotationMarkers() {
		setTextIncluded(false);
		setBgImageOption(false);
		setInfopanel(true);
		setAnn_type("com.vid.comp.Jcomp.Markers");
		setAnnName("Marker");
		setControllerClass("com.vid.controller.comp.MarkersAddController");
		setFXMLPath("fxml/addcompcont/Markers_add_popup.fxml");
	}

	@Override
	public Node getAnnotatedNode() {
		Image image = new Image("file:" + getBgfilepath());
		ImageView iew = new ImageView(image);
		PopOver popOver = createPopOver();
		MarkerPopUp markerPopUp = new MarkerPopUp(iew, popOver);
		popOver.setContentNode(markerPopUp.getContentText());
		iew.setFitWidth(getWidth());
		iew.setFitHeight(getHeight());
		iew.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evt) {
				if (popOver != null && !popOver.isDetached()) {
					popOver.hide();
				}
				if (evt.getClickCount() == 2) {
					try {
						if (popOver != null && popOver.isShowing()) {
							popOver.hide(Duration.ZERO);
						}
						popOver.show(iew);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		});

		return iew;
	}

	// Added class for the Marker control
	public class MarkerPopUp {

		ImageView view;
		PopOver popOver;
		Label contentText;

		public MarkerPopUp(ImageView view, PopOver popOver) {
			this.view = view;
			this.popOver = popOver;
			try {
				contentText = new Label(getDisplayString());
				contentText.setMaxHeight(100);
				contentText.setMaxWidth(100);
				contentText.setWrapText(true);
				contentText.setDisable(true);
				// contentText.setFont(Font.font(getFont(),
				// Integer.parseInt(getFont_size())));
				/// contentText.setStyle("-fx-text-fill: " +
				/// getDisplayStringColor() + ";");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public ImageView getView() {
			return view;
		}

		public void setView(ImageView view) {
			this.view = view;
		}

		public PopOver getPopOver() {
			return popOver;
		}

		public void setPopOver(PopOver popOver) {
			this.popOver = popOver;
		}

		public void setContentText(Label contentText) {
			this.contentText = contentText;
		}

		public Label getContentText() {
			return contentText;
		}

	}

	private PopOver createPopOver() {
		PopOver popOver = new PopOver();
		popOver.setDetachable(isDetachable());
		popOver.setDetached(isInitially_detached());
		popOver.setArrowSize(getArrow_size());
		popOver.setArrowIndent(getArrow_indent());
		popOver.setArrowLocation(getArrow_loaction());
		popOver.setCornerRadius(getCorner_radius());
		return popOver;

	}

	@Override
	public Image getGraphic() {
		URL resource = getClass().getClassLoader().getResource("icons/marker.png");
		Image image = new Image("file:" + resource.getPath());
		return image;
	}

	@Override
	public String toXml() {
		String annot = "<annotation id=\"" + getId() + "\" \n type=\"" + getAnn_type() + "\">\n" + "<starttime>"
				+ getStartTime() + "</StartTime>\n";
		annot += "<EndTime> " + getEndTime() + "</EndTime>";
		annot += "<comp_type>JCOMPONENT</comp_type>";
		annot += "<parameters set=\"" + 1 + "\"> \n";
		annot += "<StartX> " + getStartX() + "</StartX> \n";
		annot += "<StartY>" + getStartY() + "</StartY> \n";
		annot += "<Width>" + getWidth() + "</Width> \n";
		annot += "<Height>" + getHeight() + "</Height> \n";
		annot += "<Bgfilepath>" + getBgfilepath() + "</Bgfilepath> \n";
		annot += "<DisplayString> " + getDisplayString() + " </DisplayString> \n";
		annot += "<DisplayStringColor> " + getDisplayString() + " </DisplayStringColor> \n";
		annot += "<Font>" + getFont() + "</Font> \n";
		annot += "<Font_Size>" + getFont_size() + "</Font_Size> \n";
		annot += "<Bold>" + isBold() + "</Bold>\n";
		annot += "<Italic>" + isItalic() + "</Italic>\n";
		annot += "<Strikethrough>" + isStrikethrough() + "</Strikethrough>\n";
		annot += "<Detachable>" + isDetachable() + "</Detachable>\n";
		annot += "<Initially_detached>" + isInitially_detached() + "</Initially_detached>\n";
		annot += "<Auto_position>" + isAuto_position() + "</Auto_position>\n";
		annot += "<Arrow_indent>" + getArrow_indent() + "</Arrow_indent>\n";
		annot += "<Arrow_size>" + getArrow_size() + "</Arrow_size>\n";
		annot += "<Corner_radius>" + getCorner_radius() + "</Corner_radius>\n";
		annot += "<Arrow_loaction>" + getArrow_loaction() + "</Arrow_loaction>\n";

		annot += "</parameters> \n";
		annot += "</annotation> \n";
		return annot;
	}

	public static void main(String[] args) {
		AnnotationMarkers label = new AnnotationMarkers();
		label.getGraphic();
	}

}
