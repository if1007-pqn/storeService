package com.service.store.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.service.store.db.StoreMongoService;
import com.service.store.exception.InvalidLoginException;
import com.service.store.exception.InvalidTokenException;
import com.service.store.exception.InvalidUsernameException;
import com.service.store.json.GetLevelsJson;
import com.service.store.json.Json;
import com.service.store.model.Level;
import com.service.store.model.Store;
import com.service.store.util.JWTUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.MediaType;
import org.mindrot.jbcrypt.BCrypt;

@Controller
public class StoreController {

    private static final int salts = 12;
	@Autowired
    private StoreMongoService storeMongoService; //tem que ser atributo de classe
    private Json<Level[]> jsonLevel = new Json<Level[]>(Level[].class);
	
    @RequestMapping(value = "/store", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a store", notes = "Create a store")
    @ResponseBody
    public void a(
        @RequestHeader(name = "username", required = true) String username,
        @RequestHeader(name = "password", required = true) String password, 
        @RequestHeader(name = "oldDaysPurchases", required = true) double oldDaysPurchases, 
        @RequestBody String lvls
    ) {
        List<Level> levels = Arrays.asList(jsonLevel.fromJson(lvls));
        createStore(username, password, oldDaysPurchases, levels);
    }

    public void createStore(String username, String password, double oldDaysPurchases, List<Level> levels) {
        try {
            password = BCrypt.hashpw(password, BCrypt.gensalt(salts));
            storeMongoService.save(new Store(username, password, oldDaysPurchases, levels)); //save in db
        } catch (DuplicateKeyException e){
            throw new InvalidUsernameException();
        } catch (Exception e) {
            throw e;
        }
    }
    
    @RequestMapping(value = "/store", method = RequestMethod.DELETE)
    @ApiOperation(value = "Remove the store itself", notes = "Delete the store itself")
    @ResponseBody
    public void d(
        @RequestHeader(name = "token", required = true) String token
    ) {
        Store s = verifyTokenStore(token);
        storeMongoService.delete(s.getId());
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiOperation(value = "Get a store login token", notes = "Get a store login token")
    @ResponseBody
    public String b(
        @RequestHeader(name = "username", required = true) String username,
        @RequestHeader(name = "password", required = true) String password
    ) {
        return login(username, password);
    }

    public String login(String username, String password) {
        List<Store> l = storeMongoService.findByUsername(username);
        if (l.size() <= 0)
            throw new InvalidLoginException();
        Store s = l.get(0);
        
        if (!BCrypt.checkpw(password, s.getPassword()))
            throw new InvalidLoginException();
        
        return JWTUtil.create(s.getId(), "store");
    }



    @RequestMapping(value = "/levels", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update levels of a store", notes = "Add, remove, change the store levels")
    @ResponseBody
    public void c(
        @RequestHeader(name = "token", required = true) String token, 
        @RequestBody String lvls
    ) {
        List<Level> levels = Arrays.asList(jsonLevel.fromJson(lvls));
        Store s = verifyTokenStore(token);
        s.setLevels(levels);
        storeMongoService.save(s);
    }

    public Store verifyTokenStore(String token) {
        /** if token is correct return the matching store */
        
        String id = JWTUtil.getId(token, "store");
        Optional<Store> l = storeMongoService.findById(id);
        if (!l.isPresent())
            throw new InvalidTokenException();

        return l.get();
    
    }


    @RequestMapping(value = "/registerinfo", method = RequestMethod.GET)
    @ApiOperation(value = "Infos to make a register", notes = "Get infos to register a purchase in the store")
    @ResponseBody
    public GetLevelsJson g(
        @RequestHeader(name = "token", required = true) String token
    ) {
        Store s = verifyTokenStore(token);
        return new GetLevelsJson(s.getOldDaysPurchases(), s.getLevels());
    }

}
