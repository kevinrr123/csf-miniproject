package vttp2022.com.ssfminiproject.repository;

public interface RecipeRepo {
    
    public String save(String username, String password);

    public String get(String key);
    
    public Boolean saveReci(String key, String value);

    public Boolean delete(String key, String value);
    
}



