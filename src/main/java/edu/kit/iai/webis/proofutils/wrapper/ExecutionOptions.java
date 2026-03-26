/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import edu.kit.iai.webis.proofmodels.ExecutionsOptionsDetail;

/**
 * Wrapper class to provide an ExecutionOptions element stored in the configuration database
 */
public class ExecutionOptions implements IWrapper<ExecutionsOptionsDetail> {

	private ExecutionsOptionsDetail executionOptionsDetail;

	/**
	 * create an ExecutionOptions instance based on a given database element {@link ExecutionsOptionsDetail}
	 * @param executionOptionsDetail
	 */
	public ExecutionOptions(ExecutionsOptionsDetail executionOptionsDetail) {
		this.executionOptionsDetail = executionOptionsDetail;
	}

	@Override
	public ExecutionsOptionsDetail getWrappedClass() {
		return this.executionOptionsDetail;
	}

	public Boolean getOverride() {
		return this.executionOptionsDetail.getOverride();
	}

	public Boolean getManual() {
		return this.executionOptionsDetail.getManual();
	}

}
