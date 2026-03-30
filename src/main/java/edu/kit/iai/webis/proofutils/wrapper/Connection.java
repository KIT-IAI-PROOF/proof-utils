/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import edu.kit.iai.webis.proofmodels.ConnectionDetail;

/**
 * Wrapper class to provide a Connection element stored in the configuration database
 */
public class Connection implements IWrapper<ConnectionDetail> {

	private ConnectionDetail connectionDetail;

	/**
	 * create a input instance based on a given database element {@link ConnectionDetail} 
	 * @param connectionDetail the database element
	 */
	public Connection(ConnectionDetail connectionDetail) {
		this.connectionDetail = connectionDetail;
	}
	
	@Override
	public ConnectionDetail getWrappedClass() {
		return this.connectionDetail;
	}

	public String getSourceBlockID() {
		return this.connectionDetail.getSource();
	}
	
	public String getTargetBlockID() {
		return this.connectionDetail.getTarget();
	}
	
	/**
	 * get the name of the source input
	 * @return the name of the source input
	 */
	public String getInputID() {
		return this.connectionDetail.getInput();
	}
	
	/**
	 * get the name of the target output
	 * @return the name of the target output
	 */
	public String getOutputID() {
		return this.connectionDetail.getOutput();
	}

}
