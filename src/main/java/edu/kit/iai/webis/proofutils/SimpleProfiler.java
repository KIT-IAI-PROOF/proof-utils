/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils;

import java.util.ArrayList;
import java.util.List;

public class SimpleProfiler {

	private static List<SimpleProfiler> allProfilers = new ArrayList<SimpleProfiler>();
	private String name;

	private SimpleProfiler(String name) {
		this.name = name;
	}

	public static SimpleProfiler createNewInstance( String name) {
		SimpleProfiler sp = new SimpleProfiler(name);
		allProfilers.add(sp);
		return sp;
	}

	private List<TimeRecord> tsList = new ArrayList<SimpleProfiler.TimeRecord>();
	private TimeRecord currentTimeRecord;

	public void startProfiling() {
		if( this.currentTimeRecord == null ) {
			this.currentTimeRecord = new TimeRecord(System.currentTimeMillis());
			this.tsList.add(this.currentTimeRecord);
		}
		return;
	}


	private final static int ACCUMULATED = 0;
	private final static int AVERAGE = 0;

	public String finishProfiling() {
		this.currentTimeRecord.endTime = System.currentTimeMillis();
		String res = "Profiler '" + this.name + "' - time needed: "
		+ (this.currentTimeRecord.endTime - this.currentTimeRecord.startTime);
		this.currentTimeRecord = null;
		return res;
	}

	public static String summarize() {
		StringBuilder sb = new StringBuilder("Final Result for all Profilers: \n");
		allProfilers.forEach(s -> {
			final long[] results = new long[2];
			s.tsList.forEach(t -> {
				t.accuTime = t.endTime - t.startTime;
				results[ACCUMULATED]+= t.accuTime;
			});
			results[AVERAGE] = results[ACCUMULATED] / s.tsList.size();

			sb.append("Profiler '" + s.name + "' - Result:   accumulated time = " + results[ACCUMULATED] + ",  average time = " + results[AVERAGE]).append('\n');
		});
		return sb.toString();
	}

	public static class TimeRecord{
		public TimeRecord( long startTime ) {
			this.startTime = startTime;
		}
		public long startTime;
		public long endTime;
		public long accuTime;
	}


}
