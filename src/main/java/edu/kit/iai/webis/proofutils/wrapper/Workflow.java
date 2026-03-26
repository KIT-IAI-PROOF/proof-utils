/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.iai.webis.proofmodels.BlockDetail;
import edu.kit.iai.webis.proofmodels.ConnectionDetail;
import edu.kit.iai.webis.proofmodels.WorkflowDetail;
import edu.kit.iai.webis.proofutils.model.SimulationStrategy;

/**
 * a wrapper class for a workflow. It wraps the database response for a workflow query
 */
public class Workflow implements IWrapper <WorkflowDetail>{
	
	private final WorkflowDetail workflowDetail;
	private SimulationStrategy simulationStrategy;
	private final StepBasedConfiguration stepBasedConfiguration;
	private final Map<String, Block> blocks;

	/**
	 * create a workflow instance based on the database response for a workflow query
	 * @param workflowDetail the database response for a workflow query
     * <b>Note: </b> no {@link Block}s are retrieved explicitly from the worker.
     * The block instances are available with {@link Workflow#getBlocks()} or with  {@link Workflow#getBlock(String)}
	 */
	public Workflow(WorkflowDetail workflowDetail) {
		this.workflowDetail = workflowDetail;
		this.simulationStrategy = EnumMapper.getSimulationStrategyFor(this.workflowDetail.getSimulationStrategy());
		this.stepBasedConfiguration = new StepBasedConfiguration( this.workflowDetail.getStepBasedConfig() );
		this.getConnections();
		this.blocks = new HashMap<String, Block>();
	}

	/**
	 * get the {@link SimulationStrategy} for this workflow
	 * @return the SimulationStrategy
	 */
    public SimulationStrategy getSimulationStrategy() {
        return this.simulationStrategy;
    }

    /**
     * get the workflow name (label)
     * @return the name
     */
    public String getName() {
        return this.workflowDetail.getLabel();
    }

    /**
     * get the workflow id
     * @return the id
     */
	public String getId() {
		return this.workflowDetail.getId();
	}

	/**
	 * get the {@link StepBasedConfiguration} for this workflow
	 * @return the step-based configuration
	 */
    public StepBasedConfiguration getStepBasedConfig() {
        return this.stepBasedConfiguration;
    }

    private List<Connection> connections;

    /**
     * get the {@link Connection}s for this workflow
     * @return the connections retrieved from the database.
     */
    public List<Connection> getConnections() {
    	if( this.connections == null ) {
    		List<ConnectionDetail> connectionDetails = this.workflowDetail.getConnections();
    		this.connections = new ArrayList<Connection>(connectionDetails.size());
    		connectionDetails.forEach(c -> {
    			this.connections.add(new Connection(c));
    		});
    	}
    	return this.connections;
    }

    /**
     * get all {@link Block}s that are retrieved from the database.
     * @return all {@link Block}s related to the workflow
     */
	public Map<String, Block> getBlocks() {
		if( this.blocks.isEmpty() ) {
			List<BlockDetail> blockDetails = this.workflowDetail.getBlocks();
			blockDetails.forEach(b -> {
				String aidi = b.getId();
				Block blogg = new Block(b);
				this.blocks.put(aidi, blogg);
//				blocks.put(b.getId(), new Block(b));
			});
		}
		return this.blocks;
	}

	/**
	 * get a {@link Block} with a given id related to this workflow retrieved from the database
	 * @param blockID the id of the {@link Block}
	 * @return the {@link Block} or null, if there is no block with the given id
	 */
	public Block getBlock( String blockID ) {
		return this.blocks.get(blockID);
	}

	public Instant getCreationDate() {
		return this.workflowDetail.getCreationDate();
	}

	public void setCreationDate(Instant creationTime) {
		this.workflowDetail.setCreationDate(creationTime);
	}

	public Instant getLastModifiedDate() {
		return this.workflowDetail.getLastModifiedDate();
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.workflowDetail.setLastModifiedDate(lastModifiedDate);
	}

	public String getCreatedBy() {
		return this.workflowDetail.getCreatedBy();
	}

	public void setCreatedBy(String createdBy) {
		this.workflowDetail.setCreatedBy(createdBy);
	}

	public String getLastModifiedBy() {
		return this.workflowDetail.getLastModifiedBy();
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.workflowDetail.setLastModifiedBy(lastModifiedBy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WorkflowDetail getWrappedClass() {
		return this.workflowDetail;
	}

}

