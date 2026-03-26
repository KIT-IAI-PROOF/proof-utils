/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * the paradigm for the communication between blocks and the orchestrator
 */
public enum CommunicationParadigm {
	/**
	 * step based simulation
	 */
	STEPBASED(String.valueOf("STEPBASED")),

	/**
	 * event based simulation
	 */
	EVENT(String.valueOf("EVENT"));

	private String value;

	CommunicationParadigm(String value) {
		      this.value = value;
		    }

	@JsonValue
	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}

}
