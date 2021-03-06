package com.threatdetection.service;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.threatdetection.exceptions.DataDoesNotSuitSpecsException;
import com.threatdetection.model.AnalyzeReport;
import com.threatdetection.model.FileLineInfo;
import com.threatdetection.model.Threat;
import com.threatdetection.model.ThreatResult;
import com.threatdetection.model.ValidatedData;
import com.threatdetection.util.FileUtil;

@Service
public class ThreatService {

	private static final int FIELD_COUNT = 7;

	private static final int FIELD_THREAT_ID_MIN_LENGTH = 0;
	private static final int FIELD_SEVERITY_MIN_VALUE = 0;
	private static final int FIELD_SEVERITY_MAX_VALUE = 2;
	private static final int FIELD_OWNER_MAX_LENGTH = 256;

	private static final String[] FIELD_DESCRIPTION_VALUES = { "Resolved", "Infected IP" };
	private static final String[] FIELD_STATUS_VALUES = { "False", "True" };
	private static final String[] FIELD_REPORT_STATUS_VALUES = { "Infected", "Safe" };

	private static final int FIELD_NO_THREAT_ID = 0;
	private static final int FIELD_NO_IP_ADDRESS = 1;
	private static final int FIELD_NO_CREATION_TIME = 2;
	private static final int FIELD_NO_DESCRIPTION = 3;
	private static final int FIELD_NO_SEVERITY = 4;
	private static final int FIELD_NO_STATUS = 5;
	private static final int FIELD_NO_OWNER = 6;

	public static final int MINUTES_IN_HOUR = 60;

	private static final String MINUTES_STRING = " minutes";

	private static final String ERROR_MESSAGE = "Error in the field format ";

	private static final Logger logger = LoggerFactory.getLogger(ThreatService.class);

	/**
	 *
	 * This method validates the fields of the line, if data has no error creates a
	 * Threat object and returns it, if data has an error in the fields throws
	 * exception
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @return returns the data created from the fields of the line as Threat object
	 * @throws DataDoesNotSuitSpecsException
	 */

	public Threat createThreat (String[] fields, int lineNumber) throws DataDoesNotSuitSpecsException {

		try {
			ValidatedData validatedData = validateFields(fields, lineNumber);
			return new Threat(validatedData.getThreatId(), validatedData.getIpAddress(), validatedData.getCreationTime(),
					validatedData.getDescription(), validatedData.getSeverity(),
					validatedData.getStatus(), validatedData.getOwner(), lineNumber);


		} catch (DataDoesNotSuitSpecsException e) {
			throw new DataDoesNotSuitSpecsException(e.getMessage());
		}

	}

