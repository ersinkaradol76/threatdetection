package com.threatdetection.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.threatdetection.exceptions.DataDoesNotSuitSpecsException;
import com.threatdetection.model.ComparisonResult;
import com.threatdetection.model.FileLineInfo;
import com.threatdetection.model.Threat;
import com.threatdetection.model.ValidatedData;
import com.threatdetection.util.FileUtil;

@Service
public class ThreatService {

	private static final int FIELD_COUNT = 7;
	
	private static final int FIELD_PROFILE_NAME_MIN_LENGTH = 0;
	private static final int FIELD_PROFILE_NAME_MAX_LENGTH = 30;
	private static final int FIELD_TRANSACTION_NARRATIVE_MIN_LENGTH = 0;
	private static final int FIELD_TRANSACTION_NARRATIVE_MAX_LENGTH = 200;
	private static final int FIELD_TRANSACTION_TYPE_MAX_VALUE = 9;

	private static final String[] FIELD_TRANSACTION_DESCRIPTION_VALUES = { "DEDUCT", "REVERSAL" };

	private static final int FIELD_TRANSACTION_ID_LENGTH = 16;
	private static final int FIELD_WALLET_REFERENCE_LENGTH = 34;

	private static final int FIELD_NO_PROFILE_NAME = 0;
	private static final int FIELD_NO_TRANSACTION_DATE = 1;
	private static final int FIELD_NO_TRANSACTION_AMOUNT = 2;
	private static final int FIELD_NO_TRANSACTION_NARRATIVE = 3;
	private static final int FIELD_NO_TRANSACTION_DESCRIPTION = 4;
	private static final int FIELD_NO_TRANSACTION_ID = 5;
	private static final int FIELD_NO_TRANSACTION_TYPE = 6;
	private static final int FIELD_NO_WALLET_REFERENCE = 7;

	private static final String ERROR_MESSAGE = "Error in the field format ";

	private static final Logger logger = LoggerFactory.getLogger(ThreatService.class);

	/**
	 *
	 * This method validates the fields of the line, if data has no error creates a Transaction object and returns it,
	 * if data has an error in the fields throws exception
	 *
	 * @param fields 		the fields in a line of csv file
	 * @param lineNumber	line number of the line in the file
	 * @return returns the data created from the fields of the line as Transaction object
	 * @throws DataDoesNotSuitSpecsException
	 */

