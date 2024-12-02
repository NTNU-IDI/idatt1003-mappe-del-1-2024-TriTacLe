package edu.ntnu.idi.idatt.storage;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cookbook {
  private HashMap<String, Recipe> recipes;
  
  public Cookbook() {
    this.recipes = new HashMap<>();
  }
  
  public HashMap<String, Recipe> getRecipes() {
    return recipes;
  }
  
  /**
   * Adds a recipe to the cookbook.
   *
   * @param recipe the recipe to add
   * @return true if the recipe was successfully added, false if it already exists
   */
  public boolean addRecipeToCookbook(Recipe recipe) {
    if (recipes.containsKey(recipe.getName())) {
      return false; // Recipe already exists
    }
    recipes.put(recipe.getName(), recipe);
    return true;
  }

//  /**
//   * Displays all recipes in the cookbook.
//   *
//   * @return a map of all recipes
//   */
//  public HashMap<String, Recipe> getCookbook() {
//    recipes.forEach((key, value) -> {
//      System.out.println("Recipe Name: " + key);
//      System.out.println(value);
//    });
//    return recipes;
//  }
  
  /**
   * Suggests recipes that can be made based on the current food storage.
   *
   * @param foodStorage the food storage to compare against
   * @return a list of suggested recipes
   */
  public List<Recipe> getSuggestedRecipes(FoodStorage foodStorage) {
    List<Recipe> suggestedRecipes = new ArrayList<>();
    
    if (foodStorage == null || foodStorage.getIngredients() == null || recipes == null || recipes.isEmpty()) {
      return suggestedRecipes;
    }
    
    Map<String, List<Ingredient>> normalizedStorage = new HashMap<>();
    foodStorage.getIngredients().forEach((key, value) -> {
      normalizedStorage.put(key.toLowerCase().trim(), value);
    });
    
    recipes.forEach((recipeName, recipe) -> {
      boolean canMakeRecipe = recipe.getIngredientsList().stream()
          .allMatch(recipeIngredient -> {
            String ingredientName = recipeIngredient.getName().toLowerCase().trim();
            double neededQuantity = recipeIngredient.getQuantity();
            
            List<Ingredient> storageIngredients = normalizedStorage.get(ingredientName);
            
            if (storageIngredients == null || storageIngredients.isEmpty()) {
              return false;
            }
            
            double availableQuantity = storageIngredients.stream()
                .mapToDouble(storageIngredient -> {
                  if (!storageIngredient.getUnit().equals(recipeIngredient.getUnit())) {
                    try {
                      return storageIngredient.getUnit().convertValue(storageIngredient.getQuantity(), recipeIngredient.getUnit());
                    } catch (IllegalArgumentException e) {
                      return 0.0;
                    }
                  }
                  return storageIngredient.getQuantity();
                })
                .sum();
            
            return availableQuantity >= neededQuantity;
          });
      
      if (canMakeRecipe) {
        suggestedRecipes.add(recipe);
      }
    });
    return suggestedRecipes;
  }
  
}
