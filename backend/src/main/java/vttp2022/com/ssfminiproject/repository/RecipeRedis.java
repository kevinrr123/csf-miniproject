package vttp2022.com.ssfminiproject.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RecipeRedis implements RecipeRepo{

    @Autowired
    private RedisTemplate<String, Object> template;

    @Autowired
    private RedisTemplate<String, String> template2;

    @Override
    public String save(String key, String value){

        ValueOperations<String, Object> ops = template.opsForValue();
        if(!template.hasKey(key)){
        ops.set(key, value);
        ValueOperations<String, Object> ops2 = template.opsForValue();
        Object obj = ops2.get(key);

        return obj.toString();
        }
        else
            return null;       
    }

    @Override
    public String get(String key){
        ValueOperations<String,Object> ops = template.opsForValue();
        String value = "";
        if(null == ops.get(key)){
            value = "Error,Not Found";
        }
        else{
            value = ops.get(key).toString();
        }
        return value;
    }

    @Override
    public Boolean saveReci(String user,String id){
        ListOperations<String, String> listOps = template2.opsForList();
        List<String> ValueList = SavedList(user);

        if (!ValueList.contains(id)) {
            listOps.rightPush(user, id);
            return true;
        } else {
            return false;
        }
    }

    public List<String> SavedList(String name) {
        ListOperations<String, String> listOps2 = template2.opsForList();
        return listOps2.range(name, 0, listOps2.size(name) + 1);
    }

    @Override
    public Boolean delete(String key, String value) {
        ListOperations<String, String> listOps2 = template2.opsForList();
        Long del = listOps2.remove(key, 0, value);  
        return del>0;   
    }

}






