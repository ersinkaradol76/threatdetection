package com.threatdetection.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockMultipartFile;

public class FileUtilTestFixtures {

	public static final String UPLOAD_DIR = "./uploads/";

	public static String file1 = UPLOAD_DIR + "test1.csv";
	public static final int FILE1_LINE_COUNT = 3;

	public static String fileName = "sampleFile.txt";
	public static MockMultipartFile sampleFile = new MockMultipartFile("uploaded-file", fileName, "text/plain",
			"This is the test file content".getBytes());

	public static List<Path> filesToBeDeleted = new ArrayList<>();



}
