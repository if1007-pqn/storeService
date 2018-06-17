package com.porquin;

import java.util.List;
import java.util.Date;


import com.porquin.exceptions.LoginInvalidException;
import com.porquin.exceptions.UsernameInvalidException;
import com.porquin.exceptions.TokenInvalidException;
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

import org.springframework.http.MediaType;
import org.mindrot.jbcrypt.BCrypt;

@Controller
public class StoreController {

    private static final int salts = 12;
	@Autowired
    private StoreService storeService; //tem que ser atributo de classe

    /** Cria loja */
    @RequestMapping(value = "/store", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
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
            this.storeService.createAndSave(username, password, levels); //save in db
        } catch (DuplicateKeyException e){
            throw new UsernameInvalidException();
        } catch (Exception e) {
            System.out.println("Is not possible save the user " + username + e);
            throw e;
        }
    }
    


    /** Obtem token de uma loja */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
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


    /** atualiza levels de uma loja */
    @RequestMapping(value = "/levels", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void c(
        @RequestHeader(name = "token", required = true) String token, 
        @RequestBody List<Level> levels
    ) {
        updateLevels(token, levels);
    }

    public void updateLevels(String token, List<Level> levels) {
        try {
            Claims t = JWTUtil.decode(token).getBody();
            if (!t.getAudience().equals("store")
                || new Date().after(t.getExpiration()))
                throw new TokenInvalidException();
            String username = t.getId();
            List<Store> l = storeService.findByUsername(username);
            if (l.size() <= 0)
                throw new TokenInvalidException();

            Store s = l.get(0);
            s.setLevels(levels);
            storeService.save(s);
            
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException();
        } catch (SignatureException e) {
            throw new TokenInvalidException();
        }
        
    }

    /** Diz qual deve ser a porcentagem de volta para uma compra */
    @RequestMapping(value = "/percent", method = RequestMethod.GET)
    @ResponseBody
    public double d(
        @RequestHeader(name = "token", required = true) String token,    
        @RequestHeader(name = "cellphone", required = true) String phone,
        @RequestHeader(name = "value", required = true) double value,
        @RequestHeader(name = "discount", required = true) double discount
    ) {
        return 0;
    }

}
