package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.FoodStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the foodStorage class.
 */
class FoodStorageTest {
  private FoodStorage foodStorage;
  
  /**
   * Initializes a new FoodStorage instance before each test
   * Why: Ensures each test starts with a clean FoodStorage object, avoiding side effects from previous tests.
   */
  @BeforeEach
  void setUp() {
    foodStorage = new FoodStorage();
  }
  
  /**
   * Tests adding ingredients to foodStorage, including when an ingredient already exist
   */
  @Test
  void testAddIngredientToFoodStorage() {
    //Arrange
    Ingredient ingredient1 = new Ingredient("Apple", 5, Unit.KILOGRAM, LocalDate.now().plusDays(10), 20);
    //Act
    foodStorage.addIngredientToFoodStorage(ingredient1);
    //Assert
    assertTrue(foodStorage.ingredientExist("apple"), "Apple should exist in the storage");//the apple ingredient exist in storage
    assertEquals(1, foodStorage.getIngredients().get("apple").size(), "There should only be one entry for Apple");//there is only one ArrayList for apple in the hashmap
    
    //Add another Apple with the same expiration date, unit and price.
    Ingredient ingredient2 = new Ingredient("Apple", 3, Unit.KILOGRAM, LocalDate.now().plusDays(10), 20);
    foodStorage.addIngredientToFoodStorage(ingredient2);
    //Verifies that the quantity is updated and the there is only one arrayList for Apple.
    assertEquals(1, foodStorage.getIngredients().get("apple").size(), "There should still be only one entry for Apple with matching attributes");
    assertEquals(8, foodStorage.getIngredients().get("apple").get(0).getQuantity(), "Quantity should be updated to 8");
    
    //Add a Apple with different attributes
    Ingredient ingredient3 = new Ingredient("Apple", 2, Unit.KILOGRAM, LocalDate.now().plusDays(15), 22);
    foodStorage.addIngredientToFoodStorage(ingredient3);
    assertEquals(2, foodStorage.getIngredients().get("apple").size(), "A second entry should exist for Apple with different attributes");
    
    //negative test
    assertNotNull(foodStorage);
    assertThrows(NullPointerException.class, () -> foodStorage.addIngredientToFoodStorage(null));
  }
  
  /**
   * Tests searching for an ingredient instance in foodStorage class
   */
  @Test
  void testSearchForIngredientInFoodStorage() {
    Ingredient ingredient1 = new Ingredient("Milk", 1, Unit.LITRE, LocalDate.now().plusDays(5), 15);
    Ingredient ingredient2 = new Ingredient("Milk", 2, Unit.LITRE, LocalDate.now().plusDays(10), 30); // Add a second ingredient with the same name
    foodStorage.addIngredientToFoodStorage(ingredient1);
    foodStorage.addIngredientToFoodStorage(ingredient2);
    
    List<Ingredient> result1 = foodStorage.searchForIngredientsInFoodStorage("Milk");
    List<Ingredient> result2 = foodStorage.searchForIngredientsInFoodStorage("mIlK"); // Test case-insensitive
    
    assertNotNull(result1, "Milk should be found");
    assertFalse(result1.isEmpty(), "Result should not be empty");
    assertEquals(2, result1.size(), "Should find two ingredients named 'Milk'");
    assertTrue(result1.stream().allMatch(ingredient -> "Milk".equalsIgnoreCase(ingredient.getName())), "All found ingredients should have name 'Milk'");
    
    assertNotNull(result2, "Case-insensitive search should find Milk");
    assertFalse(result2.isEmpty(), "Result for case-insensitive search should not be empty");
    assertEquals(2, result2.size(), "Case-insensitive search should find two ingredients");
    assertTrue(result2.stream().allMatch(ingredient -> "Milk".equalsIgnoreCase(ingredient.getName())), "All found ingredients should have name 'Milk'");
    
    //Negative
    List<Ingredient> result3 = foodStorage.searchForIngredientsInFoodStorage("Boxing");
    assertNotNull(result3, "Result for non-existent ingredient should not be null");
    assertTrue(result3.isEmpty(), "Non-existent ingredient should return an empty list");
    
    assertThrows(NullPointerException.class, () -> foodStorage.searchForIngredientsInFoodStorage(null), "Searching for null should throw NullPointerException");
  }
  
