package com.threatdetection.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AnalyzeReport implements Comparable<AnalyzeReport>, Serializable {

	private static final long serialVersionUID = -1722426212026949667L;

	public static final int INFECTED = 0;
	public static final int SAFE = 1;



	private String ipAddress;
	private int threatsCount;
	private LocalDateTime lastEventTime;
	private String status;
	private String difference;

	public AnalyzeReport(String ipAddress, int threatsCount, LocalDateTime lastEventTime,
			String status, String difference) {
		super();
		this.setIpAddress(ipAddress);
		this.setThreatsCount(threatsCount);
		this.setLastEventTime(lastEventTime);
		this.setStatus(status);
		this.setDifference(difference);

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	@Override
	public int compareTo(AnalyzeReport analyzeReport) {
		if (this.getLastEventTime().isEqual(analyzeReport.getLastEventTime())) {
			return 0;
		} else if (this.getLastEventTime().isBefore(analyzeReport.getLastEventTime())) {
			return -1;
		} else {
			return 1;
		}
	}






}
