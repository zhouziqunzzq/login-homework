package util;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class GlobalConfigHelper {
    public static Properties getConfigFromContext(ServletContext servletContext) {
        Properties properties = null;
        if (servletContext.getAttribute("GlobalConfig") == null) {
            try {
                properties = new Properties();
                properties.load(new InputStreamReader(
                        servletContext.getResourceAsStream("/WEB-INF/config.properties"),
                        "UTF-8"));
                servletContext.setAttribute("GlobalConfig", properties);
            } catch (IOException e) {
                System.out.println("Failed to load config.properties");
                e.printStackTrace();
                System.exit(-1);
            }
        } else {
            properties = (Properties) servletContext.getAttribute("GlobalConfig");
        }
        return properties;
    }
}
