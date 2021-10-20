package com.threatdetection.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.threatdetection.model.ComparisonResult;
import com.threatdetection.model.Threat;
import com.threatdetection.service.ThreatService;
import com.threatdetection.util.FileUtil;

@Controller
public class UploadController {

	private static final String MESSAGE_PARAM_NAME = "message";

	private static final String REDIRECT_TO_BASE = "redirect:/";

	ThreatService transactionService;

	public UploadController(ThreatService transactionService) {
		this.transactionService = transactionService;
	}


	@GetMapping("/")
	public String homepage() {
		return "index";
	}


	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
			RedirectAttributes attributes, HttpServletRequest request) throws IOException {

		if (file1.isEmpty()) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "Please select first file to compare!");
			return REDIRECT_TO_BASE;
		}

		if (file2.isEmpty()) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "Please select second file to compare!");
			return REDIRECT_TO_BASE;
		}

		Path path1 = null;
		Path path2 = null;
		ComparisonResult comparisonResult = null;
		try {

			path1 = FileUtil.uploadCsvFile(file1);
			path2 = FileUtil.uploadCsvFile(file2);

			comparisonResult = transactionService.compareCsvFiles(path1, path2);

			request.getSession().setAttribute("comparisonResult", comparisonResult);

			attributes.addFlashAttribute("comparisonValues1", new String[] { String.valueOf(comparisonResult.getLineTotalCountOfFile1()),
						String.valueOf(comparisonResult.getPerfectMatchedList().size()), String.valueOf(comparisonResult.getCandidates1List().size()),
						String.valueOf(comparisonResult.getNonMatched1List().size()), String.valueOf(comparisonResult.getErrorData1List().size()),
						String.valueOf(comparisonResult.getRepeatedData1List().size())
			});

			attributes.addFlashAttribute("comparisonValues2", new String[] { String.valueOf(comparisonResult.getLineTotalCountOfFile2()),
					String.valueOf(comparisonResult.getPerfectMatchedList().size()), String.valueOf(comparisonResult.getCandidates2List().size()),
					String.valueOf(comparisonResult.getNonMatched2List().size()), String.valueOf(comparisonResult.getErrorData2List().size()),
					String.valueOf(comparisonResult.getRepeatedData2List().size())
			});

			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "Matching operation is completed!");
		} catch (FileUploadException e) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, e.getMessage());
		} finally {
			FileUtil.deleteCsvFile(path1);
			FileUtil.deleteCsvFile(path2);
		}


		attributes.addFlashAttribute("result", "Comparison Results");

		attributes.addFlashAttribute("header1", "General Info");



		if (path1 != null) {
			attributes.addFlashAttribute("file1Name", path1.getFileName());
		}
		if (path2 != null) {
			attributes.addFlashAttribute("file2Name", path2.getFileName());
		}

		return REDIRECT_TO_BASE;
	}


	@GetMapping("/detail")
	public String unmatched(HttpSession session, Model model) {
		ComparisonResult comparisonResult = (ComparisonResult) session.getAttribute("comparisonResult");

		List<Threat> nonmatcheds1 = comparisonResult.getNonMatched1List();
		List<Threat> nonmatcheds2 = comparisonResult.getNonMatched2List();
		List<Threat> candidates1 = comparisonResult.getCandidates1List();
		List<Threat> candidates2 = comparisonResult.getCandidates2List();
		List<Integer> errors1 = comparisonResult.getErrorData1List();
		List<Integer> errors2 = comparisonResult.getErrorData2List();
		List<Threat> repeats1 = comparisonResult.getRepeatedData1List();
		List<Threat> repeats2 = comparisonResult.getRepeatedData2List();

		model.addAttribute("nonmatcheds1", nonmatcheds1);
		model.addAttribute("nonmatcheds2", nonmatcheds2);
		model.addAttribute("candidates1", candidates1);
		model.addAttribute("candidates2", candidates2);
		model.addAttribute("errors1", errors1);
		model.addAttribute("errors2", errors2);
		model.addAttribute("repeats1", repeats1);
		model.addAttribute("repeats2", repeats2);

		model.addAttribute("header2", "Candidate List");
		model.addAttribute("header3", "Nonmatched List");
		model.addAttribute("header4", "Errors and Repeated Lines");

		return "detail :: attributes";
	}

}