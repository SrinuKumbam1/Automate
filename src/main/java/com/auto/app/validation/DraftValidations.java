package com.auto.app.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auto.app.common.CommonLogic;
import com.auto.app.exception.ValidationException;
import com.auto.app.model.DraftSheetRow;

@Component
public class DraftValidations {
	
	@Value("${draftDataKeys}")
	private String draftDataKeys[];
	
	@Autowired
	private CommonLogic values;
	
	public Map<String, List<DraftSheetRow>> draftLevelValidation(Sheet fieldSheetOfWorkBook2) throws ValidationException {
		int rowCount = fieldSheetOfWorkBook2.getPhysicalNumberOfRows();
		if (rowCount > 0) {
			
			Map<String, List<DraftSheetRow>> draft = new HashMap<>();
			
			List<DraftSheetRow> draftDateSheetRow = new ArrayList<DraftSheetRow>();
			List<DraftSheetRow> draftNumericSheetRow = new ArrayList<DraftSheetRow>();
			List<DraftSheetRow> draftFutureDateSheetRow = new ArrayList<DraftSheetRow>();
			List<DraftSheetRow> draftFieldActiveSheetRow = new ArrayList<DraftSheetRow>();
			List<DraftSheetRow> draftFormActiveSheetRow = new ArrayList<>();
			List<DraftSheetRow> draftFormRestrictSheetRow = new ArrayList<>();
			List<DraftSheetRow> draftRedirectPropertySheetRow = new ArrayList<>();
			List<DraftSheetRow> draftLabelFieldSheetRow = new ArrayList<>();
			List<DraftSheetRow> draftCheckboxFieldSheetRow = new ArrayList<>();
			
			for(int i=1; i<rowCount; i++) {
				
				Row fieldSheetRow = fieldSheetOfWorkBook2.getRow(i);
				
				DraftSheetRow draftDateRow = validateDateField(fieldSheetRow);
				
				if(draftDateRow != null) {
					draftDateSheetRow.add(draftDateRow);				
				}
				
				DraftSheetRow draftNumericRow = validateNumericField(fieldSheetRow);
				
				if(draftNumericRow != null) {
					draftNumericSheetRow.add(draftNumericRow);				
				}
				
				DraftSheetRow draftFutureDateRow = validateFutureDateField(fieldSheetRow);
				
				if(draftFutureDateRow != null) {
					draftFutureDateSheetRow.add(draftFutureDateRow);				
				}
				
				DraftSheetRow draftFieldActiveRow = validateFieldActiveField(fieldSheetRow);
				
				if(draftFieldActiveRow != null) {
					draftFieldActiveSheetRow.add(draftFieldActiveRow);				
				}
				
				DraftSheetRow draftFormActiveRow = validateFormActiveField(fieldSheetRow);
				
				if(draftFormActiveRow != null) {
					draftFormActiveSheetRow.add(draftFormActiveRow);				
				}
				
				DraftSheetRow draftFormRestrictRow = validateFormRestriction(fieldSheetRow);
				
				if(draftFormRestrictRow != null) {
					draftFormRestrictSheetRow.add(draftFormRestrictRow);				
				}
				
				DraftSheetRow draftRedirectPropertytRow = validateRedirectProperty(fieldSheetRow);
				
				if(draftRedirectPropertytRow != null) {
					draftRedirectPropertySheetRow.add(draftRedirectPropertytRow);				
				}
				
				DraftSheetRow draftLabelFieldRow = validateLabelField(fieldSheetRow);
				
				if(draftLabelFieldRow != null) {
					draftLabelFieldSheetRow.add(draftLabelFieldRow);				
				}
				
				DraftSheetRow draftCheckboxFieldRow = validateCheckboxField(fieldSheetRow);
				
				if(draftCheckboxFieldRow != null) {
					draftCheckboxFieldSheetRow.add(draftCheckboxFieldRow);				
				}
			}
			
			
			if (!draftDateSheetRow.isEmpty()) {
				draft.put(draftDataKeys[0], draftDateSheetRow);				
			}
			
			if (!draftNumericSheetRow.isEmpty()) {
				draft.put(draftDataKeys[1], draftNumericSheetRow);			
			}
			
			if (!draftFutureDateSheetRow.isEmpty()) {
				draft.put(draftDataKeys[2], draftFutureDateSheetRow);			
			}
			
			if (!draftFieldActiveSheetRow.isEmpty()) {
				draft.put(draftDataKeys[3], draftFieldActiveSheetRow);			
			}
			
			if (!draftFormActiveSheetRow.isEmpty()) {
				draft.put(draftDataKeys[4], draftFormActiveSheetRow);			
			}
			
			if (!draftFormRestrictSheetRow.isEmpty()) {
				draft.put(draftDataKeys[5], draftFormRestrictSheetRow);			
			}
			
			if (!draftRedirectPropertySheetRow.isEmpty()) {
				draft.put(draftDataKeys[6], draftRedirectPropertySheetRow);		
			}
			
			if (!draftLabelFieldSheetRow.isEmpty()) {
				draft.put(draftDataKeys[7], draftLabelFieldSheetRow);	
			}
			
			if (!draftCheckboxFieldSheetRow.isEmpty()) {
				draft.put(draftDataKeys[8], draftCheckboxFieldSheetRow);
			}
			
			System.out.println(draft);
			return draft;
			
		} else {
			throw new ValidationException("Field Sheet have zero rows, No rows are found for validate.");
		}
		
	}

