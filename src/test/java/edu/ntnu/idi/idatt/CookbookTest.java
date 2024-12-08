package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.Cookbook;
import edu.ntnu.idi.idatt.storage.FoodStorage;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cookbook Tests")
class CookbookTest {
  
  private Cookbook cookbook;
  
  @BeforeEach
  void setUp() {
    cookbook = new Cookbook();
  }
  
  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {
    
    @Test
    @DisplayName("Add Recipe Successfully")
    void testAddRecipeToCookbook() {
      Recipe recipe = new Recipe("Pasta", "Delicious pasta", "Cook pasta and add sauce", 2);
      boolean result = cookbook.addRecipeToCookbook(recipe);
      
      assertTrue(result);
      assertEquals(1, cookbook.getRecipes().size());
    }
    
    @Test
    @DisplayName("Search Recipe Successfully")
    void testSearchForRecipe() {
      Recipe recipe = new Recipe("Pasta", "Delicious pasta", "Cook pasta and add sauce", 2);
      cookbook.addRecipeToCookbook(recipe);
      
      Optional<java.util.Map.Entry<String, Recipe>> result
          = cookbook.searchForRecipeInCookbook("Pasta");
      assertTrue(result.isPresent());
      assertEquals("Pasta", result.get().getKey());
    }
    
    @Test
    @DisplayName("Remove Recipe Successfully")
    void testRemoveRecipe() {
      Recipe recipe = new Recipe(
          "Pasta", "Delicious pasta",
          "Cook pasta and add sauce", 2);
      cookbook.addRecipeToCookbook(recipe);
      
      boolean result = cookbook.removeRecipeFromCookbook("Pasta");
      assertTrue(result);
      assertEquals(0, cookbook.getRecipes().size());
    }
    
    @Test
    @DisplayName("Suggest Recipes with Sufficient Ingredients")
    void testGetSuggestedRecipes() {
      FoodStorage foodStorage = new FoodStorage();
      foodStorage.addIngredientToFoodStorage(new Ingredient(
          "Tomato", 2, Unit.KILOGRAM,
          LocalDate.now().plusDays(5), 20.0));
      foodStorage.addIngredientToFoodStorage(new Ingredient(
          "Pasta", 1, Unit.KILOGRAM,
          LocalDate.now().plusDays(5), 30.0));
      
      Recipe recipe = new Recipe(
          "PastaDish", "Simple pasta dish",
          "Cook pasta and add tomato", 2);
      recipe.addIngredientToRecipe(new Ingredient(
          "Tomato", 1, Unit.KILOGRAM, 20.0));
      recipe.addIngredientToRecipe(new Ingredient(
          "Pasta", 1, Unit.KILOGRAM, 30.0));
      cookbook.addRecipeToCookbook(recipe);
      
      List<Recipe> suggestedRecipes = cookbook.getSuggestedRecipes(foodStorage);
      assertEquals(1, suggestedRecipes.size());
      assertEquals("PastaDish", suggestedRecipes.get(0).getName());
    }
  }
  
  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {
    
    @Test
    @DisplayName("Add Duplicate Recipe")
    void testAddDuplicateRecipe() {
      Recipe recipe = new Recipe(
          "Pasta", "Delicious pasta",
          "Cook pasta and add sauce", 2);
      cookbook.addRecipeToCookbook(recipe);
      
      boolean result = cookbook.addRecipeToCookbook(recipe);
      assertFalse(result);
      assertEquals(1, cookbook.getRecipes().size());
    }
    
    @Test
    @DisplayName("Add Null Recipe")
    void testAddNullRecipeToCookbook() {
      try {
        cookbook.addRecipeToCookbook(null);
        fail("Expected IllegalArgumentException for name is null");
      } catch (IllegalArgumentException e) {
        assertEquals("Recipe cannot be null", e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Search Non-existent Recipe")
    void testSearchForNonExistentRecipe() {
      Optional<java.util.Map.Entry<String, Recipe>> result
          = cookbook.searchForRecipeInCookbook("NonExistentRecipe");
      assertFalse(result.isPresent());
    }
    
    @Test
    @DisplayName("Remove Non-existent Recipe")
    void testRemoveNonExistentRecipe() {
      boolean result = cookbook.removeRecipeFromCookbook("NonExistentRecipe");
      assertFalse(result, "Expected false when trying to remove a non-existent recipe");
      assertEquals(0, cookbook.getRecipes().size(), "Expected cookbook to remain empty");
    }
    
    @Test
    @DisplayName("Search for Recipe with Null Name")
    void testSearchForRecipeWithNullName() {
      try {
        cookbook.searchForRecipeInCookbook(null);
        fail("Expected IllegalArgumentException for null name");
      } catch (IllegalArgumentException e) {
        assertEquals("Recipe name cannot be null", e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Remove Null Recipe")
    void testRemoveNullRecipeFromCookbook() {
      try {
        cookbook.removeRecipeFromCookbook(null);
        fail("Expected IllegalArgumentException for name is null");
      } catch (IllegalArgumentException e) {
        assertEquals("Recipe name cannot be null", e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Suggest Recipes with Insufficient Ingredients")
    void testGetSuggestedRecipesInsufficientIngredients() {
      FoodStorage foodStorage = new FoodStorage();
      foodStorage.addIngredientToFoodStorage(new Ingredient(
          "Tomato", 0.5, Unit.KILOGRAM,
          LocalDate.now().plusDays(5), 20.0));
      foodStorage.addIngredientToFoodStorage(new Ingredient(
          "Pasta", 1, Unit.KILOGRAM,
          LocalDate.now().plusDays(5), 30.0));
      
      Recipe recipe = new Recipe("PastaDish", "Simple pasta dish", "Cook pasta and add tomato", 2);
      recipe.addIngredientToRecipe(new Ingredient(
          "Tomato", 1, Unit.KILOGRAM, 20.0));
      recipe.addIngredientToRecipe(new Ingredient(
          "Pasta", 1, Unit.KILOGRAM, 30.0));
      cookbook.addRecipeToCookbook(recipe);
      
      List<Recipe> suggestedRecipes = cookbook.getSuggestedRecipes(foodStorage);
      assertTrue(suggestedRecipes.isEmpty());
    }
    
    @Test
    @DisplayName("Suggest Recipes with Null Food Storage")
    void testGetSuggestedRecipesNullFoodStorage() {
      List<Recipe> recipes = cookbook.getSuggestedRecipes(null);
      assertNotNull(recipes);
      assertTrue(recipes.isEmpty(), "Expected no recipes to be suggested for null food storage");
    }
  }
}