package com.porquin.controller;

import java.util.List;
import java.util.Date;

import com.porquin.db.StoreMongoService;
import com.porquin.exception.LoginInvalidException;
import com.porquin.exception.UsernameInvalidException;
import com.porquin.model.Level;
import com.porquin.model.Store;
import com.porquin.exception.TokenInvalidException;
import com.porquin.util.JWTUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.MediaType;
import org.mindrot.jbcrypt.BCrypt;

@Controller
public class StoreController {

    private static final int salts = 12;
	@Autowired
    private StoreMongoService storeService; //tem que ser atributo de classe


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
            this.storeService.save(new Store(username, password, levels)); //save in db
        } catch (DuplicateKeyException e){
            throw new UsernameInvalidException();
        } catch (Exception e) {
            System.out.println("Is not possible save the user " + username + e);
            throw e;
        }
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
        List<Store> l = storeService.findByUsername(username);
        if (l.size() <= 0)
            throw new LoginInvalidException();
        Store s = l.get(0);
        
        if (!BCrypt.checkpw(password, s.getPassword()))
            throw new LoginInvalidException();
        
        return JWTUtil.create(s.getUsername(), "store");
    }



    @RequestMapping(value = "/levels", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update levels of a store", notes = "Update levels of a store")
    @ResponseBody
    public void c(
        @RequestHeader(name = "token", required = true) String token, 
        @RequestBody List<Level> levels
    ) {
        Store s = verifyTokenStore(token);
        s.setLevels(levels);
        storeService.save(s);
    }

    public Store verifyTokenStore(String token) {
        /** if token is correct return the matching store */
        try {
            Claims t = JWTUtil.decode(token).getBody();
            if (!t.getAudience().equals("store")
                || new Date().after(t.getExpiration()))
                throw new TokenInvalidException();
            String username = t.getId();
            List<Store> l = storeService.findByUsername(username);
            if (l.size() <= 0)
                throw new TokenInvalidException();

            return l.get(0);
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException();
        } catch (SignatureException e) {
            throw new TokenInvalidException();
        }
    }

    @RequestMapping(value = "/levels", method = RequestMethod.GET)
    @ApiOperation(value = "Show levels of a store", notes = "Show the levels of a store")
    @ResponseBody
    public List<Level> d(
        @RequestHeader(name = "token", required = true) String token
    ) {
        Store s = verifyTokenStore(token);
        return s.getLevels();
    }

}
