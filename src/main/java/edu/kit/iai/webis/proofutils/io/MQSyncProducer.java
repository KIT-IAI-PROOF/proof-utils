/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.io;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MQSyncProducer extends AbstractMQProducer{

    /**
     * create a MQNotifyProducer instance with a {@link RabbitTemplate}
     * @param rabbitTemplate the rabbit template
     */
    public MQSyncProducer(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

}

