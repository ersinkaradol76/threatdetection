package com.threatdetection.model;

/**
 * 
 * Class for holding the data in a line of csv file
 *
 */

public class FileLineInfo {

	private String[] fields;
	private int lineNumber;

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public FileLineInfo(String[] fields, int lineNumber) {
		super();
		this.setFields(fields);
		this.setLineNumber(lineNumber);
	}

}
