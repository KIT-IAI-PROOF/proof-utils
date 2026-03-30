/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.kit.iai.webis.proofutils.LoggingHelper;
import edu.kit.iai.webis.proofutils.exception.CannotPerformStepException;
import edu.kit.iai.webis.proofutils.wrapper.Block;
import edu.kit.iai.webis.proofutils.wrapper.StepBasedConfiguration;
import edu.kit.iai.webis.proofutils.wrapper.StepSizeDefinition;

/**
 * Manage the contents of a {@link StepSizeDefinition} for a {@link Block}
 */
public class StepSizeDefinitionHelper {

    private StepSizeDefinition stepSizeDefinition;

    private final static Integer DEFAULT_END_POINT = 99;

    private Integer endPoint = DEFAULT_END_POINT;
    private Integer startPoint = 1;

	private boolean[] relevantCommunicationPoints = null;

	private Integer localBlockId;

	private StepBasedConfiguration stepBasedConfiguration;
    private Integer communicationStepSize = 1;

    public StepSizeDefinitionHelper(StepBasedConfiguration stepBasedConfig, Integer localBlockId) {
		super();
		this.stepBasedConfiguration = Objects.requireNonNull(stepBasedConfig, "StepBasedConfiguration must be given");;
		this.localBlockId = Objects.requireNonNull(localBlockId, "localBlockId must be given");;
		this.stepSizeDefinition = this.stepBasedConfiguration.getStepSizeDefinition(""+this.localBlockId);
		if( this.stepSizeDefinition != null ) {
			LoggingHelper.debug().log("StepSizeDefinitions for Block '%s': %s %s", this.localBlockId, this.stepSizeDefinition,
				(this.stepSizeDefinition.getStepSizes() == null ? ", Stepsizes not defined" : "\nStepSizes: " + this.stepSizeDefinition.getStepSizes() ) );
		} else {
			LoggingHelper.debug().log("no StepSizeDefinitions defined for Block '%s'", this.localBlockId);
		}
		this.endPoint = this.stepBasedConfiguration.getEndPoint(""+this.localBlockId);
		this.startPoint = this.stepBasedConfiguration.getStartPoint(""+this.localBlockId);
        this.communicationStepSize = stepBasedConfig.getDefaultStepSize(""+this.localBlockId);
        LoggingHelper.trace().log("SSD-Helper-Settings: endPoint=%d, startPoint=%d, defaultStepSize (communicationStepSize)=%d"
				.formatted(this.endPoint, this.startPoint, this.communicationStepSize ));
	}

    /**
     * checks whether a step can be performed. This check is based on the optionally given stepSizeDefinitions
     * @param communicationPoint the current communication point
     * @return treu, if the step can be performed, false otherwise
     * @throws CannotPerformStepException if the step can not be performed due to the normal end of the
     * simulation, i.e. if the current communication point becomes greater than the end point
     */
	public boolean canPerformStep( Integer communicationPoint ) throws CannotPerformStepException {
		Objects.requireNonNullElse(communicationPoint, 0);
		if( communicationPoint.compareTo(this.endPoint) > 0 ) {
			throw new CannotPerformStepException("CP %d is greater than endPoint %d! ".formatted(communicationPoint, this.endPoint));
		}
        else if( this.relevantCommunicationPoints == null ) {
        	this.createRelevantStepSizeArray( this.startPoint, this.endPoint );
        }
        return  this.relevantCommunicationPoints[ communicationPoint ];
    }

