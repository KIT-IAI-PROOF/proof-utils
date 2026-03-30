/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.model;

/**
 * Type of communication for {@link IOElement}s.
 * Communication types are used to define the type of communication between two blocks.
 * A CommunicationType is dependent on the {@link CommunicationParadigm}.
 * Values may be {@link STEPBASED}, {@link EVENT}, {@link EVENT_STATIC} or {@link STEPBASED_STATIC}
 */
@SuppressWarnings("unused")
public enum CommunicationType {
    /**
     * See {@link CommunicationParadigm#STEPBASED}
     */
    STEPBASED,
    /**
     * See {@link CommunicationParadigm#EVENT}
     */
    EVENT,
    /**
     * Used for initial static value setting.
     * See {@link CommunicationParadigm#EVENT}
     */
    EVENT_STATIC,
    /**
     * Used for initial static value setting.
     * See {@link CommunicationParadigm#STEPBASED}
     */
    STEPBASED_STATIC
}
