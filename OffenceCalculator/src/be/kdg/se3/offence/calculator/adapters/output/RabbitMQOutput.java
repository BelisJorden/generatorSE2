package be.kdg.se3.offence.calculator.adapters.output;

import be.kdg.se3.offence.calculator.domain.CommunicationException;
import be.kdg.se3.offence.calculator.domain.entity.OutputMessage;
import be.kdg.se3.offence.calculator.domain.service.OutputService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;

import java.io.IOException;
import java.io.StringWriter;


/**
 * Adapts RabbitMQ to the domains OutputService
 * The message format is XML
 * Castor is used form XML conversion
 */
public class RabbitMQOutput implements OutputService {
    private String host;
    private String queueName;

    private Channel channel;
    private Connection connection;
    private Logger logger = Logger.getLogger(RabbitMQOutput.class);

    public RabbitMQOutput(String host, String queName) {

        this.host = host;
        this.queueName = queName;
    }

    @Override
    public void initialize() throws CommunicationException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        try {
            connection = factory.newConnection();
            channel   = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (Exception e) {
            throw new CommunicationException("Error during initialization of RabbitMQ channel", e);

        }
    }

    @Override
    public void publish(OutputMessage outputMessage ) throws IOException,CommunicationException {
        Marshaller marshaller;
        StringWriter strWriter = new StringWriter();
        try {
            marshaller = new Marshaller(strWriter);
            marshaller.marshal(outputMessage);
        } catch (Exception e) {
            throw new IOException("Error during conversion from message to Offence",e);
        }

        try {
            channel.basicPublish("", queueName, null, strWriter.toString().getBytes("UTF-8"));
        } catch (IOException e) {
            throw new CommunicationException("Error during communication with RabbitMQ",e);
        }
        logger.info("Set offence with price:  " + outputMessage.getOffenceAmount() + " on the output queue " + queueName);

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