/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.kit.iai.webis.proofmodels.BlockDetail;
import edu.kit.iai.webis.proofmodels.InputDetail;
import edu.kit.iai.webis.proofmodels.OutputDetail;
import edu.kit.iai.webis.proofutils.model.BlockIODefinition;
import edu.kit.iai.webis.proofutils.model.CommunicationType;
import edu.kit.iai.webis.proofutils.model.InterfaceType;
import edu.kit.iai.webis.proofutils.model.SimulationPhase;
import edu.kit.iai.webis.proofutils.model.SimulationStatus;
import edu.kit.iai.webis.proofutils.model.SyncStrategy;

/**
 * Wrapper class to provide a Block element stored in the configuration database
 */
public class Block implements IWrapper<BlockDetail> {
	private BlockDetail blockDetail;

	private SimulationStatus status = SimulationStatus.CREATED;
	private String index;
	private Map<String, Input> inputs;
	private Map<String, Output> outputs;
	private InterfaceType interfaceType;

	private final EnumMap<SimulationPhase, Map<String, String>> inputNameMappingsPerPhase = new EnumMap<SimulationPhase, Map<String, String>>(
			Map.of(
					SimulationPhase.INIT, new HashMap<String, String>(),
					SimulationPhase.EXECUTE, new HashMap<String, String>(),
					SimulationPhase.FINALIZE, new HashMap<String, String>()
			));
	private final EnumMap<SimulationPhase, Map<String, String>> outputNameMappingsPerPhase = new EnumMap<SimulationPhase, Map<String, String>>(
			Map.of(
					SimulationPhase.INIT, new HashMap<String, String>(),
					SimulationPhase.EXECUTE, new HashMap<String, String>(),
					SimulationPhase.FINALIZE, new HashMap<String, String>()
			));
	private Map<SimulationPhase, BlockIODefinition> blockIODefinitionsPerPhase = new HashMap<SimulationPhase, BlockIODefinition>();

	private Boolean shutdownRelevant;

	private SyncStrategy syncStrategy;

	private final List<Input> requiredDynamicInputs;

	private boolean blockHasDynamicInputs = false;

	private Program program;

	/**
	 * create a block instance based on a given database element {@link BlockDetail}
	 * @param blockDetail the database element
	 */
	public Block(BlockDetail blockDetail) {
		this.blockDetail = blockDetail;
		this.program = new Program(blockDetail.getProgram());
		this.shutdownRelevant = this.blockDetail.getShutdownRelevant();
		this.status = (this.blockDetail.getStatus() == null ? SimulationStatus.CREATED
				: EnumMapper.getSimulationStatusFor(this.blockDetail.getStatus()));
		this.interfaceType = EnumMapper.getInterfaceTypeFor( this.blockDetail.getInterfaceType() );
		this.getInputs();
		this.getOutputs();
		this.createIONameMappings();
		this.blockHasDynamicInputs = this.inputs.values().stream()
        		.anyMatch( (input) -> ( input.getCommunicationType() == CommunicationType.STEPBASED || input.getCommunicationType() == CommunicationType.EVENT ) );
		this.requiredDynamicInputs = this.inputs.values().stream()
				.filter((input) -> (input.getCommunicationType() == CommunicationType.STEPBASED
						|| input.getCommunicationType() == CommunicationType.EVENT) && input.isRequired())
				.collect(Collectors.toList());
		this.syncStrategy = EnumMapper.getSyncStrategyFor( this.blockDetail.getSyncStrategy() );
	}

	public InterfaceType getInterfaceType() {
		return this.interfaceType;
	}

	/**
	 * set the interface type
	 * @param interfaceType the interface type to set
	 */
	public void setInterfaceType(InterfaceType interfaceType) {
		if (interfaceType != null) {
			this.interfaceType = interfaceType;
			this.blockDetail.setInterfaceType(EnumMapper.getTypeEnumFor(interfaceType));
		}
	}

	/**
	 * create {@link BlockIODefinition}s. For each {@link SimulationPhase} exists one {@link BlockIODefinition}
	 */
	private void createIONameMappings() {
		for( SimulationPhase sPhase : SimulationPhase.values() ) {
			Map<String, String> phaseMappping = this.inputNameMappingsPerPhase.get(sPhase);
			for( Input input : this.inputs.values() ) {
				if( input.getSimulationPhase() == sPhase ) {
					phaseMappping.put( input.getName(), input.getModelVarName() );
				}
			}
			phaseMappping = this.outputNameMappingsPerPhase.get(sPhase);
			for( Output output : this.outputs.values() ) {
				if( output.getSimulationPhase() == sPhase ) {
					phaseMappping.put( output.getName(), output.getModelVarName() );
				}
			}
		}
	}

	public Map<String, String> getInputNameMappings( SimulationPhase simulationPhase ) {
		return this.inputNameMappingsPerPhase.get(simulationPhase);
	}

	public Map<String, String> getOutputNameMappings( SimulationPhase simulationPhase ) {
		return this.outputNameMappingsPerPhase.get(simulationPhase);
	}

	public String getId() {
		return this.blockDetail.getId();
	}

	public String getTemplateId() {
		return this.blockDetail.getTemplateId();
	}

	/**
	 * @return true, if the block is relevant for shutdown, e.g., all other blocks
	 *         will be shut down when this block finishes its simulation
	 */
	public Boolean isShutdownRelevant() {
		return this.shutdownRelevant;
	}

