package shifthealth.drivers;

import shifthealth.Global;
import shifthealth.drivers.exceptions.PropertyNotFoundException;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 *
 *
 * Created by John Simoes on 2016-05-26.
 */
public class ConfigurationDriver {

    public static boolean testSettings() {

        Global.log.info("Testing settings...");

        boolean result = true;

        result &= (null != ConfigurationDriver._getOrRequestProperty(Global.SETTINGS_AWS_ACCESS_KEY    , "AWS access key", null));
        result &= (null != ConfigurationDriver._getOrRequestProperty(Global.SETTINGS_AWS_SECRET_KEY    , "AWS secret key", null));
        result &= (null != ConfigurationDriver._getOrRequestProperty(Global.SETTINGS_AWS_QUEUE_URL     , "AWS queue address", null));
        result &= (null != ConfigurationDriver._getOrRequestProperty(Global.SETTINGS_TICKIT_API_URL    , "TickiT server address", null));
        result &= (null != ConfigurationDriver._getOrRequestProperty(Global.SETTINGS_DATABASE_NAME     , "NextGen database name", null));
        result &= (null != ConfigurationDriver._getOrRequestProperty(Global.SETTINGS_DATABASE_LOGIN    , "NextGen database login", null));
        result &= (null != ConfigurationDriver._getOrRequestProperty(Global.SETTINGS_DATABASE_PASSWORD , "NextGen database password", null));

        return result;
    }

    private static final String CONFIG_FILENAME = "config.properties";

    private static Properties _properties = null;

    /**
     * Lazy retrieve configuration file.
     *
     */
    private static Properties _getPropertiesFile() {

        if (null ==_properties) {

            File configFile = new File(CONFIG_FILENAME);

            if (false == configFile.isFile()) {

                configFile = new File("config.local");
            }

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

        String property = ConfigurationDriver._getPropertiesFile().getProperty(settingsKey);

        // Make property null if empty.
        if (null != property && property.length() == 0) property = null;

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
    private static String _getOrRequestProperty(String settingsKey, String description, String defaultValue) {

        String property = ConfigurationDriver._getPropertiesFile().getProperty(settingsKey);

        // Make property null if empty.
        if (null != property && property.length() == 0) property = null;

        if (null == property)
            Global.log.error(description + " (" + settingsKey + ")" + ") not found on config.properties file.");
        else
            Global.log.info(description + " found: " + property);

        return property;
    }

}
