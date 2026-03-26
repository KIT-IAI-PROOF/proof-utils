/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.io;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import edu.kit.iai.webis.proofutils.LoggingHelper;
import edu.kit.iai.webis.proofutils.message.IMessage;

public abstract class AbstractMQProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * create a protected AbstractMQProducer instance with a {@link RabbitTemplate}
     * @param rabbitTemplate the rabbit template
     */
    protected AbstractMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    /**
     * Convert a Java object to an Amqp Message and send it to a default exchange with a specific routing key (queue name), see
     * {@link RabbitTemplate#convertAndSend(String, Object)}
     * @param queueName the routing key (queue name)
     * @param message the message to send
     */
    public void sendToQueue(final String queueName, final IMessage message) {
    	LoggingHelper.trace().log("sending " + message.getClass().getSimpleName() + " (Phase=" + message.getSimulationPhase() +") to Queue '" + queueName + "')");

        this.rabbitTemplate.convertAndSend(queueName, message);
    }

    /**
     * Convert a Java object to an Amqp Message and send it to a specific exchange with a specific routing key (queue name).
     * @param exchangeName name of the exchange
     * @param queueName the routing key (queue name)
     * @param message the message to send
     */
    public void sendToExchange(final String exchangeName, final String queueName, final IMessage message) {
    	LoggingHelper.trace().log("sending " + message.getClass().getSimpleName() + " (Phase=" + message.getSimulationPhase() +") to Queue/Exchange '" + queueName + "')");

        this.rabbitTemplate.convertAndSend(exchangeName, queueName, message);
    }

}
