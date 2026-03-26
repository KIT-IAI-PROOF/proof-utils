/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

import java.util.HashMap;
import java.util.Map;

import edu.kit.iai.webis.proofutils.LoggingHelper;
import edu.kit.iai.webis.proofutils.wrapper.IWrapper;

public class BlockIODefinition {


    /**
     * The {@link SimulationPhase} where the step is processed, possible values are INIT, STEP, FINALIZE, or SHUTDOWN
     */
    private SimulationPhase simulationPhase;

    /**
     * Map containing the name mappings for the inputs of a block
     */
    private Map<String, String> inputNameMappings = new HashMap<String, String>();

    /**
     * Map containing the name mappings for the outputs of a block
     */
    private Map<String, String> outputNameMappings = new HashMap<String, String>();
    
    /**
     * Interface type for writing data
    private InterfaceType interfaceType;
     */

    public BlockIODefinition(SimulationPhase phase) {
    	this.simulationPhase = phase;
    }

    /**
     * Get the the current workflow phase
     * @return the phase
     */
    public SimulationPhase getSimulationPhase() {
        return this.simulationPhase;
    }

    /**
     * Set the the current simulation phase
     * @param phase the phase
     */
    public void setSimulationPhase(SimulationPhase phase) {
        this.simulationPhase = phase;
    }

	public SimulationPhase getPhase() {
		LoggingHelper.debug().log("\n\n==> BlockIODefinition:  replace this method with getSimulationPhase()!! \n\n");
		return simulationPhase;
	}

	public void setPhase(SimulationPhase phase) {
		LoggingHelper.debug().log("\n\n==> BlockIODefinition:  replace this method with setSimulationPhase()!! \n\n");
		this.simulationPhase = phase;
	}

	public Map<String, String> getInputNameMappings() {
		return inputNameMappings;
	}

	public void addInputNameMapping( String inputName, String modelInputVarName ) {
		this.inputNameMappings.put( inputName, modelInputVarName );
	}

	public Map<String, String> getOutputNameMappings() {
		return outputNameMappings;
	}

	public void addOutputNameMapping( String outputName, String modelOutputVarName ) {
		this.outputNameMappings.put( outputName, modelOutputVarName );
	}
	

}
