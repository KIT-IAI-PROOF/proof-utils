/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.io;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

public class MQSyncReceiver extends AbstractMQReceiver {

	/**
	 * create a MQSyncReceiver instance with given arguments
	 * @param connectionFactory the connection factory
	 * @param queueName the queue name
	 * @param messageListener the message listener
	 */
    public MQSyncReceiver(final ConnectionFactory connectionFactory, final String queueName, final MessageListener messageListener) {
    	super( connectionFactory, queueName, messageListener );
        this.setAutoDeclare(false);
        this.setDeclarationRetries(0);
    }


}
