package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.Cookbook;
import edu.ntnu.idi.idatt.storage.FoodStorage;
import edu.ntnu.idi.idatt.utils.ConsoleInputManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <h1>CookbookService.</h1>
 * Service class to handle interactions between the user interface and Cookbook.
 *
 * <p>This class provides functionalities for managing recipes in a cookbook,
 * verifying ingredient availability, and suggesting recipes based on the food storage.
 * </p>
 *
 * @author TriLe
 */
public class CookbookService {
  private final Cookbook cookbook;
  private final FoodStorage foodStorage;
  private final ConsoleInputManager inputManager;
  
  /**
   * Initializes a CookbookService instance with the given cookbook,
   * food storage, and input handler.
   *
   * @param cookbook     the cookbook to manage recipes
   * @param foodStorage  the food storage containing available ingredients
   * @param inputManager the input handler for user input validation
   */
  public CookbookService(Cookbook cookbook, FoodStorage foodStorage, ConsoleInputManager
      inputManager) {
    this.cookbook = cookbook;
    this.foodStorage = foodStorage;
    this.inputManager = inputManager;
  }
  
  /**
   * Handles the process of adding a new recipe to the cookbook.
   * Chatgpt gave me an idea to implement helpers to increase cohesion
   */
  public void handleAddRecipeToCookbook() {
    String recipeName = getRecipeName();
    Recipe recipe = getRecipeDetails(recipeName);
    addIngredientsToRecipe(recipe);
    saveRecipeToCookbook(recipe);
  }
  
  /**
   * Prompts the user for a new recipe name.
   * Will not allow duplicate recipe names.
   * Helper of handleAddRecipeToCookbook.
   *
   *
   * @return A new recipe name.
   */
  private String getRecipeName() {
    String recipeName;
    do {
      recipeName = inputManager.getValidatedString(
          "Enter recipe name: ",
          "Recipe name cannot be empty or blank.",
          "recipe name"
      );
      if (cookbook.getRecipes().containsKey(recipeName)) {
        System.out.println("Recipe \"" + recipeName + "\""
            + " already exists. Please choose a different name.");
      }
    } while (cookbook.getRecipes().containsKey(recipeName));
    return recipeName;
  }
  
  /**
   * Gets the details for a new recipe.
   * Helper of handleAddRecipeToCookbook method.
   *
   * @param recipeName The name of the recipe.
   * @return A new Recipe object with the entered details.
   */
  private Recipe getRecipeDetails(String recipeName) {
    String description = inputManager.getValidatedString(
        "Enter a description: ",
        "Description cannot be empty or blank.",
        "description"
    );
    String procedure = inputManager.getValidatedString(
        "Enter the procedure: ",
        "Procedure cannot be empty or blank.",
        "procedure"
    );
    double portions = inputManager.getValidatedDouble(
        "Enter the number of portions this recipe makes: ",
        "Portions must be a positive number.",
        "portions",
        false
    );
    return new Recipe(recipeName, description, procedure, portions);
  }
  
  /**
   * Guides the user to add ingredients to the recipe by adding
   * manually or choosing existing ingredients from the storage.
   * Helper of handleAddRecipeToCookbook method.
   *
   * @param recipe The Recipe object to which ingredients will be added.
   */
  private void addIngredientsToRecipe(Recipe recipe) {
    try {
      int choice;
      do {
        choice = inputManager.getValidatedInt(
            """
            How would you like to add ingredients?
            1: Add manually
            2: Choose from food storage
            Enter your choice: """,
            "Invalid input. Please enter 1 or 2.",
            "choice"
        );
        
        if (choice == 1) {
          addIngredientsManually(recipe);
          break;
        } else if (choice == 2) {
          addIngredientsFromStorage(recipe);
          break;
        } else {
          System.out.println("Invalid choice. Please enter 1 or 2.");
        }
      } while (true);
    } catch (IllegalArgumentException e) {
      System.out.println("Error while selecting ingredient input method: " + e.getMessage());
    }
  }
  
