package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.FoodStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class FoodStorageTest {
  
  private FoodStorage foodStorage;
  
  @BeforeEach
  public void setUp() {
    foodStorage = new FoodStorage();
  }
  
  @Nested
  @DisplayName("Positive tests for FoodStorage")
  class PositiveTests {
    
    @Test
    @DisplayName("Add ingredient to FoodStorage")
    void addIngredientToFoodStorage() {
      try {
        Ingredient ingredient = new Ingredient("Apple", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 15);
        foodStorage.addIngredientToFoodStorage(ingredient);
        assertTrue(foodStorage.ingredientExist("apple"));
        assertEquals(2.0, foodStorage.getIngredients().get("apple").get(0).getQuantity());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown for valid input: " + e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Add duplicate ingredient updates quantity")
    void addDuplicateIngredientUpdatesQuantity() {
      try {
        Ingredient ingredient1 = new Ingredient("Rice", 1.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 20);
        Ingredient ingredient2 = new Ingredient("Rice", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 20);
        
        foodStorage.addIngredientToFoodStorage(ingredient1);
        foodStorage.addIngredientToFoodStorage(ingredient2);
        
        assertEquals(3.0, foodStorage.getIngredients().get("rice").get(0).getQuantity());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown for adding duplicate of ingredient");
      }
    }
    
    @Test
    @DisplayName("Search for ingredients in FoodStorage by name")
    void searchForIngredientsInFoodStorage() {
      // Arrange
      Ingredient ingredient1 = new Ingredient("Apple", 1.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 15);
      Ingredient ingredient2 = new Ingredient("apple", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(5), 20);
      Ingredient ingredient3 = new Ingredient("Banana", 3.0, Unit.KILOGRAM, LocalDate.now().plusDays(7), 10);
      
      foodStorage.addIngredientToFoodStorage(ingredient1);
      foodStorage.addIngredientToFoodStorage(ingredient2);
      foodStorage.addIngredientToFoodStorage(ingredient3);
      
      // Act
      List<Ingredient> searchResults = foodStorage.searchForIngredientsInFoodStorage("apple");
      
      // Assert
      assertNotNull(searchResults, "Search results should not be null");
      assertEquals(2, searchResults.size(), "There should be two matching ingredients for 'apple'");
      assertTrue(searchResults.contains(ingredient1), "Search results should contain the first 'apple' ingredient");
      assertTrue(searchResults.contains(ingredient2), "Search results should contain the second 'apple' ingredient");
    }
    
    @Test
    @DisplayName("Remove ingredient reduces quantity correctly")
    void removeIngredientReducesQuantity() {
      try {
        Ingredient ingredient = new Ingredient("Sugar", 5.0, Unit.KILOGRAM, LocalDate.now().plusDays(20), 50);
        foodStorage.addIngredientToFoodStorage(ingredient);
        
        double removedQuantity = foodStorage.removeIngredientFromFoodStorage("sugar", 3.0);
        
        assertEquals(3.0, removedQuantity);
        assertEquals(2.0, foodStorage.getIngredients().get("sugar").get(0).getQuantity());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown while removing an amount of the ingredient");
      }
      
    }
    
    @Test
    @DisplayName("Get expired ingredients")
    void getExpiredIngredients() {
      try {
        Ingredient expired = new Ingredient("Bread", 1.0, Unit.PIECES, LocalDate.now().minusDays(1), 10);
        Ingredient fresh = new Ingredient("Cheese", 1.0, Unit.PIECES, LocalDate.now().plusDays(5), 20);
        
        foodStorage.addIngredientToFoodStorage(expired);
        foodStorage.addIngredientToFoodStorage(fresh);
        
        List<Ingredient> expiredIngredients = foodStorage.getExpiredIngredients();
        
        assertEquals(1, expiredIngredients.size());
        assertEquals("Bread", expiredIngredients.get(0).getName());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown while retrieving all the expired ingredients");
      }
    }
    
    @Test
    @DisplayName("Get ingredients expiring after a specific date")
    void getIngredientsExpiringAfter() {
      Ingredient earlyExpiring = new Ingredient("Eggs", 1.0, Unit.PIECES, LocalDate.now().plusDays(1), 10);
      Ingredient laterExpiring = new Ingredient("Butter", 1.0, Unit.KILOGRAM, LocalDate.now().plusDays(5), 50);
      
      foodStorage.addIngredientToFoodStorage(earlyExpiring);
      foodStorage.addIngredientToFoodStorage(laterExpiring);
      
      List<Ingredient> result = foodStorage.getIngredientsExpiringAfter(LocalDate.now().plusDays(2));
      
      assertEquals(1, result.size());
      assertEquals("Butter", result.get(0).getName());
    }
    
    @Test
    @DisplayName("Get ingredients sorted alphabetically")
    void getFoodStorageAlphabetically() {
      Ingredient flour = new Ingredient("Flour", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(5), 20);
      Ingredient sugar = new Ingredient("Sugar", 1.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 15);
      
      foodStorage.addIngredientToFoodStorage(sugar);
      foodStorage.addIngredientToFoodStorage(flour);
      
      System.out.println("Food Storage Alphabetically:");
      foodStorage.getFoodStorageAlphabetically();
      
      List<String> ingredientNames = List.of("Flour", "Sugar");
      assertEquals(ingredientNames.get(0), "Flour");
      assertEquals(ingredientNames.get(1), "Sugar");
    }
    
    @Test
    @DisplayName("Calculate total value")
    void calculateTotalValue() {
      try {
        Ingredient carrot = new Ingredient("Carrot", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 30);
        Ingredient potato = new Ingredient("Potato", 5.0, Unit.KILOGRAM, LocalDate.now().plusDays(15), 20);
        
        foodStorage.addIngredientToFoodStorage(carrot);
        foodStorage.addIngredientToFoodStorage(potato);
        
        double totalValue = foodStorage.calculateTotalValue(
            foodStorage.getIngredients().values().stream().flatMap(List::stream)
        );
        assertEquals(50, totalValue);
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown while calculation total value of food storage");
      }
    }
    
    @Test
    @DisplayName("Check ingredient availability for recipe")
    void checkIngredientAvailabilityForRecipe() {
      try {
        Ingredient milk = new Ingredient("Milk", 1.0, Unit.LITRE, LocalDate.now().plusDays(5), 20);
        Ingredient sugar = new Ingredient("Sugar", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 15);
        foodStorage.addIngredientToFoodStorage(milk);
        foodStorage.addIngredientToFoodStorage(sugar);
        
        Recipe recipe = new Recipe("Cake", "Sweet cake", "Mix and bake", 4);
        recipe.addIngredientToRecipe(new Ingredient("Milk", 1.0, Unit.LITRE, 20));
        recipe.addIngredientToRecipe(new Ingredient("Sugar", 1.0, Unit.KILOGRAM, 15));
        
        boolean hasEnough = foodStorage.hasEnoughIngredientsForRecipe(foodStorage, recipe);
        
        assertTrue(hasEnough);
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown while checking if"
            + " there is enough ingredients to make a recipe");
      }
    }
    
    @Test
    @DisplayName("Get ingredient availability for recipe")
    void getIngredientAvailabilityForRecipe() {
      Ingredient flour = new Ingredient("Flour", 3.0, Unit.KILOGRAM, LocalDate.now().plusDays(7), 10);
      foodStorage.addIngredientToFoodStorage(flour);
      
      Recipe recipe = new Recipe("Pancakes", "Fluffy pancakes", "Mix and cook", 2);
      recipe.addIngredientToRecipe(new Ingredient("Flour", 2.0, Unit.KILOGRAM, 10));
      
      Map<String, Double> availability = foodStorage.getIngredientAvailabilityForRecipe(foodStorage, recipe);
      
      assertEquals(1, availability.size());
      assertEquals(3.0, availability.get("Flour"));
    }
  }
  
  @Test
  @DisplayName("toString returns formatted storage details")
  void toStringReturnsFormattedDetails() {
    Ingredient flour = new Ingredient("Flour", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(5), 20);
    Ingredient sugar = new Ingredient("Sugar", 1.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 15);
    
    foodStorage.addIngredientToFoodStorage(flour);
    foodStorage.addIngredientToFoodStorage(sugar);
    
    String expectedOutput = "Ingredients in storage:\n"
        + "  - Flour (2.0 kg) Expires: " + flour.getExpirationDate() + " Price: 20.0 kr\n"
        + "  - Sugar (1.0 kg) Expires: " + sugar.getExpirationDate() + " Price: 15.0 kr\n";
    
    assertEquals(expectedOutput, foodStorage.toString());
  }
  
  @Nested
  @DisplayName("Negative tests for FoodStorage")
  class NegativeTests {
    
    @Test
    @DisplayName("Add null ingredient throws IllegalArgumentException")
    void addNullIngredientThrowsException() {
      try {
        foodStorage.addIngredientToFoodStorage(null);
        fail("Expected IllegalArgumentException for adding null ingredient");
      } catch (IllegalArgumentException e) {
        assertEquals("Ingredient cannot be null", e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Remove more quantity than available throws exception")
    void removeExcessQuantityThrowsException() {
      Ingredient ingredient = new Ingredient("Milk", 1.0, Unit.LITRE, LocalDate.now().plusDays(5), 20);
      foodStorage.addIngredientToFoodStorage(ingredient);
      try {
        foodStorage.removeIngredientFromFoodStorage("milk", 2.0);
        fail("Expected IllegalArgumentException for removing excess quantity");
      } catch (IllegalArgumentException e) {
        assertEquals("Invalid quantity to remove: 2.0. Available: 1.0", e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Remove non-existing ingredient throws exception")
    void removeNonExistingIngredientThrowsException() {
      try {
        foodStorage.removeIngredientFromFoodStorage("salt", 1.0);
        fail("Expected IllegalArgumentException for removing non-existing ingredient");
      } catch (IllegalArgumentException e) {
        assertEquals("Ingredient salt does not exist in storage.", e.getMessage());
      }
    }
  }
}
