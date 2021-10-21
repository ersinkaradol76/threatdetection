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

import com.threatdetection.model.AnalyzeReport;
import com.threatdetection.service.ThreatService;
import com.threatdetection.util.FileUtil;

@Controller
public class ReportController {

	private static final String MESSAGE_PARAM_NAME = "message";

	private static final String REDIRECT_TO_BASE = "redirect:/";

	ThreatService threatService;

	public ReportController(ThreatService threatService) {
		this.threatService = threatService;
	}


	@GetMapping("/")
	public String homepage() {
		return "index";
	}


	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file,
			RedirectAttributes attributes, HttpServletRequest request) throws IOException {

		if (file.isEmpty()) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "Please select threat generator file to analyze!");
			return REDIRECT_TO_BASE;
		}


		Path path = null;
		List<AnalyzeReport> analyzeReportList;

		try {

			path = FileUtil.uploadCsvFile(file);

			analyzeReportList = threatService.createAnalyzeReport(path.toString());

			request.getSession().setAttribute("analyzeReportList", analyzeReportList);


			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, "Threat analyze is completed!");
		} catch (FileUploadException e) {
			attributes.addFlashAttribute(MESSAGE_PARAM_NAME, e.getMessage());
		} finally {
			FileUtil.deleteCsvFile(path);
		}


		attributes.addFlashAttribute("result", "Analyze Results");

		attributes.addFlashAttribute("header1", "General Info");



		if (path != null) {
			attributes.addFlashAttribute("fileName", path.getFileName());
		}
		return REDIRECT_TO_BASE;
	}


	@GetMapping("/detail")
	public String unmatched(HttpSession session, Model model) {
		List<AnalyzeReport> analyzeReportList = (List<AnalyzeReport>) session.getAttribute("analyzeReportList");

		//List<Threat> nonmatcheds1 = comparisonResult.getNonMatched1List();

		//model.addAttribute("nonmatcheds1", nonmatcheds1);


		model.addAttribute("header2", "Candidate List");
		model.addAttribute("header3", "Nonmatched List");
		model.addAttribute("header4", "Errors and Repeated Lines");

		return "detail :: attributes";
	}

}