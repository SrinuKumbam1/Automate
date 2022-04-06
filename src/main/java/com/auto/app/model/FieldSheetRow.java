package com.auto.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldSheetRow {
	private String formOID;
	private String formOIDStatus;
	private String fieldOid_1;
	private String fieldOid_2;
	private String fieldStatus;
	private String formatStatus;
	private String requiredStatus;
	private String lowerRangeStatus;
	private String upperRangeStatus;
}
