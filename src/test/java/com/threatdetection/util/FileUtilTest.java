package com.threatdetection.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.threatdetection.model.FileLineInfo;

public class FileUtilTest {

	@Test
	public void uploadingFileCheckIfExist() throws Exception {
		Path path = FileUtil.uploadCsvFile(FileUtilTestFixtures.sampleFile);
		assertTrue(Files.exists(path));
		FileUtilTestFixtures.filesToBeDeleted.add(path);

	}

	@Test
	public void deletingUploadedFileCheckIfNotExist() throws Exception {
		Path path = FileUtil.uploadCsvFile(FileUtilTestFixtures.sampleFile);
		FileUtil.deleteCsvFile(path);
		assertTrue(Files.notExists(path));
		FileUtilTestFixtures.filesToBeDeleted.add(path);

	}

	@Test
	public void givenCsvFileCheckIfLinesCanBeParsed() throws Exception {

		List<FileLineInfo> list = FileUtil.readCsvFileToStringList(FileUtilTestFixtures.file1);
		assertEquals(FileUtilTestFixtures.FILE1_LINE_COUNT, list.size());

	}

	@After
	public void cleanup() {
		FileUtilTestFixtures.filesToBeDeleted.forEach(path -> {
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
