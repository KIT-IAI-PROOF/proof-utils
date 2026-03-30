/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;

import edu.kit.iai.webis.proofmodels.OutputDetail;

/**
 * Wrapper class to provide Output element stored in the configuration database
 */
public class Output extends IOElement implements IWrapper <OutputDetail>{

	private OutputDetail outputDetail;
	
	/**
	 * create an output instance based on a given database element {@link OutputDetail} 
	 * @param outputDetail the database element
	 */
	public Output(OutputDetail outputDetail) {
		this.outputDetail = outputDetail;
		setId(this.outputDetail.getId());
		setName(this.outputDetail.getLabel());
		setType(EnumMapper.getDataTypeFor( this.outputDetail.getType()) );
		setCommunicationType(EnumMapper.getCommunicationTypeFor(this.outputDetail.getCommunicationType()));
		setSimulationPhase(EnumMapper.getSimulationPhaseFor(this.outputDetail.getPhase()));
		String mvn = this.outputDetail.getModelVarName();
		setModelVarName(mvn != null && ! mvn.isEmpty() ? mvn : this.outputDetail.getLabel());
	}

	public java.time.Instant getCreationDate() {
		return this.outputDetail.getCreationDate();
	}

	public void setCreationDate(java.time.Instant creationTime) {
		this.outputDetail.setCreationDate(creationTime);
	}

	public Instant getLastModifiedDate() {
		return this.outputDetail.getLastModifiedDate();
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.outputDetail.setLastModifiedDate(lastModifiedDate);
	}

	public String getCreatedBy() {
		return this.outputDetail.getCreatedBy();
	}

	public void setCreatedBy(String createdBy) {
		this.outputDetail.setCreatedBy(createdBy);
	}

	public String getLastModifiedBy() {
		return this.outputDetail.getLastModifiedBy();
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.outputDetail.setLastModifiedBy(lastModifiedBy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputDetail getWrappedClass() {
		return this.outputDetail;
	}

}
