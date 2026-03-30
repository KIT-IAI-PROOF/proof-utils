/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

public interface IWrapper <T> {

	/**
	 * get the wrapped class the instance is based on
	 * @return the wrapped class
	 */
	public T getWrappedClass();
}
