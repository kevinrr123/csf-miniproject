package vttp2022.com.ssfminiproject.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceipeRepo {

    @Autowired
    private RedisTemplate<String, String> repo;

    public List<String> getUser(String key) {
        ListOperations<String, String> opsList = repo.opsForList();
        return opsList.range(key, 0, opsList.size(key) + 1);
    }

    public Boolean addReceipe(String key, String value) {
        ListOperations<String, String> opsList = repo.opsForList();
        List<String> valueList = getUser(key);

        if (!valueList.contains(value)) {
            opsList.rightPush(key, value);
            return true;
        } else {
            return false;
        }
    }

    public void delRecipe(String key, String value) {
        ListOperations<String, String> opsList = repo.opsForList();
        opsList.remove(key, 0, value);
    }
}