  /**
   * Attempts to add (save) the recipe to the cookbook.
   * Helper of handleAddRecipeToCookbook method.
   *
   * @param recipe The Recipe object to save.
   */
  private void saveRecipeToCookbook(Recipe recipe) {
    boolean isAdded = cookbook.addRecipeToCookbook(recipe);
    if (isAdded) {
      System.out.println("Recipe added successfully!");
      System.out.println(recipe);
    } else {
      System.out.println("Failed to add the recipe. A recipe with the name "
          + "\"" + recipe.getName() + "\" already exists.");
    }
  }
  
  /**
   * Adds ingredients manually (by entering each attribute) to a recipe.
   * Helper of addIngredientsToRecipe method.
   *
   * @param recipe the recipe to which ingredients will be added
   * @throws IllegalArgumentException if input validation fails
   */
  private void addIngredientsManually(Recipe recipe) {
    try {
      int totalIngredients = (int) inputManager.getValidatedDouble(
          "Enter how many ingredients you want this recipe to have",
          "Total ingredients cannot be negative", "Total ingredients", false);
      for (int i = 0; i < totalIngredients; i++) {
        System.out.println("Ingredient: " + (i + 1) + " of " + totalIngredients);
        final String name = inputManager.getValidatedString(
            "Enter ingredient name:",
            "Ingredient name cannot be empty/blank", "name");
        double quantity = inputManager.getValidatedDouble(
            "Enter quantity:",
            "Invalid input for quantity", "quantity", false);
        final Unit unit = inputManager.getValidatedUnit(
            "Enter unit (kg, g, L, mL, pcs):", "Invalid unit");
        final double price = inputManager.getValidatedDouble(
            "Enter price:", "Invalid input for price", "price", true);
        
        Ingredient ingredient = new Ingredient(name, quantity, unit, price);
        recipe.addIngredientToRecipe(ingredient);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error adding ingredients manually: " + e.getMessage());
    }
  }
  
  /**
   * Adds ingredients to a recipe using available items from the food storage.
   * Helper of addIngredientsToRecipe method.
   *
   * @param recipe the recipe to which ingredients will be added
   * @throws IllegalArgumentException if input validation fails
   */
  private void addIngredientsFromStorage(Recipe recipe) {
    try {
      int totalIngredients = (int) inputManager.getValidatedDouble(
          "Enter how many ingredients you want this recipe to have",
          "Total ingredients cannot be negative/other type than int or double",
          "total ingredients", false);
      
      System.out.println(
          "Every ingredient that has not expired and can be used in a recipe:");
      List<Ingredient> availableIngredients
          = foodStorage.getIngredientsExpiringAfter(LocalDate.now());
      availableIngredients.forEach(ingredient -> System.out.println("- " + ingredient));
      
      for (int i = 0; i < totalIngredients; i++) {
        System.out.println("Ingredient: " + (i + 1) + " of " + totalIngredients);
        handleIngredientAddition(recipe);
      }
    } catch (IllegalArgumentException e) {
      System.out.println(
          "An error occurred while adding ingredients from storage: " + e.getMessage());
    }
  }
  
  /**
   * Method to handle the addition of an ingredient chosen from the food storage to a recipe.
   * Helper of addIngredientsFromStorage method.
   *
   * <p>This method retrieves an ingredient from the food storage and adds it to the recipe
   * based on user input. It ensures that the requested quantity is available in the food storage.
   * </p>
   *
   * @param recipe the recipe to which the ingredient will be added
   * @throws IllegalArgumentException if input validation fails or the ingredient is not found
   */
  private void handleIngredientAddition(Recipe recipe) {
    String ingredientKey;
    List<Ingredient> ingredients;
    
    do {
      ingredientKey = inputManager.getValidatedString(
          "Enter the ingredient you want",
          "Error: Could not retrieve the ingredient name.", "name").toLowerCase();
      
      ingredients = getIngredientsFromStorage(ingredientKey);
      if (ingredients == null || ingredients.isEmpty()) {
        System.out.println("Ingredient not found in storage. Please enter a valid ingredient.");
      }
    } while (ingredients == null || ingredients.isEmpty());
    
    double totalAvailableQuantity = ingredients.stream()
        .mapToDouble(Ingredient::getQuantity)
        .sum();
    
    System.out.println(
        "Total available quantity for " + ingredientKey + ": " + totalAvailableQuantity + ". "
            + "Please read: Remember this is total quantity of "
            + ingredientKey + " in the food storage. "
            + "How much you want to add to the recipe is an unrealized amount"
            + " so it will not be deducted from the real amount.");
    
    int requestedQuantity = inputManager.getValidatedInt(
        "Enter quantity to add:",
        "Invalid input for quantity.", "requested quantity");
    
    if (requestedQuantity > totalAvailableQuantity && !confirmAdditionExceedingQuantity()) {
      System.out.println("Ingredient not added to the recipe.");
      return;
    }
    
    allocateIngredientsToRecipe(recipe, ingredients, requestedQuantity);
  }
  
  /**
   * Method to retrieve the requested ingredient as a list from the food storage.
   * Helper of handleIngredientAddition method.
   *
   * <p>This method fetches all ingredients in the food storage that match the given key (name)
   * and sorts them by expiration date.
   * The purpose is to handle duplicated ingredient and sort them according to FIFO.
   * </p>
   *
   * @param ingredientKey the name of the ingredient to search for
   * @return a list of matching ingredients, or null if none are found
   * @throws IllegalArgumentException if the ingredient key is invalid
   */
  private List<Ingredient> getIngredientsFromStorage(String ingredientKey) {
    try {
      if (foodStorage.getIngredients() == null
          || !foodStorage.getIngredients().containsKey(ingredientKey)) {
        return null;
      }
      
      List<Ingredient> ingredients
          = new ArrayList<>(foodStorage.getIngredients().get(ingredientKey));
      ingredients.sort(Comparator.comparing(Ingredient::getExpirationDate));
      return ingredients;
    } catch (IllegalArgumentException e) {
      System.out.println("Error while retrieving ingredients from storage: " + e.getMessage());
      return null;
    }
  }
  
  /**
   * Method to allocate a requested quantity of ingredients to a recipe.
   * Helper of handleIngredientAddition method.
   * Chatgpt helped with implementing math logic by adding the Math.min function
   *
   * <p>This method calculates the amount of each available ingredient to add to the recipe
   * based on the requested quantity, ensuring no more than what is available is used.
   * </p>
   *
   * @param recipe            the recipe to which the ingredients will be added
   * @param ingredients       the list of ingredients available for allocation
   * @param requestedQuantity the total quantity to allocate
   * @throws IllegalArgumentException if allocation fails due to insufficient quantity
   */
  private void allocateIngredientsToRecipe(
      Recipe recipe, List<Ingredient> ingredients, int requestedQuantity) {
    try {
      double remainingToUse = requestedQuantity;
      
      for (Ingredient ingredient : ingredients) {
        if (remainingToUse <= 0) {
          break;
        }
        
        double availableQuantity = ingredient.getQuantity();
        double toAllocate = Math.min(availableQuantity, remainingToUse);
        remainingToUse -= toAllocate;
        
        Ingredient ingredientToAdd = new Ingredient(
            ingredient.getName(), toAllocate, ingredient
            .getUnitMeasurement(), ingredient.getPrice());
        recipe.addIngredientToRecipe(ingredientToAdd);
        System.out.println(
            "Added ingredient: " + ingredientToAdd.getName() + " "
                + "(" + toAllocate + " " + ingredientToAdd.getUnitMeasurement().getSymbol()
                + ") to the recipe.");
      }
      
      if (remainingToUse > 0) {
        System.out.println("Could not use the full quantity. Short by " + remainingToUse);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error while try to allocate ingredients to the recipe: "
          + e.getMessage());
    }
  }
  
  /**
   * Searches for a recipe in the cookbook based on user input.
   *
   * @throws IllegalArgumentException if the input is invalid or the recipe does not exist
   */
  public void handleSearchRecipe() {
    final String name = inputManager.getValidatedString("Enter name of the recipe:",
        "Recipe name cannot be empty/blank", "name").trim();
    
    if (cookbook == null) {
      throw new IllegalArgumentException("Cookbook is not initialized.");
    }
    Optional<Map.Entry<String, Recipe>> recipeSearchResult
        = cookbook.searchForRecipeInCookbook(name);
    
    if (recipeSearchResult.isPresent()) {
      System.out.println("Recipe found:");
      Recipe foundRecipe = recipeSearchResult.get().getValue();
      System.out.println(foundRecipe);
    } else {
      System.out.println("Recipe: " + name + " does not exist.");
    }
  }
  
  /**
   * Helper that confirms whether to proceed with adding an ingredient to the recipe
   * even if the requested quantity exceeds availability.
   *
   * @return true if the user confirms to proceed, false otherwise
   */
  private boolean confirmAdditionExceedingQuantity() {
    try {
      String continueIfWant = inputManager.getValidatedString(
          "Requested quantity exceeds available ingredients. "
              + "Do you still want to add this ingredient? (yes/no)",
          "Invalid input. Please answer yes or no.", "yes/no input");
      return continueIfWant.equalsIgnoreCase("yes");
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid input: " + e.getMessage());
      return false;
    }
  }
  
  /**
   * Removes a recipe from the cookbook based on user input.
   *
   * @throws IllegalArgumentException if the input is invalid or the recipe does not exist
   */
  public void handleRemoveRecipe() {
    try {
      String name = inputManager.getValidatedString(
          "Enter the name of the Recipe to be removed from the cookbook: ",
          "Recipe name cannot be empty/blank", "name"
      ).toLowerCase();
      
      boolean removedStatus = cookbook.removeRecipeFromCookbook(name);
      if (removedStatus) {
        System.out.println("Recipe \"" + name + "\" removed successfully.");
      } else {
        System.out.println("Recipe \"" + name + "\" does not exist.");
      }
      
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
  
  /**
   * Checks if the required ingredients for a recipe are available in the food storage.
   *
   * @throws IllegalArgumentException if the input is invalid or the recipe is not found
   * @throws NullPointerException     if the cookbook or food storage is not initialized
   * @throws Exception                for any unknown errors
   */
  public void handleViewHasEnoughIngredientsForRecipe() {
    try {
      if (cookbook == null || cookbook.getRecipes().isEmpty()) {
        System.out.println("The cookbook is empty. Add some recipes first.");
        return;
      }
      
      System.out.println("Select a recipe to check if you have enough ingredients:");
      List<Recipe> allRecipes = new ArrayList<>(cookbook.getRecipes().values());
      for (int i = 0; i < allRecipes.size(); i++) {
        System.out.println((i + 1) + ". " + allRecipes.get(i).getName());
      }
      
      int recipeChoice;
      do {
        recipeChoice = inputManager.getValidatedInt(
            "Enter the number of the recipe: ",
            "Invalid! Please enter a valid number.",
            "recipe choice"
        );
        
        if (recipeChoice < 1 || recipeChoice > allRecipes.size()) {
          System.out.println("Please choose a number between 1 and " + allRecipes.size() + ".");
        }
      } while (recipeChoice < 1 || recipeChoice > allRecipes.size());
      
      Recipe selectedRecipe = allRecipes.get(recipeChoice - 1);
      
      boolean enoughIngredients
          = foodStorage.hasEnoughIngredientsForRecipe(foodStorage, selectedRecipe);
      Map<String, Double> availability
          = foodStorage.getIngredientAvailabilityForRecipe(foodStorage, selectedRecipe);
      
      if (enoughIngredients) {
        System.out.println(
            "Yes! There are enough ingredients in the storage to make this recipe: "
                + selectedRecipe.getName());
        selectedRecipe.getIngredientsList().forEach(recipeIngredient -> {
          double available = availability.getOrDefault(recipeIngredient.getName(), 0.0);
          System.out.println(" - " + recipeIngredient.getName()
              + ": Required = " + recipeIngredient.getQuantity()
              + ", Available: " + available);
        });
      } else {
        System.out.println("There is not enough to make this recipe: " + selectedRecipe.getName());
        selectedRecipe.getIngredientsList().forEach(recipeIngredient -> {
          double available = availability.getOrDefault(recipeIngredient.getName(), 0.0);
          if (available > 0) {
            System.out.println(" - " + recipeIngredient.getName()
                + ": Required = " + recipeIngredient.getQuantity()
                + ", Available: " + available);
          } else {
            System.out.println(" - " + recipeIngredient.getName()
                + ": Required = " + recipeIngredient.getQuantity()
                + ", Available: 0 (Not in foodStorage)");
          }
        });
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid input: " + e.getMessage());
    } catch (NullPointerException e) {
      System.out.println(
          "An error occurred: Make sure that your food storage and cookbook are initialized."
              + e.getMessage());
      //Exception handles all the exceptions
    } catch (Exception e) {
      System.out.println("An unknown error occurred: " + e.getMessage());
    }
  }
  
  /**
   * Suggests recipes that can be made with the ingredients in the food storage.
   *
   * <p>This method retrieves recipes from the cookbook where all required ingredients
   * are available in sufficient quantities in the food storage.
   * </p>
   *
   * @throws Exception if an error occurs during recipe suggestion
   */
  public void handleSuggestedRecipe() {
    try {
      List<Recipe> suggestedRecipes = cookbook.getSuggestedRecipes(foodStorage);
      
      if (suggestedRecipes.isEmpty()) {
        System.out.println("No recipes can be made with the current food storage.");
        return;
      }
      
      System.out.println("You can make the following recipes with the current food storage:");
      suggestedRecipes.forEach(recipe -> System.out.println("- " + recipe.getName()));
    } catch (Exception e) {
      System.out.println("An error occurred while suggesting recipes: " + e.getMessage());
    }
  }
  
  /**
   * Displays all recipes in the cookbook.
   *
   * <p>Iterates through all recipes in the cookbook and prints their details
   * </p>
   *
   * @throws Exception if an unknown error occurs while displaying recipes
   */
  public void handleDisplayCookbook() {
    try {
      HashMap<String, Recipe> cookbookRecipes = cookbook.getRecipes();
      if (cookbookRecipes.isEmpty()) {
        System.out.println("The cookbook does not contain any recipes.");
        return;
      }
      System.out.println("Recipes in the cookbook: ");
      System.out.println();
      cookbookRecipes.forEach((recipeName, recipe) -> {
        displayRecipe(recipe);
      });
    } catch (Exception e) {
      System.out.println("Error: Something unwanted has happened.");
    }
  }
  
  /**
   * Displays the details of a single recipe.
   *
   * <p>This helper method prints the recipe's name, description, procedure,
   * portions, and ingredient list.
   * </p>
   *
   * @param recipe the recipe whose details will be displayed
   * @throws NullPointerException if the recipe is null
   */
  private void displayRecipe(Recipe recipe) {
    System.out.println("Recipe Name: " + recipe.getName());
    System.out.println("Description: " + recipe.getDescription());
    System.out.println("Procedure: " + recipe.getProcedure());
    System.out.println("Portions: " + recipe.getPortions() + " person");
    System.out.println("Ingredients:");
    recipe.getIngredientsList().forEach(ingredient -> {
      System.out.println(" - " + ingredient.getName() + ": "
          + ingredient.getQuantity() + " " + ingredient.getUnitMeasurement().getSymbol());
    });
    System.out.println();
  }
  //System.out.println(recipe.toString());
}