	public DraftSheetRow validateDateField(Row fieldSheetRow) throws ValidationException {
		
		if (Objects.nonNull(fieldSheetRow)) {
			DraftSheetRow draftRow =null;
			
			Cell controlTypeCell = fieldSheetRow.getCell(11);
			String controlTypeValue = values.getCellValue(controlTypeCell);
			
			Cell breakSignCell = fieldSheetRow.getCell(36);
			String breakSignValue = values.getCellValue(breakSignCell);
			
			Cell queryNonConformanceCell = fieldSheetRow.getCell(30);
			String queryNonConformanceValue = values.getCellValue(queryNonConformanceCell);
			
			if((controlTypeValue.equals("DateTime")) && (breakSignValue.equals("FALSE")) && (!queryNonConformanceValue.equals("TRUE"))) {
				
				Cell formOIDCell = fieldSheetRow.getCell(0);
				String formOIDValue = values.getCellValue(formOIDCell);
				
				Cell fieldOIDCell = fieldSheetRow.getCell(1);
				String fieldOIDValue = values.getCellValue(fieldOIDCell);
				draftRow = new DraftSheetRow(formOIDValue, fieldOIDValue);
			}
			
			return draftRow;
		} else {
			String message = "Validate date field row does have data! (row number = "+fieldSheetRow.getRowNum()+")";
			throw new ValidationException(message);
		}
	}

	public DraftSheetRow validateNumericField(Row fieldSheetRow) {
		
		DraftSheetRow draftNumericRow = null;
		
		Cell controlTypeCell = fieldSheetRow.getCell(11);
		String controlTypeValue = values.getCellValue(controlTypeCell);

		Cell dataFormatCell = fieldSheetRow.getCell(7);
		String dataFormatValue = values.getCellValue(dataFormatCell);
		
		Cell breakSignCell = fieldSheetRow.getCell(36);
		String breakSignValue = values.getCellValue(breakSignCell);
		
		Cell queryNonConformanceCell = fieldSheetRow.getCell(30);
		String queryNonConformanceValue = values.getCellValue(queryNonConformanceCell);
		
		if((controlTypeValue.equals("Text") || controlTypeValue.equals("LongText")) && ((!dataFormatValue.contains("$")) && (!StringUtils.isBlank(dataFormatValue))) && (breakSignValue.equals("FALSE")) && (!queryNonConformanceValue.equals("TRUE"))) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			Cell fieldOIDCell = fieldSheetRow.getCell(1);
			String fieldOIDValue = values.getCellValue(fieldOIDCell);
			draftNumericRow = new DraftSheetRow(formOIDValue, fieldOIDValue);
		}
		
