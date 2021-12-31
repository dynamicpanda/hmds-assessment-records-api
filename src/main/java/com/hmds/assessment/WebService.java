package com.hmds.assessment;

import java.io.IOException;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebService {
  @GetMapping("/message/{type}")
  @ResponseBody
  public ResponseEntity<String> getMessage(@PathVariable String type) throws IOException {    
    System.out.println(type);

    Properties properties = new Properties();
    properties.load(WebService.class.getClassLoader().getResourceAsStream("messages.properties"));

    String message = properties.getProperty("messages." + type);
    if (message != null) {
      JsonObject json = Json.createObjectBuilder()
        .add("message", message)
        .build();
      return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }
    else
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }
}
