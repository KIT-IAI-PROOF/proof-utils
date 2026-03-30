/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.wrapper;

import java.time.Instant;

import edu.kit.iai.webis.proofmodels.AttachmentDetail;

public class Attachment implements IWrapper<AttachmentDetail> {

	private AttachmentDetail attachmentDetail;

	public Attachment(AttachmentDetail attachmentDetail) {
		this.attachmentDetail = attachmentDetail;
	}

	@Override
	public AttachmentDetail getWrappedClass() {	return this.attachmentDetail; }

	public String getDescription() {
		return this.attachmentDetail.getDescription();
	}

	public String getId() {
		return this.attachmentDetail.getId();
	}

	public void setPath(String path) {
		this.attachmentDetail.setPath(path);
	}

	public String getPath() {
		return this.attachmentDetail.getPath();
	}

	public Instant getCreationDate() {
		return this.attachmentDetail.getCreationDate();
	}

	public void setCreationDate(Instant creationTime) {
		this.attachmentDetail.setCreationDate(creationTime);
	}

	public Instant getLastModifiedDate() {
		return this.attachmentDetail.getLastModifiedDate();
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.attachmentDetail.setLastModifiedDate(lastModifiedDate);
	}

	public String getCreatedBy() {
		return this.attachmentDetail.getCreatedBy();
	}

	public void setCreatedBy(String createdBy) {
		this.attachmentDetail.setCreatedBy(createdBy);
	}

	public String getLastModifiedBy() {
		return this.attachmentDetail.getLastModifiedBy();
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.attachmentDetail.setLastModifiedBy(lastModifiedBy);
	}

}
