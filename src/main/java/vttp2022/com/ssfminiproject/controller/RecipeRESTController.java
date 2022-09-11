package vttp2022.com.ssfminiproject.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.com.ssfminiproject.service.RecipeService;

@RestController
@RequestMapping(path = "/rest")
public class RecipeRESTController {

    @Autowired  
    RecipeService recipeSvc;

    @PostMapping(path = "/signupsuccess", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> SignUpSuccess( @RequestBody MultiValueMap<String, String> form,
        HttpServletResponse response, Model model) throws IOException{
        
        String username = form.getFirst("name");
        String password = form.getFirst("password");
        String message = recipeSvc.saveUser(username, password);

        if (message.isEmpty()) {
                JsonObject payload = Json.createObjectBuilder()
                    .add("error", "Cannot save")
                    .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(payload.toString());
            } else {
                response.sendRedirect("/");
                return new ResponseEntity<String>(HttpStatus.OK);
            }
    }

    @PostMapping(path = "/favRecipes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRecipe(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response, HttpSession sess) throws IOException {

        String username = sess.getAttribute("name").toString();
        String id = form.getFirst("id").toString();

        if (sess.getAttribute("name") == null) {
            JsonObject payload = Json.createObjectBuilder()
                .add("error", "Not logged in")
                .build();
            response.sendRedirect("redirect:/");
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(payload.toString());
        }

        Boolean save = recipeSvc.saveRecipe(username, id);
        if (!save) {
            JsonObject payload = Json.createObjectBuilder()
                .add("error", "Cannot save recipe")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(payload.toString());
        } else {
            response.sendRedirect("/home");
            return new ResponseEntity<String>(HttpStatus.OK);
        }
    }

 }
