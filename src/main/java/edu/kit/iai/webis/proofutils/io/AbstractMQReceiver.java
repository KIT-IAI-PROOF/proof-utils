/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.io;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public abstract class AbstractMQReceiver extends SimpleMessageListenerContainer{

	protected AbstractMQReceiver( final ConnectionFactory connectionFactory, final String queueName, final MessageListener messageListener) {
        this.setConnectionFactory(connectionFactory);
        this.setQueueNames(queueName);
        this.setMessageListener(messageListener);
        this.setDefaultRequeueRejected(false);
        this.setPrefetchCount(1);
        this.setMaxConcurrentConsumers(1);
        this.setExclusive(true);
	}

    public void startConsumer() {
        this.doStart();
    }

}
