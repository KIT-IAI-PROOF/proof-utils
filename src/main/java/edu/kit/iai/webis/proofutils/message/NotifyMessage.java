/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.message;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.iai.webis.proofutils.model.SimulationStatus;

/**
 * Message transfer object used to send notify events using {@link MessageType#NOTIFY}
 */
public class NotifyMessage extends BaseMessage {

    /**
     * The changed status of the block or. One of {@link SimulationStatus}.
     */
    @JsonProperty("status")
    private SimulationStatus blockStatus;

    /**
     * The current error text when an error occurs
     */
    @JsonProperty("errorText")
    private String errorText;

    /**
     * The current error thrown
     */
    @JsonProperty("thrownError")
    private Error thrownError;

    /**
     * create a NotifyMessage
     */
    public NotifyMessage() {
        super(MessageType.NOTIFY);
    }

    /**
     * get the {@link SimulationStatus} of the block
     *
     * @return the block status
     */
    public SimulationStatus getBlockStatus() {
        return this.blockStatus;
    }

    /**
     * set the {@link SimulationStatus} of the block
     *
     * @param blockStatus the block status
     */
    public void setBlockStatus(SimulationStatus blockStatus) {
        this.blockStatus = blockStatus;
    }

    /**
     * get the error text
     *
     * @return the notify message error text
     */
    public String getErrorText() {
        return this.errorText;
    }

    /**
     * set the error text
     *
     * @param errorText the error text
     */
    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

	/**
	 * get a thrown error ({@link Exception} or {@link Error})
	 * @return the thrown error
	 */
    public Error getThrownError() {
		return this.thrownError;
	}

    /**
     * set an ({@link Exception} or {@link Error})
     * @param thrownError the Error or Exception thrown
     */
	public void setThrownError( Error thrownError ) {
		this.thrownError = thrownError;
	}

	@Override
    public String toString() {
        return super.toString() + ", status=" + this.blockStatus
        		+ (this.thrownError != null ? ", Error: "+this.thrownError : "" )
        		+ (this.errorText != null ? ", errorText: "+this.errorText : "" );
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(this.blockStatus);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
		}
        if (!super.equals(obj)) {
            return false;
		}
        if (!(obj instanceof NotifyMessage other)) {
            return false;
		}
        return this.blockStatus == other.blockStatus;
    }


}
