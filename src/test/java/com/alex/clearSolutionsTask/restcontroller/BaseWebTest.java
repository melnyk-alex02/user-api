package com.alex.clearSolutionsTask.restcontroller;

import com.alex.clearSolutionsTask.ClearSolutionsTaskApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = ClearSolutionsTaskApplication.class)
public abstract class BaseWebTest {

    @Autowired
    private WebApplicationContext wac;
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
        this.objectMapper= new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
}