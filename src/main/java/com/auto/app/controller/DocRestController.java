package com.auto.app.controller;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auto.app.exception.ValidationException;
import com.auto.app.model.Doc;
import com.auto.app.service.ExcelSheetService;

@RestController
@RequestMapping("/api")
public class DocRestController {

	@Autowired
	private ExcelSheetService sheetService;
		
	@PostMapping("/formANDFieldValidation")
	public ResponseEntity<ByteArrayResource> uploadFiles(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2) throws IOException, ValidationException {
		Doc doc = sheetService.validateTwoFiles(file1, file2);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
				.body(new ByteArrayResource(doc.getData()));
	}
	
	@PostMapping("/draftValidation")
	public ResponseEntity<ByteArrayResource> uploadFileForDraftValidation(@RequestParam("file1") MultipartFile file1) throws IOException, ValidationException, InvalidFormatException {
		Doc doc =sheetService.validateDraftSheet(file1);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
				.body(new ByteArrayResource(doc.getData()));
	}
}
