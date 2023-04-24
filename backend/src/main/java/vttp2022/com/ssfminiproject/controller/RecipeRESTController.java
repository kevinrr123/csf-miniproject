package vttp2022.com.ssfminiproject.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.com.ssfminiproject.service.RecipeService;
import vttp2022.com.ssfminiproject.model.Recipe;
import vttp2022.com.ssfminiproject.repository.RecipeRedis;

@RestController
@RequestMapping(path = "/api")
public class RecipeRESTController {

    @Autowired  
    RecipeService recipeSvc;

    @Autowired
    RecipeRedis redis;

    @GetMapping(path="/searchRecipe")
    public ResponseEntity<String> getRecipebyName(@RequestParam String search){
        System.out.println("keyword: " + search);
        JsonArrayBuilder ab = Json.createArrayBuilder();
        JsonArray jArray = null;
        try {
        jArray = recipeSvc.getReceipes(search);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Recipe r = new Recipe();
        ab = r.create(jArray); 
        JsonArray results = ab.build();
        //System.out.println("controller" + results.toString());
        return ResponseEntity.ok(results.toString());
    }

    @GetMapping(path="/randomReceipe")
    public ResponseEntity<String> getRandomRecipe() {
        
        JsonArrayBuilder ab = Json.createArrayBuilder();
        JsonArray jArray = null;
        try {
        jArray = recipeSvc.getRandomReceipe();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Recipe r = new Recipe();
        ab = r.create(jArray); 
        JsonArray results = ab.build();
        //System.out.println("controller" + results.toString());
        return ResponseEntity.ok(results.toString());
    }

    @GetMapping(path="/getById")
    public ResponseEntity<String> getRecipeById(@RequestParam String id) {
        JsonObject jObj = null;
        JsonArrayBuilder ab = Json.createArrayBuilder();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        Recipe r = new Recipe();
        try {
                jObj = recipeSvc.getById(id);
                arrBuilder.add(jObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArray jId = arrBuilder.build();
        ab = r.create(jId); 
        JsonArray results = ab.build();
        //System.out.println("controller" + results.toString());
        return ResponseEntity.ok(results.toString());
    }

    @PutMapping(value = "/addtofavourites", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRecipe(@RequestBody String payload) throws IOException {
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        } catch (Exception ex) {
            body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }    
        String username = body.getString("username");
        String id = body.getString("id");
        //System.out.println(">>>>>> user:" + username);
        Boolean save = recipeSvc.saveRecipe(username,id);
        if (!save) {
            JsonObject response = Json.createObjectBuilder()
                .add("error", "Cannot save recipe")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response.toString());
        } else {
            return ResponseEntity.ok(save.toString());
        }
    }

    @GetMapping(path = "/favourites")
    public ResponseEntity<String> getFavourites(@RequestParam String username) {

        List<String> recipeList = redis.SavedList(username);
        JsonObject jObj = null;
        JsonArrayBuilder ab = Json.createArrayBuilder();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        Recipe r = new Recipe();
        try{
            for (String id : recipeList) {
                jObj = recipeSvc.getById(id);
                arrBuilder.add(jObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArray favJson = arrBuilder.build();
        ab = r.create(favJson);    
        JsonArray results = ab.build();
        //System.out.println("controller" + results.toString());
        return ResponseEntity.ok(results.toString());
    }

    @PostMapping(path="/deletefavourites", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteFav(@RequestBody String payload) throws IOException {
        
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        } catch (Exception ex) {
            body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }    
        String username = body.getString("username");
        String id = body.getString("id");
        //System.out.println(">>>>>> user:" + username);
        Boolean del = recipeSvc.delRecipe(username, id);
        if (!del) {
            JsonObject response = Json.createObjectBuilder()
                .add("error", "Cannot save recipe")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response.toString());
        } else {
            return ResponseEntity.ok(del.toString());
        }
    }

    @PostMapping(value = "/addtoTried", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTried(@RequestBody String payload) throws IOException {
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        } catch (Exception ex) {
            body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        } 
        String recipeId = body.getString("recipeid");   
        String username = body.getString("username");
        String recipeName = body.getString("recipeName");
        String comments = body.getString("comments");
        //System.out.println(">>>>>> body:" + comments);
        Boolean save = recipeSvc.saveTried(recipeId,username,recipeName,comments);
        if (!save) {
            JsonObject response = Json.createObjectBuilder()
                .add("error", "Cannot save recipe")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response.toString());
        } else {
            return ResponseEntity.ok(save.toString());
        }
    }

    @GetMapping(path = "/tried")
    public ResponseEntity<String> getTried(@RequestParam String username) {

        JsonArrayBuilder ab = Json.createArrayBuilder();
        ab = recipeSvc.getTried(username);    
        JsonArray results = ab.build();
        //System.out.println("tried" + results.toString());
        return ResponseEntity.ok(results.toString());
    }
    
    @PostMapping(path="/delTried", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delFromTried(@RequestBody String payload) throws IOException {
        
        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();
        } catch (Exception ex) {
            body = Json.createObjectBuilder().add("error", ex.getMessage()).build();
            return ResponseEntity.internalServerError().body(body.toString());
        }    
        String username = body.getString("username");
        String name = body.getString("recipeName");
        //System.out.println(">>>>>> user:" + username);
        Boolean del = recipeSvc.delComment(username, name);
        if (!del) {
            JsonObject response = Json.createObjectBuilder()
                .add("error", "Cannot delete recipe")
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response.toString());
        } else {
            return ResponseEntity.ok(del.toString());
        }
    }
 }
