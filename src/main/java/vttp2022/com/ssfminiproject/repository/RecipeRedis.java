package vttp2022.com.ssfminiproject.repository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;


import vttp2022.com.ssfminiproject.model.User;

@Repository
public class RecipeRedis implements RecipeRepo{

    private User user;
    private String username;

    @Autowired
    private RedisTemplate<String, Object> template;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RecipeRedis() {

    }

    public RecipeRedis(String username) {
        this.username = username; 
    }

    @Override
    public String save(String key, String value){

        ValueOperations<String, Object> ops = template.opsForValue();
        ops.set(key, value);
        
        ValueOperations<String, Object> ops2 = template.opsForValue();
        Object obj = ops2.get(key);

        return obj.toString();
    }

    @Override
    public String get(String key){
        ValueOperations<String,Object> ops = template.opsForValue();
        String value = "";
        if(null == ops.get(key)){
            value = "ErrorNotFound";
        }
        else{
            value = ops.get(key).toString();
        }
        return value;
    }


    // @Override
    // public User getUser(String username) {
    //     User result = new User(username);

    //     Set<String> keys = template.keys("*");
    //     if (keys.contains(username)) {
    //         result = (User) template.opsForValue().get(username);
    //     }

    //     return result;
    // }

    // public Boolean addRecipe(Recipe recipe) {
    //     ListOperations<String, Object> listOps = template.opsForList();
    //     User user = getUser(username);
    //     listOps.rightPush(username, recipe);
    //     return true;
    // }

}






