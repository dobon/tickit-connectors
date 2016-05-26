package shifthealth.drivers.exceptions;

import shifthealth.Global;

/**
 * Created by John Simoes on 2016-05-26.
 *
 */
public class PropertyNotFoundException extends Exception {

    public PropertyNotFoundException(String property) {

        Global.log.error("Property '"+ property + "' could not be retrieved.");
    }
}
