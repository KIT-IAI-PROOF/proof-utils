/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */

package edu.kit.iai.webis.proofutils.logging;

import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.MDC;

/**
 * A logback filter that checks MDC values to determine whether to accept or deny log events.
 * This filter can be used in logback.xml to route log messages to different appenders based on MDC context.
 * 
 * Usage in logback.xml:
 * <pre>{@code
 * <filter class="edu.kit.iai.webis.proofutils.logging.MDCValueFilter">
 *     <key>simplifiedFormat</key>
 *     <value>true</value>
 *     <acceptOnMatch>true</acceptOnMatch>
 * </filter>
 * }</pre>
 */
public class MDCValueFilter extends Filter<ILoggingEvent> {

    private String key;
    private String value;
    private boolean acceptOnMatch = true;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (key == null) {
            return FilterReply.NEUTRAL;
        }

        String mdcValue = MDC.get(key);
        boolean matches = value != null && value.equals(mdcValue);

        if (matches) {
            return acceptOnMatch ? FilterReply.ACCEPT : FilterReply.DENY;
        } else {
            return acceptOnMatch ? FilterReply.DENY : FilterReply.NEUTRAL;
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAcceptOnMatch(boolean acceptOnMatch) {
        this.acceptOnMatch = acceptOnMatch;
    }
}