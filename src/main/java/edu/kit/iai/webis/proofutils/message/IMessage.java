/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.message;

import edu.kit.iai.webis.proofutils.model.SimulationPhase;

/**
 * definition of common methods for a message
 */
public interface IMessage {

	/**
	 * get the type of the message, either {@link MessageType#SYNC}, {@link MessageType#NOTIFY}, or {@link MessageType#VALUE}.
	 * @return the {@link MessageType}
	 */
	public MessageType getType();

    /**
     * get the creation time of the message in long form (in seconds)
     * @return the creation time of the message in seconds
     */
    public Long getTime();

    /**
     * get the creation time of the message in long form (in milliseconds)
     * @return the creation time of the message in milliseconds
     */
    public Long getTimeInMillis();

    /**
     * get the global block id. It is either a UUID or a readable name.
     * @return the global block id
     */
    public String getGlobalBlockId();

    /**
     * get the local block id. This is the number of the block in the workflow definition
     * @return the local block id
     */
    public Integer getLocalBlockId();

    /**
     * get the actual simulation phase where this message was sent
     * @return the phase
     */
    public SimulationPhase getSimulationPhase();

	/**
	 * get the id of the workflow that sent this message
	 * @return the workflow id
	 */
    public String getWorkflowId();

    /**
     * get the execution id of the workflow that sent this message
     * @return the execution id of the workflow
     */
    public String getExecutionId();

    /**
     * get the communication point (step position/number) for this sync message
     * @return the communication point
     */
    public Integer getCommunicationPoint();


}
