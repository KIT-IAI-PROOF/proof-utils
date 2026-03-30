/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.message;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * a wrapper class for Java errors and throwables to be handled by the MessageHandler
 */
public class Error {
    private final String message;
    private final List<STE> stackTrace;
    private final Error cause;

    public Error(Throwable e) {
        this.message = e.getMessage();
        this.stackTrace = Arrays.stream(e.getStackTrace()).map(STE::new).collect(Collectors.toList());
        this.cause = e.getCause() != null ? new Error(e.getCause()) : null;
    }

    public Throwable toThrowable() {
        Throwable t = new Throwable(this.message);
        t.setStackTrace(this.stackTrace.stream().map(STE::toStackTraceElement).toArray(StackTraceElement[]::new));
        if (this.cause != null) {
            t.initCause(this.cause.toThrowable());
        }
        return t;
    }

    private static class STE {
        public final String declaringClass;
        public final String methodName;
        public final String fileName;
        public final int    lineNumber;

        public STE(StackTraceElement ste) {
            this.declaringClass = ste.getClassName();
            this.methodName = ste.getMethodName();
            this.fileName = ste.getFileName();
            this.lineNumber = ste.getLineNumber();
        }

        public StackTraceElement toStackTraceElement() {
            return new StackTraceElement(this.declaringClass, this.methodName, this.fileName, this.lineNumber);
        }
    }
}
