/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.message;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message transfer object used to send values
 */
public class ValueMessage extends BaseMessage {

    /**
     * Payload of the message. Contains the data, i.e. the values.
     */
    @JsonProperty("data")
    private Object data;

    public ValueMessage() {
    	super( MessageType.VALUE );
    }

    /**
     * get the data attached to this message (payload)
     * @return the data
     */
    public Object getData() {
        return this.data;
    }

    /**
     * attach a data object to this message (payload)
     * @param data the data
     */
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString() + ", data=" + this.data;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ValueMessage other))
			return false;
		return Objects.equals(this.data, other.data);
	}

}