	/**
	 *
	 * This method validates the fields of the line
	 * If there is an error in a field, throws exception
	 *
	 * @param fields 		the fields in a line of csv file
	 * @param lineNumber	line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private ValidatedData validateFields(String[] fields, int lineNumber) throws DataDoesNotSuitSpecsException {
		try {
			ValidatedData validatedData = new ValidatedData();
			validateFieldCount(fields, lineNumber);

			validatedData.setThreatId(validateThreatIdField(fields[FIELD_NO_THREAT_ID], lineNumber));
			validatedData.setIpAddress(validateIpAddressField(fields[FIELD_NO_IP_ADDRESS], lineNumber));
			validatedData.setCreationTime(validateCreationTimeField(fields[FIELD_NO_CREATION_TIME], lineNumber));
			validatedData.setDescription(validateDescriptionField(fields[FIELD_NO_DESCRIPTION], lineNumber));
			validatedData.setSeverity(validateSeverityField(fields[FIELD_NO_SEVERITY], lineNumber));
			validatedData.setStatus(validateStatusField(fields[FIELD_NO_STATUS], lineNumber));
			validatedData.setOwner(validateOwnerField(fields[FIELD_NO_OWNER], lineNumber));
			return validatedData;
		}catch (DataDoesNotSuitSpecsException e) {
			throw new DataDoesNotSuitSpecsException(e.getMessage());

		}
	}

	/**
	 *
	 * This method validates the count of the fields,
	 * If there are exactly the same number that is expected, throws exception
	 *
	 * @param fields 		the fields in a line of csv file
	 * @param lineNumber	line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private void validateFieldCount(String[] fields, int lineNumber) throws DataDoesNotSuitSpecsException {
		if (fields.length != FIELD_COUNT) {
			throw new DataDoesNotSuitSpecsException("The line " + lineNumber + " does not ensure the required number of fields.");
		}
	}

	/**
	 *
	 * This method validates the Threat Id field, If it is a numeric value returns
	 * the string field, throws exception If it is not
	 *
	 * @param field      the Threat Id field in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private int validateThreatIdField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		try {
			int threatId = Integer.parseInt(field);
			if (threatId > FIELD_THREAT_ID_MIN_LENGTH) {
				return threatId;
			} else {
				throw new DataDoesNotSuitSpecsException(
						ERROR_MESSAGE + "Field: Threat Id --- Line Number: " + lineNumber);
			}
		} catch (NumberFormatException e) {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Threat Id --- Line Number: " + lineNumber);
		}
	}

	/**
	 *
	 * This method validates the Ip Address field, If it is not in the required ip
	 * address format, throws exception If it is in the required format, returns the
	 * data string
	 *
	 * @param field      the Ip Address field in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private String validateIpAddressField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {

	        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
	        Matcher mtch = ptn.matcher(field);
	        if (mtch.find()) {
	        	return field.trim();
	        } else {
	        	throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Ip Address --- Line Number: " + lineNumber);
	        }
	}


	/**
	 *
	 * This method validates the Creation Time field, If it is not in the required
	 * date format, throws exception If it is in the required date format, returns
	 * the data that is converted to LocalDateTime
	 *
	 * @param field      the Creation Time field in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private LocalDateTime validateCreationTimeField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
			return LocalDateTime.parse(field, formatter);
		} catch (DateTimeParseException e) {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Creation Time --- Line Number: " + lineNumber);
		}
	}

	/**
	 *
	 * This method validates the Description field, If it is equals to "Resolved",
	 * "Infected IP" returns the int value of it, if it is not throws exception
	 *
	 * @param field      the Description field in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private int validateDescriptionField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		if (field != null) {
			for (int i = 0; i < FIELD_DESCRIPTION_VALUES.length; i++) {
				if (field.equals(FIELD_DESCRIPTION_VALUES[i])) {
					return i;
				}
			}
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Description --- Line Number: " + lineNumber);
		} else {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Description --- Line Number: " + lineNumber);
		}

	}

	/**
	 *
	 * This method validates the Severity Description field, If it is a numeric
	 * value returns the string field, throws exception If it is not
	 *
	 *
	 * @param field      the Severity field in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private int validateSeverityField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		try {
			int severityField = Integer.parseInt(field);
			if (severityField >= FIELD_SEVERITY_MIN_VALUE && severityField <= FIELD_SEVERITY_MAX_VALUE) {
				return severityField;
			} else {
				throw new DataDoesNotSuitSpecsException(
						ERROR_MESSAGE + "Field: Severity --- Line Number: " + lineNumber);
			}
		} catch (NumberFormatException e) {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Severity --- Line Number: " + lineNumber);
		}

	}

	/**
	 *
	 * This method validates the Status field, If it is equals to "False", "True"
	 * returns the int value of it, if it is not throws exception
	 *
	 *
	 * @param field      the Status field in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private int validateStatusField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		if (field != null) {
			for (int i = 0; i < FIELD_STATUS_VALUES.length; i++) {
				if (field.equals(FIELD_STATUS_VALUES[i])) {
					return i;
				}
			}
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Status --- Line Number: " + lineNumber);
		} else {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Status --- Line Number: " + lineNumber);
		}

	}


	/**
	 *
	 * This method validates the Owner field, If it is lenght is less than 256
	 * returns the string field, throws exception If it is not
	 *
	 * @param field      the Owner field in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private String validateOwnerField(String field, int lineNumber) throws DataDoesNotSuitSpecsException {
		if (field != null && field.trim().length() <= FIELD_OWNER_MAX_LENGTH) {
			return field.trim();
		} else {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Owner --- Line Number: " + lineNumber);
		}

	}


	/**
	 *
	 *
	 * This method reads the csv file line by line and forms a list of FileLineInfo
	 * that holds the fields of the line, For all the FileLineInfo object in the
	 * list creates a Threat object if the data is valid, Stores the Threat object
	 * in a HashMap that the key is Ip Address and the value is the Threat Object.
	 * If the line has a field that is invalid, logs the line number
	 *
	 * @param csvFilePath file path of the csv file
	 * @return returns a HashMap that the key is the Ip Address and and the value is
	 *         the Threat Object.
	 *
	 * @throws FileUploadException
	 * @throws FileNotFoundException
	 */
	private Map<String, ThreatResult> formThreatDataMap(String csvFilePath) throws FileUploadException {

		Map<String, ThreatResult> threatResultMap = new HashMap<>();

		List<FileLineInfo> fileLineInfoList = FileUtil.readCsvFileToStringList(csvFilePath);
		for (int i = 0; i < fileLineInfoList.size(); i++) {
			FileLineInfo fileLineInfo = fileLineInfoList.get(i);
			String[] fields = fileLineInfo.getFields();
			Threat threat = null;
			try {
				threat = createThreat(fields, fileLineInfo.getLineNumber());

			} catch (DataDoesNotSuitSpecsException e) {
				logger.error(e.getMessage());
			}
			if (threat != null) {
				checkThreatMap(threatResultMap, threat);
			}
		}

		return threatResultMap;

	}

