package com.hmds.assessment;

import java.io.IOException;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot REST controller for the HMDS message API. Exposes endpoints for
 * accessing various types of messages.
 */
@RestController
public class WebService {

  /**
   * Exposes GET request to retrieve a message based on a message type provided
   * in the URI. Messages are predefined in the messages.properties resource
   * file.
   * 
   * @param type The type of message to retrieve. Provided in the URI path.
   * @return The message associated with the provided message type.
   * @throws IOException Raised if there is a problem reading the properties
   * file.
   */
  @GetMapping("/message/{type}")
  @ResponseBody
  public ResponseEntity<String> getMessage(@PathVariable String type) throws IOException {    

    // read the messages from the messages.properties file
    Properties properties = new Properties();
    properties.load(WebService.class.getClassLoader().getResourceAsStream("messages.properties"));

    // retrieve the message associated with the type. Will be {@code null} if
    // type is invalid
    String message = properties.getProperty(type);

    // if valid type, return a JSON object containing the type and value of the
    // message
    if (message != null) {
      JsonObject json = Json.createObjectBuilder()
        .add("type", type)
        .add("value", message)
        .build();

      // define the JSON response content type
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set("Content-Type", "application/json");

      // return the successful response
      return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(json.toString());
    }
    // if type invalid, return 400
    else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }
}
