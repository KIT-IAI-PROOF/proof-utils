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
	private Object defaultValue;
	private Object startValue;

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
		this.defaultValue = string2Value(this.inputDetail.getDefaultValue());
		this.startValue = string2Value(this.inputDetail.getStartValue());
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
		return super.toString() + ", Name=%s, PHASE=%s,  CommType=%s, DataType=%s, Start=%s, Default=%s".formatted(
				this.getName(), this.getSimulationPhase(), this.getCommunicationType(), this.getType(), this.getStartValue(), this.getDefaultValue());
	}

	public Object getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Object getStartValue() {
		return this.startValue;
	}

	public void setStartValue(Object startValue) {
		this.startValue = startValue;
	}

	private Object string2Value(String valueString) {
		Object val = null;
		if( valueString != null && !valueString.isBlank() ) {
			switch( this.getType() ){
			case STRING, FILE_NAME, OBJECT -> {
				val = valueString;
			}
			case INTEGER -> {
				val = Integer.valueOf(valueString);
			}
			case FLOAT -> {
				val = Float.valueOf(valueString);
			}
			case STRING_ARRAY, OBJECT_ARRAY -> {
				val = valueString.split(",");
			}
			case INTEGER_ARRAY -> {
				String[] sarr = valueString.split(",");
				Integer[] iarr = new Integer[sarr.length];
				for (int i = 0; i < iarr.length; i++) {
					iarr[i] = Integer.valueOf(sarr[i]);
				}
				val = iarr;
			}
			case FLOAT_ARRAY -> {
				String[] sarr = valueString.split(",");
				Float[] farr = new Float[sarr.length];
				for (int i = 0; i < farr.length; i++) {
					farr[i] = Float.valueOf(sarr[i]);
				}
				val = farr;
			}
			}
		}
		return val;
	}
}
