package com.vid.comp.Jcomp;

import java.net.URL;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Note extends AbstractComp {

	public Note() {
		setTextIncluded(true);
		setBgImageOption(false);
		setAnn_type("com.vid.comp.Jcomp.Note");
		setAnnName("Note");
		setControllerClass("com.vid.controller.comp.NoteAddController");
		setFXMLPath("fxml/addcompcont/Note_add_popup.fxml");
	}

	@Override
	public Image getGraphic() {
		URL resource = getClass().getClassLoader().getResource("icons/note.png");
		Image image = new Image("file:" + resource.getPath());
		return image;
	}

	@Override
	public String toXml() {
		String annot = "<annotation id=\"" + getId() + "\" \n type=\"" + getAnn_type() + "\">\n" + "<starttime>"
				+ getStartTime() + "</starttime>\n";
		annot += "<endtime> " + getEndTime() + "</endtime>";
		annot += "<comp_type>JCOMPONENT</comp_type>";
		annot += "<parameters set=\"" + 1 + "\"> \n";
		annot += "<StartX> " + getStartX() + "</StartX> \n";
		annot += "<StartY>" + getStartY() + "</StartY> \n";
		annot += "<Width>" + getWidth() + "</Width> \n";
		annot += "<Height>" + getHeight() + "</Height> \n";
		annot += "<BgColor>" + getBgColor() + "</BgColor> \n";
		annot += "<DisplayString> " + getDisplayString() + " </DisplayString> \n";
		annot += "<DisplayStringColor> " + getDisplayString() + " </DisplayStringColor> \n";
		annot += "<Font>" + getFont() + "</Font> \n";
		annot += "<FontSize>" + getFont_size() + "</FontSize> \n";
		annot += "<Bold>" + isBold() + "</Bold>\n";
		annot += "<Italic>" + isItalic() + "</Italic>\n";
		annot += "<Strikethrough>" + isStrikethrough() + "</Strikethrough>\n";
		annot += "<Underline>" + isUnderline() + "</Underline>\n";
		annot += "</parameters> \n";
		annot += "</annotation> \n";
		return annot;
	}

	public static void main(String[] args) {
	}

	@Override
	public Node getAnnotatedNode() {
		Label label = new Label();
		label.setText(getDisplayString());
		label.setMinWidth(getWidth());
		label.setMinHeight(getHeight());

		if (getBgColor() != null && !getBgColor().equalsIgnoreCase("")) {
			Color value = getColorValue(getBgColor());
			BackgroundFill fill = new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY);
			label.setBackground(new Background(fill));
		}
		label.setWrapText(true);

		// Region region = (Region) label.lookup(".content");
		// region.setStyle("-fx-background-color: " + getBgColor() + ";");

		if (getFont() != null && !getFont().equalsIgnoreCase("")) {
			label.setFont(getFont(true));
		}

		if (getDisplayStringColor() != null && !getDisplayStringColor().equalsIgnoreCase("")) {
			Color value = getColorValue(getDisplayStringColor());
			label.setTextFill(value);
		}

		// TODO underline, strikeout and justify

		return label;
	}

}
