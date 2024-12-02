package edu.ntnu.idi.idatt.service;

import edu.ntnu.idi.idatt.Utils.UserInputHandler;
import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.FoodStorage;

import java.time.LocalDate;
import java.util.List;

public class FoodStorageService {
  private FoodStorage foodStorage;
  private UserInputHandler inputHandler;
  
  public FoodStorageService(FoodStorage foodStorage, UserInputHandler inputHandler) {
    this.foodStorage = foodStorage;
    this.inputHandler = inputHandler;
  }
  
  public void handleAddIngredient() {
    try {
      final String name = inputHandler.getValidatedString("Enter ingredient name:", "Ingredient name cannot be empty/blank", "name");
      double quantity = inputHandler.getValidatedDouble("Enter quantity:", "Invalid input for quantity", "quantity");
      final Unit unit = inputHandler.getValidatedUnit("Enter unit (kg, g, L, mL, pcs):", "Invalid unit");
      final LocalDate expirationDate = inputHandler.getValidatedDate("Enter a date in the format (yyyy-mm-dd):", "Please enter a date in the format yyyy-mm-dd");
      final double pricePerUnit = inputHandler.getValidatedDouble("Enter the price of the amount of the ingredient added:", "Invalid input for price", "price");
      
      Ingredient ingredient = new Ingredient(name, quantity, unit, expirationDate, pricePerUnit);
      foodStorage.addIngredientToFoodStorage(ingredient);
      
      System.out.println("Ingredient added: " + ingredient);
      
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
  
  public void handleSearchIngredient() {
    try {
      String name = inputHandler.getValidatedString("Enter name of the ingredient:", "Ingredient name cannot be empty/blank", "name");
      
      if (foodStorage == null) {
        throw new IllegalArgumentException("Food storage is not initialized.");
      }
      
      List<Ingredient> searchStatus = foodStorage.searchForIngredientsInFoodStorage(name.toLowerCase());
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
  
  public void handleRemoveIngredient() {
    try {
      String name = inputHandler.getValidatedString(
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
      
      double quantity = inputHandler.getValidatedDouble(
          "Enter how much quantity of the Ingredient: " + matchingIngredients.getFirst().getName()
              + "(" + totalQuantity + matchingIngredients.getFirst().getUnit().getSymbol()
              + ") to be removed: ", "Invalid quantity", "quantity"
      );
      
      double removedQuantity = foodStorage.removeIngredientFromFoodStorage(name, quantity);
      
      System.out.println("Removed " + removedQuantity + matchingIngredients.getFirst().getUnit().getSymbol() + " from " + matchingIngredients.getFirst().getName());
      
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
  
  public void handleDisplayExpiredIngredients() {
    /*getExpiredIngredients method are being called from the foodStorage class
    And the result is being assigned to the expiredIngredients variable*/
    try {
      List<Ingredient> expiredIngredients = foodStorage.getExpiredIngredients();
      
      if (expiredIngredients == null || expiredIngredients.isEmpty()) {
        System.out.println("No Ingredients is expired! Nice!");
        return;
      }
      
      double totalValue = foodStorage.calculateTotalValue(expiredIngredients.stream());
      
      System.out.println("Expired Ingredients");
      expiredIngredients.forEach(Ingredient -> System.out.println("- " + Ingredient));
      System.out.printf("Total value of expired Ingredients: %.2f kr%n", totalValue); //2 desimaler
      System.out.println("Try to conserve more food");
    } catch (IllegalArgumentException e) {
      System.out.println("Error while trying to display expired Ingredients: " + e.getMessage());
    }
  }
  
  public void handleTotalValue() {
    try {
      if (foodStorage == null || foodStorage.isEmpty()) {
        System.out.println("Food storage is empty");
        return;
      }
      double totalValue = foodStorage.calculateTotalValue(foodStorage.getIngredients().values().stream()
          .flatMap(List::stream));
      
      System.out.println("The total value of the food storage is: " + totalValue + " kr");
    } catch (IllegalArgumentException e) {
      System.out.println("Error while getting the total value of food storage: " + e.getMessage());
    }
  }
  
  public void handleViewIngredientsBeforeDate() {
    LocalDate date = inputHandler.getValidatedDate("Enter a date (yyyy-mm-dd) to view Ingredients expiring before it: ", "Please enter a date in the format yyyy-mm-dd");
    try {
      List<Ingredient> IngredientsBeforeDate = foodStorage.getIngredientsExpiringBefore(date);
      if (IngredientsBeforeDate == null || IngredientsBeforeDate.isEmpty()) {
        System.out.println("No Ingredients expire before: " + date);
      } else {
        System.out.println("----Ingredients expiring before date: " + date + "----");
        //IngredientsBeforeDate.forEach(System.out::println);
        IngredientsBeforeDate.forEach(Ingredient -> System.out.println("- " + Ingredient));
        
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Error while retrieving Ingredients: " + e.getMessage());
    }
  }
  
  public void handleDisplayFoodStorageAlphabetically() {
    System.out.println("----Food storage sorted out alphabetically by name----");
    if (foodStorage == null || foodStorage.isEmpty()) {
      System.out.println("Food storage is empty. These is not Ingredients in food storage.");
    }
    try {
      foodStorage.getFoodStorageAlphabetically();
    } catch (IllegalArgumentException e) {
      System.out.println("Error while displaying the food storage from a-z: " + e.getMessage());
    }
  }
}
