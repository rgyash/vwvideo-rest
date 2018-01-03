package com.vwtest.video;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.vwtest.video.domain.Users;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VWUserRegistrationTests {

	@Autowired
	private MockMvc mvc;

	private static String SERVER_HOST = "http://localhost:9022";
	
	@Test
	public void testRegistration() throws Exception {
		Users testUser = new Users("Test1", "Test1", "Test1@gmail.com", Arrays.asList("USER"));
		mvc.perform(post(SERVER_HOST + "/rest/account")
						.param("username", "Test1")
						.param("password", "password")
						.param("email", "Test1@domain.com"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo(testUser.getName()))).andReturn();
	}
	
	@Test
	public void testUserRetrieval() throws Exception {
		Users testUser = new Users("User1", "User1", "User1@gmail.com", Arrays.asList("USER"));
		
		mvc.perform(get(SERVER_HOST + "/rest/account/User1")
				.with(httpBasic("User1","User1"))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", equalTo(testUser.getName()))).andReturn();
		}
	
}
