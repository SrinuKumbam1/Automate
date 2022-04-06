package com.auto.app.service;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import com.auto.app.exception.ValidationException;
import com.auto.app.model.Doc;

public interface ExcelSheetService  {

	Doc validateTwoFiles(MultipartFile file1, MultipartFile file2) throws IOException, ValidationException;
	Doc validateDraftSheet(MultipartFile file1) throws IOException, ValidationException, InvalidFormatException;
}
