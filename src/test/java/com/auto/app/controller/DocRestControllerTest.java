package com.auto.app.controller;

import java.io.File;
import java.io.FileInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import com.auto.app.model.Doc;
import com.auto.app.service.ExcelSheetService;

@WebMvcTest(DocRestController.class)
public class DocRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ExcelSheetService service;

	private Doc doc;

	@BeforeEach
	void setUp() {
		doc = Doc.builder().data(null).docName("Test").docType("application/vnd.ms-excel").build();
	}

	@Test
	void uploadFiles() throws Exception {
		MultipartFile file1 = new MockMultipartFile("file1.xlsx", new FileInputStream(new File(
				"C:\\Users\\2066653\\OneDrive - Cognizant\\Documents\\Copy of Mxx-xxx_EDC_Specification_Vx.xx (Template V2.0)_Conference Room Pilot Feb2022.xls")));
		MultipartFile file2 = new MockMultipartFile("file2.xlsx", new FileInputStream(new File(
				"C:\\Users\\2066653\\OneDrive - Cognizant\\Documents\\Foundational_Foundational_Standards_Version_4.0.xls")));

		Mockito.when(service.validateTwoFiles(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(doc);
		MockHttpServletRequestBuilder  builder=MockMvcRequestBuilders.post("/api/uploadFiles");
		builder.contentType(MediaType.MULTIPART_FORM_DATA);
		MockHttpServletRequest  request=builder.buildRequest(null);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/uploadFiles").contentType(MediaType.MULTIPART_FORM_DATA))
				
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void uploadFileForDraftValidation() throws Exception {
		MultipartFile file2 = new MockMultipartFile("file2.xlsx", new FileInputStream(new File(
				"C:\\Users\\2066653\\OneDrive - Cognizant\\Documents\\Foundational_Foundational_Standards_Version_4.0.xls")));

		Mockito.when(service.validateDraftSheet(file2)).thenReturn(doc);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/uploadFile").contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
