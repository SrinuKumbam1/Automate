package com.auto.app.validation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auto.app.common.CommonLogic;
import com.auto.app.exception.ValidationException;
import com.auto.app.model.FormSheetRow;
import com.auto.app.model.ResultBook;

@Component
public class FormValidations {	
	
	@Autowired
	private CommonLogic values;
	public ResultBook formValidations(Sheet sheetOneOfWorkBook1, Sheet formsSheetOfWorkBook2) throws ValidationException {
		
		ResultBook resultBook = null;
		
		int rowCount1= sheetOneOfWorkBook1.getPhysicalNumberOfRows();
		int rowsCountOfFormSheet= formsSheetOfWorkBook2.getPhysicalNumberOfRows();

		if(rowCount1 > 0) {
		    Row row1 = sheetOneOfWorkBook1.getRow(1);			    
		    
		    Cell oIdCell = row1.getCell(1);
		    String oId = values.getCellValue(oIdCell);
		    
		    Row nameRowOfBook1 = sheetOneOfWorkBook1.getRow(0);
		    Cell nameCellOfBook1 = nameRowOfBook1.getCell(1);
		    String name = values.getCellValue(nameCellOfBook1);
		    
		    Row hepTextRowOfBook1 = sheetOneOfWorkBook1.getRow(2);
		    Cell helpTextCellOfBook1 = hepTextRowOfBook1.getCell(1);
		    String helpText1 = values.getCellValue(helpTextCellOfBook1);
		    
		    Row signRowOfBook1 = sheetOneOfWorkBook1.getRow(3);
		    Cell signCellOfBook1 = signRowOfBook1.getCell(1);
		    String signature1 =  values.getValidSignValue(values.getCellValue(signCellOfBook1));			    
		    
		    String name2 = "";
		    String helpText2 = "";
		    String signature2 = "";
		    
		    if (rowsCountOfFormSheet >= 0) {
		    	//OID, name, helptext and signature validation
			    for (int i = 1; i < rowsCountOfFormSheet; i++) {
			        Row rowOfFormSheet = formsSheetOfWorkBook2.getRow(i);
			        
			        Cell oIdCellOfFormsSheet = rowOfFormSheet.getCell(0);
			        String OId2 = values.getCellValue(oIdCellOfFormsSheet);			       
			        		
			        if(oId.equals(OId2))
			        {
			        	Cell nameCellOfBook2 = rowOfFormSheet.getCell(2);
			        	name2 = values.getCellValue(nameCellOfBook2);
			        	
			        	Cell helpTextCellOfBook2 = rowOfFormSheet.getCell(4);
			        	helpText2 = values.getCellValue(helpTextCellOfBook2);		
			        	
			        	Cell signatureCellOfBook2 = rowOfFormSheet.getCell(6);
			        	signature2 = values.getCellValue(signatureCellOfBook2);
			        	
			        	String nameStatus = values.getStatus(name, name2);
			        	String helpTextStatus = values.getStatus(helpText1, helpText2);
			        	String signatureStatus = values.getStatus(signature1, signature2);
			        	
			        	if (nameStatus.equals("Matched") && helpTextStatus.equals("Matched") && signatureStatus.equals("Matched")) {
			        		resultBook = null;
						} else {
							
							 FormSheetRow formSheetRow = new FormSheetRow();
						        formSheetRow.setOId(oId);
						        formSheetRow.setName_1(name);
						        formSheetRow.setName_2(name2);
						        formSheetRow.setNameStatus(nameStatus);
						        formSheetRow.setHelpText1(helpText1);
						        formSheetRow.setHelpText2(helpText2);			        
						        formSheetRow.setHelpTextStatus(helpTextStatus);
						        formSheetRow.setSignature1(signature1);
						        formSheetRow.setSignature2(signature2);
						        formSheetRow.setSignatureStatus(signatureStatus);
						        
						        resultBook = new ResultBook();
						        if(formSheetRow != null)
						        	resultBook.setFormSheetRow(formSheetRow);
						}   
			        }
			        else
			        {
			        	continue;
			        }
			       
			    }//exit for loop of OID, helptext, signature validation				
			} else {
				throw new ValidationException("Sheet"+ formsSheetOfWorkBook2.getSheetName()+" does not have rows");
			}
		}else {
			throw new ValidationException("Sheet"+ sheetOneOfWorkBook1.getSheetName()+" does not have rows");
		}
		
		return resultBook;
	}

}