/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.exception;

@SuppressWarnings("unused")
public class NotImplementedException extends RuntimeException {

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
        System.exit(1);
    }

    public NotImplementedException(String message) {
        super(message);
        System.exit(1);
    }
}
