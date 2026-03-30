/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

public class CommonStringTemplates {

	public static final String PHASE_KEY = "phase";
    public static final String TYPE_KEY = "type";
    public static final String DATA_KEY = "data";
    public static final String STATUS_KEY = "status";
    public static final String WORKFLOW_PHASE_ID_KEY = "phaseId";

    public static final String IN_TEMPLATE = "%s_%s_IN_%s";
    public static final String INPORT_TEMPLATE = "%s_%s_%s_IN_%s";
    public static final String OUT_TEMPLATE = "%s_%s_OUT_%s";
    public static final String OUTPORT_TEMPLATE = "%s_%s_%s_OUT_%s";
    public static final String SINGLE_TEMPLATE = "[%s%s%s]";
    public static final String SINGLE_BLUE_TEMPLATE = "[\\u001B[34m%s\\u001B[0m]";
    public static final String JSON = "json";
    public static final String STRING = "string";
    public static final String NUMBER = "number";

    public final static String TYPE_JSON_VALUE = "json";
    public final static String TYPE_STRING_VALUE = "string";
    public final static String TYPE_FLOAT_VALUE = "float";
    public final static String TYPE_INTEGER_VALUE = "integer";
    public final static String TYPE_OBJECT_VALUE = "object";
    public final static String TYPE_STRING_ARRAY = "stringarray";
    public final static String TYPE_FLOAT_ARRAY = "floatarray";
    public final static String TYPE_INTEGER_ARRAY = "integerarray";
    public final static String TYPE_OBJECT_ARRAY = "objectarray";
    public final static String TYPE_FILE_NAME_VALUE = "filename";

    public final static String NOTIFY_QUEUE_NAME_TEMPLATE = "NOTIFY_%s_%s_%d";
    public final static String SYNC_QUEUE_NAME_TEMPLATE = "SYNC_%s_%s_%d";
    public final static String STATIC_INPUTS_QUEUE_NAME_TEMPLATE = "STATIC_INPUTS_%s_%s_%d";
    public final static String FILE_IO_NAME_TEMPLATE = "FILEIO_%s_%s_%d";

    public final static String COULD_NOT_RETRIEVE_ELEMENTS = "could not retrieve the list of all %s. Reason: %s";
    public final static String COULD_NOT_RETRIEVE_ELEMENT_WITH_ID = "could not retrieve the %s with id '%s'. Reason: %s";
    public final static String COULD_NOT_SAVE_ELEMENT_WITH_ID = "could not save the %s with id '%s'. Reason: %s";
    public final static String WRONG_EXECUTION_ID_EXPECTED_ID = "wrong execution id '%s' (expected id: %s)!";

    public static final String EXECUTION_ID_PLACEHOLDER = "%EXEC_ID";

}
