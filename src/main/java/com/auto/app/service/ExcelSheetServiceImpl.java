package com.auto.app.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.auto.app.common.CommonLogic;
import com.auto.app.exception.ValidationException;
import com.auto.app.model.Doc;
import com.auto.app.model.DraftSheetRow;
import com.auto.app.model.FieldSheetRow;
import com.auto.app.model.ResultBook;
import com.auto.app.validation.DraftValidations;
import com.auto.app.validation.FieldValidation;
import com.auto.app.validation.FormValidations;
import com.auto.app.write.WriteData;

@Component
public class ExcelSheetServiceImpl implements ExcelSheetService {

	@Value("${bookName.book1}")
	private String firstBook;

	@Value("${bookName.book2}")
	private String secondBook;
	
	@Value("${type}")
	private String type;

	@Value("${bookName.book1.sheet1}")
	private String sheetNameOfFirstBook;

	@Value("${bookName.book1.sheet1}")
	private String sheetNamesOfSecondBook;

	@Value("${bookName.book1.sheet1.headers}")
	private String headers[];

	@Autowired
	private FormValidations formValidations;

	@Autowired
	private FieldValidation fieldValidation;

	@Autowired
	private DraftValidations draftValidation;

	@Autowired
	private WriteData write;

	@Autowired
	private CommonLogic logic;

	public Doc validateTwoFiles(MultipartFile file1, MultipartFile file2) throws IOException, ValidationException {
		Doc doc = null;
		Workbook workBook1 = null;
		try {
			
			// Converting multipart file1 to excel book1
//			workBook1 = new HSSFWorkbook(file1.getInputStream());
			workBook1 = logic.getworkBook(file1);
			
			int sheetCount = workBook1.getNumberOfSheets();

			// Converting multipart file2 to excel book2
//			HSSFWorkbook workBook2 = new HSSFWorkbook(file2.getInputStream());
			
			Workbook workBook2 = logic.getworkBook(file2);
			
			// loading sheets
			Sheet formsSheetOfWorkBook2 = workBook2.getSheet("Forms");

			Sheet fieldSheetOfWorkBook2 = workBook2.getSheet("Fields");

			// Creating new workbook for output excel file
			Workbook book = new HSSFWorkbook();
			CellStyle headerStyle = logic.createHeaderCellStyle(book);
			// Creating Form sheet and writing data
			Sheet formSheet = book.createSheet(sheetNameOfFirstBook);
			Row headerRow = formSheet.createRow(0);

			logic.setHeaderCellData(headerRow, headers, headerStyle);

			// loop for multiple input sheets
			for (int i = 14; i < sheetCount; i++) {
				Sheet sheetOneOfWorkBook1 = workBook1.getSheetAt(i);
				if (Objects.nonNull(sheetOneOfWorkBook1)) {
					String sheetName = sheetOneOfWorkBook1.getSheetName();
					// Form & Field Validation
					ResultBook resultData = formValidations.formValidations(sheetOneOfWorkBook1, formsSheetOfWorkBook2);

					List<FieldSheetRow> fieldSheetRow = fieldValidation.fieldSheetValidations(sheetOneOfWorkBook1, fieldSheetOfWorkBook2);
					if (resultData != null) {
						resultData.setFieldSheetRows(fieldSheetRow);

						// Writing the data to output excel file sheets
						write.createResultFile(book, formSheet, resultData, sheetName);
					}
					
				} else {
					throw new ValidationException("Sheet does not exist at index "+i);
				}
			}
			
			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 					book.write(bos);			  
			 byte[] bytes = bos.toByteArray();
			  
			doc = Doc.builder()
					.docName(firstBook)
					.docType(type)
					.data(bytes)
					.build();
			
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public Doc validateDraftSheet(MultipartFile file1) throws IOException, ValidationException  {
		
		// Converting multipart file2 to excel book2
//		Workbook workBook2 = new HSSFWorkbook(file1.getInputStream());
		Workbook workBook2 = logic.getworkBook(file1);
		
		if (Objects.nonNull(workBook2)) {
			
			Sheet fieldSheetOfWorkBook2 = workBook2.getSheet("Fields");
			
			if (Objects.nonNull(fieldSheetOfWorkBook2)) {
				
				Map<String, List<DraftSheetRow>> draftRow = draftValidation.draftLevelValidation(fieldSheetOfWorkBook2);
				// creating Draft sheet and writing data
				if (!draftRow.isEmpty()) {
					Workbook book2 = write.createDraftSheets(draftRow);
					
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					try {
					    book2.write(bos);
					} finally {
					    bos.close();
					}
					byte[] bytes = bos.toByteArray();
					
					Doc doc2 = Doc.builder().docName(secondBook)
							.docType(type)
							.data(bytes)
							.build();
					return doc2;
				} else {
					throw new ValidationException("Validated sheet but could not find any data to generate o/p sheet");
				}
				
			} else {
				throw new ValidationException("Couldn't find Field sheet in input file, Please upload valid file!");
			}
		} else {
			throw new ValidationException("Please upload valid file, it should be .xls or xlsx");
		}		
	}
}