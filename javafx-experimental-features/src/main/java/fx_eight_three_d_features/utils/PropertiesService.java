package fx_eight_three_d_features.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.System.err;

public class PropertiesService {

    private PropertiesService() {
    }

    private static final Properties PROPERTIES = configuredProperties();

    private static Properties configuredProperties() {
        Properties properties = new Properties();
        InputStream fileInputStream = PropertiesService.class.getResourceAsStream("/xylofoon.properties");
        if (fileInputStream != null) {
            try {
                properties.load(new BufferedInputStream(fileInputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            err.println("file inputstream was null...");
        }
        return properties;
    }

    public static int parsedIntAppProp(String property, int defaultVal) {
        int value = defaultVal;
        String propertyVal = PROPERTIES.getProperty(property);
        if (propertyVal != null) {
            try {
                value = Integer.parseInt(propertyVal);
            } catch (NumberFormatException e) {
                err.printf("Property '%s' with value '%s' could not be parsed to an int... " +
                        "Falling back to default: %d...%n", property, propertyVal, defaultVal);
            }
        } else {
            err.printf("Property '%s' not found. Falling back to default: %d%n",
                    property, defaultVal);
        }
        return value;
    }

    public static double parsedDoubleAppProp(String property, double defaultVal) {
        double value = defaultVal;
        String propertyVal = PROPERTIES.getProperty(property);
        if (propertyVal != null) {
            try {
                value = Double.parseDouble(propertyVal);
            } catch (NumberFormatException e) {
                err.printf("Property '%s' with value '%s' could not be parsed to an int... " +
                        "Falling back to default: %f...%n", property, propertyVal, defaultVal);
            }
        } else {
            err.printf("Property '%s' not found. Falling back to default: %f%n",
                    property, defaultVal);
        }
        return value;
    }

}
