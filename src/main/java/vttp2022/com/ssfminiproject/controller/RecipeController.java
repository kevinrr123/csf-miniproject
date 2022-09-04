package vttp2022.com.ssfminiproject.controller;


import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.com.ssfminiproject.model.Recipe;
import vttp2022.com.ssfminiproject.model.User;
import vttp2022.com.ssfminiproject.repository.RecipeRedis;
import vttp2022.com.ssfminiproject.service.RecipeService;

@Controller
@RequestMapping(path="")
public class RecipeController {

    @Autowired
    RecipeRedis redis;

    @Autowired  
    RecipeService recipeSvc;

    @GetMapping(path="/")
    public String getIndex() {        
        
        return "index";
    }

    @PostMapping(path = "/login", consumes="application/x-www-form-urlencoded", produces="text/html")
    public String CheckLogin(
        @RequestParam(name = "name") String username,
        @RequestParam(name = "password") String password,
        HttpSession sess, Model model)
    {   
        User u = new User();
        if(recipeSvc.getUser(username+"user").equals(password)){
            sess.setAttribute("name", username.toUpperCase());
            model.addAttribute("name", username);
            model.addAttribute("user", u);
            return "home";
        }
        else
        
        return "error";
    }

    @GetMapping(path = "/signUp")
    public String getSignUpPage(){
        return "signUp";
    }


    // @PostMapping(path = "/", consumes="application/x-www-form-urlencoded", produces="text/html")
    // public String userPage(@RequestBody MultiValueMap<String, String> form, HttpSession sess, Model model) {
    //     User u = new User();
    //     String username = form.getFirst("name");
    //     sess.setAttribute("name", username);
    //     model.addAttribute("name", username);
    //     model.addAttribute("user", u);
    //     return "home";
    // }

    @GetMapping(path="/SearchRecipe")
    public String getRecipebyName(@RequestParam String mealName, HttpSession sess, Model model){
        
        String username = sess.getAttribute("name").toString();
        //User u = redis.getUser(username);
        List<Recipe> recipeList = recipeSvc.getReceipes(mealName);
        if (recipeList.isEmpty()) {
            return "error";
        } else {
            model.addAttribute("name", username);
            model.addAttribute("recipes", recipeList);
            //model.addAttribute("user", u);
        }
        return "home";
    }

    @GetMapping(path="/getById/{id}")
    public String getRecipeById(HttpSession sess,@PathVariable(value="id") String id, Model model) {
  
        if (sess.getAttribute("name") == null) {
            return "redirect:/";
        }
        
        Recipe recipe = recipeSvc.getById(id);
        model.addAttribute("recipe", recipe); 
        return "recipeDetails";
    }

    @GetMapping(path="/RandomRecipe")
    public String getRandomRecipe(HttpSession sess,Model model) {
        
        if (sess.getAttribute("name") == null) {
            return "redirect:/";
        }
        
        Optional<Recipe> opt = recipeSvc.getRandomRecipe();

        if (opt.isEmpty()) {
            return "error";
        } else {
            Recipe recipe = opt.get();
            model.addAttribute("recipe", recipe); 
        }
        return "recipeDetails";
    }

    @GetMapping(path="/logout")
    public String getLogout(HttpSession sess) {
        sess.invalidate();
        return "redirect:/";
    }

}
