package org.example.xpneo4j.acceptance.resource.registration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

public class ResourceRegistrationUtilities {

  public static void registerDetachedResource(
      MockMvc mockMvc, RegisterDetachedResourceRequest request) {
    try {
      registerDetachedResource(mockMvc, new ObjectMapper().writeValueAsString(request));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static void registerDetachedResource(MockMvc mockMvc, String request) {
    try {
      mockMvc
          .perform(
              post("/register-detached").contentType(MediaType.APPLICATION_JSON).content(request))
          .andExpect(status().isCreated());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void registerNeighborResource(MockMvc mockMvc, RegisterNeighborRequest request) {
    try {
      registerNeighborResource(mockMvc, new ObjectMapper().writeValueAsString(request));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static void registerNeighborResource(MockMvc mockMvc, String request) {
    try {
      mockMvc
          .perform(
              post("/register-neighbor").contentType(MediaType.APPLICATION_JSON).content(request))
          .andExpect(status().isCreated());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
