/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * a very simple adapter service to provide properties defined in file applicaion.properties
 * <b>Note: </b> to autowire and integrate the service, a "deep component scan" must be performed in the main app by using the following class annotation:
 * <code>@ComponentScan(basePackages = { ... , "edu.kit.iai.webis.proofutils", ...  })</code> 
 */
@Service 
public class ConfigPropertyService {

	@Autowired
	private Environment environment;

	/**
	 * get a property with a given name that is defined in file applicaion.properties
	 * @param key the name (key) of the desired property 
	 * @return
	 */
	public String getProperty( String key ) {
		return this.environment.getProperty( key );
	}
}
