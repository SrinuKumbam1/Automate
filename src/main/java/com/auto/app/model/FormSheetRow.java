package com.auto.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormSheetRow {

	private String oId;
	private String name_1;
	private String name_2;
	private String nameStatus;
	private String helpText1;
	private String helpText2;
	private String helpTextStatus;
	private String signature1;
	private String signature2;
	private String signatureStatus;
}
