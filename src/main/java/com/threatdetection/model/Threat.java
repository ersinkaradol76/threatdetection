package com.threatdetection.model;

import java.time.LocalDateTime;

public class Threat {

	public static final int RESOLVED = 0;
	public static final int INFECTED_IP = 1;

	public static final int FALSE = 0;
	public static final int TRUE = 1;

	private int threatId;
	private String ipAddress;
	private LocalDateTime creationTime;
	private int description;
	private int severity;
	private int status;
	private String owner;
	private int lineNumber;

	public Threat(int threatId, String ipAddress, LocalDateTime creationTime, int description, int severity, int status,
			String owner, int lineNumber) {
		super();
		this.setThreatId(threatId);
		this.setIpAddress(ipAddress);
		this.setCreationTime(creationTime);
		this.setDescription(description);
		this.setSeverity(severity);
		this.setStatus(status);
		this.setOwner(owner);
		this.setLineNumber(lineNumber);
	}

	public int getThreatId() {
		return threatId;
	}

	public void setThreatId(int threatId) {
		this.threatId = threatId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public int getDescription() {
		return description;
	}

	public void setDescription(int description) {
		this.description = description;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
