/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.message;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.iai.webis.proofutils.model.SimulationPhase;


/**
 * parent class for the data transfer messages {@link NotifyMessage}, {@link SyncMessage}, and {@link ValueMessage}
 */
@SuppressWarnings( "serial" )
public class BaseMessage implements Serializable, Cloneable,  IMessage {

    /**
     * Time that the value message was created in seconds.
     */
    @JsonProperty("time")
    private Long time;

    /**
     * Time that the value message was created in milliseconds.
     */
    @JsonProperty("timeInMillis")
    private Long timeInMillis;

    /**
     * Type of the Message. Should be VALUE, since this is a ValueMessage.
     */
    @JsonProperty("type")
    private MessageType type = MessageType.SYNC;

    /**
     * the workflow phase for which the message is valid.
     */
    @JsonProperty("phase")
    private SimulationPhase phase;

    /**
     * UUID of the block this message originates from.
     */
    @JsonProperty("globalBlockId")
    private String globalBlockId;

    /**
     * Local id of the block this message originates from.
     */
    @JsonProperty("localBlockId")
    private Integer localBlockId;

    /**
     * UUID of the current workflow this message is designated for.
     */
    @JsonProperty("workflowId")
    private String workflowId;

    /**
     * UUID of the execution. One Workflow can be executed multiple in parallel.
     */
    @JsonProperty("executionId")
    private String executionId;

    /**
     * Tracks the current step.
     */
//    @Field(type = FieldType.Integer)
    @JsonProperty("communicationPoint")
    private Integer communicationPoint;



    public BaseMessage() {}

    /**
     * use the parent class constructor with a given {@link MessageType}
     * @param type the message type
     */
    protected BaseMessage( MessageType type ) {
    	this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getTime() {
        return this.time;
    }

    /**
     * set the creation time of the message in long form
     * @param time the creation time of the message
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getTimeInMillis() {
    	return this.timeInMillis;
    }

    /**
     * set the creation time of the message in long form
     * @param time the creation time of the message
     */
    public void setTimeInMillis(Long timeInMillis) {
    	this.timeInMillis = timeInMillis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageType getType() {
        return this.type;
    }

    /**
     * get the global block id. It is either a UUID or a readable name.
     * @return the global block id
     */
    @Override
    public String getGlobalBlockId() {
        return this.globalBlockId;
    }

    /**
     * set the global block id. It is either a UUID or a readable name.
     * @param globalBlockId the global block id
     */
    public void setGlobalBlockId(String globalBlockId) {
        this.globalBlockId = globalBlockId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getLocalBlockId() {
        return this.localBlockId;
    }

    /**
     * set the local block id. This is the number of the block in the workflow definition
     * @param localBlockId the local block id
     */
    public void setLocalBlockId(Integer localBlockId) {
        this.localBlockId = localBlockId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimulationPhase getSimulationPhase() {
		return this.phase;
	}

    /**
     * set the actual simulation phase where this message was sent
     * @param phase the current simulation phase
     */
	public void setSimulationPhase( SimulationPhase phase ) {
		this.phase = phase;
	}

    /**
     * set the id of the workflow that sent this message
     * @param workflowId the workflow id
     */
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkflowId() {
    	return this.workflowId;
    }

    /**
     * set the execution id of the workflow that sent this message
     * @param executionId the execution id of the workflow
     */
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExecutionId() {
    	return this.executionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getCommunicationPoint() {
        return this.communicationPoint;
    }

    /**
     * set the communication point (step position/number) for this sync message
     * @param communicationPoint the communication point
     */
    public void setCommunicationPoint(Integer communicationPoint) {
        this.communicationPoint = communicationPoint;
    }



    /**
     * {@inheritDoc}
     */
	@Override
    public String toString() {
        return "time=" + this.time +
                ", type=" + this.type +
                ", phase=" + this.phase +
                ", localBlockID=" + (this.localBlockId == null ? "not set" : this.localBlockId) +
                ", globalBlockID=" + (this.globalBlockId == null ? "not set" : this.globalBlockId ) +
                ", executionID=" + (this.executionId == null ? "not set" : this.executionId ) +
                ", workflowID=" + (this.workflowId == null ? "not set" : this.workflowId ) +
                ", communicationPoint=" + (this.communicationPoint == null ? "not set" : this.communicationPoint );
    }

    @Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( !(obj instanceof BaseMessage other) ) {
			return false;
		}
		return Objects.equals(this.communicationPoint, other.communicationPoint) && Objects.equals(this.executionId, other.executionId) && Objects.equals(this.globalBlockId, other.globalBlockId) && Objects.equals(this.localBlockId, other.localBlockId) && this.phase == other.phase && Objects.equals(this.time, other.time) && this.type == other.type && Objects.equals(this.workflowId, other.workflowId);
	}

    @Override
	public int hashCode() {
		return Objects.hash(this.executionId, this.globalBlockId, this.localBlockId, this.phase,
				this.time, this.type, this.workflowId, this.communicationPoint);
	}

    @Override
	public BaseMessage clone() throws CloneNotSupportedException {
    	BaseMessage baseMessage = (BaseMessage)super.clone();
    	baseMessage.time = this.time;
    	baseMessage.timeInMillis = this.timeInMillis;
    	baseMessage.type = this.type;
    	baseMessage.phase = this.phase;
    	baseMessage.localBlockId = this.localBlockId;
    	baseMessage.globalBlockId = this.globalBlockId;
    	baseMessage.executionId = this.executionId;
    	baseMessage.workflowId = this.workflowId;
    	return baseMessage;
    }
}
