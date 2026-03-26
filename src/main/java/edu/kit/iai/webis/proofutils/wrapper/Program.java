/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import edu.kit.iai.webis.proofmodels.AttachmentDetail;
import edu.kit.iai.webis.proofmodels.ProgramDetail;

/**
 * Wrapper class to provide a Program element stored in the configuration database
 */
public class Program implements IWrapper<ProgramDetail> {

	public enum Runtime {
	    /**
	     * Use development environment with common used python libraries.
	     */
	    PYTHON("python"),
	    /**
	     * Use development environment with common used java libraries.
	     */
	    JAVA("java -jar"),

	    /**
	     * Use development environment with common used matlab runtime.
	     */
	    MATLAB("");

	    /**
	     * Command to use in executor.
	     */
	    private final String command;

	    Runtime(String command) {
	        this.command = command;
	    }

		public String getCommand() {
			return this.command;
		}

		@Override
		public String toString() {
			return String.valueOf(this.command);
		}
	}

	private final ProgramDetail programDetail;
	private List<Attachment> attachments;
	private final Runtime runtime;
	private String entryPoint;

	/**
	 * create a Program instance based on a given database element {@link ProgramDetail}
	 * @param programDetail the database element
	 */
	public Program(ProgramDetail programDetail) {
		this.programDetail = programDetail;
		this.attachments = this.getAttachments();
		this.runtime = (this.programDetail.getRuntime() == null ? Runtime.PYTHON
						: EnumMapper.getRuntimeFor(this.programDetail.getRuntime()));
		this.entryPoint = this.programDetail.getEntryPoint();
	}

	public List<Attachment> getAttachments() {
		if (this.attachments == null) {
			this.attachments = new ArrayList<Attachment>();
			List<AttachmentDetail> aDList = this.programDetail.getAttachments();
			if (aDList != null) {
				aDList.forEach(a -> this.attachments.add(new Attachment(a)));
			}
		}
		return this.attachments;
	}

	public void addAttachment(Attachment attachment) {
		this.attachments.add(attachment);
		this.programDetail.addAttachmentsItem(attachment.getWrappedClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramDetail getWrappedClass() {
		return this.programDetail;
	}

	public String getDescription() {
		return this.programDetail.getDescription();
	}

	public String getId() {
		return this.programDetail.getId();
	}

	public Instant getCreationDate() {
		return this.programDetail.getCreationDate();
	}

	public void setCreationDate(Instant creationTime) {
		this.programDetail.setCreationDate(creationTime);
	}

	public Instant getLastModifiedDate() {
		return this.programDetail.getLastModifiedDate();
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.programDetail.setLastModifiedDate(lastModifiedDate);
	}

	public String getCreatedBy() {
		return this.programDetail.getCreatedBy();
	}

	public void setCreatedBy(String createdBy) {
		this.programDetail.setCreatedBy(createdBy);
	}

	public String getLastModifiedBy() {
		return this.programDetail.getLastModifiedBy();
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.programDetail.setLastModifiedBy(lastModifiedBy);
	}

	public Runtime getRuntime() {
		return this.runtime;
	}

	/**
	 * get the file name of the entry point (file name of the main attachment)
	 * @return the file name or <b>null</b>, if no entry point is given
	 */
	public String getEntryPointFileName() {
		for( Attachment attachment : this.attachments ) {
			if( attachment.getId().equals(this.entryPoint) ) {
				return attachment.getPath();
			}
		}
		return null;
	}


	public String getEntryPoint() {
		return this.entryPoint;
	}

}
