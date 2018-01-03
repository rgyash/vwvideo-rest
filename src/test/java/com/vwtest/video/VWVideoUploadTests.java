package com.vwtest.video;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({ "test" })
public class VWVideoUploadTests {

	@Autowired
	private MockMvc mvc;
	
	private static String SERVER_HOST = "http://localhost:9022";

	@Test
	public void testFileUpload() throws Exception {
		File file = new File("./files/big_buck_bunny.mp4");
		MockMultipartFile mockFile= new MockMultipartFile("file", "orig", "video/mp4", FileUtils.readFileToByteArray(file));
		mvc.perform(MockMvcRequestBuilders.fileUpload(SERVER_HOST + "/rest/file")
                		.file(mockFile)
						.with(httpBasic("User1","User1"))
						.param("fileName", "file3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.Content-Type", equalTo("video/mp4"))).andReturn();
	}
	
	@Test
	public void testFileDownload() throws Exception {
		mvc.perform(get(SERVER_HOST + "/rest/file/file3")
				.with(httpBasic("User1","User1"))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		}
	

	@Test
	public void testInvalidFileUpload() throws Exception {
		File file = new File("./files/wallpaper-06.jpg");
		MockMultipartFile mockFile= new MockMultipartFile("file", "orig", null, FileUtils.readFileToByteArray(file));
		mvc.perform(MockMvcRequestBuilders.fileUpload(SERVER_HOST + "/rest/file")
                		.file(mockFile)
						.with(httpBasic("User1","User1"))
						.param("fileName", "file4"))
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString("Only files of these types are allowed to be uploaded"))).andReturn();
	}

	/// Same way this test can run for invalid file ( file greater than 10 mins)	
}
