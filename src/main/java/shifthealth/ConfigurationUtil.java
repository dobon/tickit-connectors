package shifthealth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shifthealth.drivers.exceptions.PropertyNotFoundException;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * Created by John Simoes on 2016-05-26.
 *
 */
public class ConfigurationUtil {

    private static final String CONFIG_FILENAME = "config.properties";

    private static Properties _properties = null;

    /**
     * Lazy retrieve configuration file.
     *
     */
    private static Properties _getPropertiesFile() {

        if (null ==_properties) {

            File configFile = new File(CONFIG_FILENAME);

            try {

                Properties newProps = new Properties();

                if (configFile.isFile()) {

                    FileReader reader = new FileReader(configFile);
                    newProps.load(reader);
                    reader.close();
                }

                _properties = newProps;

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

        return _properties;
    }

    public static String getProperty(String settingsKey) throws PropertyNotFoundException {

        String property = ConfigurationUtil._getPropertiesFile().getProperty(settingsKey);

        if (null == property)
            throw new PropertyNotFoundException(settingsKey);

        return property;
    }


    /**
     *
     *
     * @param settingsKey
     * @param description
     * @param defaultValue
     */
    public static String getOrRequestProperty(String settingsKey, String description, String defaultValue) {

        String property = ConfigurationUtil._getPropertiesFile().getProperty(settingsKey);

        if (null == property)
            log.error(description + " (" + settingsKey + ")" + ") not found.");
        else
            log.info(description + " found: " + property);

        return property;
    }

    private static Logger log = LoggerFactory.getLogger(DeployChecker.class);
}
