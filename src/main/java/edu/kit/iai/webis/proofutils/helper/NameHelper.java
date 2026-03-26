/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.helper;

import java.io.File;

import edu.kit.iai.webis.proofutils.CommonStringTemplates;
import edu.kit.iai.webis.proofutils.wrapper.Block;


public class NameHelper {

	/**
	 * Return service queue name of static inputs queue for a given block
	 * 
	 * @param executionID the execution id
	 * @param block the given block
	 * @return static inputs queue name
	 */
	public static String getStaticInputsQueueName(String executionID, Block block) {
        return CommonStringTemplates.STATIC_INPUTS_QUEUE_NAME_TEMPLATE.formatted(
                executionID, block.getId(), block.getIndex());
    }

	/**
	 * Return service queue name of sync queue for given block id and block index
	 * 
	 * @param executionID the execution id
	 * @param blockID the id of the block
	 * @param index
	 * @return sync queue name
	 */
    public static String getSyncQueueName(String executionID, String blockID, Integer index) {
        return CommonStringTemplates.SYNC_QUEUE_NAME_TEMPLATE.formatted(
                executionID, blockID, index);

    }

    /**
     * Return service queue name of sync queue for a given block
     * 
     * @param executionID the execution id
     * @param block the given block
     * @return the sync queue name
     */
    public static String getSyncQueueName(String executionID, Block block) {
        return getSyncQueueName(executionID, block.getId(), block.getIndex());
    }

    /**
     * Return service queue name of notify queue for a given block
     *
     * @param executionID the execution id
     * @param block the given block
     * @return the notify queue name
     */
    public static String getNotifyQueueName(String executionID, String blockID, Integer index) {
    	return CommonStringTemplates.NOTIFY_QUEUE_NAME_TEMPLATE.formatted(
    			executionID, blockID, index);
    }
    
    public static String getNotifyQueueName(String executionID, Block block) {
    	return getNotifyQueueName(executionID, block.getId(), block.getIndex());
    }
    
    public static String getInputQueueName(String executionID, String workflowID, Integer blockIndex, String portName) {
    	return String.format( CommonStringTemplates.INPORT_TEMPLATE, executionID, workflowID, blockIndex, portName);
    }
    
    public static String getOutputQueueName(String executionID, String workflowID, Integer blockIndex, String portName) {
    	return String.format( CommonStringTemplates.OUTPORT_TEMPLATE, executionID, workflowID, blockIndex, portName);
    }
    
    public static String getIOPath( String workspacePath, String executionID, Block block ) {
    	return workspacePath + File.separator +
    			CommonStringTemplates.FILE_IO_NAME_TEMPLATE.formatted( executionID, block.getId(), block.getIndex() );
    }
}
