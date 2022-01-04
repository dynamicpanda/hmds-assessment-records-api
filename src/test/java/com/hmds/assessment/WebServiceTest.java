/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.hmds.assessment;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.jayway.jsonpath.JsonPath;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Test class for HMDS record API framework. Tests GET, PUT, DELETE with
 * negative tests for bad response codes.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebServiceTest {

	/** The mock model/view/controller with which to run the web service */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Cleans up after tests are run by removing all records in the records
	 * folder.
	 * 
	 * @throws Exception Raised if any exception occurs during teardown.
	 */
	@AfterClass
	public static void teardown() throws Exception {

		// determine the path of the records folder based on the current folder
		Path currentPath = Paths.get(System.getProperty("user.dir"));
        File recordsFolder = new File(Paths.get(currentPath.toString(), "records").toString());

		// delete all files in the records folder
		for (String recordFile : recordsFolder.list()) {
            (new File(recordFile)).delete();
        }
	}

	/**
	 * Tests that a record can be retrieved successfully by ID.
	 * 
	 * @throws Exception Raised if the test encounters any exception.
	 */
	@Test
	public void getsRecordDataById() throws Exception {

		// create an empty JSON object to use as a request body
        String requestBody = (new JSONObject("{}")).toString();

		// create a new record to get a valid ID
        MvcResult result = mockMvc.perform(put("/record")
		    .content(requestBody))
			.andExpect(status().isCreated())
			.andReturn();
		
		// get the ID from the response body
		String responseBody = result.getResponse().getContentAsString();
		String id = new JSONObject(responseBody).getString("id");

		// call the service and assert that it responds ok with the correct
		// record data
		this.mockMvc.perform(get("/record/" + id))
            .andDo(print())
            .andExpect(status().isOk())
			.andExpect(jsonPath("$.recordData", equalTo(JsonPath.read(requestBody, "$"))))
			.andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
	}

	/**
	 * Tests that a GET request for a missing record correctly responds with a
	 * NOT FOUND.
	 * 
	 * @throws Exception Raised if the test encounters any exception.
	 */
	@Test
	public void getMissingRecordRespondsNotFound() throws Exception {
		// call the service and assert that it responds with a not found code
		this.mockMvc.perform(get("/record/missing"))
            .andDo(print())
            .andExpect(status().isNotFound());
	}

	/**
	 * Test that a put of an empty record is successfully created.
	 * 
	 * @throws Exception Raised if the test encounters any exception.
	 */
	@Test
	public void createsEmptyRecordSuccessfully() throws Exception {

        String requestBody = (new JSONObject("{}")).toString();

		// call the service and assert that it response ok and contains an ID
        mockMvc.perform(put("/record")
		    .content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", notNullValue()));
	}

	/**
	 * Test that a put of a populated record is successfully created.
	 * 
	 * @throws Exception Raised if the test encounters any exception.
	 */
	@Test
	public void createsPopulatedRecordSuccessfully() throws Exception {

        String requestBody = (new JSONObject(
			"{\"key1\": \"value1\", \"array1\": [\"arrayValue1\"]}"
		)).toString();

		// call the service and assert that it responds okay and contains an ID
        mockMvc.perform(put("/record")
		    .content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", notNullValue()));
	}

	/**
	 * Test that a put of a record with invalid JSON is rejected with a bad
	 * request response.
	 * 
	 * @throws Exception Raised if the test encounters any exception.
	 */
	@Test
	public void putRejectsInvalidJson() throws Exception {

        String requestBody = "invalid";

		// call the service and assert that it response with a bad request code
        mockMvc.perform(put("/record")
		    .content(requestBody))
			.andExpect(status().isBadRequest());
	}

	/**
	 * Tests that a record can be deleted successfully by ID.
	 * 
	 * @throws Exception Raised if the test encounters any exception.
	 */
	@Test
	public void deletesRecordDataByIdSuccessfully() throws Exception {

        String requestBody = (new JSONObject("{}")).toString();

		// create a new record to get a valid ID to use
        MvcResult result = mockMvc.perform(put("/record")
		    .content(requestBody))
			.andExpect(status().isCreated())
			.andReturn();
		
		// get the ID from the response body
		String responseBody = result.getResponse().getContentAsString();
		String id = new JSONObject(responseBody).getString("id");

		// call the service and assert that it responds ok
		this.mockMvc.perform(delete("/record/" + id))
            .andDo(print())
            .andExpect(status().isOk());
		
		// assert that the record no longer exists
		this.mockMvc.perform(get("/record/" + id))
		   .andDo(print())
		   .andExpect(status().isNotFound());
	}

	/**
	 * Tests that a DELETE request for a missing record correctly responds with
	 * a NOT FOUND.
	 * 
	 * @throws Exception Raised if the test encounters any exception.
	 */
	@Test
	public void deleteMissingRecordRespondsNotFound() throws Exception {
		// call the service and assert that it responds ok with not found
		this.mockMvc.perform(get("/record/missing"))
            .andDo(print())
            .andExpect(status().isNotFound());
	}
}