package com.threatdetection.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.threatdetection.model.Transaction;

public class TransactionServiceTestFixtures {

	public static String[] fieldsValid = { "Card Campaign", "2014-01-11 22:27:44", "-20000",
			"*MOLEPS ATM25             MOLEPOLOLE    BW", "DEDUCT", "0584011808649511", "1",
			"P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5" };

	public static String[] fieldsInvalid = { "Card Campaign", "2014-01-11.22:27:44", "-20000",
			"*MOLEPS ATM25             MOLEPOLOLE    BW", "DEDUCT", "0584011808649511", "1",
			"P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5" };

	public static int lineNumber = 548;

	static LocalDateTime now = LocalDateTime.now();

	public static Transaction transaction1 = new Transaction("profile", now, new BigDecimal(100), "Narrative A",
			"DEDUCT",
			"0584011808649511", 0, "P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5", 127);
	public static Transaction transaction2 = new Transaction("profile", now, new BigDecimal(100), "Narrative B",
			"DEDUCT",
			"0584011808649511", 0, "P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5", 236);

	public static Transaction transaction3 = new Transaction("profile", now, new BigDecimal(100), "Narrative C",
			"DEDUCT", "0584011808649510", 0, "P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5", 236);

	public static Transaction transaction4 = new Transaction("profile", now, new BigDecimal(100),
			"**+546545    narrative 5435345 A ,.", "DEDUCT", "0584011808649510", 0,
			"P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzB7", 236);

	public static final String UPLOAD_DIR = "./uploads/";

	public static String file1 = UPLOAD_DIR + "test1.csv";
	public static String file2 = UPLOAD_DIR + "test2.csv";

	public static final int TRANSACTION1_MAP_SIZE = 303;
	public static final int TRANSACTION2_MAP_SIZE = 16;
	public static final int FILE1_VALID_LINE_COUNT = 303;
	public static final int FILE2_VALID_LINE_COUNT = 304;
	public static final int ERRORDATA1_LIST_SIZE = 1;
	public static final int ERRORDATA2_LIST_SIZE = 0;
	public static final int REPEATEDDATA1_LIST_SIZE = 2;
	public static final int REPEATEDDATA2_LIST_SIZE = 1;
	public static final int PERFECTMATCH_LIST_SIZE = 288;
	public static final int NONMATCHEDDATA1_LIST_SIZE = 2;
	public static final int NONMATCHEDDATA2_LIST_SIZE = 3;
	public static final int CANDIDATEDATA1_LIST_SIZE = 13;
	public static final int CANDIDATEDATA2_LIST_SIZE = 13;

}
