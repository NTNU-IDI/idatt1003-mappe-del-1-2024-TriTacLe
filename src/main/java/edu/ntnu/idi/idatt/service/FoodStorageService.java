package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.utils.ConsoleInputManager;
import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.FoodStorage;

import java.time.LocalDate;
import java.util.List;

/**
 * <h1>FoodStorageService.</h1>
 * Service class to handle interactions the between the user interface and food storage.
 *
 * <p>This service class provides functionality to manage the food storage.
 * It handles these operations: adding, searching, removing ingredients, and displaying
 * food storage information such as expired ingredients and total storage value.
 * </p>
 *
 * @author TriLe
 */
public class FoodStorageService {
  private final FoodStorage foodStorage;
  private final ConsoleInputManager inputManager;
  private Unit unit;
  
  /**
   * Constructs a FoodStorageService with the specified food storage and input handler.
   *
   * @param foodStorage  the food storage to manage
   * @param inputManager the input handler for user input validation
   */
  public FoodStorageService(FoodStorage foodStorage, ConsoleInputManager inputManager, Unit unit) {
    this.foodStorage = foodStorage;
    this.inputManager = inputManager;
    this.unit = unit;
  }
  
  /**
   * Updates the current currency unit for displaying values.
   *
   * @param unit the new currency unit
   */
  public void setUnit(Unit unit) {
    this.unit = unit;
    Ingredient.setUnitCurrency(unit);
  }
  
