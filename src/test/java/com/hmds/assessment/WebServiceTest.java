/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.hmds.assessment;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for HMDS message API framework.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebServiceTest {

	// the mock model/view/controller with which to run the web service
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Test that a greeting message properly returns the correct greeting.
	 * @throws Exception
	 */
	@Test
	public void shouldReturnGreetingMessage() throws Exception {

		final String TYPE = "greeting";

		// retrieve the expected message from the properties file
		Properties messages = new Properties();
    	messages.load(WebService.class.getClassLoader().getResourceAsStream("messages.properties"));
		String expectedMessage = messages.getProperty(TYPE);

		// call the service and assert that it responds ok with the correct
		// message
		this.mockMvc.perform(get("/message/greeting"))
            .andDo(print())
            .andExpect(status().isOk())
			.andExpect(jsonPath("$.type", is(TYPE)))
			.andExpect(jsonPath("$.value", is(expectedMessage)));
	}
}