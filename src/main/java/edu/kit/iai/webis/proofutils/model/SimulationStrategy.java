/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

/**
 * Possible Actions in case of an asynchronous execution with missing values.
 * Values may be {@link #ABORT}, {@link #IGNORE}, {@link #LATEST}, {@link #WAIT_AND_CONTINUE}, or {@link #WAIT_AND_RETRY}.
 */
public enum SimulationStrategy {
    /**
     * Abort Workflow when dependent values are not present.
     */
    ABORT,
    /**
     * Ignore missing values and just try (best effort).
     */
    IGNORE,
    /**
     * Use latest received values and continue.
     */
    LATEST,
    /**
     * Wait until all values are present and continue with next workflow sync step (after stepBasedConfig duration).
     */
    WAIT_AND_CONTINUE,

    /**
     * Wait until all values are present and retry with current workflow sync step (after stepBasedConfig duration).
     */
    WAIT_AND_RETRY
}
