/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

/**
 * Possible handling types for the data communication between worker and wrapper. 
 * Values may be {@link #FILE}, {@link #STDIO}, or {@link #SOCKET}
 */
public enum InterfaceType {
    /**
     * communication via a file
     */
    FILE,
    /**
     * communication via console 
     */
    STDIO,
    /**
     * communication via sockets 
     */
    SOCKET;
	
}