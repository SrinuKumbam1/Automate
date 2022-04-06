package com.auto.app.write;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auto.app.common.CommonLogic;
import com.auto.app.model.DraftSheetRow;
import com.auto.app.model.FieldSheetRow;
import com.auto.app.model.FormSheetRow;
import com.auto.app.model.ResultBook;

@Component
public class WriteData {
	
	@Value("${bookName.book1.nextSheets.headers}")
	private String headersForFieldSheet[];
	
	@Value("${bookName.book2.sheetNames}")
	private String sheetNamesOfSecondBook[];
	
	@Value("${bookName.book2.sheetNames.headers}")
	private String headersForDraftSheets[];
	
	@Value("${draftDataKeys}")
	private String draftDataKeys[];
	
	@Autowired
	private CommonLogic logic;
	
	public void createResultFile(Workbook book, Sheet formSheet, ResultBook resultBook, String sheetName) throws IOException {
		try { 
			
        CellStyle headerStyle =logic.createHeaderCellStyle(book);

        writeFormSheetRow(formSheet, resultBook.getFormSheetRow());
        
		//Creating Field sheet and writing data
		Sheet fieldSheet = book.createSheet(sheetName);
		Row headerRowForFieldSheet = fieldSheet.createRow(0);
		
		logic.setHeaderCellData(headerRowForFieldSheet, headersForFieldSheet, headerStyle);
		writeFieldSheetData(fieldSheet, resultBook.getFieldSheetRows() );
	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void writeFormSheetRow(Sheet formSheet, FormSheetRow formSheetRow) {
		int numberOfRows = formSheet.getPhysicalNumberOfRows();
			Row row = formSheet.createRow(numberOfRows);
			row.createCell(0).setCellValue(formSheetRow.getOId());
			row.createCell(1).setCellValue(formSheetRow.getName_1());
			row.createCell(2).setCellValue(formSheetRow.getName_2());
			row.createCell(3).setCellValue(formSheetRow.getNameStatus());
			row.createCell(4).setCellValue(formSheetRow.getHelpText1());
			row.createCell(5).setCellValue(formSheetRow.getHelpText2());
			row.createCell(6).setCellValue(formSheetRow.getHelpTextStatus());
			row.createCell(7).setCellValue(formSheetRow.getSignature1());
			row.createCell(8).setCellValue(formSheetRow.getSignature2());
			row.createCell(9).setCellValue(formSheetRow.getSignatureStatus());		
	}

	private void writeFieldSheetData(Sheet fieldSheet, List<FieldSheetRow> fieldSheetRows) {
		
		int rows = 1;
		for (FieldSheetRow fieldSheetRow : fieldSheetRows) {
			Row row = fieldSheet.createRow(rows);
			row.createCell(0).setCellValue(fieldSheetRow.getFormOID());
			row.createCell(1).setCellValue(fieldSheetRow.getFormOIDStatus());
			row.createCell(2).setCellValue(fieldSheetRow.getFieldOid_1());
			row.createCell(3).setCellValue(fieldSheetRow.getFieldOid_2());
			row.createCell(4).setCellValue(fieldSheetRow.getFieldStatus());
			row.createCell(5).setCellValue(fieldSheetRow.getFormatStatus());
			row.createCell(6).setCellValue(fieldSheetRow.getRequiredStatus());
			row.createCell(7).setCellValue(fieldSheetRow.getLowerRangeStatus());
			row.createCell(8).setCellValue(fieldSheetRow.getUpperRangeStatus());
			rows++;
		}		
	}

	public Workbook createDraftSheets(Map<String, List<DraftSheetRow>> map) {
		
		Workbook book = new HSSFWorkbook(); 
        CellStyle headerStyle =logic.createHeaderCellStyle(book);
		
		for(int i=0; i<draftDataKeys.length; i++) {
			
			List<DraftSheetRow> draftSheetRows = map.get(draftDataKeys[i]);
			if (Objects.nonNull(draftSheetRows)) {
				Sheet draftSheet = book.createSheet(sheetNamesOfSecondBook[i]);
				Row headerRowForDraftDateSheet = draftSheet.createRow(0);
				logic.setHeaderCellData(headerRowForDraftDateSheet, headersForDraftSheets, headerStyle);
				
				writeDraftSheetData(draftSheet, draftSheetRows);				
			} else {
				continue;
			}
		}		
		return book;
	}
	
	private void writeDraftSheetData(Sheet draftSheet, List<DraftSheetRow> draftSheetRows) {
		int i = 1;
		for (DraftSheetRow draftSheetRow : draftSheetRows) {
			Row row = draftSheet.createRow(i);
			row.createCell(0).setCellValue(draftSheetRow.getFormOID());
			row.createCell(1).setCellValue(draftSheetRow.getFieldOID());
			i++;
		}
	}

}
