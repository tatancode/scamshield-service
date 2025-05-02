package com.valor.scamshield_service.analyze;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Focus testing on the AnalyzeController and related web layer components
@WebMvcTest(AnalyzeController.class)
class AnalyzeControllerTest {

    @Autowired
    private MockMvc mockMvc; // Allows simulating HTTP requests

    @Autowired
    private ObjectMapper objectMapper; // Helps convert Java objects to JSON

    @Test
    void analyze_whenMessageIsBlank_shouldReturnBadRequest() throws Exception {
        // Arrange: Create a request with a blank message
        AnalyzeRequest request = new AnalyzeRequest(); // Use default constructor
        request.setMessage(""); // Use the explicit setter

        // Act & Assert: Perform POST request and verify the response
        mockMvc.perform(post("/api/v1/analyze") // Ensure path matches controller's @RequestMapping + @PostMapping
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Send JSON body
                .andExpect(status().isBadRequest()); // Expect HTTP 400
    }
}
