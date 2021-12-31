package com.hmds.assessment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebService {
  @GetMapping("/hello")
  public String hello(String message) {
    System.out.println(message);
    return message;
  }
}
