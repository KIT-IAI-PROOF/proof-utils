/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

/**
 * Colors for printing colored output.<br>
 * <br>Possible values:<br>
 * ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE,
 * and their BOLD versions as well as ANSI_RESET to reset the colors to standard color.
 */
public enum Colors {

	ANSI_RESET("\u001B[0m"),
	ANSI_BLACK("\u001B[30m"),
	ANSI_RED("\u001B[31m"),
	ANSI_GREEN("\u001B[32m"),
	ANSI_YELLOW("\u001B[33m"),
	ANSI_BLUE("\u001B[34m"),
	ANSI_PURPLE("\u001B[35m"),
	ANSI_CYAN("\u001B[36m"),
	ANSI_WHITE("\u001B[37m"),

	ANSI_BLACK_BOLD("\u001B[30;1m"),
	ANSI_RED_BOLD("\u001B[31;1m"),
	ANSI_GREEN_BOLD("\u001B[32;1m"),
	ANSI_YELLOW_BOLD("\u001B[33;1m"),
	ANSI_BLUE_BOLD("\u001B[34;1m"),
	ANSI_PURPLE_BOLD("\u001B[35;1m"),
	ANSI_CYAN_BOLD("\u001B[36;1m"),
	ANSI_WHITE_BOLD("\u001B[37;1m");

	private String ansiStr;

	Colors( String colStr ) {
		this.ansiStr = colStr;
	}

	public String value() {
		return this.ansiStr;
	}
}
