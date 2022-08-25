package vttp2022.com.ssfminiproject.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.JsonObject;

public class Recipe {
        private String id;
        private String name;
        private String category;
        private String area;
        private String instructions;
        private String imageUrl;
        private List<String> ingredient;
        private List<String> measure;

        public List<String> getMeasure() {
            return measure;
        }
        public void setMeasure(List<String> measure) {
            this.measure = measure;
        }
        public List<String> getIngredient() {
            return ingredient;
        }
        public void setIngredient(List<String> ingredient) {
            this.ingredient = ingredient;
        }

        private String sourceUrl;
    
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }
        public String getArea() {
            return area;
        }
        public void setArea(String area) {
            this.area = area;
        }
        public String getInstructions() {
            return instructions;
        }
        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }
        public String getImageUrl() {
            return imageUrl;
        }
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }
        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }
    
        public static Recipe create(JsonObject jObj) {
            Recipe meal = new Recipe();
            meal.id = jObj.getString("idMeal");
            meal.name = jObj.getString("strMeal");
            meal.category = jObj.getString("strCategory");
            meal.area = jObj.getString("strArea");
            meal.instructions = jObj.getString("strInstructions");
            meal.imageUrl = jObj.getString("strMealThumb");    

            List<String> ingredients = new LinkedList<>();
            for (int i = 1; i <= 20; i++) {
            if (jObj.getString("strIngredient%d".formatted(i)).isEmpty())
                break;

            String ingredientStr = jObj.getString("strIngredient%d".formatted(i)).replace(",", "");
            ingredients.add(ingredientStr);
            }
            meal.ingredient = ingredients;

            List<String> measures = new LinkedList<>();
            for (int i = 1; i <= 20; i++) {
            if (jObj.getString("strMeasure%d".formatted(i)).isEmpty())
                break;
           
            String measureStr = jObj.getString("strMeasure%d".formatted(i)).replace(",", "");

            measures.add(measureStr);
            }
            meal.measure = measures;
            
            meal.sourceUrl = jObj.getString("strSource");
    
            return meal;
        }
}
