/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

import java.util.HashMap;
import java.util.Map;

public class SimpleStaticProfiler {

	private static Map<String, SimpleStaticProfiler> allProfilers = new HashMap<String, SimpleStaticProfiler>();

	private String name;
	private long startTime;
	private long stopTime;
	private long accuTime;

	private SimpleStaticProfiler(String name) {
		this.name = name;
	}

	public static SimpleStaticProfiler getInstance( String name) {
		SimpleStaticProfiler ssp = allProfilers.get(name);
		if( ssp == null ) {
			ssp = new SimpleStaticProfiler(name);
			System.out.println("SSP: not found for name " + name + ", created: " + ssp);
			allProfilers.put(name, ssp);
		}
		else {
			System.out.println("SSP: found for name >> " + ssp.getName() + " << :" + ssp+  "    (started at "+ ssp.getStartTime()+")");
		}
		System.out.println("existing Profilers:");
		allProfilers.values().forEach(p -> System.out.println(p + "\t"+ p.getName()));
		return ssp;
	}

	public String getName() {
		return this.name;
	}
	public long getStartTime() {
		return this.startTime;
	}

	public void start() {
		this.startTime = System.currentTimeMillis();
		System.out.println("SimpleStaticProfiler >> " + this.name + " << (re)started at " + this.startTime);
	}

	public void stop() {
		this.stopTime = System.currentTimeMillis();
		this.accuTime = this.stopTime - this.startTime;
	}

	public String summarize() {
		StringBuilder sb = new StringBuilder("Summarize for Profiler '");
		sb.append(this.name).append("':  time needed: ").append(this.accuTime);
		return sb.toString();
	}

	public static String finalResult() {
		StringBuilder sb = new StringBuilder("Final Result for all Profilers: \n ");
		 allProfilers.forEach((n,p) -> {
			 sb.append(n).append(":  time needed: ").append(p.accuTime);
		 });
		return sb.toString();
	}
}
