/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.message;

/**
 * Possible Message Types. Blocks communicate using these Message Types for different functionalities.
 * Possible values are {@link #SYNC}, {@link #VALUE}, and {@link #NOTIFY}
 */
public enum MessageType {
    /**
     * Send from orchestrator to workers.
     * Command to execute corresponding lifecycle.
     */
    SYNC,
    /**
     * Used by workers and orchestrator to exchange data.
     */
    VALUE,
    /**
     * Send from worker to orchestrator.
     * Used to notify an orchestrator of block status changes.
     */
    NOTIFY
}
