package com.auto.app.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultBook {
	
	private FormSheetRow formSheetRow;
	private List<FieldSheetRow> fieldSheetRows;
}