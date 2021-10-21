package com.threatdetection.model;

import java.time.LocalDateTime;

public class ThreatResult {

	public static final int INFECTED = 0;
	public static final int SAFE = 1;

	private String ipAddress;
	private int threatsCount;
	private LocalDateTime firtEventTime;
	private LocalDateTime lastEventTime;
	private int lastStatus;

	public ThreatResult(String ipAddress, int threatsCount, LocalDateTime firstEventTime, LocalDateTime lastEventTime,
			int lastStatus) {
		super();
		this.setIpAddress(ipAddress);
		this.setThreatsCount(threatsCount);
		this.setFirtEventTime(firstEventTime);
		this.setLastEventTime(lastEventTime);
		this.setLastStatus(lastStatus);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getThreatsCount() {
		return threatsCount;
	}

	public void setThreatsCount(int threatsCount) {
		this.threatsCount = threatsCount;
	}

	public LocalDateTime getFirtEventTime() {
		return firtEventTime;
	}

	public void setFirtEventTime(LocalDateTime firtEventTime) {
		this.firtEventTime = firtEventTime;
	}

	public LocalDateTime getLastEventTime() {
		return lastEventTime;
	}

	public void setLastEventTime(LocalDateTime lastEventTime) {
		this.lastEventTime = lastEventTime;
	}

	public int getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(int lastStatus) {
		this.lastStatus = lastStatus;
	}

}
