package be.kdg.se3.offence.calculator.adapters.input;

import be.kdg.se3.offence.calculator.domain.CommunicationException;
import be.kdg.se3.offence.calculator.domain.entity.EmissionOffence;
import be.kdg.se3.offence.calculator.domain.entity.Offence;
import be.kdg.se3.offence.calculator.domain.entity.SpeedingOffence;
import be.kdg.se3.offence.calculator.domain.service.InputListener;
import be.kdg.se3.offence.calculator.domain.service.InputService;
import com.rabbitmq.client.*;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Adapts a RabbitMQ queue endpoint receiving string messages to the domains InputService
 * The message format is XML
 * Castor is used form XML conversion
 */
public class RabbitMQInput implements InputService {
    private final String host;
    private final String queueName;

    private Connection connection;
    private Channel channel;

    private Logger logger = Logger.getLogger(RabbitMQInput.class);

    public RabbitMQInput(String host, String queueName) {
        this.queueName = queueName;
        this.host = host;
    }

    @Override
    public void initialize(InputListener listener) throws CommunicationException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            connection =  factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName,
                    false, /* non-durable */
                    false, /* non-exclusive */
                    false, /* do not auto delete */
                    null); /* no other construction arguments */
            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    logger.info("Received message from RabbitMQ queue " + queueName);
                    String message = new String(body, "UTF-8");
                    logger.debug("Message content: " + message);

                    Reader reader = new StringReader(message);
                    Offence offence = null;
                    if (message.contains("speeding-offence")) {
                        try {
                            offence = (SpeedingOffence) Unmarshaller.unmarshal(SpeedingOffence.class, reader);
                        } catch (Exception e) {
                            throw new IOException("Error during conversion to SpeedingOffence", e);
                        }
                    } else if (message.contains("emission-offence")) {
                        try {
                            offence = (EmissionOffence) Unmarshaller.unmarshal(EmissionOffence.class, reader);
                        } catch (Exception e) {
                            throw new IOException("Error during conversion to EmissionOffence", e);
                        }
                    }

                    if (listener != null) {
                        listener.onReceive(offence);
                    }
                }
            };
            channel.basicConsume(queueName, true, consumer);

        } catch (Exception e) {
            throw new CommunicationException("Error during  channel initialisation", e);
        }
    }


    @Override
    public void shutdown() throws CommunicationException {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new CommunicationException("Unable to close connection to RabbitMQ", e);
        }
    }
}
