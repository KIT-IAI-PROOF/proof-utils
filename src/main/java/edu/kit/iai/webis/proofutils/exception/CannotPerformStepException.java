/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.exception;

@SuppressWarnings("serial")
public class CannotPerformStepException extends Exception {

    public CannotPerformStepException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotPerformStepException(String message) {
        super(message);
    }

}
