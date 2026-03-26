/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.util.Map;

import edu.kit.iai.webis.proofmodels.StepBasedConfigurationDetail;
import edu.kit.iai.webis.proofmodels.StepSizeDefinitionDetail;

/**
 * Wrapper class to provide a StepBasedConfiguration element stored in the configuration database
 */
public class StepBasedConfiguration implements IWrapper <StepBasedConfigurationDetail>{

	private StepBasedConfigurationDetail stepBasedConfigurationDetail;

	private Map<String, StepSizeDefinitionDetail> stepSizeDefinitions;

	private String id;
    /**
     * get the start time of the underlying step-based workflow.
     * The start time defines the start delay in milliseconds,
     * i.e. how many milliseconds should pass before the process starts
     */
     private Long startTime = 0L;

    private Long endTime = 1000L;

    private Integer startPoint = 0;

    private Integer endPoint = 1000;

    /**
     * the time between two simulation status checks
     */
    private Long duration = 1000L;

    private Integer defaultStepSize = 1;

    private Map<String, Integer> stepSizes;

	/**
	 * create a StepBasedConfiguration instance based on a given database element {@link StepBasedConfigurationDetail}
	 * @param sbc the database element
	 */
	public StepBasedConfiguration( StepBasedConfigurationDetail sbc ) {
		this.stepBasedConfigurationDetail = sbc;
		this.id = sbc.getId();
		this.defaultStepSize = this.getDefaultStepSize();
		this.duration = this.getDuration();
		this.startTime = this.getStartTime();
		this.endTime = this.getEndTime();
		this.startPoint = this.getStartPoint();
		this.endPoint = this.getEndPoint();
		this.stepSizeDefinitions = sbc.getStepSizeDefinitions();
	}

    /**
     * get the id for the step-based configuration
     * @return the id.
     */
	public String getId() {
		String id = this.stepBasedConfigurationDetail.getId();
		return id != null ? id : this.id;
	}

    /**
     * set the id for the step-based configuration
     * @param id the id to set.
     */
	public void setId(String id) {
		this.stepBasedConfigurationDetail.setId(id);
		this.id = id;
	}

	/**
	 * get the default step size for a given block
	 * @param localBlockId the local id of a block
	 * @return the default step size of the block.
	 * <br><b>Note:</b> If there is no default step size given for the desired block,
	 * the default step size of this configuration is returned.
	 */
	public Integer getDefaultStepSize( String localBlockId ) {
		StepSizeDefinitionDetail sDef = null;
		if( this.stepSizeDefinitions != null && ( sDef = this.stepSizeDefinitions.get( localBlockId ) ) != null
				&& sDef.getDefaultSize() != null ) {
			return sDef.getDefaultSize();
		} else {
			return this.defaultStepSize;
		}
	}

    /**
     * get the start point for a given block
     * @param localBlockId the local id of a block
     * @return the start point of the block.
     * <br><b>Note:</b> If there is no start point given for the desired block,
     * the default start point of this configuration is returned.
     */
	public Integer getStartPoint( String localBlockId ) {
		StepSizeDefinitionDetail sDef = null;
		if( this.stepSizeDefinitions != null && ( sDef = this.stepSizeDefinitions.get( localBlockId ) ) != null
				&& sDef.getStartPoint() != null ) {
			return sDef.getStartPoint();
		} else {
			return this.startPoint;
		}
	}

	/**
	 * get the end point for a given block
	 * @param localBlockId the local id of a block
	 * @return the end point of the block.
	 * <br><b>Note:</b> If there is no end point given for the desired block,
	 * the default end point of this configuration is returned.
	 */
	public Integer getEndPoint( String localBlockId ) {
		StepSizeDefinitionDetail sDef = null;
		if( this.stepSizeDefinitions != null && ( sDef = this.stepSizeDefinitions.get( localBlockId ) ) != null
				&& sDef.getEndPoint() != null ) {
			return sDef.getEndPoint();
		}
		else {
			return this.endPoint;
		}
	}

    /**
     * get a {@link StepSizeDefinition} for a given block
     * @param localBlockId the id of the local block
     * @return the desired {@link StepSizeDefinition} or <b>null</b>
     */
    public StepSizeDefinition getStepSizeDefinition( String localBlockId ) {
    	StepSizeDefinitionDetail sDef = null;
    	if( this.stepSizeDefinitions != null && ( sDef = this.stepSizeDefinitions.get( localBlockId ) ) != null ) {
    		return new StepSizeDefinition(sDef);
    	}
    	return null;
   }

    /**
     * get the end point for the simulation
     * @return the end point.
     */
	public Integer getEndPoint() {
		Integer eP = this.stepBasedConfigurationDetail.getEndPoint();
		return eP != null ? eP : this.endPoint;
	}

	/**
	 * get the start point for the simulation
	 * @return the start point.
	 */
	public Integer getStartPoint() {
		Integer sP = this.stepBasedConfigurationDetail.getStartPoint();
		return sP != null ? sP : this.startPoint;
	}

	public Long getStartTime() {
		Long sT = this.stepBasedConfigurationDetail.getStartTime();
		return sT != null ? sT : this.startTime;
	}

	public Long getEndTime() {
		Long eT = this.stepBasedConfigurationDetail.getEndTime();
		return eT != null ? eT : this.endTime;
	}

	public Long getDuration() {
		Long duration = this.stepBasedConfigurationDetail.getDuration();
		return duration != null ? duration : this.duration;
    }

	public Integer getDefaultStepSize() {
		Integer dss = this.stepBasedConfigurationDetail.getDefaultStepSize();
		return dss != null ? dss : this.defaultStepSize;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepBasedConfigurationDetail getWrappedClass() {
		return this.stepBasedConfigurationDetail;
	}
}