	public Threat createTransaction (String[] fields, int lineNumber) throws DataDoesNotSuitSpecsException {

		try {
			ValidatedData validatedData = validateFields(fields, lineNumber);
			return new Threat(validatedData.getProfileName(),
					validatedData.getTransactionDate(), validatedData.getTransactionAmount(),
					validatedData.getTransactionNarrative(), validatedData.getTransactionDescription(),
					validatedData.getTransactionID(), validatedData.getTransactionType(),
					validatedData.getWalletReference(), lineNumber);


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

			validatedData.setProfileName(validateProfileNameField(fields[FIELD_NO_PROFILE_NAME], lineNumber));
			validatedData
					.setTransactionDate(validateTransactionDateField(fields[FIELD_NO_TRANSACTION_DATE], lineNumber));
			validatedData.setTransactionAmount(
					validateTransactionAmountField(fields[FIELD_NO_TRANSACTION_AMOUNT], lineNumber));
			validatedData.setTransactionNarrative(
					validateTransactionNarrativeField(fields[FIELD_NO_TRANSACTION_NARRATIVE], lineNumber));
			validatedData.setTransactionDescription(
					validateTransactionDescriptionField(fields[FIELD_NO_TRANSACTION_DESCRIPTION], lineNumber));
			validatedData
					.setTransactionID(validateTransactionIDField(fields[FIELD_NO_TRANSACTION_ID], lineNumber));
			validatedData
					.setTransactionType(validateTransactionTypeField(fields[FIELD_NO_TRANSACTION_TYPE], lineNumber));
			validatedData
					.setWalletReference(validateWalletReferenceField(fields[FIELD_NO_WALLET_REFERENCE], lineNumber));
			validatedData.setLineNumber(lineNumber);
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
	 * This method validates the Profile Name field, If it is less than 30 chars
	 * returns the string field, throws exception If it is longer than 30 chars
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private String validateThreatIdField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		try {
			int type = Integer.parseInt(typeField);
			if (type <= FIELD_TRANSACTION_TYPE_MAX_VALUE) {
				return type;
			} else {
				throw new DataDoesNotSuitSpecsException(
						ERROR_MESSAGE + "Field: Transaction Type --- Line Number: " + lineNumber);
			}
		} catch (NumberFormatException e) {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction Type --- Line Number: " + lineNumber);
		}
	}

	/**
	 *
	 * This method validates the Transaction Date field, If it is not in the
	 * required date format, throws exception If it is in the required date format,
	 * returns the data that is converted to LocalDateTime
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private LocalDateTime validateTransactionDateField(String dateField, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			return LocalDateTime.parse(dateField, formatter);
		} catch (DateTimeParseException e) {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction Date --- Line Number: " + lineNumber);
		}
	}


	/**
	 *
	 * This method validates the Transaction Amount field,
	 * If it is not in the required numeric format, throws exception
	 * If it is in the required numeric format, returns the data that is converted to BigDecimal
	 *
	 * @param fields 		the fields in a line of csv file
	 * @param lineNumber	line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private BigDecimal validateTransactionAmountField(String amountField, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		try {
			return new BigDecimal(amountField);
		} catch (NumberFormatException e) {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction Amount --- Line Number: " + lineNumber);
		}
	}

	/**
	 *
	 * This method validates the Transaction Narrative field, If it is less than 200
	 * chars returns the string field, throws exception If it is longer than 200
	 * chars
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private String validateTransactionNarrativeField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		if (field != null && field.trim().length() > FIELD_TRANSACTION_NARRATIVE_MIN_LENGTH
				&& field.trim().length() <= FIELD_TRANSACTION_NARRATIVE_MAX_LENGTH) {
			return field.trim();
		} else {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction Narrative --- Line Number: " + lineNumber);
		}

	}

	/**
	 *
	 * This method validates the Transaction Description field, If it is equals to
	 * DEDUCT or REVERSAL returns the int value of it, if it is not throws exception
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private String validateTransactionDescriptionField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		if (field != null) {
			for (int i = 0; i < FIELD_TRANSACTION_DESCRIPTION_VALUES.length; i++) {
				if (field.equals(FIELD_TRANSACTION_DESCRIPTION_VALUES[i])) {
					return field.trim();
				}
			}
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction Description --- Line Number: " + lineNumber);
		} else {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction Description --- Line Number: " + lineNumber);
		}

	}

	/**
	 *
	 * This method validates the Transaction ID field, If it is lenght is 16 and a
	 * numeric value returns the string field, throws exception If it is not
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private String validateTransactionIDField(String field, int lineNumber)
			throws DataDoesNotSuitSpecsException {
		if (field != null && field.trim().length() == FIELD_TRANSACTION_ID_LENGTH) {
			try {
				Long.parseLong(field);
				return field.trim();
			} catch (NumberFormatException e) {
				throw new DataDoesNotSuitSpecsException(
						ERROR_MESSAGE + "Field: Transaction ID --- Line Number: " + lineNumber);
			}

		} else {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction ID --- Line Number: " + lineNumber);
		}

	}

	/**
	 *
	 * This method validates the Transaction Type field, If it is not in the
	 * required numeric format, throws exception If it is in the required numeric
	 * format, if the number less than 10 then returns the data that is converted to
	 * int
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private int validateTransactionTypeField(String typeField, int lineNumber) throws DataDoesNotSuitSpecsException {
		try {
			int type = Integer.parseInt(typeField);
			if (type <= FIELD_TRANSACTION_TYPE_MAX_VALUE) {
				return type;
			} else {
				throw new DataDoesNotSuitSpecsException(
						ERROR_MESSAGE + "Field: Transaction Type --- Line Number: " + lineNumber);
			}
		} catch (NumberFormatException e) {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Transaction Type --- Line Number: " + lineNumber);
		}
	}

	/**
	 *
	 * This method validates the Wallet Reference field, If it is lenght is 34
	 * returns the string field, throws exception If it is not
	 *
	 * @param fields     the fields in a line of csv file
	 * @param lineNumber line number of the line in the file
	 * @throws DataDoesNotSuitSpecsException
	 */
	private String validateWalletReferenceField(String field, int lineNumber) throws DataDoesNotSuitSpecsException {
		if (field != null && field.trim().length() == FIELD_WALLET_REFERENCE_LENGTH) {
			return field.trim();
		} else {
			throw new DataDoesNotSuitSpecsException(
					ERROR_MESSAGE + "Field: Wallet Reference --- Line Number: " + lineNumber);
		}

	}

	/**
	 *
	 * This method compares two Transaction object If Transaction ID of them equals
	 * to each other this objects are evaluated as matching candidate If Wallet
	 * Reference of them equals to each other this objects are evaluated as matching
	 * candidate If Transaction Date, Transaction Amount and the Transaction
	 * Narrative (that the punctuation, numbers and blank characters removed and
	 * converted to lowercase) of them equals to each other this objects are
	 * evaluated as matching candidate
	 *
	 * @param transaction1 the Transaction data created from a line of the first csv
	 *                     file
	 * @param transaction2 the Transaction data created from a line of the second
	 *                     csv file
	 * @return true if the datas are candidate
	 */
	public boolean isMatchingCandidate(Threat transaction1, Threat transaction2) {

		return ((transaction1.getTransactionID().equals(transaction2.getTransactionID()))
				|| (transaction1.getWalletReference().equals(transaction2.getWalletReference()))
				||
				(transaction1.getTransactionDate().equals(transaction2.getTransactionDate())
						&& transaction1.getTransactionAmount().equals(transaction2.getTransactionAmount())
						&& filterData(transaction1.getTransactionNarrative())
								.equals(filterData(transaction2.getTransactionNarrative()))));
	}

	/**
	 *
	 * This methot removes the punctuation, numbers and blank characters from the data and converts lowercase
	 *
	 * @param data 		the String data that will be filtered
	 * @return  the filtered data
	 */
	private String filterData(String data) {
		// removes all the punctuation chars, digits and blanks from the string and
		// converts to lowercase
		return data.replaceAll("[^a-zA-Z]", "").toLowerCase();
	}



	/**
	 *
	 *
	 * This method reads the csv file line by line and forms a list of FileLineInfo
	 * that holds the fields of the line, For all the FileLineInfo object in the
	 * list creates a Transaction object if the data is valid and not repeated,
	 * Stores the Transaction object in a HashMap that the key is the combination of
	 * the Transaction ID and the Transaction Description and the value is the
	 * Transaction Object. If the line has a field that is invalid, stores the line
	 * number in error list If the line is a repeat of another line, stores the line
	 * number in the repeated list
	 *
	 * @param csvFilePath      file path of the csv file
	 * @param errorList        the number of the line in csv file that has at least
	 *                         an invalid field
	 * @param repeatedDataList the number of the line in csv file that is a repeat
	 *                         of another line
	 * @return returns a HashMap that the key is the combination of the Transaction
	 *         ID and the Transaction Description and the value is the Transaction
	 *         Object.
	 * @throws FileUploadException
	 * @throws FileNotFoundException
	 */
	private Map<String, Threat> formTransactionDataMap(String csvFilePath, List<Integer> errorList,
			List<Threat> repeatedDataList) throws FileUploadException {

		Map<String, Threat> transactionMap = new HashMap<>();
		List<FileLineInfo> fileLineInfoList = FileUtil.readCsvFileToStringList(csvFilePath);
		for (int i = 0; i < fileLineInfoList.size(); i++) {
			FileLineInfo fileLineInfo = fileLineInfoList.get(i);
			String[] fields = fileLineInfo.getFields();
			Threat transaction = null;
			try {
				transaction = createTransaction(fields, fileLineInfo.getLineNumber());

			} catch (DataDoesNotSuitSpecsException e) {
				logger.error(e.getMessage());
				errorList.add(fileLineInfo.getLineNumber());
			}
			if (transaction != null) {
				String key = transaction.getTransactionID() + transaction.getTransactionDescription();
				if (transactionMap.get(key) != null) {
					repeatedDataList.add(transaction);
				} else {
					transactionMap.put(key, transaction);
				}
			}
		}
		return transactionMap;

	}


	/**
	 *
	 * This method compares the to HashMaps created from the two csv files,
	 * If all the fields of the lines equal, it is a perfect match and the Transaction data is stored in the perfectMatch list and
	 * this data removed from the second to prevent repeated data and gain performance,
	 *
	 * If the data is not matched, that is stored in the nonMatched list to search for the candidates
	 *
	 * @param transaction1Map		the valid data of the first csv file formed HashMap that the key is the combination of the Transaction ID and the Transaction Description and the value is the Transaction Object.
	 * @param transaction2Map		the valid data of the second csv file formed HashMap that the key is the combination of the Transaction ID and the Transaction Description and the value is the Transaction Object.
	 * @param perfectMatchedList	the list of data that all the fields of that are equal in both transaction maps
	 * @param nonMatched1List		the data of the first file that are not matched
	 * @param nonMatched2List		the data of the second file that are not matched
	 */
	private void findPerfectMatchs(Map<String, Threat> transaction1Map, Map<String, Threat> transaction2Map,
			List<Threat> perfectMatchedList, List<Threat> nonMatched1List,
			List<Threat> nonMatched2List) {

		for (Iterator<String> iterator = transaction1Map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Threat transaction1 = transaction1Map.get(key);
			Threat transaction2 = transaction2Map.get(key);

			if (transaction2 != null) {
				if (transaction1.equals(transaction2)) {
					transaction1.setMatching(Threat.MATCHED);
					perfectMatchedList.add(transaction1);
					transaction2Map.remove(key);
				} else {
					transaction1.setMatching(Threat.MATCHING_CANDIDATE);
					nonMatched1List.add(transaction1);
				}
			}else {
				transaction1.setMatching(Threat.NOT_MATCHED);
				nonMatched1List.add(transaction1);
			}
		}
		for (Iterator<String> iterator = transaction2Map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Threat transaction = transaction2Map.get(key);
			transaction.setMatching(Threat.NOT_MATCHED);
			nonMatched2List.add(transaction);
		}
	}

	/**
	 *
	 * This method finds the matching candidates from the nonMatched data lists,
	 * If the data meets the candidate condition, stores in the candidate list, and removes from the nonMatched List
	 * At last only the nonMatched data remains in the nonMatched list
	 *
	 * @param nonMatched1List		the data of the first file that are not matched
	 * @param nonMatched2List		the data of the second file that are not matched
	 * @param candidate1List		the data of the first file that are matching candidate
	 * @param candidate2List		the data of the second file that are matching candidate
	 */
	private void findMatchingCandidates(List<Threat> nonMatched1List, List<Threat> nonMatched2List,
			List<Threat> candidate1List, List<Threat> candidate2List) {

		int candidateIndex1 = 0;
		int candidateIndex2 = 0;
		for (Iterator<Threat> iterator1 = nonMatched1List.iterator(); iterator1.hasNext();) {
			Threat transaction1 = iterator1.next();
			for (Iterator<Threat> iterator2 = nonMatched2List.iterator(); iterator2.hasNext();) {
				Threat transaction2 = iterator2.next();
				if (isMatchingCandidate(transaction1, transaction2)) {
					transaction1.setMatching(Threat.MATCHING_CANDIDATE);
					transaction1.setCandidateIndex(candidateIndex2);
					candidate1List.add(candidateIndex1, transaction1);

					iterator1.remove();

					transaction2.setMatching(Threat.MATCHING_CANDIDATE);
					transaction2.setCandidateIndex(candidateIndex1);
					candidate2List.add(candidateIndex2, transaction2);

					iterator2.remove();
					candidateIndex1++;
					candidateIndex2++;
					break;
				}
			}
		}
	}


	/**
	 *
	 * This method performs the main business logic Forms the HashMap for the two
	 * files that hold the valid data Compares the two maps and separates the data
	 * as perfect match, candidate, non matched, repeated and error.
	 *
	 * @param file1Path file path of the first file
	 * @param file2Path file path of the second file
	 * @return
	 * @throws FileUploadException
	 */
	public ComparisonResult compareCsvFiles(Path file1Path, Path file2Path) throws FileUploadException {

		ComparisonResult comparisonResult = new ComparisonResult();

		comparisonResult.setTransaction1Map(formTransactionDataMap(file1Path.toString(),
				comparisonResult.getErrorData1List(), comparisonResult.getRepeatedData1List()));

		comparisonResult.setLineCountOfFile1(comparisonResult.getTransaction1Map().size());
		comparisonResult.setLineTotalCountOfFile1(comparisonResult.getTransaction1Map().size() + comparisonResult.getErrorData1List().size() + comparisonResult.getRepeatedData1List().size());

		comparisonResult.setTransaction2Map(formTransactionDataMap(file2Path.toString(),
				comparisonResult.getErrorData2List(), comparisonResult.getRepeatedData2List()));

		comparisonResult.setLineCountOfFile2(comparisonResult.getTransaction2Map().size());
		comparisonResult.setLineTotalCountOfFile2(comparisonResult.getTransaction2Map().size() + comparisonResult.getErrorData2List().size() + comparisonResult.getRepeatedData2List().size());

		if (comparisonResult.getTransaction1Map() != null && comparisonResult.getTransaction2Map() != null) {
			findPerfectMatchs(comparisonResult.getTransaction1Map(), comparisonResult.getTransaction2Map(),
					comparisonResult.getPerfectMatchedList(), comparisonResult.getNonMatched1List(),
					comparisonResult.getNonMatched2List());
			findMatchingCandidates(comparisonResult.getNonMatched1List(), comparisonResult.getNonMatched2List(),
					comparisonResult.getCandidates1List(), comparisonResult.getCandidates2List());
		} else {
			comparisonResult.setPerfectMatchedList(null);
			comparisonResult.setNonMatched1List(null);
			comparisonResult.setNonMatched2List(null);
		}

		return comparisonResult;

	}


}
