package com.threatdetection.service;

import java.time.LocalDateTime;

public class ThreatServiceTestFixtures {

	public static String[] fieldsValid = { "1", "10.0.14.101", "12/30/2019 09:40:00", "Infected IP", "1", "False",
			"Fonzie McCalum" };

	public static String[] fieldsInvalid = { "1", "10.0.14.1a1", "12/30/2019 10:40:00", "Infected IP", "1", "False",
			"Fonzie McCalum" };

	public static int lineNumber = 3;

	static LocalDateTime now = LocalDateTime.now();

	public static final String UPLOAD_DIR = "./uploads/";

	public static String file1 = UPLOAD_DIR + "test1.csv";

	public static final int RESULT_LIST_SIZE = 2;

}
