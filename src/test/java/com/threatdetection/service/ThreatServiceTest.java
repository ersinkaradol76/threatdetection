package com.threatdetection.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.threatdetection.exceptions.DataDoesNotSuitSpecsException;
import com.threatdetection.model.AnalyzeReport;
import com.threatdetection.model.Threat;


public class ThreatServiceTest {

	@InjectMocks
	ThreatService transactionService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void withValidFieldsCreateThreatExpectNotNull() throws DataDoesNotSuitSpecsException {

		Threat transaction = transactionService.createThreat(ThreatServiceTestFixtures.fieldsValid,
				ThreatServiceTestFixtures.lineNumber);
		assertNotNull(transaction);

	}

	@Test
	public void withInvalidDateFieldCanNotCreateThreatExpectNull() {

		Threat transaction = null;
		try {
			transaction = transactionService.createThreat(ThreatServiceTestFixtures.fieldsInvalid,
					ThreatServiceTestFixtures.lineNumber);
		} catch (DataDoesNotSuitSpecsException e) {
			assertNull(transaction);
			assertTrue(e.getMessage().contains("Ip Address"));
		}
	}


	@Test
	public void afterAnalyzeOfFilesTheCountsMustMatched() throws FileUploadException {

		List<AnalyzeReport> resultList = transactionService.createAnalyzeReport(ThreatServiceTestFixtures.file1);


		assertEquals(ThreatServiceTestFixtures.RESULT_LIST_SIZE, resultList.size());


	}

}