	/**
	 * @param shutdownRelevant the relevance for shutdown to set
	 */
	public void setShutdownRelevant(Boolean shutdownRelevant) {
		this.shutdownRelevant = shutdownRelevant;
		this.blockDetail.setShutdownRelevant(shutdownRelevant);
	}

	/**
	 * get the underlying docker container image
	 * @return the docker container image
	 */
	public String getContainerImage() {
		return this.blockDetail.getContainerImage();
	}

    /**
     * get the {@link Input}s of the block.
     * @return the {@link Input}s of the block
     */
	public Map<String, Input> getInputs() {
		if (this.inputs == null) {
			List<InputDetail> inputDetails = this.blockDetail.getInputs();
			this.inputs = new HashMap<String, Input>(inputDetails.size());
			inputDetails.forEach(i -> this.inputs.put(i.getId(), new Input(i)));
		}
		return this.inputs;
	}

    /**
     * get inputs for a given {@link SimulationPhase}
     * @param phase the {@link SimulationPhase}
     * @return the list of inputs matching the phase
     */
    public List<Input> getInputs( SimulationPhase phase) {
    	return this.inputs.values().stream().filter( i -> i.getSimulationPhase() == phase ).toList();
    }

    /**
     * get the {@link Input} of the block for a given id
     * @param id the given unique id
     * @return the {@link Input} of the block with the given id or <b>null</b>
     */
	public Input getInput(String id) {
		return (this.inputs != null) ? this.inputs.get(id) : null;
	}

    /**
     * get the {@link Output}s of the block.
     * @return the {@link Output}s of the block
     */
	public Map<String, Output> getOutputs() {
		if (this.outputs == null) {
			List<OutputDetail> outputDetails = this.blockDetail.getOutputs();
			this.outputs = new HashMap<String, Output>(outputDetails.size());
			outputDetails.forEach(o -> this.outputs.put(o.getId(), new Output(o)));
		}
		return this.outputs;
	}

    /**
     * get outputs for a given {@link SimulationPhase}
     * @param phase the {@link SimulationPhase}
     * @return the list of outputs matching the phase
     */
    public List<Output> getOutputs( SimulationPhase phase) {
    	return this.outputs.values().stream().filter( i -> i.getSimulationPhase() == phase ).toList();
    }



	@Override
	public BlockDetail getWrappedClass() {
		return this.blockDetail;
	}

	public String getName() {
		return this.blockDetail.getLabel();
	}

	public Instant getCreationDate() {
		return this.blockDetail.getCreationDate();
	}

	public void setCreationDate(Instant creationTime) {
		this.blockDetail.setCreationDate(creationTime);
	}

	public Instant getLastModifiedDate() {
		return this.blockDetail.getLastModifiedDate();
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.blockDetail.setLastModifiedDate(lastModifiedDate);
	}

	public String getCreatedBy() {
		return this.blockDetail.getCreatedBy();
	}

	public void setCreatedBy(String createdBy) {
		this.blockDetail.setCreatedBy(createdBy);
	}

	public String getLastModifiedBy() {
		return this.blockDetail.getLastModifiedBy();
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.blockDetail.setLastModifiedBy(lastModifiedBy);
	}

	public Program getProgram() {
		return this.program;
	}

	public String getProgramId() {
		return this.program.getId();
	}

	public SimulationStatus getStatus() {
		return this.status;
	}

	public void setStatus(SimulationStatus status) {
		this.status = status;
		this.blockDetail.setStatus(EnumMapper.getStatusEnumFor(status));
	}

	public Integer getIndex() {
		return this.blockDetail.getIndex();
	}

	public String getInputName(String inputId) {
		Input i = this.inputs.get(inputId);
		if (i != null) {
			return i.getName();
		}
		return "unknownInput";
	}

	public String getOutputName(String outputId) {
		Output o = this.outputs.get(outputId);
		if (o != null) {
			return o.getName();
		}
		return "unknownOutput";
	}

	/**
	 * get the required dynamic (i.e. non-static) inputs of a block
	 *
	 * @return the list of required dynamic inputs
	 */
	public List<Input> getRequiredDynamicInputs() {
		return this.requiredDynamicInputs;
	}

	/**
	 * get the required static inputs of a block
	 *
	 * @return the list of required static inputs
	 */
	public List<Input> getRequiredStaticInputs() {
		return this.inputs.values().stream().filter(
				(input) -> (input.getCommunicationType() == CommunicationType.STEPBASED_STATIC && input.isRequired()))
				.collect(Collectors.toList());
	}

    /**
     * check whether one block has dynamic (i.e. non-static) inputs
     *
     * @return true, if the block has dynamic inputs, false, if not
     */
    public boolean hasDynamicInputs() {
    	return this.blockHasDynamicInputs;
    }

    /**
     * get the dynamic (i.e. non-static) inputs of a block
     *
     * @return the list of dynamic inputs
     */
    public List<Input> getDynamicInputs() {
    	return this.inputs.values().stream()
    		.filter( (input) -> ( input.getCommunicationType() == CommunicationType.STEPBASED || input.getCommunicationType() == CommunicationType.EVENT ) )
    		.collect( Collectors.toList() );
    }

    /**
     * get the number of dynamic (i.e. non-static) inputs from one block
     *
     * @return the number of dynamic inputs
     *  <br><b>Note: </b> this method uses {@link #getDynamicInputs()}
     */
    public int getNumberOfDynamicInputs() {
    	return this.getDynamicInputs().size();
    }

    public SyncStrategy getSyncStrategy() {
		return this.syncStrategy;
	}

}
