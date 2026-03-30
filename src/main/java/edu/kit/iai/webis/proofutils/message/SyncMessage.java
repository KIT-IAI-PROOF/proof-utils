/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.message;

import java.util.Objects;

/**
 * Message transfer object used to send and receive synchronization events to and from workers
 */
public class SyncMessage extends BaseMessage {

	/**
     * Tracks the next (upcoming) stepSize to use.
     */
    private Integer communicationStepSize;

    /**
     * create a SyncMessage
     */
    public SyncMessage() {
    	super( MessageType.SYNC );
    }

    /**
     * get the communication step size for this sync message
     * @return the communication step size
     */
    public Integer getCommunicationStepSize() {
        return this.communicationStepSize;
    }

    /**
     * set the communication step size for this sync message
     * @param communicationStepSize the communication step size
     */
    public void setCommunicationStepSize(Integer communicationStepSize) {
        this.communicationStepSize = communicationStepSize;
    }

    @Override
    public String toString() {
        return super.toString() +
//                ", communicationPoint=" + this.communicationPoint +
                ", communicationStepSize=" + this.communicationStepSize;
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof SyncMessage other)) {
			return false;
		}
		return Objects.equals(this.communicationStepSize, other.communicationStepSize);
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.communicationStepSize);
		return result;
	}
}
