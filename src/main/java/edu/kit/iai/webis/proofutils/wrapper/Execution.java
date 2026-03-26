/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;
import java.util.Map;

import edu.kit.iai.webis.proofmodels.ExecutionDetail;
import edu.kit.iai.webis.proofutils.model.SimulationStatus;
import edu.kit.iai.webis.proofutils.model.InterfaceType;
import edu.kit.iai.webis.proofutils.model.ProcessEnvironment;

/**
 * Wrapper class to provide an Execution element stored in the configuration database
 */
public class Execution implements IWrapper<ExecutionDetail> {
	private final ExecutionDetail executionDetail;

	private final String executionId;
	//private SimulationStatus status;
	private final Workflow workflow;

	/**
	 * create an execution instance based on a given database element {@link ExecutionDetail}
	 * @param executionDetail the database element
	 */
	public Execution(ExecutionDetail executionDetail) {
		this.executionDetail = executionDetail;
		this.executionId = executionDetail.getId();
		//this.status = EnumMapper.getSimulationStatusFor(executionDetail.getStatus());
		this.workflow = new Workflow(this.executionDetail.getWorkflow());
	}

	public ExecutionOptions getExecutionOptions() {
		return new ExecutionOptions(this.executionDetail.getOptions());
	}

	public String getId() {
		return this.executionId;
	}

	public Map<String, String> getAppliedInputs() {
		return this.executionDetail.getAppliedInputs();
	}

	@Override
	public ExecutionDetail getWrappedClass() {
		return this.executionDetail;
	}

	public String getName() {
		return this.executionDetail.getLabel();
	}

	public void setCreationDate(Instant creationTime) {
		this.executionDetail.setCreationDate(creationTime);
	}

	public SimulationStatus getStatus() {
		//return this.status;
		return EnumMapper.getSimulationStatusFor(this.executionDetail.getStatus());
	}

	public int getCommunicationPoint() {
		return this.executionDetail.getCurrentCP();
	}

	public Workflow getWorkflow() {
		return this.workflow;
	}

	public String getWorkflowId() {
		return this.workflow.getId();
	}

	public void setStartedAt( String dateAndTime ) {
		this.executionDetail.setStartedAt(dateAndTime);
	}

	public void setStoppedAt( String dateAndTime ) {
		this.executionDetail.setStoppedAt(dateAndTime);
	}

	public void setStatus( SimulationStatus status ) {
		this.executionDetail.setStatus(EnumMapper.getExecutionStatusEnumFor(status));
	}

	public void setCommunicationPoint( Integer CP ) {
		this.executionDetail.setCurrentCP(CP);
	}

	/**
	 * Get the {@link InterfaceType} for the execution
	 * Possible handling types for the data communication between worker and wrapper.
	 * Values may be {@link #FILE}, {@link #STDIO}, or {@link #SOCKET}
	 */
	public InterfaceType getInterfaceType() {
		return EnumMapper.getInterfaceTypeFor( this.executionDetail.getInterfaceType() );
	}

	/**
	 * define the environment where the workflow should be processed.
	 * Possible values are {@link ProcessEnvironment#LOCAL}, {@link ProcessEnvironment#DOCKER}, and {@link ProcessEnvironment#KUBERNETES}.
	 */
	public ProcessEnvironment getProcessEnvironment() {
		return EnumMapper.getProcessEnvironmentFor(this.executionDetail.getProcessEnvironment());
	}
}
