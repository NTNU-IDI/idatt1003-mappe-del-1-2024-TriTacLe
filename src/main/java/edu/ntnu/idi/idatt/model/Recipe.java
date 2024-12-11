package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.utils.InputValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a recipe with a name, description, procedure, portions, and a list of ingredients.
 *
 * <p>This class provides functionality to manage and display recipe details, as well as
 * adding ingredients to the recipe while handling potential duplicates.</p>
 *
 * @author TriLe
 * @version 1.2.1
 */
public class Recipe {
  private final String name;
  private final String description;
  private final String procedure;
  private final double portions;
  private final ArrayList<Ingredient> ingredientsList;
  
  /**
   * Initializes a new Recipe instance with the specified attributes.
   * An object is created if its passes the validation method by InputValidator
   *
   * @param name        the name of the recipe; must not be null or empty.
   * @param description a short description of the recipe; must not be null or empty.
   * @param procedure   the procedure to prepare the recipe; must not be null or empty.
   * @param portions    the number of portions for the recipe; must be a positive number.
   * @throws IllegalArgumentException if any input is invalid.
   */
  public Recipe(String name, String description, String procedure, double portions) {
    InputValidator.validateString(name, "Name");
    InputValidator.validateString(description, "Description");
    InputValidator.validateString(procedure, "Procedure");
    InputValidator.validateDouble(portions, "Portions");
    
    this.name = name;
    this.description = description;
    this.procedure = procedure;
    this.portions = portions;
    this.ingredientsList = new ArrayList<>();
  }
  
  /**
   * Gets the name of the recipe.
   *
   * @return the name of the recipe.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Gets the description of the recipe.
   *
   * @return the description of the recipe.
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * Gets the procedure for preparing the recipe.
   *
   * @return the procedure of the recipe.
   */
  public String getProcedure() {
    return procedure;
  }
  
  /**
   * Gets the number of portions the recipe is intended for.
   *
   * @return the number of portions.
   */
  public double getPortions() {
    return portions;
  }
  
  /**
   * Gets the list of ingredients required for the recipe.
   *
   * @return the list of ingredients.
   */
  public List<Ingredient> getIngredientsList() {
    return ingredientsList;
  }
  
  /**
   * Adds an ingredient to the recipe. If the ingredient already exists (by name),
   * its quantity is updated. Otherwise, it is added to the list.
   * Optional used to handle the possibility if a matching ingredient
   * might not be found in the ingredientsList.
   *
   * @param ingredient the ingredient to be added; must not be null.
   * @throws IllegalArgumentException if the ingredient is null.
   */
  public void addIngredientToRecipe(Ingredient ingredient) {
    try {
      if (ingredient == null) {
        throw new IllegalArgumentException("Ingredient cannot be null");
      }
      Optional<Ingredient> existingIngredient = ingredientsList.stream()
          .filter(ingredientInRegister ->
              ingredientInRegister.getName().equals(ingredient.getName()))
          .findAny();
      
      if (existingIngredient.isPresent()) {
        existingIngredient.get().updateQuantity(ingredient.getQuantity());
      } else {
        ingredientsList.add(ingredient);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error adding ingredient to recipe: " + e.getMessage());
    }
  }
  
  /**
   * Returns a string representation of the recipe, including its name, description,
   * procedure, and list of ingredients.
   *
   * @return a string representation of the recipe.
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
        stringBuilder.append(" - ").append(ingredient).append("\n");
      }
    }
    return stringBuilder.toString();
  }
}
