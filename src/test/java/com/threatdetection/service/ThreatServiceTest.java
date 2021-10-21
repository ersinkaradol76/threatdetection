package com.threatdetection.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.threatdetection.exceptions.DataDoesNotSuitSpecsException;
import com.threatdetection.model.ComparisonResult;
import com.threatdetection.model.Threat;
import com.threatdetection.service.ThreatService;


public class ThreatServiceTest {

	@InjectMocks
	ThreatService transactionService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void withValidFieldsCreateTransactionExpectNotNull() throws DataDoesNotSuitSpecsException {

		Threat transaction = transactionService.createTransaction(ThreatServiceTestFixtures.fieldsValid,
				ThreatServiceTestFixtures.lineNumber);
		assertNotNull(transaction);

	}

	@Test
	public void withInvalidDateFieldCanNotCreateTransactionExpectNull() {

		Threat transaction = null;
		try {
			transaction = transactionService.createTransaction(ThreatServiceTestFixtures.fieldsInvalid, ThreatServiceTestFixtures.lineNumber);
		} catch (DataDoesNotSuitSpecsException e) {
			assertNull(transaction);
			assertTrue(e.getMessage().contains("Transaction Date"));
		}
	}

	@Test
	public void givenTransactionIDsEqualCheckMatchingCandidate() {

		assertTrue(transactionService.isMatchingCandidate(ThreatServiceTestFixtures.transaction1, ThreatServiceTestFixtures.transaction2));
	}

	@Test
	public void givenTransactionIDsNotEqualWalletReferencesEqualCheckMatchingCandidate() {

		assertTrue(transactionService.isMatchingCandidate(ThreatServiceTestFixtures.transaction1, ThreatServiceTestFixtures.transaction3));
	}

	@Test
	public void givenTransactionIDsWalletReferencesNotEqualDateAmountNarrativeEqualCheckMatchingCandidate() {

		assertTrue(transactionService.isMatchingCandidate(ThreatServiceTestFixtures.transaction1, ThreatServiceTestFixtures.transaction4));
	}

	@Test
	public void givenTransactionIDsWalletReferencesDateAmountNarrativeNotEqualCheckMatchingCandidate() {

		assertFalse(transactionService.isMatchingCandidate(ThreatServiceTestFixtures.transaction2, ThreatServiceTestFixtures.transaction4));
	}

	@Test
	public void afterComparisonOfFilesTheCountsMustMatched() throws FileUploadException {

		ComparisonResult comparisonResult = transactionService.compareCsvFiles(Path.of(ThreatServiceTestFixtures.file1),
				Path.of(ThreatServiceTestFixtures.file2));

		assertEquals(ThreatServiceTestFixtures.TRANSACTION1_MAP_SIZE, comparisonResult.getTransaction1Map().size());
		assertEquals(ThreatServiceTestFixtures.FILE1_VALID_LINE_COUNT, comparisonResult.getLineCountOfFile1());
		assertEquals(ThreatServiceTestFixtures.TRANSACTION2_MAP_SIZE, comparisonResult.getTransaction2Map().size());
		assertEquals(ThreatServiceTestFixtures.FILE2_VALID_LINE_COUNT, comparisonResult.getLineCountOfFile2());
		assertEquals(ThreatServiceTestFixtures.ERRORDATA1_LIST_SIZE, comparisonResult.getErrorData1List().size());
		assertEquals(ThreatServiceTestFixtures.ERRORDATA2_LIST_SIZE, comparisonResult.getErrorData2List().size());
		assertEquals(ThreatServiceTestFixtures.REPEATEDDATA1_LIST_SIZE, comparisonResult.getRepeatedData1List().size());
		assertEquals(ThreatServiceTestFixtures.REPEATEDDATA2_LIST_SIZE, comparisonResult.getRepeatedData2List().size());
		assertEquals(ThreatServiceTestFixtures.PERFECTMATCH_LIST_SIZE, comparisonResult.getPerfectMatchedList().size());
		assertEquals(ThreatServiceTestFixtures.NONMATCHEDDATA1_LIST_SIZE, comparisonResult.getNonMatched1List().size());
		assertEquals(ThreatServiceTestFixtures.NONMATCHEDDATA2_LIST_SIZE, comparisonResult.getNonMatched2List().size());
		assertEquals(ThreatServiceTestFixtures.CANDIDATEDATA1_LIST_SIZE, comparisonResult.getCandidates1List().size());
		assertEquals(ThreatServiceTestFixtures.CANDIDATEDATA2_LIST_SIZE, comparisonResult.getCandidates2List().size());

	}

}