  /**
   * Tests removing ingredients from foodStorage
   */
  @Test
  void testRemoveIngredientFromFoodStorage() {
    Ingredient ingredient1 = new Ingredient("Orange", 3, Unit.KILOGRAM, LocalDate.now().plusDays(5), 25);
    Ingredient ingredient2 = new Ingredient("Orange", 2, Unit.KILOGRAM, LocalDate.now().plusDays(10), 25);
    foodStorage.addIngredientToFoodStorage(ingredient1);
    foodStorage.addIngredientToFoodStorage(ingredient2);
    double removedQuantity = foodStorage.removeIngredientFromFoodStorage("Orange", 4);
    assertEquals(4, removedQuantity, "Total removed quantity should be 4");
    
    assertNotNull(foodStorage.getIngredients().get("orange"), "Orange list should still exist in storage");
    assertEquals(1, foodStorage.getIngredients().get("orange").get(0).getQuantity(), "Remaining quantity of the first ingredient should be 1");
    
    foodStorage.removeIngredientFromFoodStorage("Orange", 1);
    assertFalse(foodStorage.ingredientExist("orange"), "Orange should no longer exist in storage after removing all quantities");
    
    //negative tests
    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeIngredientFromFoodStorage("Orange", 1), "Removing non-existent ingredient should throw IllegalArgumentException");
    assertThrows(IllegalArgumentException.class, () -> foodStorage.removeIngredientFromFoodStorage("Apple", 2), "Removing an ingredient not in storage should throw IllegalArgumentException");
  }
  
  /**
   * Tests retrieving expired ingredients from foodStorage.
   */
  @Test
  void testGetExpiredIngredients() {
    Ingredient ingredient1 = new Ingredient("Tomahawk", 1, Unit.PIECES, LocalDate.now().minusDays(1), 1);
    Ingredient ingredient2 = new Ingredient("Chicken", 1, Unit.KILOGRAM, LocalDate.now().plusDays(5), 90);
    foodStorage.addIngredientToFoodStorage(ingredient1);
    foodStorage.addIngredientToFoodStorage(ingredient2);
    
    List<Ingredient> expiredIngredients = foodStorage.getExpiredIngredients();
    assertEquals(1, expiredIngredients.size(), "Only one ingredient should be expired");
    assertEquals("Tomahawk", expiredIngredients.get(0).getName(), "Expired ingredient name should be Tomahawk");
    
    //Negative tests
    assertTrue(foodStorage.getExpiredIngredients().stream()
        .noneMatch(ingredient -> ingredient.getName().equals("Chicken")), "Non-expired ingredients should not appear in the expired list");
    foodStorage = new FoodStorage();
    assertTrue(foodStorage.getExpiredIngredients().isEmpty(), "Empty storage should return no expired ingredients");
  }
  
  /**
   * Tests getting ingredients with expiration date before a date
   */
  @Test
  void testGetIngredientsExpirationDateBefore() {
    Ingredient ingredient1 = new Ingredient("Salt", 1, Unit.GRAM, LocalDate.of(3000, 10, 10), 60);
    Ingredient ingredient2 = new Ingredient("Steak", 1, Unit.KILOGRAM, LocalDate.now().plusDays(10), 40);
    foodStorage.addIngredientToFoodStorage(ingredient1);
    foodStorage.addIngredientToFoodStorage(ingredient2);
    
    List<Ingredient> ingredientsExpiringBeforeDate = foodStorage.getIngredientsExpiringBefore(LocalDate.now().plusDays(12));
    
    assertEquals(1, ingredientsExpiringBeforeDate.size(), "Only one ingredient should expire before the given date");
    assertEquals("Steak", ingredientsExpiringBeforeDate.getFirst().getName(), "Ingredient name should be Steak");
    
    //negative
    assertTrue(foodStorage.getIngredientsExpiringBefore(LocalDate.now().plusDays(1)).isEmpty(), "No ingredients should expire before today");
    assertThrows(NullPointerException.class, () -> foodStorage.getIngredientsExpiringBefore(null), "Null date should throw NullPointerException");
  }
  
  /**
   * Test checking if
   */
  @Test
  void testSortAlphabetically() {
    Ingredient ingredient1 = new Ingredient("Banan", 5.0, Unit.PIECES, LocalDate.now().plusDays(10), 30.0);
    Ingredient ingredient2 = new Ingredient("Apple", 5.0, Unit.PIECES, LocalDate.now().plusDays(7), 20.0);
    
    foodStorage.addIngredientToFoodStorage(ingredient1);
    foodStorage.addIngredientToFoodStorage(ingredient2);
    
    foodStorage.getFoodStorageAlphabetically();
    
    List<String> sortedKeys = new ArrayList<>(foodStorage.getIngredients().keySet()).stream()
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList();
    
    assertEquals("apple", sortedKeys.get(0), "Apple should be sorted first");
    assertEquals("banan", sortedKeys.get(1), "Banan should be sorted second");
  }
  
  /**
   *
   */
  @Test
  void testTotalValue() {
    Ingredient ingredient1 = new Ingredient("Eggs", 12, Unit.PIECES, LocalDate.now().plusDays(5), 2);
    Ingredient ingredient2 = new Ingredient("Creatine", 1000, Unit.GRAM, LocalDate.of(2028, 2, 10), 15);
    foodStorage.addIngredientToFoodStorage(ingredient1);
    foodStorage.addIngredientToFoodStorage(ingredient2);
    
    double totalValue = foodStorage.calculateTotalValue(foodStorage.getIngredients().values().stream()
        .flatMap(List::stream));
    double expectedTotalValue = ingredient1.getPrice() + ingredient2.getPrice();
    
    //assertEquals(17, totalValue);
    assertEquals(expectedTotalValue, totalValue, 0.01);
    
    //negative
    assertThrows(IllegalArgumentException.class, () -> foodStorage.calculateTotalValue(null), "Null stream should throw Ill.Arg.Exc.");
    foodStorage = new FoodStorage();
    assertEquals(0, foodStorage.calculateTotalValue(foodStorage.getIngredients().values().stream().flatMap(List::stream)));
    
    foodStorage = new FoodStorage();
    assertEquals(0, foodStorage.calculateTotalValue(
        foodStorage.getIngredients().values().stream().flatMap(List::stream)
    ), "Total value of empty storage should be 0");
  }
  
  /**
   * Tests checking if FoodStorage is empty.
   */
  @Test
  void testIsEmpty() {
    assertTrue(foodStorage.isEmpty());
    Ingredient ingredient = new Ingredient("Banana", 10, Unit.KILOGRAM, LocalDate.now().plusDays(7), 15);
    foodStorage.addIngredientToFoodStorage(ingredient);
    assertFalse(foodStorage.isEmpty());
    foodStorage.removeIngredientFromFoodStorage("Banana", 10);
    assertTrue(foodStorage.isEmpty());
    foodStorage = new FoodStorage();
    assertTrue(foodStorage.isEmpty());
  }
}
