package com.auto.app.common;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.auto.app.exception.ValidationException;

@Component
public class CommonLogic {

	@SuppressWarnings("deprecation")
	public String getCellValue(Cell controlTypeCell) {
		 if (controlTypeCell != null) {
			 controlTypeCell.setCellType(CellType.STRING);
		     return (controlTypeCell.getStringCellValue()); 
		 }
		 return "";
	}
	
	public String getValidSignValue(String sign) {
		if(sign.equalsIgnoreCase("yes")) {
			return "True";
		}else
		{
			return "False";
		}
	}
	
	public CellStyle createHeaderCellStyle(Workbook book) {
		Font headerFont = book.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)12);
		headerFont.setColor(IndexedColors.BLACK.index);
		//Create a CellStyle with the font
		CellStyle headerStyle = book.createCellStyle();
		headerStyle.setFont(headerFont);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		
		return headerStyle;
	}
	
	public void setHeaderCellData(Row headerRow, String headers[], CellStyle headerStyle) {
		for(int i=0; i< headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}		
	}
	
	public String getStatus(String formatCellValue_1, String formatCellValue_2) {
		if (formatCellValue_1.equalsIgnoreCase(formatCellValue_2)) {
			return "Matched";
		} else {
//			System.out.println("Value from book 1 = " +formatCellValue_1 + " Value from Book2 = " +formatCellValue_2);
			return "Not matched";
		}
	}
	
	public String getFileExtension(MultipartFile file1) {
		String name1 = file1.getOriginalFilename();
		char[] c = name1.toCharArray();
		String s = "";
		for(int i=c.length-1; i>0; i--) {
			if(c[i] == '.') {
				break;
			}
			else {
				s = s+c[i];
			}
		}

		StringBuffer sb = new StringBuffer(s);
		return sb.reverse().toString();
	}
	
	public Workbook getworkBook(MultipartFile file) throws IOException, ValidationException {
		String fileExtension = getFileExtension(file);
		
		if (fileExtension.equalsIgnoreCase("xls") || fileExtension.equalsIgnoreCase("xlsx")) {
			
			Workbook book = new HSSFWorkbook(file.getInputStream());
			return book;			
		}
		else {
			throw new ValidationException(file.getOriginalFilename()+" is not valid format, file should be in xlsx or xls format");
		}
	}
	
/*	public boolean isBlankString(String value) {
		char[] characters = value.toCharArray();
		boolean status = true;
		for(int i = 0; i<characters.length; i++) {
			if (characters[i] != ' ') {
				status = false;
				break;
			}
		}
		return status;
	}*/
}
