package vttp2022.com.ssfminiproject.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.com.ssfminiproject.model.Recipe;
import vttp2022.com.ssfminiproject.service.RecipeService;

@Controller
@RequestMapping(path="")
public class RecipeController {

    @Autowired  
    private RecipeService RecipeSvc;

    @GetMapping(path="/")
    public String getIndex() {        
        return "index";
    }


    @GetMapping(path="/SearchRecipe")
    public String getRecipebyName(@RequestParam String mealName, Model model){
        List<Recipe> recipeList = RecipeSvc.getReceipes(mealName);
        if (recipeList.isEmpty()) {
            return "error";
        } else {
            model.addAttribute("recipes", recipeList);
        }
        return "index";
    }

    @GetMapping(path="/getById/{id}")
    public String getRecipeById(@PathVariable(value="id") String id, Model model) {
  
        Recipe recipe = RecipeSvc.getById(id);
        model.addAttribute("recipe", recipe); 
        return "recipeDetails";
    }

    @GetMapping(path = "/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping(path = "/user")
    public String name(
            @RequestBody MultiValueMap<String, String> form,
            Model model) {

        String name = form.getFirst("name");
        List<String> userList = RecipeSvc.getUser(name);
        List<Recipe> recipeList = new LinkedList<>();

        for (String id : userList) {
            Recipe recipe = RecipeSvc.getById(id);
            recipeList.add(recipe);
        }

        model.addAttribute("name", name);
        model.addAttribute("receipe", recipeList);
        return "userprofile";
    }
}
