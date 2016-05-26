package shifthealth.drivers.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shifthealth.DeployChecker;

/**
 * Created by John Simoes on 2016-05-26.
 *
 */
public class PropertyNotFoundException extends Exception {

    public PropertyNotFoundException(String property) {

        log.error("Property '"+ property + "' could not be retrieved.");
    }

    private static Logger log = LoggerFactory.getLogger(DeployChecker.class);
}
