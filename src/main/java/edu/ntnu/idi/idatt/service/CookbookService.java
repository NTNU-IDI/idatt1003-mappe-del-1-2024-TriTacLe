package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.Utils.UserInputHandler;
import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.Cookbook;
import edu.ntnu.idi.idatt.storage.FoodStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CookbookService {
  private final Cookbook cookbook;
  private final FoodStorage foodStorage;
  private final UserInputHandler inputHandler;
  
  public CookbookService(Cookbook cookbook, FoodStorage foodStorage, UserInputHandler inputHandler) {
    this.cookbook = cookbook;
    this.foodStorage = foodStorage;
    this.inputHandler = inputHandler;
  }
  
  /**
   *
   */
  public void handleAddRecipeToCookbook() {
    try {
      String nameRecipe;
      do {
        nameRecipe = inputHandler.getValidatedString("Enter recipe name: ", "Recipe name cannot be empty/blank", "recipe name");
        if (cookbook.getRecipes().containsKey(nameRecipe)) {
          System.out.println("Recipe: " + nameRecipe + " already exist");
        }
      } while (cookbook.getRecipes().containsKey(nameRecipe));
      
      final String description = inputHandler.getValidatedString("Enter a description: ", "Description cannot be empty/blank", "description");
      final String procedure = inputHandler.getValidatedString("Enter the procedure: ", "Procedure cannot be empty/blank", "procedure");
      final double portions = inputHandler.getValidatedDouble("Enter how many people this recipe is for", "Portions have to be a number and it has to be positive", "portions");
      
      Recipe recipe = new Recipe(nameRecipe, description, procedure, portions);
      try {
        int choice;
        do {
          choice = inputHandler.getValidatedInt(
              "How would you like to add ingredients? Enter 1: Add manually. Enter 2: Choose from available food storage ingredients.\nEnter your choice: ",
              "Invalid input. Enter 1 or 2", "choice");
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
        System.out.println("Invalid choice input. Please enter a valid option (1 or 2)");
      }
      
      boolean success = cookbook.addRecipeToCookbook(recipe);
      if (success) {
        System.out.println("Recipe added to the cookbook successfully:");
        System.out.println(recipe);
      } else {
        System.out.println("Recipe for " + recipe.getName() + " already exists in the cookbook.");
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error while creating recipe: " + e.getMessage());
    }
  }
  
  private void addIngredientsManually(Recipe recipe) {
    try {
      int totalIngredients = (int) inputHandler.getValidatedDouble("Enter how many ingredients you want this recipe to have", "Total ingredients cannot be negative", "Total ingredients");
      for (int i = 0; i < totalIngredients; i++) {
        System.out.println("Ingredient: " + (i + 1) + " of " + totalIngredients);
        final String name = inputHandler.getValidatedString("Enter ingredient name:", "Ingredient name cannot be empty/blank", "name");
        double quantity = inputHandler.getValidatedDouble("Enter quantity:", "Invalid input for quantity", "quantity");
        final Unit unit = inputHandler.getValidatedUnit("Enter unit (kg, g, L, mL, pcs):", "Invalid unit");
        final double pricePerUnit = inputHandler.getValidatedDouble("Enter price per unit:", "Invalid input for price", "price");
        
        Ingredient ingredient = new Ingredient(name, quantity, unit, pricePerUnit);
        recipe.addIngredientToRecipe(ingredient);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error adding ingredients manually: " + e.getMessage());
    }
  }
  
  
  private void addIngredientsFromStorage(Recipe recipe) {
    try {
      int totalIngredients = (int) inputHandler.getValidatedDouble("Enter how many ingredients you want this recipe to have",
          "Total ingredients cannot be negative/other type than int or double",
          "total ingredients");
      
      System.out.println("Every ingredient that has not expired and can be used in a recipe:");
      List<Ingredient> availableIngredients = foodStorage.getIngredientsExpiringAfter(LocalDate.now());
      availableIngredients.forEach(ingredient -> System.out.println("- " + ingredient));
      
      for (int i = 0; i < totalIngredients; i++) {
        System.out.println("Ingredient: " + (i + 1) + " of " + totalIngredients);
        handleIngredientAddition(recipe);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("An error occurred while adding ingredients from storage: " + e.getMessage());
    }
  }
  
  private void handleIngredientAddition(Recipe recipe) {
    try {
      String ingredientKey;
      List<Ingredient> ingredients;
      
      do {
        ingredientKey = inputHandler.getValidatedString("Enter the ingredient you want", "Error: Could not retrieve the ingredient name.", "name").toLowerCase();
        
        ingredients = getIngredientsFromStorage(ingredientKey);
        if (ingredients == null || ingredients.isEmpty()) {
          System.out.println("Ingredient not found in storage. Please enter a valid ingredient.");
        }
      } while (ingredients == null || ingredients.isEmpty());
      
      double totalAvailableQuantity = ingredients.stream()
          .mapToDouble(Ingredient::getQuantity)
          .sum();
      
      System.out.println("Total available quantity for " + ingredientKey + ": " + totalAvailableQuantity + ". Please read: Remember this is total quantity of " + ingredientKey + " in the food storage. How much you want to add to the recipe is an unrealized amount so it will not be deducted from the real amount.");
      
      int requestedQuantity = inputHandler.getValidatedInt("Enter quantity to add:", "Invalid input for quantity.", "requested quantity");
      
      if (requestedQuantity > totalAvailableQuantity && !confirmAdditionExceedingQuantity()) {
        System.out.println("Ingredient not added to the recipe.");
        return;
      }
      
      allocateIngredientsToRecipe(recipe, ingredients, requestedQuantity);
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid input: " + e.getMessage());
    }
  }
  
  private List<Ingredient> getIngredientsFromStorage(String ingredientKey) {
    try {
      if (foodStorage.getIngredients() == null || !foodStorage.getIngredients().containsKey(ingredientKey)) {
        return null;
      }
      
      List<Ingredient> ingredients = new ArrayList<>(foodStorage.getIngredients().get(ingredientKey));
      ingredients.sort(Comparator.comparing(Ingredient::getExpirationDate));
      return ingredients;
    } catch (IllegalArgumentException e) {
      System.out.println("Error while retrieving ingredients from storage: " + e.getMessage());
      return null;
    }
  }
  
  private boolean confirmAdditionExceedingQuantity() {
    try {
      String proceed = inputHandler.getValidatedString("Requested quantity exceeds available ingredients. Do you still want to add this ingredient? (yes/no)", "Invalid input. Please answer yes or no.", "yes/no input");
      return proceed.equalsIgnoreCase("yes");
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid input: " + e.getMessage());
      return false;
    }
  }
  
  private void allocateIngredientsToRecipe(Recipe recipe, List<Ingredient> ingredients, int requestedQuantity) {
    try {
      double remainingToUse = requestedQuantity;
      
      for (Ingredient ingredient : ingredients) {
        if (remainingToUse <= 0) break;
        
        double availableQuantity = ingredient.getQuantity();
        double toAllocate = Math.min(availableQuantity, remainingToUse);
        remainingToUse -= toAllocate;
        
        Ingredient ingredientToAdd = new Ingredient(ingredient.getName(), toAllocate, ingredient.getUnit(), ingredient.getPrice());
        recipe.addIngredientToRecipe(ingredientToAdd);
        System.out.println("Added ingredient: " + ingredientToAdd.getName() + " (" + toAllocate + " " + ingredientToAdd.getUnit().getSymbol() + ") to the recipe.");
      }
      
      if (remainingToUse > 0) {
        System.out.println("Could not use the full quantity. Short by " + remainingToUse);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error while try to allocate ingredients to the recipe: " + e.getMessage());
    }
  }
  
  
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
        recipeChoice = inputHandler.getValidatedInt(
            "Enter the number of the recipe: ",
            "Invalid! Please enter a valid number.",
            "recipe choice"
        );
        
        if (recipeChoice < 1 || recipeChoice > allRecipes.size()) {
          System.out.println("Please choose a number between 1 and " + allRecipes.size() + ".");
        }
      } while (recipeChoice < 1 || recipeChoice > allRecipes.size());
      
      Recipe selectedRecipe = allRecipes.get(recipeChoice - 1);
      
      boolean enoughIngredients = foodStorage.hasEnoughIngredientsForRecipe(foodStorage, selectedRecipe);
      Map<String, Double> availability = foodStorage.getIngredientAvailabilityForRecipe(foodStorage, selectedRecipe);
      
      if (enoughIngredients) {
        System.out.println("Yes! There are enough ingredients in the storage to make this recipe: " + selectedRecipe.getName());
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
      System.out.println("An error occurred: Make sure that your food storage and cookbook are initialized." + e.getMessage());
      //Exception handles all the exceptions
    } catch (Exception e) {
      System.out.println("An unknown error occurred: " + e.getMessage());
    }
  }
  
  
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
  
  public void handleDisplayCookbook() {
    try {
      HashMap<String, Recipe> cookbookContents = cookbook.getRecipes();
      if (cookbookContents.isEmpty()) {
        System.out.println("The cookbook does not contain any recipes.");
        return;
      }
      System.out.println("Recipes in the cookbook: ");
      System.out.println();
      cookbookContents.forEach((recipeName, recipe) -> {
        displayRecipe(recipe);
      });
    } catch (Exception e) {
      System.out.println("Error: Something unwanted has happened.");
    }
  }
  
  /**
   * Helper
   *
   * @param recipe
   */
  private void displayRecipe(Recipe recipe) {
    System.out.println("Recipe Name: " + recipe.getName());
    System.out.println("Description: " + recipe.getDescription());
    System.out.println("Procedure: " + recipe.getProcedure());
    System.out.println("Portions: " + recipe.getPortions() + " person");
    System.out.println("Ingredients:");
    recipe.getIngredientsList().forEach(ingredient -> {
      System.out.println(" - " + ingredient.getName() + ": " + ingredient.getQuantity() + " " + ingredient.getUnit().getSymbol());
    });
    System.out.println();
  }
  //System.out.println(recipe.toString());
  
}
