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
import com.threatdetection.model.Transaction;
import com.threatdetection.service.TransactionService;


public class TransactionServiceTest {

	@InjectMocks
	TransactionService transactionService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void withValidFieldsCreateTransactionExpectNotNull() throws DataDoesNotSuitSpecsException {

		Transaction transaction = transactionService.createTransaction(TransactionServiceTestFixtures.fieldsValid,
				TransactionServiceTestFixtures.lineNumber);
		assertNotNull(transaction);

	}

	@Test
	public void withInvalidDateFieldCanNotCreateTransactionExpectNull() {

		Transaction transaction = null;
		try {
			transaction = transactionService.createTransaction(TransactionServiceTestFixtures.fieldsInvalid, TransactionServiceTestFixtures.lineNumber);
		} catch (DataDoesNotSuitSpecsException e) {
			assertNull(transaction);
			assertTrue(e.getMessage().contains("Transaction Date"));
		}
	}

	@Test
	public void givenTransactionIDsEqualCheckMatchingCandidate() {

		assertTrue(transactionService.isMatchingCandidate(TransactionServiceTestFixtures.transaction1, TransactionServiceTestFixtures.transaction2));
	}

	@Test
	public void givenTransactionIDsNotEqualWalletReferencesEqualCheckMatchingCandidate() {

		assertTrue(transactionService.isMatchingCandidate(TransactionServiceTestFixtures.transaction1, TransactionServiceTestFixtures.transaction3));
	}

	@Test
	public void givenTransactionIDsWalletReferencesNotEqualDateAmountNarrativeEqualCheckMatchingCandidate() {

		assertTrue(transactionService.isMatchingCandidate(TransactionServiceTestFixtures.transaction1, TransactionServiceTestFixtures.transaction4));
	}

	@Test
	public void givenTransactionIDsWalletReferencesDateAmountNarrativeNotEqualCheckMatchingCandidate() {

		assertFalse(transactionService.isMatchingCandidate(TransactionServiceTestFixtures.transaction2, TransactionServiceTestFixtures.transaction4));
	}

	@Test
	public void afterComparisonOfFilesTheCountsMustMatched() throws FileUploadException {

		ComparisonResult comparisonResult = transactionService.compareCsvFiles(Path.of(TransactionServiceTestFixtures.file1),
				Path.of(TransactionServiceTestFixtures.file2));

		assertEquals(TransactionServiceTestFixtures.TRANSACTION1_MAP_SIZE, comparisonResult.getTransaction1Map().size());
		assertEquals(TransactionServiceTestFixtures.FILE1_VALID_LINE_COUNT, comparisonResult.getLineCountOfFile1());
		assertEquals(TransactionServiceTestFixtures.TRANSACTION2_MAP_SIZE, comparisonResult.getTransaction2Map().size());
		assertEquals(TransactionServiceTestFixtures.FILE2_VALID_LINE_COUNT, comparisonResult.getLineCountOfFile2());
		assertEquals(TransactionServiceTestFixtures.ERRORDATA1_LIST_SIZE, comparisonResult.getErrorData1List().size());
		assertEquals(TransactionServiceTestFixtures.ERRORDATA2_LIST_SIZE, comparisonResult.getErrorData2List().size());
		assertEquals(TransactionServiceTestFixtures.REPEATEDDATA1_LIST_SIZE, comparisonResult.getRepeatedData1List().size());
		assertEquals(TransactionServiceTestFixtures.REPEATEDDATA2_LIST_SIZE, comparisonResult.getRepeatedData2List().size());
		assertEquals(TransactionServiceTestFixtures.PERFECTMATCH_LIST_SIZE, comparisonResult.getPerfectMatchedList().size());
		assertEquals(TransactionServiceTestFixtures.NONMATCHEDDATA1_LIST_SIZE, comparisonResult.getNonMatched1List().size());
		assertEquals(TransactionServiceTestFixtures.NONMATCHEDDATA2_LIST_SIZE, comparisonResult.getNonMatched2List().size());
		assertEquals(TransactionServiceTestFixtures.CANDIDATEDATA1_LIST_SIZE, comparisonResult.getCandidates1List().size());
		assertEquals(TransactionServiceTestFixtures.CANDIDATEDATA2_LIST_SIZE, comparisonResult.getCandidates2List().size());

	}

}
