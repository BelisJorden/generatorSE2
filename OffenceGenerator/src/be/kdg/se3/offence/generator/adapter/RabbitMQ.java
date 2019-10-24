package be.kdg.se3.offence.generator.adapter;

import be.kdg.se3.offence.generator.domain.CommunicationException;
import be.kdg.se3.offence.generator.domain.entity.Offence;
import be.kdg.se3.offence.generator.domain.service.OutputService;
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
public class RabbitMQ implements OutputService {
    private String host;
    private String queueName;

    private Channel channel;
    private Connection connection;
    private Logger logger = Logger.getLogger(RabbitMQ.class);

    public RabbitMQ(String host, String queName) {
        this.host = host;
        this.queueName = queName;
    }

    @Override
    public void initialize() throws CommunicationException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (Exception e) {
            throw new CommunicationException("Error during initialization of rabbitMQ channel", e);
        }

    }



    @Override
    public void publish(Offence offence) throws IOException,CommunicationException {
        Offence message = offence;
        Marshaller marshaller = null;
        StringWriter strWriter = new StringWriter();
        try {
            marshaller = new Marshaller(strWriter);
            marshaller.marshal(message);

        } catch (Exception e) {
            throw new IOException("Error during conversion from message to Offence",e);
        }
        try {
            channel.basicPublish("", queueName, null,strWriter.toString().getBytes("UTF-8"));
            logger.info("Put message on RabbitMQ queue " + queueName);
            logger.debug("Message content:" + strWriter.toString());

        } catch (IOException e) {
            throw new CommunicationException("Error during communication with RabbitMQ",e);
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