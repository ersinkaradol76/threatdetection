package com.threatdetection.model;

import java.time.LocalDateTime;

public class Threat {

	public static final int INFECTED_IP = 1;
	public static final int RESOLVED = 0;

	private int threatId;
	private String ipAddress;
	private LocalDateTime creationTime;
	private int description;
	private int severity;
	private boolean status;
	private String owner;
	
	
	public Threat(int threatId, String ipAddress, LocalDateTime creationTime, int description, int severity,
			boolean status, String owner) {
		super();
		this.threatId = threatId;
		this.ipAddress = ipAddress;
		this.creationTime = creationTime;
		this.description = description;
		this.severity = severity;
		this.status = status;
		this.owner = owner;
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


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public static int getInfectedIp() {
		return INFECTED_IP;
	}


	public static int getResolved() {
		return RESOLVED;
	}
	
	
	
	
	
	


}
