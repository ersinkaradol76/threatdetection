package com.threatdetection.model;

import java.time.LocalDateTime;

public class AnalyzeReport {

	public static final int INFECTED = 0;
	public static final int SAFE = 1;

	private String ipAddress;
	private int threatsCount;
	private LocalDateTime lastEventTime;
	private int status;
	private long differenceHours;
	private long differenceMinutes;

	public AnalyzeReport(String ipAddress, int threatsCount, LocalDateTime lastEventTime,
			int status, long differenceHours, long differenceMinutes) {
		super();
		this.setIpAddress(ipAddress);
		this.setThreatsCount(threatsCount);
		this.setLastEventTime(lastEventTime);
		this.setStatus(status);
		this.setDifferenceHours(differenceHours);
		this.setDifferenceMinutes(differenceMinutes);
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

	public LocalDateTime getLastEventTime() {
		return lastEventTime;
	}

	public void setLastEventTime(LocalDateTime lastEventTime) {
		this.lastEventTime = lastEventTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getDifferenceHours() {
		return differenceHours;
	}

	public void setDifferenceHours(long differenceHours) {
		this.differenceHours = differenceHours;
	}

	public long getDifferenceMinutes() {
		return differenceMinutes;
	}

	public void setDifferenceMinutes(long differenceMinutes) {
		this.differenceMinutes = differenceMinutes;
	}





}
