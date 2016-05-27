package shifthealth;

import shifthealth.drivers.ConfigurationDriver;
import shifthealth.drivers.DatabaseDriver;
import shifthealth.drivers.SQSDriver;
import shifthealth.drivers.TickiTDriver;

public class Main {

    public static void main(String[] args) {

        Global.log.info("Welcome to Shift Health NextGen Connector");

        // TODO separate tests from main loop

        Global.log.info("Healthcheck started");

        boolean ok = true;

        if (ok) ok &= ConfigurationDriver.testSettings();
        if (ok) ok &= SQSDriver.testSQS();
        if (ok) ok &= DatabaseDriver.testDatabase();
        if (ok) ok &= TickiTDriver.testRESTTickit();

        if (ok)
            Global.log.info("All tests passed.");
        else
            Global.log.error("One or more tests failed - cannot proceed.");
    }
}
