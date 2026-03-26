/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//import edu.kit.iai.webis.ApiClient;
import edu.kit.iai.webis.proofapi.AttachmentControllerApi;
import edu.kit.iai.webis.proofapi.BlockControllerApi;
import edu.kit.iai.webis.proofapi.ExecutionControllerApi;
import edu.kit.iai.webis.proofapi.TemplateControllerApi;
import edu.kit.iai.webis.proofapi.ProgramControllerApi;
import edu.kit.iai.webis.proofapi.WorkflowControllerApi;
import edu.kit.iai.webis.proofmodels.AttachmentDetail;
import edu.kit.iai.webis.proofmodels.BlockDetail;
import edu.kit.iai.webis.proofmodels.TemplateDetail;
import edu.kit.iai.webis.proofmodels.ExecutionDetail;
import edu.kit.iai.webis.proofmodels.ProgramDetail;
import edu.kit.iai.webis.proofmodels.StepBasedConfigurationDetail;
import edu.kit.iai.webis.proofmodels.WorkflowDetail;
import edu.kit.iai.webis.proofutils.Colors;
import edu.kit.iai.webis.proofutils.CommonStringTemplates;
import edu.kit.iai.webis.proofutils.LoggingHelper;
import edu.kit.iai.webis.proofutils.exception.DBAccessException;
import edu.kit.iai.webis.proofutils.exception.ExecutionException;
import edu.kit.iai.webis.proofutils.model.SimulationStatus;
import edu.kit.iai.webis.proofutils.wrapper.Attachment;
import edu.kit.iai.webis.proofutils.wrapper.Block;
import edu.kit.iai.webis.proofutils.wrapper.Execution;
import edu.kit.iai.webis.proofutils.wrapper.Program;
import edu.kit.iai.webis.proofutils.wrapper.StepBasedConfiguration;
import edu.kit.iai.webis.proofutils.wrapper.Template;
import edu.kit.iai.webis.proofutils.wrapper.Workflow;

/**
 * A Wrapper class for the access to the proof configuration manager service
 */
@Component
public class ConfigManagerService {

	private final WorkflowControllerApi workflowControllerApi;
	private final BlockControllerApi blockControllerApi;
	private final TemplateControllerApi templateControllerApi;
	private final AttachmentControllerApi attachmentControllerApi;
	private final ProgramControllerApi programControllerApi;
	private final ExecutionControllerApi executionControllerApi;
    private List<StepBasedConfiguration> stepBasedConfigList;
	private Execution activeExecution;

	public ConfigManagerService(
            @Qualifier("workflowControllerApi") final WorkflowControllerApi workflowControllerApi,
            @Qualifier("blockControllerApi") final BlockControllerApi blockControllerApi,
            @Qualifier("templateControllerApi") final TemplateControllerApi templateControllerApi,
            @Qualifier("attachmentControllerApi") final AttachmentControllerApi attachmentControllerApi,
            @Qualifier("programControllerApi") ProgramControllerApi programControllerApi,
            @Qualifier("executionControllerApi") ExecutionControllerApi executionControllerApi)
	{
		this.workflowControllerApi = workflowControllerApi;
		this.blockControllerApi = blockControllerApi;
		this.templateControllerApi = templateControllerApi;
		this.attachmentControllerApi = attachmentControllerApi;
		this.programControllerApi = programControllerApi;
		this.executionControllerApi = executionControllerApi;
    }


