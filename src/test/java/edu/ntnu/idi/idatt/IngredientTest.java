package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Unit;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@DisplayName("Tests for Ingredient Class")
class IngredientTest {
  
  private Ingredient ingredient;
  
  @BeforeEach
  public void setUp() {
    ingredient = new Ingredient(
        "Protein Bar", 2.5,
        Unit.KILOGRAM, LocalDate.now().plusDays(5), 75.5);
  }
  
  @Nested
  @DisplayName("Positive tester for FoodStorage")
  class PositiveTests {
    
    @Test
    @DisplayName("Should create Ingredient with valid attributes")
    void shouldCreateIngredientWithValidAttributes() {
      try {
        assertNotNull(ingredient);
        assertEquals("Protein Bar", ingredient.getName());
        assertEquals(2.5, ingredient.getQuantity());
        assertEquals(Unit.KILOGRAM, ingredient.getUnitMeasurement());
        assertEquals(LocalDate.now().plusDays(5), ingredient.getExpirationDate());
        assertEquals(75.5, ingredient.getPrice());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown for valid input: " + e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Should allow creation without expiration date")
    void shouldCreateIngredientWithoutExpirationDate() {
      try {
        assertNotNull(ingredient);
        assertEquals("Protein Bar", ingredient.getName());
        assertEquals(2.5, ingredient.getQuantity());
        assertEquals(Unit.KILOGRAM, ingredient.getUnitMeasurement());
        assertEquals(75.5, ingredient.getPrice());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown for valid input: " + e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Should update quantity correctly")
    void shouldUpdateQuantity() {
      try {
        ingredient.updateQuantity(3.0);
        assertEquals(5.5, ingredient.getQuantity());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown while updating quantity: " + e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Should set quantity correctly")
    void shouldSetQuantityCorrectly() {
      try {
        ingredient.setQuantity(4.0);
        assertEquals(4.0, ingredient.getQuantity());
      } catch (IllegalArgumentException e) {
        fail("Exception should not have been thrown while setting quantity" + e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Should return correct expiration date")
    void shouldReturnCorrectExpirationDate() {
      assertEquals(LocalDate.now().plusDays(5), ingredient.getExpirationDate());
    }
    
    @Test
    @DisplayName("toString should return formatted string")
    void shouldReturnFormattedToString() {
      String expectedString = ""
          + "Protein Bar (2.5 kg) Expires: "
          + LocalDate.now().plusDays(5) + " Price: 75.5 kr";
      assertEquals(expectedString, ingredient.toString());
    }
  }
  
  @Nested
  @DisplayName("Negative tests")
  class NegativeTests {
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("Should throw exception for empty name")
    void shouldThrowExceptionForEmptyName(String invalidName) {
      try {
        new Ingredient(invalidName, 2.5, Unit.GRAM, LocalDate.now().plusDays(10), 75.5);
        fail("Expected IllegalArgumentException for empty name");
      } catch (IllegalArgumentException e) {
        assertEquals("Name cannot be empty or blank", e.getMessage());
      }
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {-75.5, -100000000.0, Double.NaN})
    @DisplayName("Should throw exception for negative price")
    void shouldThrowExceptionForNegativePrice(double invalidPrice) {
      try {
        new Ingredient(
            "Protein Bar", 2.5, Unit.KILOGRAM,
            LocalDate.now().plusDays(10), invalidPrice);
        fail("Expected IllegalArgumentException for invalid price: " + invalidPrice);
      } catch (IllegalArgumentException e) {
        assertEquals("Price cannot be negative or NaN", e.getMessage());
      }
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.0, 0.0, Double.NaN})
    @DisplayName("Should throw exception for invalid quantities")
    void shouldThrowExceptionForNegativeQuantity(double invalidQuantity) {
      try {
        ingredient.setQuantity(invalidQuantity);
        fail("Expected IllegalArgumentException for invalid quantity: " + invalidQuantity);
      } catch (IllegalArgumentException e) {
        assertEquals("Quantity cannot be negative or NaN", e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Should throw exception for null unit")
    void shouldThrowExceptionForNullUnit() {
      try {
        new Ingredient("Protein Bar", 2.5, null, LocalDate.now().plusDays(10), 75.5);
        fail("Expected IllegalArgumentException for null unit");
      } catch (IllegalArgumentException e) {
        assertEquals("Unit cannot be empty or null: null", e.getMessage());
      }
      ;
    }
    
    @Test
    @DisplayName("Should throw exception for invalid quantity in setter")
    void shouldThrowExceptionForInvalidQuantityInSetter() {
      try {
        ingredient.setQuantity(-1.0);
        fail("Expected IllegalArgumentException for invalid quantity");
      } catch (IllegalArgumentException e) {
        assertEquals("Quantity cannot be negative or NaN", e.getMessage());
      }
    }
    
    @Test
    @DisplayName("Should throw exception for null expiration date")
    void shouldThrowExceptionForNullExpirationDate() {
      try {
        new Ingredient("Creatine", 2.5, Unit.KILOGRAM, null, 15.0);
        fail("Expected IllegalArgumentException for null expiration date");
      } catch (IllegalArgumentException e) {
        assertEquals("Expiration date cannot be null", e.getMessage());
      }
    }
  }
}
