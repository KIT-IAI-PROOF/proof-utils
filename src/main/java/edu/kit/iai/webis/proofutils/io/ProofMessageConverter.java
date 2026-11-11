/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.io;

import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * Factory for {@link Jackson2JsonMessageConverter} instances used for AMQP (de-)serialization of
 * {@link edu.kit.iai.webis.proofutils.message.BaseMessage} subtypes (e.g. NotifyMessage, ValueMessage, SyncMessage).
 * <p>
 * Spring AMQP's {@link DefaultJackson2JavaTypeMapper} only trusts the {@code java.util} and {@code java.lang}
 * packages by default, so any message carrying a {@code __TypeId__} header for our own DTOs would otherwise be
 * rejected with an "is not in the trusted packages" {@link IllegalArgumentException} on deserialization.
 */
public final class ProofMessageConverter {

    public static final String TRUSTED_MESSAGE_PACKAGE = "edu.kit.iai.webis.proofutils.message";

    private ProofMessageConverter() {
    }

    /**
     * create a {@link Jackson2JsonMessageConverter} that trusts {@link #TRUSTED_MESSAGE_PACKAGE}
     *
     * @return the configured converter
     */
    public static Jackson2JsonMessageConverter create() {
        final Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        final DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages(TRUSTED_MESSAGE_PACKAGE);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
