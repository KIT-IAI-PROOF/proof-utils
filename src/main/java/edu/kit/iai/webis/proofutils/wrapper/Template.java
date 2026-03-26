/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.iai.webis.proofmodels.TemplateDetail;
import edu.kit.iai.webis.proofmodels.InputDetail;
import edu.kit.iai.webis.proofmodels.OutputDetail;
import edu.kit.iai.webis.proofmodels.TemplateDetail.InterfaceTypeEnum;

/**
 * Wrapper class to provide a Template element stored in the configuration database
 */
public class Template implements IWrapper<TemplateDetail> {

	private final TemplateDetail templateDetail;
	private Program program;
	private Map<String, Input> inputs;
	private Map<String, Output> outputs;

	/**
	 * create a Template instance based on a given database element {@link TemplateDetail}
	 * @param templateDetail the database element
	 */
	public Template(TemplateDetail templateDetail) {
		this.templateDetail = templateDetail;
		if (templateDetail.getProgram() != null) {
			this.program = new Program(templateDetail.getProgram());
		}
		this.getInputs();
		this.getOutputs();
	}

	/**
	 * get the template id
	 * @return the id
	 */
	public String getId() {
		return this.templateDetail.getId();
	}

	/**
	 * get the template name
	 * @return the name
	 */
	public String getName() {
		return this.templateDetail.getName();
	}

	/**
	 * get the template description
	 * @return the description
	 */
	public String getDescription() {
		return this.templateDetail.getDescription();
	}

	/**
	 * get the template color
	 * @return the color in hex format
	 */
	public String getColor() {
		return this.templateDetail.getColor();
	}

	/**
	 * get the template type
	 * @return the type
	 */
	public String getType() {
		return this.templateDetail.getType();
	}

	/**
	 * get the block type
	 * @return the block type
	 */
	public String getBlockType() {
		return this.templateDetail.getBlockType() != null ? this.templateDetail.getBlockType().toString() : null;
	}

	/**
	 * get the container image
	 * @return the container image
	 */
	public String getContainerImage() {
		return this.templateDetail.getContainerImage();
	}

	/**
	 * get the block status
	 * @return the status
	 */
	public String getStatus() {
		return this.templateDetail.getStatus() != null ? this.templateDetail.getStatus().toString() : null;
	}

	/**
	 * get the communication paradigm
	 * @return the communication paradigm
	 */
	public String getCommunicationParadigm() {
		return this.templateDetail.getCommunicationParadigm() != null ? this.templateDetail.getCommunicationParadigm().toString() : null;
	}

	/**
	 * get the sync strategy
	 * @return the sync strategy
	 */
	public String getSyncStrategy() {
		return this.templateDetail.getSyncStrategy() != null ? this.templateDetail.getSyncStrategy().toString() : null;
	}

	/**
	 * get the program associated with this template
	 * @return the wrapped program or null
	 */
	public Program getProgram() {
		return this.program;
	}

	/**
	 * get the interface type
	 * @return the interface type
	 */
	public String getInterfaceType() {
		return this.templateDetail.getInterfaceType() != null ? this.templateDetail.getInterfaceType().toString() : null;
	}

	/**
	 * set the interface type
	 * @param the interface type
	 */
	public void setInterfaceType(InterfaceTypeEnum interfaceType) {
        if (this.templateDetail != null) {
            this.templateDetail.setInterfaceType(interfaceType);
        }
	}

	/**
	 * check whether the template is relevant for shutdown
	 * @return true if shutdown relevant, false otherwise
	 */
	public Boolean isShutdownRelevant() {
		return this.templateDetail.getShutdownRelevant();
	}

	/**
	 * get the user who created this template
	 * @return the created by user
	 */
	public String getCreatedBy() {
		return this.templateDetail.getCreatedBy();
	}

	/**
	 * get the user who last modified this template
	 * @return the last modified by user
	 */
	public String getLastModifiedBy() {
		return this.templateDetail.getLastModifiedBy();
	}

	/**
	 * get the creation date
	 * @return the creation date
	 */
	public Instant getCreationDate() {
		return this.templateDetail.getCreationDate();
	}

	/**
	 * get the last modified date
	 * @return the last modified date
	 */
	public Instant getLastModifiedDate() {
		return this.templateDetail.getLastModifiedDate();
	}

	/**
	 * get the {@link Input}s of the template.
	 * @return the {@link Input}s of the template mapped by their id
	 */
	public Map<String, Input> getInputs() {
		if (this.inputs == null) {
			List<InputDetail> inputDetails = this.templateDetail.getInputs();
			this.inputs = new HashMap<String, Input>();
			if (inputDetails != null) {
				inputDetails.forEach(i -> this.inputs.put(i.getId(), new Input(i)));
			}
		}
		return this.inputs;
	}

	/**
	 * get the {@link Output}s of the template.
	 * @return the {@link Output}s of the template mapped by their id
	 */
	public Map<String, Output> getOutputs() {
		if (this.outputs == null) {
			List<OutputDetail> outputDetails = this.templateDetail.getOutputs();
			this.outputs = new HashMap<String, Output>();
			if (outputDetails != null) {
				outputDetails.forEach(o -> this.outputs.put(o.getId(), new Output(o)));
			}
		}
		return this.outputs;
	}

	/**
	 * set the user who created this template
	 * @param createdBy the user who created this template
	 */
	public void setCreatedBy(String createdBy) {
		if (this.templateDetail != null) {
			this.templateDetail.setCreatedBy(createdBy);
		}
	}

	/**
	 * set the user who last modified this template
	 * @param lastModifiedBy the user who last modified this template
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		if (this.templateDetail != null) {
			this.templateDetail.setLastModifiedBy(lastModifiedBy);
		}
	}

	/**
	 * set the creation date
	 * @param creationDate the creation date
	 */
	public void setCreationDate(Instant creationDate) {
		if (this.templateDetail != null) {
			this.templateDetail.setCreationDate(creationDate);
		}
	}

	/**
	 * set the last modified date
	 * @param lastModifiedDate the last modified date
	 */
	public void setLastModifiedDate(Instant lastModifiedDate) {
		if (this.templateDetail != null) {
			this.templateDetail.setLastModifiedDate(lastModifiedDate);
		}
	}

	/**
	 * set the template color
	 * @param color the color in hex format
	 */
	public void setColor(String color) {
		if (this.templateDetail != null) {
			this.templateDetail.setColor(color);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateDetail getWrappedClass() {
		return this.templateDetail;
	}

}
