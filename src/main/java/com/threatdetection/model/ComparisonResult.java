package com.threatdetection.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * Class for holding all the required data after the comparison of the two csv files
 *
 * transaction1Map 		: all the valid data of the first csv file
 * lineCountOfFile1		: number of the valid data of the first csv file
 * lineTotalCountOfFile1: number of the total data of the first csv file
 * transaction2Map 		: all the valid data of the second csv file
 * lineCountOfFile2		: number of the valid data of the second csv file
 * lineTotalCountOfFile2: number of the total data of the second csv file
 * errorData1List		: all the invalid data of the first csv file
 * errorData2List		: all the invalid data of the second csv file
 * repeatedData1List	: all the repeated data of the first csv file
 * repeatedData2List	: all the repeated data of the second csv file
 * perfectMatchedList	: all the data that all the fields of two files match
 * candidates1List		: all the selected as a candidate in the first file for matching with a line in the second file
 * candidates2List		: all the selected as a candidate in the second file for matching with a line in the first file
 * nonMatched1List		: all the data that are not selected as a candidate in the first file
 * nonMatched2List		: all the data that are not selected as a candidate in the second file
 *
 */
public class ComparisonResult implements Serializable {

	private static final long serialVersionUID = -1455553459281240385L;

	private List<Integer> errorData1List;
	private List<Integer> errorData2List;
	private transient List<Threat> repeatedData1List;
	private transient List<Threat> repeatedData2List;
	private transient Map<String, Threat> transaction1Map;
	private int lineCountOfFile1;
	private int lineTotalCountOfFile1;
	private transient Map<String, Threat> transaction2Map;
	private int lineCountOfFile2;
	private int lineTotalCountOfFile2;

	private transient List<Threat> perfectMatchedList;
	private transient List<Threat> nonMatched1List;
	private transient List<Threat> nonMatched2List;
	private transient List<Threat> candidates1List;
	private transient List<Threat> candidates2List;

	public List<Integer> getErrorData1List() {
		return errorData1List;
	}
	public void setErrorData1List(List<Integer> errorData1List) {
		this.errorData1List = errorData1List;
	}
	public List<Integer> getErrorData2List() {
		return errorData2List;
	}
	public void setErrorData2List(List<Integer> errorData2List) {
		this.errorData2List = errorData2List;
	}
	public List<Threat> getRepeatedData1List() {
		return repeatedData1List;
	}
	public void setRepeatedData1List(List<Threat> repeatedData1List) {
		this.repeatedData1List = repeatedData1List;
	}
	public List<Threat> getRepeatedData2List() {
		return repeatedData2List;
	}
	public void setRepeatedData2List(List<Threat> repeatedData2List) {
		this.repeatedData2List = repeatedData2List;
	}

	public Map<String, Threat> getTransaction1Map() {
		return transaction1Map;
	}
	public void setTransaction1Map(Map<String, Threat> transaction1Map) {
		this.transaction1Map = transaction1Map;
	}
	public Map<String, Threat> getTransaction2Map() {
		return transaction2Map;
	}
	public void setTransaction2Map(Map<String, Threat> transaction2Map) {
		this.transaction2Map = transaction2Map;
	}
	public List<Threat> getPerfectMatchedList() {
		return perfectMatchedList;
	}
	public void setPerfectMatchedList(List<Threat> perfectMatchedList) {
		this.perfectMatchedList = perfectMatchedList;
	}
	public List<Threat> getNonMatched1List() {
		return nonMatched1List;
	}
	public void setNonMatched1List(List<Threat> nonMatched1List) {
		this.nonMatched1List = nonMatched1List;
	}
	public List<Threat> getNonMatched2List() {
		return nonMatched2List;
	}
	public void setNonMatched2List(List<Threat> nonMatched2List) {
		this.nonMatched2List = nonMatched2List;
	}
	public List<Threat> getCandidates1List() {
		return candidates1List;
	}
	public void setCandidates1List(List<Threat> candidates1List) {
		this.candidates1List = candidates1List;
	}
	public List<Threat> getCandidates2List() {
		return candidates2List;
	}
	public void setCandidates2List(List<Threat> candidates2List) {
		this.candidates2List = candidates2List;
	}


	public int getLineCountOfFile1() {
		return lineCountOfFile1;
	}
	public void setLineCountOfFile1(int lineCountOfFile1) {
		this.lineCountOfFile1 = lineCountOfFile1;
	}
	public int getLineCountOfFile2() {
		return lineCountOfFile2;
	}
	public void setLineCountOfFile2(int lineCountOfFile2) {
		this.lineCountOfFile2 = lineCountOfFile2;
	}



	public int getLineTotalCountOfFile1() {
		return lineTotalCountOfFile1;
	}
	public void setLineTotalCountOfFile1(int lineTotalCountOfFile1) {
		this.lineTotalCountOfFile1 = lineTotalCountOfFile1;
	}
	public int getLineTotalCountOfFile2() {
		return lineTotalCountOfFile2;
	}
	public void setLineTotalCountOfFile2(int lineTotalCountOfFile2) {
		this.lineTotalCountOfFile2 = lineTotalCountOfFile2;
	}

	public ComparisonResult() {
		super();
		this.errorData1List = new ArrayList<>();
		this.errorData2List = new ArrayList<>();
		this.repeatedData1List = new ArrayList<>();
		this.repeatedData2List = new ArrayList<>();
		this.transaction1Map = new HashMap<>();
		this.transaction2Map = new HashMap<>();
		this.perfectMatchedList = new ArrayList<>();
		this.nonMatched1List = new ArrayList<>();
		this.nonMatched2List = new ArrayList<>();
		this.candidates1List = new ArrayList<>();
		this.candidates2List = new ArrayList<>();
		this.setLineCountOfFile1(0);
		this.setLineCountOfFile2(0);
		this.setLineTotalCountOfFile1(0);
		this.setLineTotalCountOfFile2(0);

	}


}
