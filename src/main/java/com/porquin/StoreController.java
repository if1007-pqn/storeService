package com.porquin;

import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//
@Controller
@Service
public class StoreController {

    @Autowired
    private StoreService storeService; //tem que ser atributo de classe

    /** Cria loja */
    @RequestMapping(value = "/store", method = RequestMethod.POST, consumes = "application/json") //define route POST /store
    @ResponseBody
    public void a(
        @RequestHeader(name = "username", required = true) String username,
        @RequestHeader(name = "password", required = true) String password, 
        @RequestBody List<Level> levels
    ) {
        // createStore(username, password, levels)
        
        // store.deleteAll();
        // Store s = new Store(username, password, levels);
        try {
            this.storeService.save(username, password, levels); //save in db
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o user " + username);
        }
    }

    // createStore(...) {
    // throw new UsernameExistException();
    // }

    // @ExceptionHandler(UsernameExistException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public void invalidLogin() {
    // }

    /** Obtem token de uma loja */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String b(
        @RequestHeader(name = "username", required = true) String username,
        @RequestHeader(name = "password", required = true) String password
    ) {
        String token = "";
        //
        return token;
    }

    /** atualiza levels de uma loja */
    @RequestMapping(value = "/levels", method = RequestMethod.PUT, consumes = "application/json")
    public void c(
        @RequestHeader(name = "token", required = true) String token, 
        @RequestBody List<Level> levels
    ) {

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
