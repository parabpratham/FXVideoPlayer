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
import javafx.scene.text.Font;

public class Title extends AbstractComp {

	public Title() {
		setTextIncluded(true);
		setBgImageOption(false);
		setAnn_type("com.vid.comp.Jcomp.Title");
		setAnnName("Title");
		setControllerClass("com.vid.controller.comp.TitleAddController");
		setFXMLPath("fxml/addcompcont/Title_add_popup.fxml");
	}

	@Override
	public Image getGraphic() {
		URL resource = getClass().getClassLoader().getResource("icons/TitleComment.png");
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
		annot += "<StartX> " + 0 + "</StartX> \n";
		annot += "<StartY>" + 0 + "</StartY> \n";
		annot += "<popupinterval>" + getPopupinterval() + "</popupinterval> \n";
		annot += "<underline>" + isUnderline() + "</underline>\n";
		annot += "<Width>" + getWidth() + "</Width> \n";
		annot += "<Height>" + getHeight() + "</Height> \n";
		annot += "<BgColor>" + getBgColor() + "</BgColor> \n";
		annot += "<DisplayString> " + getDisplayString() + " </DisplayString> \n";
		annot += "<DisplayStringColor> " + getDisplayString() + " </DisplayStringColor> \n";
		annot += "<Font>" + getFont() + "</Font> \n";
		annot += "<FontSize>" + getFont_size() + "</FontSize> \n";
		annot += "<bold>" + isBold() + "</bold>\n";
		annot += "<italic>" + isItalic() + "</italic>\n";
		annot += "<strikethrough>" + isStrikethrough() + "</strikethrough>\n";
		annot += "<underline>" + isUnderline() + "</underline>\n";
		annot += "</parameters> \n";
		annot += "</annotation> \n";
		return annot;
	}

	public static void main(String[] args) {
		Title label = new Title();
		label.getGraphic();
	}

	@Override
	public Node getAnnotatedNode() {
		Label label = new Label();
		label.setText(getDisplayString());
		label.setMinWidth(getWidth());
		label.setMinHeight(getHeight());
		if (getBgColor() != null && !getBgColor().equalsIgnoreCase("")) {
			Color value = getBgcolorValue(getBgColor());
			BackgroundFill fill = new BackgroundFill(value, CornerRadii.EMPTY, Insets.EMPTY);
			label.setBackground(new Background(fill));

			if (getFont() != null && !getFont().equalsIgnoreCase("")) {
				if (getFont_size() != null && !getFont_size().equalsIgnoreCase("")) {
					label.setFont(Font.font(getFont(), Double.parseDouble(getFont_size())));
				} else
					label.setFont(Font.font(getFont()));
			}
		}
		return label;

	}

}
