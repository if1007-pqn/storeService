package com.service.store.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.google.gson.Gson;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.service.store.Application;
import com.service.store.model.Level;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitConfig(classes = Application.class)
@SpringBootTest
public class StoreControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void testStore() throws Exception {
		String getLoginUrl = new StringBuilder()
				.append("/login").toString();
		
		
		MvcResult result = this.mockMvc.perform(get(getLoginUrl).header("username", "laroca")
				.header("password", "larocastore")).andExpect(status().isOk())
				.andDo(print()).andReturn();
		
		
		String content = result.getResponse().getContentAsString();
		testLevels(content);
	}
	
	public void testLevels(String token) throws Exception {
		String getLevelUrl = new StringBuilder()
				.append("/test").toString();
		
		ArrayList<Level> levels = new ArrayList<Level>();
		
		for(int i = 0; i < 3; i++) {
			levels.add(new Level(1000.0, 5.0-i, 10.0+i));
		}
		
		Gson gson = new Gson();
		String jsonInString = gson.toJson(levels);
		
		
		this.mockMvc.perform(put(getLevelUrl).header("token", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonInString))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		
				
	}
}