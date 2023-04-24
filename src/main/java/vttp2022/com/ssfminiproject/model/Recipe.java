package vttp2022.com.ssfminiproject.model;

import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Recipe implements Serializable{
    public String id;
    private String name;
    private String category;
    private String area;
    private String instructions;
    private String imageUrl;
    private String sourceUrl;
    
    private static final long serialVersionUID = 1L;

    public JsonArrayBuilder create(JsonArray jArr) {
        JsonArrayBuilder ab = Json.createArrayBuilder();
        for (int i = 0; i < jArr.size(); i++) {
            JsonObject jObj = jArr.getJsonObject(i);
            id = jObj.getString("idMeal");
            name = jObj.getString("strMeal");
            category = jObj.getString("strCategory");
            area = jObj.getString("strArea");
            instructions = jObj.getString("strInstructions");
            imageUrl = jObj.getString("strMealThumb");
            
            JsonArrayBuilder ingAb = Json.createArrayBuilder();
            for (int j = 1; j <= 20; j++) {
                if (jObj.getString("strIngredient%d".formatted(j)).trim().isEmpty())
                    break;

                String ingredientStr = jObj.getString("strIngredient%d".formatted(j)).replace(",", "");
                ingAb.add(ingredientStr);
            }

            JsonArrayBuilder meAb = Json.createArrayBuilder();
            for (int x = 1; x <= 20; x++) {
                if (jObj.getString("strMeasure%d".formatted(x)).trim().isEmpty())
                    break;
            
                String measureStr = jObj.getString("strMeasure%d".formatted(x)).replace(",", "");    
                meAb.add(measureStr);
            }
            sourceUrl = jObj.getString("strSource");

            ab.add(Json.createObjectBuilder()
            .add("id", id)
            .add("name",name)
            .add("category",category)
            .add("area",area)
            .add("instructions",instructions)
            .add("imageUrl",imageUrl)
            .add("ingredients", ingAb.build())
            .add("measures", meAb.build())
            .add("sourceUrl", sourceUrl));  
        }
        
        return ab;
    }
}
