package shifthealth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class DeployChecker {

    private static final String SETTINGS_AWS_ACCESS_KEY    = "aws.access_key";
    private static final String SETTINGS_AWS_SECRET_KEY    = "aws.secret_key";
    private static final String SETTINGS_AWS_QUEUE_URL     = "aws.queue_url";
    private static final String SETTINGS_TICKIT_API_URL    = "tickit.api_url";
    private static final String SETTINGS_DATABASE_LOGIN    = "database.login";
    private static final String SETTINGS_DATABASE_PASSWORD = "database.password";
    private static final String SETTINGS_DATABASE_NAME     = "database.name";

    public static void main (String[] args) {

        log.info("Healthcheck started");

        boolean ok = true;

//        if (ok) ok &= testSettings();
//        if (ok) ok &= testSQS();
        if (ok) ok &= testDatabase();
//        if (ok) ok &= testRESTTickit();

        if (ok)
            log.info("All tests passed.");
        else
            log.error("Error, one of more tests failed.");
    }

    public static boolean testSettings() {

        log.info("Testing settings...");

        boolean result = true;

        result &= (null != ConfigurationUtil.getOrRequestProperty(SETTINGS_AWS_ACCESS_KEY    , "AWS access key", null));
        result &= (null != ConfigurationUtil.getOrRequestProperty(SETTINGS_AWS_SECRET_KEY    , "AWS secret key", null));
        result &= (null != ConfigurationUtil.getOrRequestProperty(SETTINGS_AWS_QUEUE_URL     , "AWS queue address", null));
        result &= (null != ConfigurationUtil.getOrRequestProperty(SETTINGS_TICKIT_API_URL    , "TickiT server address", null));
        result &= (null != ConfigurationUtil.getOrRequestProperty(SETTINGS_DATABASE_NAME     , "NextGen database name", null));
        result &= (null != ConfigurationUtil.getOrRequestProperty(SETTINGS_DATABASE_LOGIN    , "NextGen database login", null));
        result &= (null != ConfigurationUtil.getOrRequestProperty(SETTINGS_DATABASE_PASSWORD , "NextGen database password", null));

        return result;
    }

    public static boolean testDatabase() {

        log.info("Testing database connection...");

        boolean result = false;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            Connection connection =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/tickit_staging?user=root&password=&useSSL=false&useTimezone=true&serverTimezone=UTC");

            Statement statement = connection.createStatement();

            if (statement.execute("SELECT company_name FROM accounts;")) {

                result = true;
            }

            result = true;
        }
        catch (Exception ex) {

            ex.printStackTrace();
        }

        return result;
    }

    public static boolean testRESTTickit() {

        log.info("Testing TickiT API connection...");

        return true;
    }

    public static boolean testSQS() {

        boolean result = true;

        log.info("Testing Amazon SQS connection...");

        try {

            AWSCredentials credentials = new BasicAWSCredentials(
                    ConfigurationUtil.getProperty(SETTINGS_AWS_ACCESS_KEY),
                    ConfigurationUtil.getProperty(SETTINGS_AWS_SECRET_KEY));

            AmazonSQS sqs = new AmazonSQSClient(credentials);

            final String queueAddress = ConfigurationUtil.getProperty(SETTINGS_AWS_QUEUE_URL);

            String messageBody = "" + System.currentTimeMillis();

            // Send message.
            {
                SendMessageRequest messageRequest = new SendMessageRequest(queueAddress, messageBody);
                sqs.sendMessage(messageRequest);
            }

            result = true;

            // Receive message.
            {
                ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueAddress)
                        .withMaxNumberOfMessages(10)
                        .withMessageAttributeNames("All");

                List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

                for (Message message : messages) {

                    String receivedMessageBody = message.getBody();

                    /*
                     * Delete message.
                     */
                    String messageHandle = message.getReceiptHandle();
                    sqs.deleteMessage(new DeleteMessageRequest(queueAddress, messageHandle));

                    if (receivedMessageBody.equals(messageBody)) {

                        log.info("Message found as expected.");
                        break;
                    } else {

                        result = false;
                        log.error("Wrong message found:" + receivedMessageBody);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    private static Logger log = LoggerFactory.getLogger(DeployChecker.class);
}
