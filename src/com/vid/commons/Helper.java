package com.vid.commons;

import java.awt.Font;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.controlsfx.control.PopOver.ArrowLocation;
import org.jdom2.Element;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Helper {

	private static Map<String, Map<String, Class<?>[]>> classMethods = new HashMap<>();

	/**
	 * @param ClassName
	 * @return MethodParameterMap Map<MethodName, Parameters : Class<?>[]>
	 * 
	 *         <p>
	 *         A sort of cache for the CustomComponent classes setter
	 *         metthods.Stores the method name against the parameters for
	 *         invoking the methods later.
	 *         </p>
	 */
	public static Map<String, Class<?>[]> addToMethodMap(String className) {

		if (getClassMethods().get(className) == null) {
			//System.out.println(className);
			Map<String, Class<?>[]> methodParameterMap = new HashMap<String, Class<?>[]>();
			try {
				Method[] methods = Class.forName(className).newInstance().getClass().getMethods();
				for (Method method : methods) {
					if (method.getName().startsWith("set")) {
						methodParameterMap.put(method.getName(), method.getParameterTypes());
					}
				}
				getClassMethods().put(className, methodParameterMap);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return getClassMethods().get(className);
	}

	public static String setTotalTime(long millis) {
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		return s;
	}

	/**
	 * @param Class<?>
	 *            pType
	 * @param XMLElement
	 *            parameter
	 * @return InstanceOfTheComponent
	 * 
	 *         Depending upon the component_type generates the appropriate
	 *         instance of the parameters to be passed for invoking the setter
	 *         methods
	 * 
	 */

	public static Object parseParameter(Class<?> pType, Element parameter) {

		if (parameter.getTextTrim().equalsIgnoreCase("")
				&& !pType.getCanonicalName().equalsIgnoreCase(Font.class.getCanonicalName()))
			return null;

		// System.out.print(pType.getCanonicalName() + " " + parameter.getName()
		// + " " + parameter.getTextTrim() + " ");
		Object newInstance = null;
		try {

			if (pType.getCanonicalName().equalsIgnoreCase(double.class.getCanonicalName())) {
				newInstance = Double.parseDouble(parameter.getTextTrim());
			} else if (pType.getCanonicalName().equalsIgnoreCase(int.class.getCanonicalName())) {
				newInstance = Integer.parseInt(parameter.getTextTrim());
			} else if (pType.getCanonicalName().equalsIgnoreCase(String.class.getCanonicalName())) {
				newInstance = parameter.getTextTrim();
			} /*
				 * else if
				 * (pType.getCanonicalName().equalsIgnoreCase(javafx.scene.paint
				 * .Color.class.getCanonicalName())) { String[] colorDetails =
				 * parameter.getTextTrim().split(","); Color color; try { Field
				 * field =
				 * Class.forName(Color.class.getCanonicalName()).getField(
				 * colorDetails[0]); color = (Color) field.get(null); } catch
				 * (Exception e) { color = null; // Not defined } newInstance =
				 * new SupportedColors(color,
				 * Integer.parseInt(colorDetails[1])); }
				 */
			else if (pType.getCanonicalName().equalsIgnoreCase(Font.class.getCanonicalName())) {
				if (parameter.getTextTrim() == null || parameter.getTextTrim().equalsIgnoreCase(""))
					newInstance = Fonts.getAppFont();
				else {
					String[] parameters = parameter.getTextTrim().split(",");
					// 0 = name, 1=Stylr,2=size
					newInstance = Fonts.getFont(parameters[0], Integer.parseInt(parameters[1]),
							Integer.parseInt(parameters[2]));
				}
			} else if (pType.getCanonicalName().equalsIgnoreCase(boolean.class.getCanonicalName())) {
				newInstance = Boolean.parseBoolean(parameter.getTextTrim());
			} else if (pType.getCanonicalName().equalsIgnoreCase(ArrowLocation.class.getName())) {
				newInstance = ArrowLocation.valueOf(parameter.getTextTrim());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return newInstance;
	}

	public static Map<String, Map<String, Class<?>[]>> getClassMethods() {
		return classMethods;
	}

	public static void setClassMethods(Map<String, Map<String, Class<?>[]>> classMethods) {
		Helper.classMethods = classMethods;
	}

	public static WritableImage createBorderImage(ImageView iew, javafx.scene.paint.Color color) {
		int width = (int) iew.getFitWidth();
		int height = (int) iew.getFitHeight();
		//System.out.println("createBorderImage " + width + " " + height);
		WritableImage writableImage = new WritableImage(width, height);
		try {
			PixelWriter writer = writableImage.getPixelWriter();

			for (int i = 0; i < height; i++) {
				writer.setColor(0, i, color);
				writer.setColor(width - 1, i, color);
			}

			for (int j = 0; j < width; j++) {
				writer.setColor(j, 0, color);
				writer.setColor(j, height - 1, color);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return writableImage;
	}

	public static WritableImage createFilledImage(ImageView iew, javafx.scene.paint.Color color) {
		int width = (int) iew.getFitWidth();
		int height = (int) iew.getFitHeight();
		//System.out.println("createBorderImage " + width + " " + height);
		WritableImage writableImage = new WritableImage(width, height);
		try {
			PixelWriter writer = writableImage.getPixelWriter();
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					writer.setColor(i, j, color);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return writableImage;
	}

	public static Color getColorWithOpacity(Color color, double opacity) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
	}

}
