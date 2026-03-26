/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import edu.kit.iai.webis.proofutils.LoggingHelper;
import edu.kit.iai.webis.proofutils.io.AbstractMQReceiver;
import edu.kit.iai.webis.proofutils.io.MQNotifyReceiver;
import edu.kit.iai.webis.proofutils.io.MQSyncReceiver;
import edu.kit.iai.webis.proofutils.io.MQValueReceiver;
import edu.kit.iai.webis.proofutils.message.MessageType;

/**
 * Class to manage instances of AMQP receivers ({@link MQNotifyReceiver}, {@link MQSyncReceiver}, and {@link MQValueReceiver})
 */
@Service
public class ConsumerManager {

    private final Map<String, SimpleMessageListenerContainer> receivers = new HashMap<>();
    private final ConnectionFactory connectionFactory;

    public ConsumerManager(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public AbstractMQReceiver instantiateReceiver(final String queueName, MessageType type,  final MessageListener messageListener) {
    	Objects.requireNonNull(queueName, "queue name must be given!");
    	AbstractMQReceiver receiver = null;
    	switch (type) {
			case SYNC: {
		    	receiver = new MQSyncReceiver(this.connectionFactory, queueName, messageListener);
			}
			case VALUE: {
				receiver = new MQValueReceiver(this.connectionFactory, queueName, messageListener);
			}
			case NOTIFY: {
				receiver = new MQNotifyReceiver(this.connectionFactory, queueName, messageListener);
			}
    	}
    	this.receivers.put(queueName, receiver);
    	LoggingHelper.debug().log("Receiver %s instanciated", queueName);
    	return receiver;
    }

    /**
     * Start consumer for a specific receiver
     */
    public void startConsumers() {
    	LoggingHelper.debug().log("starting consumers ");
        this.receivers.values().parallelStream().forEach((SimpleMessageListenerContainer mqReceiver) -> {
        	LoggingHelper.debug().log( "starting " + mqReceiver );
        	mqReceiver.start();
        });
    }

    /**
     * Stop consumer for a specific receiver
     */
    public void stopConsumers() {
        this.receivers.values().parallelStream().forEach((SimpleMessageListenerContainer mqReceiver) -> mqReceiver.destroy());

    }
}
