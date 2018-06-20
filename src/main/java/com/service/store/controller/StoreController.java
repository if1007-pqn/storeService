package com.service.store.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import com.service.store.db.StoreMongoService;
import com.service.store.exception.LoginInvalidException;
import com.service.store.exception.UsernameInvalidException;
import com.service.store.json.PurchaseInfoJson;
import com.service.store.model.Level;
import com.service.store.model.Store;
import com.service.store.exception.TokenInvalidException;
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


    @RequestMapping(value = "/store", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a store", notes = "Create a store")
    @ResponseBody
    public void a(
        @RequestHeader(name = "username", required = true) String username,
        @RequestHeader(name = "password", required = true) String password, 
        @RequestBody List<Level> levels
    ) {
        createStore(username, password, levels);
    }

    public void createStore(String username, String password, List<Level> levels) {
        try {
            password = BCrypt.hashpw(password, BCrypt.gensalt(salts));
            storeMongoService.save(new Store(username, password, levels)); //save in db
        } catch (DuplicateKeyException e){
            throw new UsernameInvalidException();
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
            throw new LoginInvalidException();
        Store s = l.get(0);
        
        if (!BCrypt.checkpw(password, s.getPassword()))
            throw new LoginInvalidException();
        
        return JWTUtil.create(s.getId(), "store");
    }



    @RequestMapping(value = "/levels", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update levels of a store", notes = "Add, remove, change the store levels")
    @ResponseBody
    public void c(
        @RequestHeader(name = "token", required = true) String token, 
        @RequestBody List<Level> levels
    ) {
        Store s = verifyTokenStore(token);
        s.setLevels(levels);
        storeMongoService.save(s);
    }

    public Store verifyTokenStore(String token) {
        /** if token is correct return the matching store */
        
        String id = JWTUtil.getId(token, "store");
        Optional<Store> l = storeMongoService.findById(id);
        if (!l.isPresent())
            throw new TokenInvalidException();

        return l.get();
    
    }

    @RequestMapping(value = "/purchaseinfo", method = RequestMethod.GET)
    @ApiOperation(value = "Simulates a purchase", notes = "Returns a percent and date to expirate credits")
    @ResponseBody
    public PurchaseInfoJson d(
        @RequestHeader(name = "token", required = true) String token,
        @RequestHeader(name = "amount", required = true) double amount 
    ) {
        Store s = verifyTokenStore(token);
        List<Level> ll = s.getLevels();
        Level current = ll.get(0);
        for (Level l : ll) {
            current = l;
            if (amount >= l.getNextLevel()) {
                amount -= l.getNextLevel();
            } else {
                break;
            }
        }
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, (int) current.getDays()*24*60*60);
        return new PurchaseInfoJson(current.getPercent(), calendar.getTime() );

    }

}