	/**
	 * The method takes Threat object and the HashMap that the key is Ip Address and
	 * the value is the Threat Object, cheks if the threat object is in the map, if
	 * it not puts in it, if it in the map updates the data
	 *
	 *
	 * @param threatResultMap
	 * @param threat
	 */
	private void checkThreatMap(Map<String, ThreatResult> threatResultMap, Threat threat) {
		String key = threat.getIpAddress();
		if (threatResultMap.get(key) != null) {
			ThreatResult threatResult = threatResultMap.get(key);
			threatResult.setThreatsCount(threatResult.getThreatsCount() + 1);
			if(threat.getCreationTime().isAfter(threatResult.getLastEventTime())) {
				threatResult.setLastEventTime(threat.getCreationTime());
				threatResult.setLastStatus(threat.getStatus());
			}
			if(threat.getCreationTime().isBefore(threatResult.getFirtEventTime())) {
				threatResult.setFirtEventTime(threat.getCreationTime());
			}
			threatResultMap.replace(key, threatResult);
		} else {
			ThreatResult threatResult = new ThreatResult(key, 1, threat.getCreationTime(), threat.getCreationTime(), threat.getStatus());
			threatResultMap.put(key, threatResult);
		}
	}

	/**
	 * This method returns the difference string by calculating the hour and minute
	 *
	 * @param diffInMinutes
	 * @param diffInHours
	 * @return
	 */
	private String returnDifferenceString(long diffInMinutes, long diffInHours) {
		if (diffInHours == 0) {
			if (diffInMinutes == 1) {
				return diffInMinutes + " minute";
			} else {
				return diffInMinutes + MINUTES_STRING;
			}
		} else {
			long remainingMinutes = (diffInMinutes - (MINUTES_IN_HOUR * diffInHours));
			if (diffInHours == 1) {
				return diffInHours + " hour " + remainingMinutes + MINUTES_STRING;
			}else {
				return diffInHours + " hours " + remainingMinutes + MINUTES_STRING;
			}
		}
	}

	/**
	 * This method creates the analyze report values
	 *
	 * @param csvFilePath
	 * @return
	 * @throws FileUploadException
	 */
	public List<AnalyzeReport> createAnalyzeReport(String csvFilePath) throws FileUploadException {
		Map<String, ThreatResult> threatResultMap = formThreatDataMap(csvFilePath);
		List<AnalyzeReport> analyzeReportList = new ArrayList<>();
		for (Iterator<String> iterator = threatResultMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			ThreatResult threatResult = threatResultMap.get(key);
		     long diffInMinutes = ChronoUnit.MINUTES.between(threatResult.getFirtEventTime(), threatResult.getLastEventTime());
		     long diffInHours = ChronoUnit.HOURS.between(threatResult.getFirtEventTime(), threatResult.getLastEventTime());
			AnalyzeReport analyzeReport = new AnalyzeReport(threatResult.getIpAddress(), threatResult.getThreatsCount(),
					threatResult.getLastEventTime(), FIELD_REPORT_STATUS_VALUES[threatResult.getLastStatus()],
					returnDifferenceString(diffInMinutes, diffInHours));

			analyzeReportList.add(analyzeReport);



		}
		Collections.sort(analyzeReportList);
		return analyzeReportList;

	}




}
