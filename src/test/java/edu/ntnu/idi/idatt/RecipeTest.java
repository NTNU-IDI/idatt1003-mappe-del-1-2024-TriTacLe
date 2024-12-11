package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@DisplayName("Tests for Recipe Class")
class RecipeTest {
  
  private Recipe recipe;
  private Ingredient ingredient1;
  private Ingredient ingredient2;
  private Ingredient ingredient3;
  
  @BeforeEach
  void setUp() {
    recipe = new Recipe("Pancakes", "Simple pancake recipe", "Mix and cook", 4);
    ingredient1 = new Ingredient("Flour", 200, Unit.GRAM, 5.0);
    ingredient2 = new Ingredient("Milk", 1, Unit.LITRE, 15.0);
    ingredient3 = new Ingredient("Egg", 6, Unit.PIECES, 2.0);
  }
  
  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {
    
    @Test
    @DisplayName("Should create Recipe with valid attributes")
    void shouldCreateRecipeWithValidAttributes() {
      assertNotNull(recipe);
      assertEquals("Pancakes", recipe.getName());
      assertEquals("Simple pancake recipe", recipe.getDescription());
      assertEquals("Mix and cook", recipe.getProcedure());
      assertEquals(4, recipe.getPortions());
    }
    
    @Test
    @DisplayName("Should add ingredient to recipe")
    void shouldAddIngredientToRecipe() {
      recipe.addIngredientToRecipe(ingredient1);
      assertTrue(recipe.getIngredientsList().contains(ingredient1));
    }
    
    @Test
    @DisplayName("Should update quantity if ingredient already exists")
    void shouldUpdateQuantityForExistingIngredient() {
      recipe.addIngredientToRecipe(ingredient1);
      Ingredient duplicate = new Ingredient("Flour", 100, Unit.GRAM, 5.0);
      recipe.addIngredientToRecipe(duplicate);
      assertEquals(300, ingredient1.getQuantity());
    }
    
    
    @Test
    @DisplayName("toString should return formatted recipe details")
    void shouldReturnFormattedToString() {
      recipe.addIngredientToRecipe(ingredient1);
      recipe.addIngredientToRecipe(ingredient2);
      
      String expected = "Name: Pancakes\n"
          + "Description: Simple pancake recipe\n"
          + "Procedure: Mix and cook\n"
          + "Ingredients: \n"
          + " - Flour (200.0 g)" + " Price: 5.0 NOK\n"
          + " - Milk (1.0 L)" + " Price: 15.0 NOK\n";
      
      assertEquals(expected, recipe.toString());
    }
  }
  
  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should throw exception for invalid name")
    void shouldThrowExceptionForInvalidName(String invalidName) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Recipe(invalidName, "Description", "Procedure", 4);
      });
      assertEquals("Name cannot be empty or blank", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should throw exception for invalid description")
    void shouldThrowExceptionForInvalidDescription(String invalidDescription) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Recipe("Recipe", invalidDescription, "Procedure", 4);
      });
      assertEquals("Description cannot be empty or blank", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should throw exception for invalid procedure")
    void shouldThrowExceptionForInvalidProcedure(String invalidProcedure) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Recipe("Recipe", "Description", invalidProcedure, 4);
      });
      assertEquals("Procedure cannot be empty or blank", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {-3.5, -100000000.0, 0.0, Double.NaN})
    @DisplayName("Should throw exception for negative portions")
    void shouldThrowExceptionForNegativePortions(double invalidPortions) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Recipe("Recipe", "Description", "Procedure", invalidPortions);
      });
      assertEquals("Portions cannot be negative or NaN", exception.getMessage());
    }
  }
}