    /**
     * get an execution instance from the database for a given id
     *
     * @param id the id
     * @return the execution with the given id
     * @throws RuntimeException, when there exists no execution instance for the given id, or when another error has occurred.
     */
    public Execution getExecution(final String id) throws DBAccessException {
    	try {
    		ExecutionDetail executionDetail =
    				this.executionControllerApi
    				.getExecution(id)
    				.block();
    		this.activeExecution = new Execution(executionDetail);
    		return this.activeExecution;
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("Execution", id, e.getMessage()));
    	}
    }

    /**
     * save the start information for a given execution id
     *
     * @param executionId the execution id
     * @throws RuntimeException, when there exists no execution instance for the given id, or when another error has occurred.
     */
    public void saveExecutionStart(final String executionId) throws DBAccessException, ExecutionException {
    	if( this.activeExecution != null && this.activeExecution.getId().equals(executionId) ) {
    		try {
    	    		ExecutionDetail executionDetail =
    	    				this.executionControllerApi
    	    				.getExecution(executionId)
    	    				.block();

        			this.activeExecution.setStatus(SimulationStatus.WAITING);
        			this.activeExecution.setStartedAt(Instant.now().toString());
					Integer startPoint = executionDetail.getWorkflow().getStepBasedConfig().getStartPoint();
					this.activeExecution.setCommunicationPoint(startPoint);

    			    this.executionControllerApi.updateExecution(executionId, this.activeExecution.getWrappedClass(), "0").block();

    		} catch (final Exception e) {
    			throw new DBAccessException(
    					CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("Execution", executionId, e.getMessage()), e);
    		}
    	}
    	else {
    		throw new ExecutionException(CommonStringTemplates.WRONG_EXECUTION_ID_EXPECTED_ID.formatted(executionId, this.activeExecution.getId()));
    	}
    }

    /**
     * save a given simulation status for a given execution id
     *
     * @param executionId the id
	 * @param status the status of the execution to be saved
     * @throws RuntimeException, when there exists no execution instance for the given id, or when another error has occurred.
     */
    public void saveExecutionStatus( final String executionId, SimulationStatus status ) throws DBAccessException, ExecutionException {
    	if( this.activeExecution != null && this.activeExecution.getId().equals(executionId) ) {
    		try {
    			this.activeExecution.setStatus(status);
    			if( status == SimulationStatus.STOPPED || status == SimulationStatus.SHUT_DOWN ) {
        			this.activeExecution.setStoppedAt(Instant.now().toString());
    			}

    			this.executionControllerApi.updateExecution(executionId, this.activeExecution.getWrappedClass(), "0" ).block();

    		} catch (final Exception e) {
    			throw new DBAccessException(
    					CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("Execution", executionId, e.getMessage()), e);
    		}
    	}
    	else {
    		throw new ExecutionException(CommonStringTemplates.WRONG_EXECUTION_ID_EXPECTED_ID.formatted(executionId, this.activeExecution.getId()));
    	}
    }

	/**
	 * save a given communication step for a given execution id
	 *
	 * @param executionId the id
	 * @param communicationPoint the communication point to be saved for the execution
	 * @throws RuntimeException, when there exists no execution instance for the given id, or when another error has occurred.
	 */
	public void saveCommunicationPoint( final String executionId, Integer communicationPoint ) throws DBAccessException, ExecutionException {
		if( this.activeExecution != null && this.activeExecution.getId().equals(executionId) ) {
			try {
				this.activeExecution.setCommunicationPoint(communicationPoint);
				this.executionControllerApi.updateExecution(executionId, this.activeExecution.getWrappedClass(), "0" ).block();

			} catch (final Exception e) {
				throw new DBAccessException(
					CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("Execution", executionId, e.getMessage()), e);
			}
		}
		else {
			throw new ExecutionException(CommonStringTemplates.WRONG_EXECUTION_ID_EXPECTED_ID.formatted(executionId, this.activeExecution.getId()));
		}
	}

	/**
	 * get a workflow instance from the database for a given id
	 *
	 * @param id the id
	 * @return the workflow with the given id
	 * @throws RuntimeException, when there exists no workflow instance for the given id, or when another error has
	 * occurred.
	 */
    public Workflow getWorkflow(final String id) throws DBAccessException {
        try {
        	WorkflowDetail workflowDetail =
        			this.workflowControllerApi
                    .getWorkflow(id)
                    .block();
        	return new Workflow(workflowDetail);
        } catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("Workflow", id, e.getMessage()));
        }
    }
    
    /**
     * get all workflows from the database
     *
     * @return the list of all workflow instances in the database
     * @throws RuntimeException, when there exists no workflow instance for the given id, or when another error has occurred.
     */
    public List<Workflow> getWorkflows() throws DBAccessException {
    	try {
            List<WorkflowDetail> workflowDetails = 
                    this.workflowControllerApi
                    .getWorkflows()
                    .collectList()
                    .block();
            return workflowDetails.stream()
                .map(Workflow::new)
                .collect(Collectors.toList());
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENTS.formatted("Workflows", e.getMessage()));
    	}
    }

    /**
     * get a StepBasedConfiguration from the database for a given id
     *
     * @param id the id
     * @return the StepBasedConfiguration with the given id
     * @throws RuntimeException, when there exists no StepBasedConfiguration for the given id, or when another error has occurred.
     */
    public StepBasedConfiguration getStepBasedConfiguration(final String id) throws DBAccessException {
    	try {
            // REFACTOR: this is a work-around as no stepBasedConfig-ControllerApi exists. This should be refactored as soon as such an API is available.
            if (stepBasedConfigList == null || !stepBasedConfigList.stream().anyMatch(sbc -> sbc.getId().equals(id))) {
                getStepBasedConfigurations();
            }
            return stepBasedConfigList.stream()
                .filter(sbc -> sbc.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DBAccessException(
                        CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("StepBasedConfiguration", id, "not found")));
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("StepBasedConfiguration", id, e.getMessage()));
    	}
    }
    
    /**
     * get all StepBasedConfigurations from the database
     *
     * @return the list of all StepBasedConfiguration instances in the database
     * @throws RuntimeException, when there exists no StepBasedConfiguration instance for the given id, or when another error has occurred.
     */
    public List<StepBasedConfiguration> getStepBasedConfigurations() throws DBAccessException {
    	try {
            // REFACTOR: this is a work-around as no stepBasedConfig-ControllerApi exists. This should be refactored as soon as such an API is available.
            // Loop over all workflows and extract step based configs and store in internal list.
			List<Workflow> workflows = getWorkflows();
            // Update the internal list and return the step based configs.
			stepBasedConfigList = workflows.stream()
					.filter(wf -> wf.getStepBasedConfig() != null)
					.map(Workflow::getStepBasedConfig)
					.collect(Collectors.toList());
            return stepBasedConfigList;
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENTS.formatted("StepBasedConfigurations", e.getMessage()));
    	}
    }
    
    /**
     * get a block from the database for a given id
     *
     * @param id the id
     * @return the block with the given id
     * @throws RuntimeException, when there exists no block for the given id, or when another error has occurred.
     */
    public Block getBlock(final String id) throws DBAccessException {
    	try {
    		BlockDetail blockDetail =
    				this.blockControllerApi
    				.getBlock(id)
    				.block();
    		return new Block(blockDetail);
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("Block", id, e.getMessage()));
    	}
    }

    /**
     * get all blocks from the database
     *
     * @return the list of all block instances in the database
     * @throws RuntimeException, when there exists no block instance for the given id, or when another error has occurred.
     */
    public List<Block> getBlocks() throws DBAccessException {
    	try {
            List<BlockDetail> blockDetails = 
                    this.blockControllerApi
                    .getBlocks()
                    .collectList()
                    .block();
            return blockDetails.stream()
                .map(Block::new)
                .collect(Collectors.toList());
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENTS.formatted("Blocks", e.getMessage()));
    	}
    }

    /**
     * get a template from the database for a given id
     *
     * @param id the id
     * @return the template with the given id
     * @throws RuntimeException, when there exists no template for the given id, or when another error has occurred.
     */
    public Template getTemplate(final String id) throws DBAccessException {
    	try {
    		TemplateDetail templateDetail =
    				this.templateControllerApi
    				.getTemplate(id)
    				.block();
    		return new Template(templateDetail);
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("Template", id, e.getMessage()));
    	}
    }

    /**
     * get all templates from the database
     *
     * @return the list of all template instances in the database
     * @throws RuntimeException, when there exists no template instance for the given id, or when another error has occurred.
     */
    public List<Template> getTemplates() throws DBAccessException {
    	try {
            List<TemplateDetail> templateDetails = 
                    this.templateControllerApi
                    .getTemplates()
                    .collectList()
                    .block();
            return templateDetails.stream()
                .map(Template::new)
                .collect(Collectors.toList());
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENTS.formatted("Templates", e.getMessage()));
    	}
    }

    /**
     * get a program instance from the database for a given id
     *
     * @param id the id
     * @return the program instance with the given id
     * @throws RuntimeException, when there exists no program instance for the given id, or when another error has occurred.
     */
    public Program getProgram(final String id) throws DBAccessException {
    	try {
    		ProgramDetail programDetail =
    				this.programControllerApi
    				.getProgram(id)
    				.block();
    		return new Program(programDetail);
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("Program", id, e.getMessage()));
    	}
    }

    /**
     * get all programs from the database
     *
     * @return the list of all program instances in the database
     * @throws RuntimeException, when there exists no program instance for the given id, or when another error has occurred.
     */
    public List<Program> getPrograms() throws DBAccessException {
    	try {
            List<ProgramDetail> programDetails = 
                    this.programControllerApi
                    .getPrograms()
                    .collectList()
                    .block();
            return programDetails.stream()
                .map(Program::new)
                .collect(Collectors.toList());
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENTS.formatted("Programs", e.getMessage()));
    	}
    }

    /**
     * get an attachment instance from the database for a given id
     *
     * @param id the id
     * @return the attachment instance with the given id
     * @throws RuntimeException, when there exists no attachment instance for the given id, or when another error has occurred.
     */
    public Attachment getAttachment(final String id) throws DBAccessException {
    	try {
    		AttachmentDetail attachmentDetail =
    				this.attachmentControllerApi
    				.getAttachment(id)
    				.block();
    		return new Attachment(attachmentDetail);
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENT_WITH_ID.formatted("Attachment", id, e.getMessage()));
    	}
    }

    /**
     * get all attachments from the database
     *
     * @return the list of all attachment instances in the database
     * @throws RuntimeException, when there exists no attachment instance for the given id, or when another error has occurred.
     */
    public List<Attachment> getAttachments() throws DBAccessException {
    	try {
            List<AttachmentDetail> attachmentDetails = 
                    this.attachmentControllerApi
                    .getAttachments()
                    .collectList()
                    .block();
            return attachmentDetails.stream()
                .map(Attachment::new)
                .collect(Collectors.toList());
    	} catch (final Exception e) {
            throw new DBAccessException(
            		CommonStringTemplates.COULD_NOT_RETRIEVE_ELEMENTS.formatted("Attachments", e.getMessage()));
    	}
    }


    /**
     * add a new workflow instance to the database
     *
     * @param workflow the workflow to be added
     * @throws DBAccessException when an error occurs while saving
     */
    public Workflow createWorkflow(final Workflow workflow) throws DBAccessException {
    	String workflowId = workflow.getId();
		LoggingHelper.debug().log("Trying to create workflow with id '%s'", workflowId);
		try {
            WorkflowDetail created = this.workflowControllerApi
                .createWorkflow(workflow.getWrappedClass(), "0")
                .block();
            return new Workflow(created);
    	} catch (final Exception e) {
    		throw new DBAccessException(
                CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("workflow",
                    workflowId != null ? workflowId : "new", e.getMessage()), e);
    	}
    }

    /**
     * update a workflow instance in the database
     *
     * @param workflow the workflow to be updated
     * @throws DBAccessException when an error occurs while updating
     */
    public Workflow updateWorkflow(final Workflow workflow) throws DBAccessException {
    	String workflowId = workflow.getId();
		LoggingHelper.debug().log("Trying to update workflow with id '%s'", workflowId);
        try {
            WorkflowDetail updated = this.workflowControllerApi
                    .updateWorkflow(workflowId, workflow.getWrappedClass(), "0")
                    .block();
            return new Workflow(updated);
        } catch (Exception e) {
            throw new DBAccessException(
                CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("workflow",
                    workflowId != null ? workflowId : "new", e.getMessage()), e);
        }
    }

    /**
     * add a new block instance to the database
     *
     * @param block the block to be added
     * @throws DBAccessException when an error occurs while saving
     */
    public Block createBlock(final Block block) throws DBAccessException {
    	String blockId = block.getId();
		LoggingHelper.debug().log("Trying to create block with id '%s'", blockId);
		try {
            BlockDetail created = this.blockControllerApi
                .createBlock(block.getWrappedClass(), "0")
                .block();
            return new Block(created);
    	} catch (final Exception e) {
    		throw new DBAccessException(
                CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("block",
                    blockId != null ? blockId : "new", e.getMessage()), e);
    	}
    }

    /**
     * update a block instance in the database
     *
     * @param block the block to be updated
     * @throws DBAccessException when an error occurs while updating
     */
    public Block updateBlock(final Block block) throws DBAccessException {
    	String blockId = block.getId();
		LoggingHelper.debug().log("Trying to update block with id '%s'", blockId);
        try {
            BlockDetail updated = this.blockControllerApi
                    .updateBlock(blockId, block.getWrappedClass(), "0")
                    .block();
            return new Block(updated);
        } catch (Exception e) {
            throw new DBAccessException(
                CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("block",
                    blockId != null ? blockId : "new", e.getMessage()), e);
        }
    }

    /**
     * add a new template instance to the database
     *
     * @param template the template to be added
     * @throws DBAccessException when an error occurs while saving
     */
    public Template createTemplate(final Template template) throws DBAccessException {
    	String templateId = template.getId();
		LoggingHelper.debug().log("Trying to create template with id '%s'", templateId);
		try {
            TemplateDetail created = this.templateControllerApi
                .createTemplate(template.getWrappedClass(), "0")
                .block();
            return new Template(created);
    	} catch (final Exception e) {
    		throw new DBAccessException(
                CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("template",
                    templateId != null ? templateId : "new", e.getMessage()), e);
    	}
    }

    /**
     * update a template instance in the database
     *
     * @param template the template to be updated
     * @throws DBAccessException when an error occurs while updating
     */
    public Template updateTemplate(final Template template) throws DBAccessException {
    	String templateId = template.getId();
		LoggingHelper.debug().log("Trying to update template with id '%s'", templateId);
        try {
            TemplateDetail updated = this.templateControllerApi
                    .updateTemplate(templateId, template.getWrappedClass(), "0")
                    .block();
            return new Template(updated);
        } catch (Exception e) {
            throw new DBAccessException(
                CommonStringTemplates.COULD_NOT_SAVE_ELEMENT_WITH_ID.formatted("template",
                    templateId != null ? templateId : "new", e.getMessage()), e);
        }
    }

}
