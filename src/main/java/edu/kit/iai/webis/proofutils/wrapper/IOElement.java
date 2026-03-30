/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.util.Map;
import java.util.Objects;

import edu.kit.iai.webis.proofutils.model.CommunicationType;
import edu.kit.iai.webis.proofutils.model.SimulationPhase;
import edu.kit.iai.webis.proofutils.CommonStringTemplates;

/**
 * Abstract parent of {@link Input} and {@link Output}.
 */
public abstract class IOElement {

	/**
	 * the data type of the input or output element, may be one of {@link #STRING}, {@link #INTEGER}, {@link #FLOAT}, or {@link #OBJECT},
	 */
	public enum DataType {
		/**
		 * string type, all characters of the character set
		 */
		STRING(CommonStringTemplates.TYPE_STRING_VALUE),
		/**
		 * integer type
		 */
		INTEGER(CommonStringTemplates.TYPE_INTEGER_VALUE),
		/**
		 * float type
		 */
		FLOAT(CommonStringTemplates.TYPE_FLOAT_VALUE),
		/**
		 * object type, any possible type
		 */
		OBJECT(CommonStringTemplates.TYPE_OBJECT_VALUE),
		/**
		 * file type
		 */
		FILE_NAME(CommonStringTemplates.TYPE_FILE_NAME_VALUE),
		/**
		 * string array type
		 */
		STRING_ARRAY(CommonStringTemplates.TYPE_STRING_ARRAY),
		/**
		 * integer array type
		 */
		INTEGER_ARRAY(CommonStringTemplates.TYPE_INTEGER_ARRAY),
		/**
		 * float array type
		 */
		FLOAT_ARRAY(CommonStringTemplates.TYPE_FLOAT_ARRAY),
		/**
		 * object array type, any possible type
		 */
		OBJECT_ARRAY(CommonStringTemplates.TYPE_OBJECT_ARRAY);



		private String value;

		DataType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		@Override
		public String toString() {
			return String.valueOf(this.value);
		}
	}

	/**
	 * the unique id
	 */
	private String id;

	/**
	 * Human-readable name of the input/output element.
	 */
	private String name;

	/**
	 * description of the input/output element. Can be e.g. used for Tool Tip Texts.
	 */
	private String description;

	/**
	 * The datatype of the exchanged data.
	 */
	private DataType type;

	/**
	 * {@link Integer} that represents the phase of the step of the input/output
	 * element (see {@link WorkflowPhase}).
	 */
	private SimulationPhase phase;

	/**
	 * {@link Integer} that identifies the event of the input/output element.
	 */
	private Integer eventId;

	/**
	 * Communication Type of the IOElement. This is used to define the type of
	 * communication between two blocks. Examples are
	 * {@link CommunicationType#STEPBASED} or {@link CommunicationType#EVENT}.
	 */
	private CommunicationType communicationType;

	/**
	 * Custom Metadata field for the block. This can be used to store additional
	 * information about the block.
	 */
	private Map<String, Object> metadata;

	/**
	 * unit of the IOElement. Examples are {@link CommunicationType#STEPBASED} or
	 * {@link CommunicationType#EVENT}.
	 */
	private String unit;

	/**
	 * the name of the input of the underlying model (e.g. Python script).
	 * This allows a flexible mapping of different input names that are visible in the UI.
	 */
	private String modelVarName;

	/**
	 * Default Constructor
	 */
	public IOElement() {
	}

	/**
	 * get the input or output name that is visible in the UI
	 * @return the input or output name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * set the input or output name that is visible in the UI
	 * @param name the input or output name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the {@link DataType} of the input or output
	 * @return the data type
	 */
	public DataType getType() {
		return this.type;
	}

	/**
	 * set the {@link DataType} of the input or output
	 * @param type the data type
	 */
	public void setType(DataType type) {
		this.type = type;
	}

	/**
	 * get the {@link SimulationPhase} of the input or output
	 * @return the simulation phase
	 */
	public SimulationPhase getSimulationPhase() {
		return this.phase;
	}

	/**
	 * set the {@link SimulationPhase} of the input or output
	 * @param phase the simulation phase
	 */
	public void setSimulationPhase(SimulationPhase phase) {
		this.phase = phase;
	}

	public Integer getEventId() {
		return this.eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	/**
	 * get the {@link CommunicationType} of the input or output
	 * @return the communication type
	 */
	public CommunicationType getCommunicationType() {
		return this.communicationType;
	}

	/**
	 * set the {@link CommunicationType} of the input or output
	 * @param communicationType the communication type
	 */
	public void setCommunicationType(CommunicationType communicationType) {
		this.communicationType = communicationType;
	}

	/**
	 * get a map of optional additional meta data
	 * @return additional meta data
	 */
	public Map<String, Object> getMetadata() {
		return this.metadata;
	}

	/**
	 * set optional additional meta data
	 * @param the additional meta data
	 */
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	/**
	 * set the unique id.
	 * @param id the unique id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * get the unique id.
	 * @return id the unique id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return this.unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * get the input variable name of the model
	 * @return the input variable name of the model
	 */
	public String getModelVarName() {
		return this.modelVarName;
	}

	/**
	 * set the input variable name of the model
	 *
	 * @param modelVarName the input variable name of the model
	 */
	public void setModelVarName(String modelVarName) {
		this.modelVarName = modelVarName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.communicationType, this.description, this.eventId, this.id, this.metadata, this.modelVarName, this.name, this.phase, this.type,
				this.unit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		IOElement other = (IOElement) obj;
		return this.communicationType == other.communicationType && Objects.equals(this.description, other.description)
				&& Objects.equals(this.eventId, other.eventId) && Objects.equals(this.id, other.id)
				&& Objects.equals(this.metadata, other.metadata) && Objects.equals(this.modelVarName, other.modelVarName)
				&& Objects.equals(this.name, other.name) && this.phase == other.phase && this.type == other.type
				&& Objects.equals(this.unit, other.unit);
	}

}
