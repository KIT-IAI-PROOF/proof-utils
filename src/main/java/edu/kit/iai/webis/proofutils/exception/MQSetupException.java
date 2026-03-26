/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.exception;

@SuppressWarnings("unused")
public class MQSetupException extends RuntimeException {

    public MQSetupException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQSetupException(String message) {
        super(message);
    }
}
