package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.FoodStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {
  
  private Recipe recipe;
  private Ingredient ingredient1;
  private Ingredient ingredient2;
  private Ingredient ingredient3;
  private Ingredient ingredient4;
  private FoodStorage foodStorage;
  
  private static final double ITEM1_INITIAL_QUANTITY = 20;
  private static final double ITEM2_INITIAL_QUANTITY = 30;
  private static final double ITEM3_INITIAL_QUANTITY = 40;
  private static final double ITEM4_INITIAL_QUANTITY = 20;
  private static final double ITEM1_ADDITIONAL_QUANTITY = 20;
  
  @BeforeEach
  void setUp() {
    //ingredients
    ingredient1 = new Ingredient("Apple", 20, Unit.PIECES, LocalDate.of(2000, 12, 15), ITEM1_INITIAL_QUANTITY);
    ingredient2 = new Ingredient("Milk", 100, Unit.DESILITRE, LocalDate.of(2024, 12, 15), ITEM2_INITIAL_QUANTITY);
    ingredient3 = new Ingredient("Sugar", 3000, Unit.GRAM, LocalDate.of(2025, 12, 24), ITEM3_INITIAL_QUANTITY);
    ingredient4 = new Ingredient("Orange", 5, Unit.PIECES, LocalDate.of(1900, 12, 15), ITEM4_INITIAL_QUANTITY);
    //recipe
    recipe = new Recipe("Cake", "Delicious chocolate cake", "Mix all ingredients and bake", 4);
    //foodstorage
    foodStorage = new FoodStorage();
    foodStorage.addIngredientToFoodStorage(ingredient1);
    foodStorage.addIngredientToFoodStorage(ingredient2);
  }
  
  @Test
  void testCreateRecipe() {
    assertNotNull(recipe, "Recipe should be created.");
    assertEquals("Cake", recipe.getName(), "Name should be 'Cake'");
    assertEquals("Delicious chocolate cake", recipe.getDescription(), "Description should be 'Delicious chocolate cake'");
    assertEquals("Mix all ingredients and bake", recipe.getProcedure(), "Procedure should match");
    assertEquals(4, recipe.getPortions(), "Recipe serves should be 4");
  }
  
  @Test
  void testAddIngredientToRecipe() {
    // Add ingredient1 to the recipe
    recipe.addIngredientToRecipe(ingredient1);
    assertTrue(recipe.getIngredientsList().contains(ingredient1), "Ingredient1 should be added to the recipe.");
    
    // Add ingredient3 (Sugar) to the recipe
    recipe.addIngredientToRecipe(ingredient3);
    assertTrue(recipe.getIngredientsList().contains(ingredient3), "Ingredient3 (Sugar) should be added to the recipe.");
    
    // Add a new ingredient to the recipe (ingredient4)
    recipe.addIngredientToRecipe(ingredient4);
    assertTrue(recipe.getIngredientsList().contains(ingredient4), "Ingredient4 (Orange) should be added to the recipe.");
    
    // Add ingredient1 again (should increase the quantity)
    Ingredient ingredient1Duplicate = new Ingredient("Apple", 20, Unit.PIECES, LocalDate.of(2000, 12, 15), ITEM1_ADDITIONAL_QUANTITY);
    recipe.addIngredientToRecipe(ingredient1Duplicate);
    assertEquals(ITEM1_INITIAL_QUANTITY + ITEM1_ADDITIONAL_QUANTITY, ingredient1.getQuantity(),
        "Quantity of Apple should be updated to the new total.");
  }
  
  @Test
  void testHasEnoughIngredientsForRecipe() {
    recipe.addIngredientToRecipe(ingredient1);  // Apple
    recipe.addIngredientToRecipe(ingredient2);  // Milk
    
    // Check if the recipe has enough ingredients in the food storage
    recipe.hasEnoughIngredientsForRecipe(foodStorage);
    // Expected output should confirm that the storage has enough quantities of Apple and Milk
  }
  
  @Test
  void testHasNotEnoughIngredientsForRecipe() {
    // Simulate the case where eggs are missing
    foodStorage.getIngredients().clear();
    foodStorage.addIngredientToFoodStorage(new Ingredient("Eggs", 10, Unit.PIECES, LocalDate.of(2025, 12, 24), 3));
    
    recipe.addIngredientToRecipe(ingredient1);  // Apple
    recipe.addIngredientToRecipe(ingredient2);  // Milk
    
    // The recipe should report that there are not enough ingredients
    recipe.hasEnoughIngredientsForRecipe(foodStorage);
    // Expected output should indicate insufficient quantities for some ingredients
  }
  
  @Test
  void testInvalidConstructorArguments() {
    assertThrows(IllegalArgumentException.class, () -> new Recipe("", "description", "procedure", 4),
        "Constructor should throw IllegalArgumentException for an empty name.");
    
    assertThrows(IllegalArgumentException.class, () -> new Recipe("Recipe", "", "procedure", 4),
        "Constructor should throw IllegalArgumentException for an empty description.");
    
    assertThrows(IllegalArgumentException.class, () -> new Recipe("Recipe", "description", "", 4),
        "Constructor should throw IllegalArgumentException for an empty procedure.");
    
    assertThrows(IllegalArgumentException.class, () -> new Recipe("Recipe", "description", "procedure", -1),
        "Constructor should throw IllegalArgumentException for negative recipeServes.");
  }
  
  @Test
  void testToString() {
    recipe.addIngredientToRecipe(ingredient1);
    recipe.addIngredientToRecipe(ingredient2);
    
    String expectedOutput = "Name: Cake\n" +
        "Description: Delicious chocolate cake\n" +
        "Procedure: Mix all ingredients and bake\n" +
        "Ingredients: \n" +
        " - Apple(20.0 Pieces) Expires: 2000-12-15 Price: 20.0 kr \n" +
        " - Milk(100.0 Milliliters) Expires: 2024-12-15 Price: 30.0 kr \n";
    
    assertEquals(expectedOutput, recipe.toString(), "The toString method should output the correct recipe details.");
  }
}
