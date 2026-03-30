/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;

import edu.kit.iai.webis.proofmodels.InputDetail;

/**
 * Wrapper class to provide an Input element stored in the configuration database
 */
public class Input extends IOElement implements IWrapper <InputDetail>{

	private InputDetail inputDetail;
	private Boolean required = false;
	private String defaultValue;

	/**
	 * create a input instance based on a given database element {@link InputDetail}
	 * @param inputDetail the database element
	 */
	public Input(InputDetail inputDetail) {
		this.inputDetail = inputDetail;
		this.setId(this.inputDetail.getId());
		this.setName(this.inputDetail.getLabel());
		this.setType(EnumMapper.getDataTypeFor( this.inputDetail.getType()) );
		this.setCommunicationType(EnumMapper.getCommunicationTypeFor(this.inputDetail.getCommunicationType()));
		this.setSimulationPhase(EnumMapper.getSimulationPhaseFor(this.inputDetail.getPhase()));
		if( this.inputDetail.getRequired() != null ) {
			this.required = this.inputDetail.getRequired();
		}
		String mvn = this.inputDetail.getModelVarName();
		this.setModelVarName(mvn == null || mvn.isEmpty() ? this.getName() : mvn);
		this.defaultValue = inputDetail.getDefaultValue();
	}

	@Override
	public InputDetail getWrappedClass() {
		return this.inputDetail;
	}

	public boolean isRequired() {
		return this.required;
	}

	public java.time.Instant getCreationDate() {
		return this.inputDetail.getCreationDate();
	}

	public void setCreationDate(java.time.Instant creationTime) {
		this.inputDetail.setCreationDate(creationTime);
	}

	public Instant getLastModifiedDate() {
		return this.inputDetail.getLastModifiedDate();
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.inputDetail.setLastModifiedDate(lastModifiedDate);
	}

	public String getCreatedBy() {
		return this.inputDetail.getCreatedBy();
	}

	public void setCreatedBy(String createdBy) {
		this.inputDetail.setCreatedBy(createdBy);
	}

	public String getLastModifiedBy() {
		return this.inputDetail.getLastModifiedBy();
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.inputDetail.setLastModifiedBy(lastModifiedBy);
	}

	@Override
	public String toString() {
		return super.toString() + ", Name=%s, PHASE=%s,  CommType=%s, DataType=%s".formatted(
				this.getName(), this.getSimulationPhase(), this.getCommunicationType(), this.getType());
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}
}
