package shifthealth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global resources: settings, constants, logger etc.
 *
 *
 * Created by John Simoes on 2016-05-26.
 */
public class Global {

    public static final String SETTINGS_AWS_ACCESS_KEY    = "aws.access_key";
    public static final String SETTINGS_AWS_SECRET_KEY    = "aws.secret_key";
    public static final String SETTINGS_AWS_QUEUE_URL     = "aws.queue_url";
    public static final String SETTINGS_TICKIT_API_URL    = "tickit.api_url";
    public static final String SETTINGS_DATABASE_LOGIN    = "database.login";
    public static final String SETTINGS_DATABASE_PASSWORD = "database.password";
    public static final String SETTINGS_DATABASE_NAME     = "database.name";

    public static Logger log = LoggerFactory.getLogger(Global.class);
}
