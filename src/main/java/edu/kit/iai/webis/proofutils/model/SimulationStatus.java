/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

import edu.kit.iai.webis.proofutils.message.NotifyMessage;
import edu.kit.iai.webis.proofutils.message.SyncMessage;
import edu.kit.iai.webis.proofutils.wrapper.StepSizeDefinition;

/**
 * The status of a WorkflowContainer or a Block.
 * Values may be {@link CREATED}, {@link INITIALIZED}, {@link WAITING}, {@link READY}, {@link STOPPED} or
 * {@link ABORTED} for blocks and workflows,
 * <p>
 * {@link INITIALIZED}, {@link EXECUTION_FINISHED}, {@link FINALIZED}, {@link ERROR_INIT}, {@link ERROR_STEP}, ,
 * {@link ERROR_FINALIZE} or {@link EXECUTION_STEP_FINISHED} for blocks
 */
public enum SimulationStatus {

    /**
     * The initial value for a block
     */
    UNKNOWN,

    /**
     * The block is created and ready to work
     */
    CREATED,

    /**
     * a block is initialized, i.e., the initialization phase (phase 1 of 3)
     * has finished.
     */
    INITIALIZED,

    /**
     * NEW MEANING
     * Block has got a {@link SyncMessage} (phase execute) and is running.
     * The orchestrator is waiting for the {@link NotifyMessage} with
     * {@link EXECUTION_STEP_FINISHED} of the running block
     */
    WAITING,

    /**
     * NEW status has two meanings:
     * 1. The orchestrator can send a sync message to the worker,
     * since the worker is ready to work.
     * 2. The orchestrator has sent a sync message to the worker,
     * but the worker is not yet able to work, because he still lacks information or data.
     * Then the worker sends a notify message with the status READY back to the orchestrator, which means that the
     * worker is ready for receiving the next sync message.
     */
    READY,

    /**
     * a block has finished an execution step in the execution phase (phase 2 of 3).
     */
    EXECUTION_STEP_FINISHED,

    /**
     * a block has finished the execution phase (phase 2 of 3).
     */
    EXECUTION_FINISHED,

    /**
     * a block has passed the finalize phase (phase 3 of 3)
     */
    FINALIZED,

    /**
     * the WorkflowContainer and all its blocks (BlockContainers)
     * are aborted
     */
    ABORTED,

    /**
     * the WorkflowContainer and all its blocks (BlockContainers)
     * are stopped
     */
    STOPPED,

    /**
     * a block (BlockContainer) has been shut down
     */
    SHUT_DOWN,

    /**
     * a block is suspended, when it is waiting for the next working step.
     * This new step depends on the current communication point and its corresponding
     * {@link StepSizeDefinition}
     */
    SUSPENDED,

    /**
     * an error/exception occurred in the init phase (phase 1 of 3).
     */
    ERROR_INIT,

    /**
     * an error/exception occurred in the step phase (phase 2 of 3).
     */
    ERROR_STEP,

    /**
     * an error/exception occurred in the finalize phase (phase 3 of 3).
     */
    ERROR_FINALIZE,

	/**
	 * The orchestrator has sent a sync message to the worker,
     * but the worker is not yet able to work, because he still lacks information or data.
     * Then the worker sends a notify message with the status RETRY back to the orchestrator, which means that the
     * worker is ready for receiving the same sync message.	 */
	RETRY;


}