		return draftNumericRow;
	}

	private DraftSheetRow validateFutureDateField(Row fieldSheetRow) {
		DraftSheetRow draftFutureDateRow = null;
		
		Cell controlTypeCell = fieldSheetRow.getCell(11);
		String controlTypeValue = values.getCellValue(controlTypeCell);

		Cell dataFormatCell = fieldSheetRow.getCell(7);
		String dataFormatValue = values.getCellValue(dataFormatCell);
		
		Cell breakSignCell = fieldSheetRow.getCell(36);
		String breakSignValue = values.getCellValue(breakSignCell);
		
		Cell queryFutureDateCell = fieldSheetRow.getCell(25);
		String queryFutureDateValue = values.getCellValue(queryFutureDateCell);
		
		if((controlTypeValue.equals("DateTime")) && ((!dataFormatValue.equalsIgnoreCase("HH:nn")) && (!dataFormatValue.equalsIgnoreCase("HH:nn:ss"))) && (breakSignValue.equals("FALSE")) && (!queryFutureDateValue.equals("TRUE"))) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			Cell fieldOIDCell = fieldSheetRow.getCell(1);
			String fieldOIDValue = values.getCellValue(fieldOIDCell);
			draftFutureDateRow = new DraftSheetRow(formOIDValue, fieldOIDValue);
		}
		
		return draftFutureDateRow;
	}

	private DraftSheetRow validateFieldActiveField(Row fieldSheetRow) {
		DraftSheetRow draftFieldActiveRow = null;
		Cell draftFieldActiveCell = fieldSheetRow.getCell(5);
		String draftFieldActive = values.getCellValue(draftFieldActiveCell);
		
		if(draftFieldActive.equals("FALSE")) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			Cell fieldOIDCell = fieldSheetRow.getCell(1);
			String fieldOIDValue = values.getCellValue(fieldOIDCell);
			
			draftFieldActiveRow = new DraftSheetRow(formOIDValue, fieldOIDValue);
		}
		
		return draftFieldActiveRow;
	}
	
	private DraftSheetRow validateFormActiveField(Row fieldSheetRow) {
		
		DraftSheetRow draftFormActiveRow = null;
		Cell draftFieldNumberCell = fieldSheetRow.getCell(3);
		String draftFieldNumberValue = values.getCellValue(draftFieldNumberCell);
		
		if(draftFieldNumberValue.equals("FALSE")) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			draftFormActiveRow = DraftSheetRow.builder()
								.formOID(formOIDValue).build();
		}
		
		return draftFormActiveRow;
	}

	private DraftSheetRow validateFormRestriction(Row fieldSheetRow) {
		
		DraftSheetRow draftFormRestrictRow = null;
		
		Cell draftVariableOIDCell = fieldSheetRow.getCell(6);
		String draftVariableOIDValue = values.getCellValue(draftVariableOIDCell);
		
		Cell draftDataDictionaryNameCell = fieldSheetRow.getCell(8);
		String draftDataDictionaryNameValue = values.getCellValue(draftDataDictionaryNameCell);
		
		Cell draftUnitDictionaryNameCell = fieldSheetRow.getCell(9);
		String draftUnitDictionaryNameValue = values.getCellValue(draftUnitDictionaryNameCell);
		
		if((draftVariableOIDValue.equals("FALSE")) && (StringUtils.isBlank(draftDataDictionaryNameValue) && StringUtils.isBlank(draftUnitDictionaryNameValue))) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			draftFormRestrictRow = DraftSheetRow.builder().formOID(formOIDValue).build();
		}
		
		return draftFormRestrictRow;
	}

	private DraftSheetRow validateRedirectProperty(Row fieldSheetRow) {
		DraftSheetRow draftRedirectPropertyRow = null;
		
		Cell draftAcceptableFileExtensionsCell = fieldSheetRow.getCell(6);
		String draftAcceptableFileExtensionsCellValue = values.getCellValue(draftAcceptableFileExtensionsCell);
		
		if(!draftAcceptableFileExtensionsCellValue.equals("NoLink")) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			draftRedirectPropertyRow = DraftSheetRow.builder().formOID(formOIDValue).build();
		}
		
		return draftRedirectPropertyRow;
	}

	private DraftSheetRow validateLabelField(Row fieldSheetRow) {
		
		DraftSheetRow draftLabelRow = null;
		
		Cell draftFieldOIDCell = fieldSheetRow.getCell(1);
		String draftFieldOIDValue = values.getCellValue(draftFieldOIDCell);
		
		Cell draftDoesNotBreakSignatureCell = fieldSheetRow.getCell(36);
		String draftDoesNotBreakSignatureValue = values.getCellValue(draftDoesNotBreakSignatureCell);
		
		if(draftFieldOIDValue.equals("LABEL") && draftDoesNotBreakSignatureValue.equals("FALSE")) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			draftLabelRow = new DraftSheetRow(formOIDValue, draftFieldOIDValue);
		}
		
		return draftLabelRow;
	}

	private DraftSheetRow validateCheckboxField(Row fieldSheetRow) {
		
		DraftSheetRow draftCheckboxRow = null;
		
		Cell draftControlTypeCell = fieldSheetRow.getCell(11);
		String draftControlTypeValue = values.getCellValue(draftControlTypeCell);
		
		Cell draftDataFormatCell = fieldSheetRow.getCell(7);
		String draftDataFormatValue = values.getCellValue(draftDataFormatCell);
		
		if(draftControlTypeValue.equals("Checkbox") && !draftDataFormatValue.equals("1")) {
			
			Cell formOIDCell = fieldSheetRow.getCell(0);
			String formOIDValue = values.getCellValue(formOIDCell);
			
			Cell fieldOIDCell = fieldSheetRow.getCell(1);
			String fieldOIDValue = values.getCellValue(fieldOIDCell);
			
			draftCheckboxRow = new DraftSheetRow(formOIDValue, fieldOIDValue);
		}
		
		return draftCheckboxRow;
	}
}
