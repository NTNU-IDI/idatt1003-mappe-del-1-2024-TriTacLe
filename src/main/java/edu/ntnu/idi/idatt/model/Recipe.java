package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.Utils.InputValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Recipe {
  private final String name;
  private final String description;
  private final String procedure;
  private final double portions;
  private final ArrayList<Ingredient> ingredientsList;
  private InputValidation inputValidation;
  
  public Recipe(String name, String description, String procedure, double portions) {
    inputValidation.validateString(name, "Name of the recipe cannot be blank/empty");
    inputValidation.validateString(description, "Description cannot be blank/empty");
    inputValidation.validateString(procedure, "Procedure cannot be blank/empty");
    inputValidation.validateDouble(portions, "Portions cannot negative");
    
    this.name = name;
    this.description = description;
    this.procedure = procedure;
    this.portions = portions;
    this.ingredientsList = new ArrayList<>();
  }
  
  
  public String getName() {
    return name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public String getProcedure() {
    return procedure;
  }
  
  public double getPortions() {
    return portions;
  }
  
  public List<Ingredient> getIngredientsList() {
    return ingredientsList;
  }
  
  /**
   * Adds ingredient objects to the recipe (ingredientsList ArrayList)
   *
   * @param ingredient
   */
  public boolean addIngredientToRecipe(Ingredient ingredient) {
    Optional<Ingredient> existingIngredient = ingredientsList.stream()
        .filter(ingredientInRegister -> ingredientInRegister.getName().equals(ingredient.getName()))
        .findAny();
    
    if (existingIngredient.isPresent()) {
      existingIngredient.get().updateQuantity(ingredient.getQuantity());
      return false;
    } else {
      ingredientsList.add(ingredient);
      return true;
    }
  }
  /*
  /**
   * Checks if there are enough ingredients in the food storage to prepare the recipe.
   * <p>
   * This method iterates through the list of ingredients required for the recipe and compares
   * the total quantity available in the food storage with the quantity needed for each
   * ingredient. It prints out whether there are enough ingredients in the storage and displays the
   * required and available quantities for each ingredient.
   * </p>
   *
   * @param foodStorage The food storage containing a collection of ingredients in the kitchen.
   *                    This is used to check the availability of each ingredient required for
   *                    the recipe.
   */
  /*
  public void hasEnoughIngredientsForRecipe(FoodStorage foodStorage) {
    boolean enoughIngredients = ingredientsList.stream()
        .allMatch(recipeIngredient -> {
          double totalAvailable = foodStorage.getIngredients().values().stream()
              .flatMap(List::stream)
              .filter(storageIngredient -> storageIngredient.getName().equals(recipeIngredient.getName()))
              .mapToDouble(storageIngredient -> {
                if (!storageIngredient.getUnit().equals(recipeIngredient.getUnit())) {
                  return storageIngredient.getUnit().converter(storageIngredient.getQuantity(), recipeIngredient.getUnit());
                }
                return storageIngredient.getQuantity();
              })
              .sum();
          return totalAvailable >= recipeIngredient.getQuantity();
        });
    
    if (enoughIngredients) {
      System.out.println("There is enough ingredients in the storage to make this recipe: " + getName());
      ingredientsList.forEach(recipeIngredient -> {
        double totalQuantityAvailable = foodStorage.getIngredients().values().stream()
            .flatMap(List::stream)
            .filter(storageIngredient -> storageIngredient.getName().equals(recipeIngredient.getName()))
            .mapToDouble(Ingredient::getQuantity)
            .sum();
        
        System.out.println(" - " + recipeIngredient.getName()
            + ": Required = " + recipeIngredient.getQuantity()
            + ", Available: " + totalQuantityAvailable);
      });
    } else {
      System.out.println("There is not enough to make this recipe: " + getName());
      ingredientsList.forEach(recipeIngredient -> {
        double totalAvailable = foodStorage.getIngredients().values().stream()
            .flatMap(List::stream)
            .filter(storageIngredient -> storageIngredient.getName().equals(recipeIngredient.getName()))
            .mapToDouble(Ingredient::getQuantity)
            .sum();
        
        if (totalAvailable > 0) {
          System.out.println(" - " + recipeIngredient.getName()
              + ": Required = " + recipeIngredient.getQuantity()
              + ", Available: " + totalAvailable);
        } else {
          System.out.println(" - " + recipeIngredient.getName()
              + ": Required = " + recipeIngredient.getQuantity()
              + ", Available: 0 (Not in foodStorage)");
        }
      });
    }
  }
   */
  
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (name != null && !name.isBlank()) {
      stringBuilder.append("Name: ").append(name).append("\n");
    }
    if (description != null && !description.isEmpty()) {
      stringBuilder.append("Description: ").append(description).append("\n");
    }
    if (procedure != null && !procedure.isEmpty()) {
      stringBuilder.append("Procedure: ").append(procedure).append("\n");
    }
    if (ingredientsList != null && !ingredientsList.isEmpty()) {
      stringBuilder.append("Ingredients: \n");
      for (Ingredient ingredient : ingredientsList) {
        stringBuilder.append(" - ").append(ingredient);
      }
    }
    return stringBuilder.toString();
  }
  
}
