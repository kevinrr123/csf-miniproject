package vttp2022.com.ssfminiproject.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import vttp2022.com.ssfminiproject.model.Recipe;
import vttp2022.com.ssfminiproject.repository.ReceipeRepo;

@Service
public class RecipeService{
    
    private static Logger logger = LoggerFactory.getLogger(RecipeService.class);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ReceipeRepo repo;
    
    //can use the test API key "1" during development of app    
    private final String apiURL = "https://www.themealdb.com/api/json/v1/1/";

    public List<Recipe> getReceipes(String Name) {

        String searchUrl = apiURL + "search.php?s=" + Name;
        String url = UriComponentsBuilder.fromUriString(searchUrl).toUriString();
        
        logger.info(url);
        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(request, String.class);

        try {
            InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            JsonArray jArray = reader.readObject().getJsonArray("meals");

            List<Recipe> recipeList = new LinkedList<Recipe>();
            
            for (int i = 0; i < jArray.size(); i++) {
                JsonObject jObj = jArray.getJsonObject(i);
                recipeList.add(Recipe.create(jObj));
            }

            return recipeList;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();            
        }
        return List.of();
    }

    public Recipe getById(String id) {
        String url = apiURL + "/lookup.php?i=" + id;

        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(request, String.class);

        try {
            InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            JsonObject jObj = reader.readObject().getJsonArray("meals").getJsonObject(0);
            return Recipe.create(jObj);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    public List<String> getUser(String name) {
        return repo.getUser(name);
    }
}
