/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.util.Map;
import java.util.StringJoiner;

import edu.kit.iai.webis.proofmodels.StepSizeDefinitionDetail;

/**
 * Wrapper class to provide a StepSizeDefinition element stored in the configuration database
 */
public class StepSizeDefinition implements IWrapper <StepSizeDefinitionDetail>{

	private StepSizeDefinitionDetail stepSizeDefinitionDetail;
    private Map<String, Integer> stepSizes;
    private Integer defaultSize;
    private Integer startPoint;
    private Integer endPoint;
    
	/**
	 * create a StepSizeDefinition instance based on a given database element {@link StepSizeDefinitionDetail} 
	 * @param ssd the database element
	 */
	public StepSizeDefinition( StepSizeDefinitionDetail ssd ) {
		this.stepSizeDefinitionDetail = ssd;
		this.stepSizes = this.stepSizeDefinitionDetail.getStepSizes();
		this.defaultSize = this.stepSizeDefinitionDetail.getDefaultSize();
		this.startPoint = this.stepSizeDefinitionDetail.getStartPoint();
		this.endPoint = this.stepSizeDefinitionDetail.getEndPoint();
	}

	/**
     * get the step size for a given communication point,
     * i.e. get the step size value that is valid for a given step number.
     *
     * @param communicationPoint the communication point
     * @return the step size for the given communication point or <b>null</b>,
     * if there is no value for the point
     */
    public Integer getStepSize(Integer communicationPoint) {
        return this.stepSizes != null ? this.stepSizes.get("" + communicationPoint) : null;
    }

    /**
     * get the step size for a given communication point,
     * i.e. get the step size value that is valid for a given step number or the default step size.
     *
     * @param communicationPoint the communication point
     * @param takeDefault        if true, the default value will be returned.
     * @return the step size for the given communication point or <b>null</b>,
     * if there is no value for the point, or the default step size, if takeDefault is true.
     */
    public Integer getStepSize(Integer communicationPoint, boolean takeDefault) {
        Integer stepSize = this.getStepSize(communicationPoint);
        return stepSize == null && takeDefault ? this.defaultSize : stepSize;
    }

	/**
	 * @return the stepSizes
	 */
	public Map<String, Integer> getStepSizes() {
		return this.stepSizeDefinitionDetail.getStepSizes();
	}
    
    
    @Override
    public String toString() {

        StringJoiner sj = new StringJoiner(", ");
        sj.add("startPoint=" + (this.startPoint == null ? "null" : this.startPoint));
        sj.add("endPoint=" + (this.endPoint == null ? "null" : this.endPoint));
        sj.add("defaultSize=" + this.defaultSize);
        sj.add("stepsizes=" + (this.stepSizes == null ? "null" : this.stepSizes ));

        return sj.toString();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StepSizeDefinitionDetail getWrappedClass() {
		return this.stepSizeDefinitionDetail;
	}

}
