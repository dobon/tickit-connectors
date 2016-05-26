package shifthealth.drivers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import shifthealth.Global;

import java.util.List;

/**
 * Amazon Web Services SQS Driver.
 *
 * Created by John Simoes on 2016-05-26.
 */
public class SQSDriver {


    public static boolean testSQS() {

        boolean result = true;

        Global.log.info("Testing Amazon SQS connection...");

        try {

            AWSCredentials credentials = new BasicAWSCredentials(
                    ConfigurationDriver.getProperty(Global.SETTINGS_AWS_ACCESS_KEY),
                    ConfigurationDriver.getProperty(Global.SETTINGS_AWS_SECRET_KEY));

            AmazonSQS sqs = new AmazonSQSClient(credentials);

            final String queueAddress = ConfigurationDriver.getProperty(Global.SETTINGS_AWS_QUEUE_URL);

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

                        Global.log.info("Message found as expected.");
                        break;
                    } else {

                        result = false;
                        Global.log.error("Wrong message found:" + receivedMessageBody);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