  /**
   * Adds a new ingredient to the food storage.
   *
   * <p>Validates user inputs for ingredient details and updates the storage accordingly.
   * </p>
   *
   * @throws IllegalArgumentException if any user input is invalid
   */
  public void handleAddIngredient() {
    try {
      final String name = inputManager.getValidatedString(
          "Enter ingredient name:",
          "Ingredient name cannot be empty/blank", "Name");
      double quantity = inputManager.getValidatedDouble(
          "Enter quantity:",
          "Invalid input for quantity", "Quantity", false);
      final Unit unit = inputManager.getValidatedUnit(
          "Enter unit (kg, g, L, mL, pcs):", "Invalid unit");
      final LocalDate expirationDate = inputManager.getValidatedDate(
          "Enter a date in the format (yyyy-mm-dd):",
          "Please enter a date in the format yyyy-mm-dd");
      final double pricePerUnit = inputManager.getValidatedDouble(
          "Enter the price of the amount of the ingredient added:",
          "Invalid input for price", "Price", true);
      
      Ingredient ingredient = new Ingredient(name, quantity, unit, expirationDate, pricePerUnit);
      foodStorage.addIngredientToFoodStorage(ingredient);
      
      System.out.println("Ingredient added: " + ingredient);
      
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
  
  /**
   * Searches for an ingredient in the food storage by name.
   *
   * @throws IllegalArgumentException if the food storage is not initialized or the name is invalid
   */
  public void handleSearchIngredient() {
    try {
      String name = inputManager.getValidatedString(
          "Enter name of the ingredient:",
          "Ingredient name cannot be empty/blank", "name").trim();
      
      if (foodStorage == null) {
        throw new IllegalArgumentException("Food storage is not initialized.");
      }
      
      List<Ingredient> searchStatus
          = foodStorage.searchForIngredientsInFoodStorage(name.toLowerCase());
      if (searchStatus != null && !searchStatus.isEmpty()) {
        System.out.println("Ingredient found: ");
        searchStatus.forEach(ingredient -> System.out.println(" - " + ingredient));
      } else {
        System.out.println("Ingredient: " + name + " does not exist.");
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid input: " + e.getMessage());
    }
  }
  
  /**
   * Removes a specified quantity of an ingredient from the food storage.
   *
   * @throws IllegalArgumentException if the ingredient does not exist or the quantity is invalid
   */
  
  public void handleRemoveIngredient() {
    try {
      String name = inputManager.getValidatedString(
          "Enter the name of the Ingredient to be removed from the food storage: ",
          "Ingredient name cannot be empty/blank", "name"
      ).toLowerCase();
      
      List<Ingredient> matchingIngredients = foodStorage.searchForIngredientsInFoodStorage(name);
      if (matchingIngredients == null || matchingIngredients.isEmpty()) {
        throw new IllegalArgumentException("Ingredient " + name + " does not exist.");
      }
      double totalQuantity = matchingIngredients.stream()
          .mapToDouble(Ingredient::getQuantity)
          .sum();
      
      double quantity = inputManager.getValidatedDouble(
          "Enter how much quantity of the Ingredient: " + matchingIngredients.getFirst().getName()
              + "(" + totalQuantity + matchingIngredients.getFirst().getUnitMeasurement().getSymbol()
              + ") to be removed: ", "Invalid quantity", "quantity", false
      );
      
      double removedQuantity = foodStorage.removeIngredientFromFoodStorage(name, quantity);
      
      System.out.println(
          "Removed " + removedQuantity + matchingIngredients.getFirst().getUnitMeasurement().getSymbol()
              + " from " + matchingIngredients.getFirst().getName());
      
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
  
  /**
   * Displays all expired ingredients in the food storage along with their total value.
   *
   * @throws IllegalArgumentException if there is an error while fetching expired ingredients
   */
  public void handleDisplayExpiredIngredients() {
    try {
      List<Ingredient> expiredIngredients = foodStorage.getExpiredIngredients();
      
      if (expiredIngredients == null || expiredIngredients.isEmpty()) {
        System.out.println("No Ingredients is expired! Nice!");
        System.out.println("Nice work saving the environment!");
        return;
      }
      
      double totalValue = foodStorage.calculateTotalValue(expiredIngredients.stream());
      
      System.out.println("Expired ingredients:");
      expiredIngredients.forEach(ingredient -> System.out.println("- " + ingredient));
      System.out.println("Total value of expired ingredients: "
          + totalValue + " " + unit.getSymbol());
      System.out.println("Try to conserve more food please!");
    } catch (IllegalArgumentException e) {
      System.out.println("Error while trying to display expired Ingredients: " + e.getMessage());
    }
  }
  
  /**
   * Displays the total value of all ingredients in the food storage.
   *
   * @throws IllegalArgumentException if there is an error while calculating the total value
   */
  public void handleTotalValue() {
    try {
      if (foodStorage == null || foodStorage.isEmpty()) {
        System.out.println("Food storage is empty");
        return;
      }
      double totalValue = foodStorage
          .calculateTotalValue(foodStorage.getIngredients().values().stream()
              .flatMap(List::stream));
      
      System.out.println("The total value of the food storage is: " + totalValue + unit.getSymbol());
    } catch (IllegalArgumentException e) {
      System.out.println("Error while getting the total value of food storage: " + e.getMessage());
    }
  }
  
  /**
   * Displays all ingredients expiring before a specified date.
   *
   * @throws IllegalArgumentException if there is an error while fetching the ingredients
   */
  public void handleViewIngredientsBeforeDate() {
    LocalDate date = inputManager.getValidatedDate(
        "Enter a date (yyyy-mm-dd) to view Ingredients expiring before it: ",
        "Please enter a date in the format yyyy-mm-dd");
    try {
      List<Ingredient> ingredientsBeforeDate = foodStorage.getIngredientsExpiringBefore(date);
      if (ingredientsBeforeDate == null || ingredientsBeforeDate.isEmpty()) {
        System.out.println("No Ingredients expire before: " + date);
      } else {
        System.out.println("----Ingredients expiring before date: " + date + "----");
        ingredientsBeforeDate.forEach(ingredient -> System.out.println("- " + ingredient));
        
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error while retrieving Ingredients: " + e.getMessage());
    }
  }
  
  /**
   * Displays the food storage sorted alphabetically by ingredient names.
   *
   * @throws IllegalArgumentException if there is an error while sorting or displaying the storage
   */
  public void handleDisplayFoodStorageAlphabetically() {
    System.out.println("----Food storage sorted out alphabetically by name----");
    if (foodStorage == null || foodStorage.isEmpty()) {
      System.out.println("Food storage is empty. These is not Ingredients in food storage.");
    }
    try {
      if (foodStorage != null) {
        foodStorage.getFoodStorageAlphabetically();
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error while displaying the food storage from a-z: " + e.getMessage());
    }
  }
}
