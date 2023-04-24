package vttp2022.com.ssfminiproject.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.com.ssfminiproject.repository.RecipeRedis;


@Service
public class RecipeService{
    
    private static Logger logger = LoggerFactory.getLogger(RecipeService.class);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RecipeRedis redis;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    //can use the test API key "1" during development of app    
    private final String apiURL = "https://www.themealdb.com/api/json/v1/1/";

    public JsonArray getReceipes(String Name){

        String searchUrl = apiURL + "search.php?s=" + Name;
        String url = UriComponentsBuilder.fromUriString(searchUrl).toUriString();
        
        logger.info(url);
        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(request, String.class);
        JsonArray jArray = null;
        try {
            InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            jArray = reader.readObject().getJsonArray("meals");
            return jArray;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();            
        }
        return jArray;          
    }

    public JsonArray getRandomReceipe() {
        String url = apiURL + "/random.php";

        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(request, String.class);
        JsonArray jArray = null;
        try {
            InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            jArray = reader.readObject().getJsonArray("meals");
            return jArray;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public JsonObject getById(String id) {
        String url = apiURL + "/lookup.php?i=" + id;

        RequestEntity<Void> request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(request, String.class);
        JsonObject jObj = null;
        try {
            InputStream is = new ByteArrayInputStream(response.getBody().getBytes());
            JsonReader reader = Json.createReader(is);
            jObj = reader.readObject().getJsonArray("meals").getJsonObject(0);
            return jObj;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        
    }

    public Boolean saveRecipe(String name, String id) {
        return redis.saveReci(name, id);
    }

    public Boolean delRecipe(String key, String value) {
        return redis.delete(key, value);
    }

    public boolean saveTried(String recipeId,String username,String recipeName,String comments) {
        int userid = 0;
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select userid from users where username = ?",username);
        if (rs.next()){
            userid = rs.getInt("userid");
        }
        int saved = jdbcTemplate.update("replace into commenttable (recipe_id,userid, recipename, commentitem, dates) values (?, ?, ?, ?, ?)", recipeId, userid, recipeName,comments,formattedDate);
        return saved > 0;
    }

    public JsonArrayBuilder getTried(String username) {
        
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from commenttable inner join users on users.userid = commenttable.userid where username = ?", username);
        
        if (!rs.next())
            return null;
        else
            rs.beforeFirst();
        JsonArrayBuilder ab = Json.createArrayBuilder();
        while (rs.next()){
            ab.add(Json.createObjectBuilder()
            .add("recipename",rs.getString("recipename"))
            .add("comments", rs.getString("commentitem"))
            .add("date", rs.getString("dates"))
            .add("id", rs.getString("recipe_id")));
        }
        
        return ab;
    }

    public boolean delComment(String username,String recipeName){
        int userid = 0;
        SqlRowSet rs = jdbcTemplate.queryForRowSet("select userid from users where username = ?",username);
        if (rs.next()){
            userid = rs.getInt("userid");
        }
        int saved = jdbcTemplate.update("delete from commenttable where userid=? and recipename= ?",userid,recipeName);
        return saved>0;
    }

}
