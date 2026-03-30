/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

/**
 * define the environment where the workflow should be processed.
 * Possible values are {@link #LOCAL}, {@link #DOCKER}, and {@link #KUBERNETES}.
 */
public enum ProcessEnvironment {
	/**
	 * local execution using pure Java jar files for orchestrator or worker or both.
	 * This option is intended for the development of blocks.
	 */
    LOCAL(String.valueOf("LOCAL")),

    /**
     * PROOF orchestrator and all workers are started as docker containers
     */
    DOCKER(String.valueOf("DOCKER")),

    /**
     * PROOF orchestrator and all workers are started as containers (pods) on a Kubernetes cluster
     */
    KUBERNETES(String.valueOf("KUBERNETES"));

    private String value;

    ProcessEnvironment(String value) {
      this.value = value;
    }

    public String getValue() {
    	return this.value;
    }

    public static ProcessEnvironment forName( String name ) {
    	for (ProcessEnvironment pe : ProcessEnvironment.values()) {
            if (pe.value.equalsIgnoreCase(name)) {
                return pe;
            }
        }
        return null;
    }
}
