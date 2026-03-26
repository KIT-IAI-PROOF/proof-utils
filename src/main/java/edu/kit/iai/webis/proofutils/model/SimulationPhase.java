/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

/**
 * Phases of a workflow and its blocks during a simulation.
 * <br><br><b>Note: </b>A workflow is in a particular phase, when not all blocks have finished this phase.
 * When the last block has finished the phase, the next phase will be entered until {@link FINALIZE} is passed.
 * <br><br>Values may be {@link INIT}, {@link EXECUTE}, {@link FINALIZE}, or {@link SHUTDOWN}
 */
public enum SimulationPhase {
    /**
     * Used for initial setup.
     */
    CREATE,
    /**
     * Used for initial setup.
     */
    INIT,
    /**
     * Used as main action, the processing of the model for several steps.
     */
    EXECUTE,
    /**
     * Used to clean up the process.
     */
    FINALIZE,
    /**
     * Used to shut down a block.
     */
    SHUTDOWN;

}
