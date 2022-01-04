package com.hmds.assessment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot REST controller for the HMDS records API. Exposes endpoints for
 * accessing records on the system.
 */
@RestController
public class WebService {

    /**
     * Exposes GET request to retrieve a record based on a record ID provided
     * in the URI. Returns the content of the record's file.
     * 
     * @param id The ID of the record to retrieve. Provided in the URI path.
     * @return The record data stored for the record as a ResponseEntity.
     * @throws IOException Raised if there is a problem reading the record's file.
     */
    @GetMapping("/record/{id}")
    @ResponseBody
    public ResponseEntity<String> getRecord(@PathVariable String id) throws IOException {    
  
        // get the current working path of the application and define the JSON path
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path jsonFilePath = Paths.get(currentPath.toString(), "records", id + ".json");
    
        // if the JSON file exists, retrieve its contents
        if ((new File(jsonFilePath.toString())).exists()) {
    
            // construct a JSON response to send back which includes the record data
            JSONObject json = new JSONObject();
            json.put("recordData", new JSONObject(Files.readString(jsonFilePath)));
    
            // define the JSON response content type
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Content-Type", "application/json");
    
            // return the successful response with the record data
            return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(json.toString());
        }
        // if a file does not exist for the record, return a 404
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Record %s not found.", id));
        }
    }
  
    /**
     * Exposes PUT request to add a new record to the system. Returns the ID
     * of the newly created record.
     * 
     * @param body The request body of the PUT request.
     * @return A JSON response containing the ID of the newly created record.
     * @throws IOException Raised if there is a problem writing the record's file.
     */
    @PutMapping("/record")
    @ResponseBody
    public ResponseEntity<String> putRecord(@RequestBody String body) throws IOException {    
  
        // ensure the body is valid JSON
        JSONObject json;
        try {
            json = new JSONObject(body);
        } 
        // otherwise respond with a 400
        catch (JSONException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Record data is not valid JSON");
        }
    
        // generate a unique ID for the record
        String id = UUID.randomUUID().toString();
    
        // determine the path of the JSON file based on the current working folder
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path recordsFolderPath = Paths.get(currentPath.toString(), "records");
        Path jsonFilePath = Paths.get(recordsFolderPath.toString(), id + ".json");
    
        // create the record folder if one does not exist
        File recordsFolder = new File(recordsFolderPath.toString());
        if (!recordsFolder.exists())
            recordsFolder.mkdirs();
    
        // write the record JSON data to a file
        FileWriter file = new FileWriter(jsonFilePath.toString());
        file.write(json.toString());
        file.close();
    
        // prepare the response JSON containing the record ID
        JSONObject response = new JSONObject();
        response.put("id", id);
    
        // define the JSON response content type
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");
    
        // respond with the record ID
        return ResponseEntity.status(HttpStatus.CREATED)
            .headers(responseHeaders)
            .body(response.toString());
    }
  
    /**
     * Exposes DELETE request to delete a record based on the record ID provided.
     * Removes the file associated with that record ID.
     * 
     * @param id The ID of the record to delete. Provided in the URI path.
     * @return an empty ResponseEntity indicated a successful response.
     * @throws IOException Raised if there is a problem deleting the record's
     * file.
     */
    @DeleteMapping("/record/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteRecord(@PathVariable String id) throws IOException {    
  
        // determine the path of the JSON file to delete based on the current folder
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path jsonFilePath = Paths.get(currentPath.toString(), "records", id + ".json");
    
        // if the file exists, delete it
        File jsonFile = new File(jsonFilePath.toString());
        if (jsonFile.exists()) {
    
            // if the deletion is successful, response with an ok response
            if (jsonFile.delete()) {
                return ResponseEntity.ok().build();
            }
            // otherwise response with an internal server error
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        // if the file does not exist, return with a 404
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Record %s not found.", id));
        }
    }
}
