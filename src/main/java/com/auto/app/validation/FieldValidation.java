package com.auto.app.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auto.app.common.CommonLogic;
import com.auto.app.exception.ValidationException;
import com.auto.app.model.FieldSheetRow;

@Component
public class FieldValidation {
	
	@Autowired
	private CommonLogic values;
	
	public List<FieldSheetRow> fieldSheetValidations(Sheet sheetOneOfWorkBook1, Sheet fieldSheetOfWorkBook2) throws ValidationException {
	    
	    int formRowsCount= sheetOneOfWorkBook1.getPhysicalNumberOfRows();
	    
	    int fieldRowsCount = fieldSheetOfWorkBook2.getPhysicalNumberOfRows();
	    String formOID2 = "";
	    String fieldOid1 = "";
	    String fieldOid2 = "";
	    String fieldStatus = "";
	    
	    List<FieldSheetRow> rows = new ArrayList<>();
	    if (formRowsCount > 0) {
	    	Row rowForForm = sheetOneOfWorkBook1.getRow(1);			    
		    Cell formOIdCell = rowForForm.getCell(1);
		    String formOId = values.getCellValue(formOIdCell);
			
		    
			for (int i = 8; i < formRowsCount ; i++) {
				Row rowForField = sheetOneOfWorkBook1.getRow(i);
			    boolean isFieldNotFound = true;
				if(rowForField != null) {
					 Cell fieldCell = rowForField.getCell(1);
					 fieldOid1 = values.getCellValue(fieldCell);
					 
					 for(int j=1; j<fieldRowsCount; j++) {	    		
						Row rowOfFieldSheet = fieldSheetOfWorkBook2.getRow(j);
						Cell formCellOfFieldSheet = rowOfFieldSheet.getCell(0);
						formOID2 = values.getCellValue(formCellOfFieldSheet);
						
						if(formOId.equals(formOID2)) {
							
							Cell fieldCellOfFieldSheet = rowOfFieldSheet.getCell(1);
							fieldOid2 = values.getCellValue(fieldCellOfFieldSheet);
							
							if(fieldOid1.isEmpty() || StringUtils.isBlank(fieldOid1)) {
								continue;
							} else 
							{
								if(fieldOid1.equals(fieldOid2)) {
									fieldStatus = "Field OID is matched";
									isFieldNotFound = false;
									
									FieldSheetRow fieldSheetRow= validateAndSetFieldSheetData(rowForField , rowOfFieldSheet);
									
									if(fieldSheetRow != null) {
										fieldSheetRow.setFormOID(formOId);
										fieldSheetRow.setFieldOid_1(fieldOid1);
										fieldSheetRow.setFieldOid_2(fieldOid2);
										fieldSheetRow.setFieldStatus(fieldStatus);
										
										rows.add(fieldSheetRow);
									}																    	
								}
							}							
						}
						else {
							continue;
						}
					}

					if(isFieldNotFound && !StringUtils.isBlank(fieldOid1)) {
						fieldStatus = "Field OID is not matched";
						org.apache.commons.lang3.StringUtils.isBlank(fieldStatus);
						FieldSheetRow fieldSheetRow = new FieldSheetRow();
						
						fieldSheetRow.setFormOID(formOId);
						fieldSheetRow.setFieldOid_1(fieldOid1);
						fieldSheetRow.setFieldStatus(fieldStatus);
						
						rows.add(fieldSheetRow); 
					}
				
				}
			}		    
		} else {
			throw new ValidationException("While processing Field Validation, spec sheet is empty");
		}
	    return rows;
	}
	
	private FieldSheetRow validateAndSetFieldSheetData(Row rowForField, Row rowOfFieldSheet) {
			
			String formatStatus ="";
			String requiredFieldStatus ="";
			String lowerRangeStatus = "";
			String upperRangeStatus = "";
		
			//validating Format cells data 
		    Cell formatCell_1 = rowForField.getCell(3);
		    String formatCellValue_1 = values.getCellValue(formatCell_1);
		    
		    Cell formatCell_2 = rowOfFieldSheet.getCell(7);
		    String formatCellValue_2 = values.getCellValue(formatCell_2);
		    
			//validating Required field cells data 
		    Cell requiredFieldCell_1 = rowForField.getCell(5);
		    String requiredCellValue_1 = values.getCellValue(requiredFieldCell_1);
		    
		    Cell requiredFieldCell_2 = rowOfFieldSheet.getCell(25);
		    String requiredCellValue_2 = values.getCellValue(requiredFieldCell_2);
		    
		    
			//validating Lower range field cells data 
		    Cell lowerRangeFieldCell_1 = rowForField.getCell(6);
		    String lowerRangeCellValue_1 = values.getCellValue(lowerRangeFieldCell_1);
		    
		    Cell lowerRangeFieldCell_2 = rowOfFieldSheet.getCell(37);
		    String lowerRangeCellValue_2 = values.getCellValue(lowerRangeFieldCell_2);	    

		    
			//validating upper range field cells data 
		    Cell upperRangeFieldCell_1 = rowForField.getCell(7);
		    String upperRangeCellValue_1 = values.getCellValue(upperRangeFieldCell_1);
		    
		    Cell upperRangeFieldCell_2 = rowOfFieldSheet.getCell(38);
		    String upperRangeCellValue_2 = values.getCellValue(upperRangeFieldCell_2);
		    
		    FieldSheetRow fieldRow = null;
		    
		    if((formatCellValue_1.equals(formatCellValue_2)) && (requiredCellValue_1.equals(requiredCellValue_2)) && (lowerRangeCellValue_1.equals(lowerRangeCellValue_2)) && (upperRangeCellValue_1.equals(upperRangeCellValue_2))) {
		    	return fieldRow;
		    }else
		    {
		    	formatStatus = values.getStatus(formatCellValue_1, formatCellValue_2);
		    	requiredFieldStatus = values.getStatus(requiredCellValue_1, requiredCellValue_2);
		    	lowerRangeStatus = values.getStatus(lowerRangeCellValue_1, lowerRangeCellValue_2);
		    	upperRangeStatus = values.getStatus(upperRangeCellValue_1, upperRangeCellValue_2);
		    	

			    fieldRow = new FieldSheetRow();
			    
			    fieldRow.setFormatStatus(formatStatus);
			    fieldRow.setRequiredStatus(requiredFieldStatus);
			    fieldRow.setLowerRangeStatus(lowerRangeStatus);
			    fieldRow.setUpperRangeStatus(upperRangeStatus);
			    
			    return fieldRow;
		    }   
		}
}
