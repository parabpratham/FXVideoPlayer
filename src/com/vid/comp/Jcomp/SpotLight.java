package com.vid.comp.Jcomp;

import java.net.URL;

import org.controlsfx.control.InfoOverlay;

import com.vid.commons.Helper;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SpotLight extends AbstractComp {

	public SpotLight() {
		setTextIncluded(false);
		setBgImageOption(false);
		setInfopanel(true);
		setAnn_type("com.vid.comp.Jcomp.SpotLight");
		setAnnName("SpotLight");
		setControllerClass("com.vid.controller.comp.SpotLightAddController");
		setFXMLPath("fxml/addcompcont/SpotLight_add_popup.fxml");
	}

	@Override
	public Image getGraphic() {
		URL resource = getClass().getClassLoader().getResource("icons/spot_light.png");
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
		annot += "<Bgfilepath>" + getBgfilepath() + "</Bgfilepath> \n";
		annot += "<Showonhover>" + isShowonhover() + "</Showonhover> \n";
		annot += "<Fillbg>" + isFillbg() + "</Fillbg> \n";
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

	@Override
	public Node getAnnotatedNode() {
		ImageView iew = new ImageView();
		InfoOverlay infoOverlay = new InfoOverlay(iew, getDisplayString());
		iew.setFitWidth(getWidth());
		iew.setFitHeight(getHeight());
		infoOverlay.setPrefWidth(getWidth());
		infoOverlay.setPrefHeight(getHeight());
		infoOverlay.setShowOnHover(isShowonhover());

		// TODO change the method to read from container
		if (getBgfilepath() != null && !getBgfilepath().equalsIgnoreCase(""))
			iew.setImage(new Image("file:" + getBgfilepath()));
		else if (getBgColor() != null && !getBgColor().equalsIgnoreCase("")) {
			Color value = getBgcolorValue(getBgColor());
			WritableImage createImage = null;
			createImage = isFillbg() ? Helper.createFilledImage(iew, value) : Helper.createBorderImage(iew, value);
			iew.setImage(createImage);
		}

		// TODO Change text attributes
		Font display = Font.font(getFont());
		
		return infoOverlay;
	}

	public static void main(String[] args) {
		SpotLight label = new SpotLight();
		label.getGraphic();
	}

}
