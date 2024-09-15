package org.jmj.controllers;

import org.jmj.configurations.SubSystemRegisterer;
import org.jmj.entity.*;
import org.jmj.repository.RequestRepository;
import org.jmj.repository.ResponseRepository;
import org.jmj.repository.SubsystemRepository;
import org.jmj.services.RequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ConfigurationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubsystemRepository subsystemRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ResponseRepository responseRepository;

    @Mock
    private SubSystemRegisterer subSystemRegisterer;

    @Mock
    private RequestProcessor requestProcessor;

    @InjectMocks
    private ConfigurationController configurationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(configurationController).build();
    }

    @Test
    public void testCreateSubSystem() throws Exception {
        SubSystem subSystem = new SubSystem();
        subSystem.setName("TestSubSystem");

        mockMvc.perform(post("/configuration/create-subsystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestSubSystem\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Created SubSystem"));
    }

    @Test
    public void testCreateRequest() throws Exception {
        mockMvc.perform(post("/configuration/create-request/TestSubSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"id\":{\"path\":\"/test\",\"method\":\"GET\"}}]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Modified Request"));
    }

    @Test
    public void testUpdateStatusForRequestId() throws Exception {
        mockMvc.perform(post("/configuration/update-status-request/TestSubSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"statusCode\":\"OK\"}]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated Status"));
    }

    @Test
    public void testCreateResponse() throws Exception {
        Request request = new Request();
        request.setId(new RequestId("/test", HttpMethod.GET,  "TestSubSystem"));
        when(requestRepository.findById(any(RequestId.class))).thenReturn(Optional.of(request));

        mockMvc.perform(post("/configuration/create-response/TestSubSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":{\"path\":\"/test\",\"method\":\"GET\"},\"responses\":[{\"statusCode\":\"OK\"}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['GET:/test:TestSubSystem']").value("201 CREATED"));
    }

    @Test
    public void testGetResponsesForRequestId() throws Exception {
        Response response = new Response();
        response.setId(1L);
        response.setBody("{\"message\":\"test\"}");
        when(responseRepository.findByRequest_Id(any(RequestId.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/configuration/get-response/TestSubSystem")
                        .param("requestMethod", "GET")
                        .param("requestPath", "/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['1']").value("[{\"message\":\"test\"}]"));
    }
}