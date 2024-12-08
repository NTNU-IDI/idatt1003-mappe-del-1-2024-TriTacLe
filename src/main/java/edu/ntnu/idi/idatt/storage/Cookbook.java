package edu.ntnu.idi.idatt.storage;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * The {@code Cookbook} class represents a collection of recipes.
 * It provides functionality to add, search, remove,
 * and suggest recipes based on the available ingredients in a food storage.
 * This class utilizes a {@code HashMap} to store recipes with their names as keys.
 *
 * @author TriLe
 */
public class Cookbook {
  private HashMap<String, Recipe> recipes;
  
  
  /**
   * Constructs an empty Cookbook.
   */
  public Cookbook() {
    this.recipes = new HashMap<>();
  }
  
  /**
   * Retrieves all recipes in the cookbook.
   *
   * @return a HashMap containing all recipe where
   * the key is the recipe name and the value is the Recipe instance.
   */
  public HashMap<String, Recipe> getRecipes() {
    return recipes;
  }
  
  /**
   * Adds a recipe to the cookbook.
   *
   * @param recipe the Recipe object to be added
   * @return true if the recipe was successfully added,
   * false if a recipe with the same name already exists
   * @throws NullPointerException if the recipe parameter is null
   */
  public boolean addRecipeToCookbook(Recipe recipe) {
    if (recipes.containsKey(recipe.getName())) {
      return false;
    }
    recipes.put(recipe.getName(), recipe);
    return true;
  }
  
  /**
   * Searches for a recipe in the cookbook by its name.
   *
   * @param nameRecipe the name of the recipe to search for
   * @return an Optional containing the matching recipe entry if found,
   * or an empty Optional if not found.
   */
  public Optional<Map.Entry<String, Recipe>> searchForRecipeInCookbook(String nameRecipe) {
    return recipes.entrySet().stream()
        .filter(entry -> entry.getKey().equalsIgnoreCase(nameRecipe))
        .findFirst();
  }
  
  /**
   * Removes a recipe from the cookbook by its name.
   *
   * @param name the name of the recipe to remove
   * @return true if the recipe was successfully removed, false if the recipe does not exist
   * @throws NullPointerException if the name parameter is null
   */
  public boolean removeRecipeFromCookbook(String name) {
    String keyToRemove = recipes.keySet().stream()
        .filter(key -> key.equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
    
    if (keyToRemove != null) {
      recipes.remove(keyToRemove);
      return true;
    }
    return false;
  }
  
  /**
   * Suggests recipes that can be prepared based on the
   * available ingredients in the given {@code FoodStorage}.
   * A recipe is suggested if all required ingredients are
   * present in sufficient quantity in the food storage.
   *
   * @param foodStorage the FoodStorage to compare against
   * @return a List of Recipe instances that can be made
   * @throws NullPointerException if the foodStorage parameter is null
   */
  public List<Recipe> getSuggestedRecipes(FoodStorage foodStorage) {
    List<Recipe> suggestedRecipes = new ArrayList<>();
    
    if (foodStorage == null || foodStorage.getIngredients() == null
        || recipes == null || recipes.isEmpty()) {
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
                      return storageIngredient.getUnit()
                          .convertValue(storageIngredient.getQuantity(),
                              recipeIngredient.getUnit());
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
