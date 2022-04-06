package com.auto.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.auto.app.exception.DocumentNotFoundException;
import com.auto.app.exception.ValidationException;
import com.auto.app.model.Doc;
import com.auto.app.validation.DraftValidations;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class ExcelSheetServiceImplTest {

    @Autowired
	private ExcelSheetService service;
    
    @InjectMocks
    private DraftValidations draftValidation;
	
	private MultipartFile file1;
	private MultipartFile file2;

	@BeforeEach
	void setUp() throws IOException {
		
		Doc doc = Doc.builder()
				.docName("Result")
				.docType("application/vnd.ms-excel")
				.build();
		
//		Mockito.when(docService.saveExcelFile(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(doc);
	}

    @Test
    void testValidateDraftSheet() throws IOException, ValidationException, InvalidFormatException {
        String fileName = "Draft";
        String fileType = "application/vnd.ms-excel";

        file2 = new MockMultipartFile("file2.xls", new FileInputStream(new File("C:\\Users\\2066653\\OneDrive - Cognizant\\Documents\\Foundational_Foundational_Standards_Version_4.0.xls")));
 
		Doc doc = service.validateDraftSheet(file2);
		
		assertEquals(fileName, doc.getDocName());
		assertEquals(fileType, doc.getDocType());
    }

    @Test
    void testValidateTwoFiles() throws FileNotFoundException, IOException, ValidationException {
    	String fileName = "Result";
        String fileType = "application/vnd.ms-excel";
        
        file1 = new MockMultipartFile("file1.xlsx", new FileInputStream(new File("C:\\Users\\2066653\\OneDrive - Cognizant\\Documents\\Copy of Mxx-xxx_EDC_Specification_Vx.xx (Template V2.0)_Conference Room Pilot Feb2022.xls")));
    	file2 = new MockMultipartFile("file2.xls", new FileInputStream(new File("C:\\Users\\2066653\\OneDrive - Cognizant\\Documents\\Foundational_Foundational_Standards_Version_4.0.xls")));
        
		Doc doc = service.validateTwoFiles(file1, file2);
		
		assertEquals(fileName, doc.getDocName());
		assertEquals(fileType, doc.getDocType());
    }
}