    /**
     * get the communication step size for a given communicationPoint
     *
     * @param communicationPoint the (current) communicationPoint
     * @return the associated communication step size, if there is one, otherwise,
     * the default communication step size is used (see {@link StepBasedConfiguration}).
     */
    public Integer getCommunicationStepSize(Integer communicationPoint) {
    	if (this.stepSizeDefinition != null) {
    		Integer stepSize = this.stepSizeDefinition.getStepSize(communicationPoint);
    		if (stepSize != null) {
    			LoggingHelper.debug().log(" stepSize for CP %d  is SPECIAL stepSize %d", communicationPoint, stepSize );
    			return stepSize;
    		}
    		else {
    			LoggingHelper.debug().log(" stepSize for CP %d: stepSize == NULL (StepSizeDefinition is given), return DEFAULT stepSize  %d", communicationPoint, this.communicationStepSize );
    			return this.communicationStepSize;
    		}
    	}
    	LoggingHelper.debug().log(" stepSize for CP %d  is DEFAULT stepSize %d", communicationPoint, this.communicationStepSize );
    	return this.communicationStepSize;
    }

	private void createRelevantStepSizeArray( int startPoint, int numberOfCommunicationPoints ) {
		this.relevantCommunicationPoints = new boolean[numberOfCommunicationPoints + 1];
		Map<String, Integer> stepSizes = null;
		if( this.stepSizeDefinition != null ) {
			stepSizes = this.stepSizeDefinition.getStepSizes();
		}

		Arrays.fill(  this.relevantCommunicationPoints, startPoint, numberOfCommunicationPoints, false);

		// If there are no stepSizes: use the default step size (this.communicationStepSize)
		if( stepSizes == null || stepSizes.isEmpty() ){
			for (int i = this.startPoint; i < this.relevantCommunicationPoints.length; i+= this.communicationStepSize) {
				this.relevantCommunicationPoints[i] = true;
			}
		}
		else // if( stepSizes != null && ! stepSizes.isEmpty() )
		{
			LoggingHelper.printHashMapContents(stepSizes, System.out, "Stepsizes:\n");
			List<Integer> cps = new ArrayList<Integer>(stepSizes.size());
			stepSizes.keySet().forEach( k -> cps.add( Integer.valueOf(k)));
			Collections.sort(cps);

			int ssdStartPoint = cps.get(0);

			if( startPoint < ssdStartPoint ) {
				for (int i = startPoint; i < ssdStartPoint ; i++) {
					this.relevantCommunicationPoints[i] = true;
				}
			}

			int end = startPoint;
			for (int i = 0; i < cps.size(); i++) {
				int start = cps.get(i);
				end = ( i < cps.size()-1 ) ? cps.get(i+1) : numberOfCommunicationPoints;
				LoggingHelper.trace().log("START, END: " + start + ", " + end);
				if( end > numberOfCommunicationPoints ) {
			    	LoggingHelper.error().log("Given end point (%d) is greater than given number of communication points (%d)! => all values after key %s are ignored",
			    			 end, numberOfCommunicationPoints, i);
			    	break;
				}
				int step = stepSizes.get(""+start);
				this.relevantCommunicationPoints[start] = true;
				for( int j = start + step ; j <= end ; j += step ) {
					this.relevantCommunicationPoints[j] = true;
				}
			}
		}

		if( LoggingHelper.isLevelDebugOrTrace() ) {
			System.out.println("StepSizeDefinitionHelper:  Relevant CommunicationPoint Array: ");
			int jj = 0;
			for (boolean b : this.relevantCommunicationPoints) {
				System.out.print((b ? "x" : "-"));
				if( ++jj % 10 == 0 ) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * @return the stepSizeDefinition
	 */
	public StepSizeDefinition getStepSizeDefinition() {
		return this.stepSizeDefinition;
	}

	/**
	 * @return the endPoint
	 */
	public Integer getEndPoint() {
		return this.endPoint;
	}

	/**
	 * @return the startPoint
	 */
	public Integer getStartPoint() {
		return this.startPoint;
	}

	/**
	 * @return the localBlockId
	 */
	public Integer getLocalBlockId() {
		return this.localBlockId;
	}

	/**
	 * @return the stepBasedConfiguration
	 */
	public StepBasedConfiguration getStepBasedConfiguration() {
		return this.stepBasedConfiguration;
	}


}
