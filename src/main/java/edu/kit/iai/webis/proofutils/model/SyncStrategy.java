/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

import edu.kit.iai.webis.proofutils.wrapper.Block;
/**
 * The synchronization strategy for a {@link Block}.
 * Values may be {@link #WAIT_FOR_SYNC}, {@link #ALL_VALUES}, or {@link #INSTANT}
 */
public enum SyncStrategy {
    /**
     * The block waits for a synchronization message to forward the outputs.
     */
	WAIT_FOR_SYNC,
    /**
     * The block forwards the outputs if all input values are given
     */
	ALL_VALUES,
    /**
     * The block forwards each output value as soon as one input value arrives
     */
	INSTANT
}
