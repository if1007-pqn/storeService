package com.service.store.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.google.gson.Gson;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.service.store.Application;
import com.service.store.db.StoreMongoService;
import com.service.store.model.Level;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitConfig(classes = Application.class)
@SpringBootTest
public class StoreControllerTest {
	
	@Autowired
	private WebApplicationContext context;
    
    @Autowired
    private StoreMongoService storeMongoService;

    private void dropDB() {
        storeMongoService.deleteAll();
    }
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private MockMvc mockMvc;
	
	public String createStoreAndReturnToken() {
		String newStoreUrl = "/store";
		String token = "";
		ArrayList<Level> levels = new ArrayList<Level>();
		
		for(int i = 0; i < 3; i++) {
			levels.add(new Level(1000.0, 5.0-i, 10.0+i));
		}
		
		Gson gson = new Gson();
		String jsonInString = gson.toJson(levels);
		
		
		try {
			this.mockMvc.perform(post(newStoreUrl)
					.header("username", "dstore").header("password", "opa13")
					.header("oldDaysPurchases", 100.0)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonInString))
					.andDo(print())
					.andExpect(status().is2xxSuccessful())
					.andReturn();
			
			//created store, now login to get token
			MvcResult result = this.mockMvc.perform(get("/login").header("username", "dstore")
					.header("password", "opa13")).andExpect(status().isOk())
					.andDo(print()).andReturn();
			
			token = result.getResponse().getContentAsString();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return token;
		
		
	}
	
	public void deleteStore(String token) throws Exception {
		this.mockMvc.perform(delete("/store").header("token", token))
			.andExpect(status().isOk());
	}
	
	@Before
	public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	/**
	 * test for login on store preloaded & change levels
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateLevels() throws Exception {
		this.dropDB();
		
		String token = createStoreAndReturnToken();
		testLevels(token);
	}
	
	public void testLevels(String token) throws Exception {
		String getLevelUrl = new StringBuilder()
				.append("/levels").toString();
		
		ArrayList<Level> levels = new ArrayList<Level>();
		
		for(int i = 0; i < 3; i++) {
			levels.add(new Level(5700.0, 7.0-i, 14.0+i));
		}
		
		Gson gson = new Gson();
		String jsonInString = gson.toJson(levels);
		
		
		this.mockMvc.perform(put(getLevelUrl).header("token", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonInString))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		// deleteStore(token);
		
	}
	
	/**
	 * test creation of new stores.
	 * needs username, password, oldDaysPurchases (double) and levels
	 * @throws Exception 
	 */
	@Test
	public void testCreateAndRemoveStore() throws Exception {
        this.dropDB();
		String newStoreUrl = "/store";
		
		ArrayList<Level> levels = new ArrayList<Level>();
		
		for(int i = 0; i < 3; i++) {
			levels.add(new Level(1000.0, 5.0-i, 10.0+i));
		}
		
		Gson gson = new Gson();
		String jsonInString = gson.toJson(levels);
		
		
		this.mockMvc.perform(post(newStoreUrl)
				.header("username", "pqnstore").header("password", "porquin")
				.header("oldDaysPurchases", 100.0)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonInString))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		// login to delete store
		MvcResult result = this.mockMvc.perform(get("/login").header("username", "pqnstore")
				.header("password", "porquin")).andExpect(status().isOk())
				.andDo(print()).andReturn();
		
		String token = result.getResponse().getContentAsString();
		
		deleteStore(token);
	}
	
	/**
	 * testing info for registers (purchases in store)
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testRegisterInfo() throws Exception {
        this.dropDB();
		String token = createStoreAndReturnToken();
		
		this.mockMvc.perform(get("/registerinfo").header("token", token))
			.andExpect(status().isOk()).andReturn();
		
		// deleteStore(token);
	}
    
    @After
    public void after() {
        this.dropDB();
    }
}