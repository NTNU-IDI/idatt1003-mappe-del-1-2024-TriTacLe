package edu.ntnu.idi.idatt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
      assertNotNull(ingredient);
      assertEquals("Protein Bar", ingredient.getName());
      assertEquals(2.5, ingredient.getQuantity());
      assertEquals(Unit.KILOGRAM, ingredient.getUnitMeasurement());
      assertEquals(LocalDate.now().plusDays(5), ingredient.getExpirationDate());
      assertEquals(75.5, ingredient.getPrice());
    }
    
    @Test
    @DisplayName("Should allow creation without expiration date")
    void shouldCreateIngredientWithoutExpirationDate() {
      assertNotNull(ingredient);
      assertEquals("Protein Bar", ingredient.getName());
      assertEquals(2.5, ingredient.getQuantity());
      assertEquals(Unit.KILOGRAM, ingredient.getUnitMeasurement());
      assertEquals(75.5, ingredient.getPrice());
    }
    
    @Test
    @DisplayName("Should update quantity correctly")
    void shouldUpdateQuantity() {
      ingredient.updateQuantity(3.0);
      assertEquals(5.5, ingredient.getQuantity());
    }
    
    @Test
    @DisplayName("Should set quantity correctly")
    void shouldSetQuantityCorrectly() {
      ingredient.setQuantity(4.0);
      assertEquals(4.0, ingredient.getQuantity());
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
          + LocalDate.now().plusDays(5)
          + " Price: 75.5 "
          + Ingredient.getUnitCurrency().getSymbol();
      assertEquals(expectedString, ingredient.toString());
    }
  }
  
  @Test
  @DisplayName("Default currency should be KR")
  void defaultCurrencyShouldBeKR() {
    Ingredient.setUnitCurrency(Unit.KR);
    assertEquals(Unit.KR, Ingredient.getUnitCurrency(),
        "Default currency should be KR.");
  }
  
  @Test
  @DisplayName("Should allow setting a valid currency")
  void shouldAllowSettingValidCurrency() {
    Ingredient.setUnitCurrency(Unit.USD);
    assertEquals(Unit.USD, Ingredient.getUnitCurrency(), "Currency should be updated to USD.");
    
    Ingredient.setUnitCurrency(Unit.EUR);
    assertEquals(Unit.EUR, Ingredient.getUnitCurrency(), "Currency should be updated to EUR.");
  }
  
  @Nested
  @DisplayName("Negative tests")
  class NegativeTests {
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("Should throw exception for empty name")
    void shouldThrowExceptionForEmptyName(String invalidName) {
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> new Ingredient(invalidName, 2.5, Unit.GRAM, LocalDate.now().plusDays(10), 75.5),
          "Expected IllegalArgumentException for empty name"
      );
      assertEquals("Name cannot be empty or blank", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {-75.5, -100000000.0, Double.NaN})
    @DisplayName("Should throw exception for negative price")
    void shouldThrowExceptionForNegativePrice(double invalidPrice) {
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> new Ingredient("Protein Bar", 2.5, Unit.KILOGRAM,
              LocalDate.now().plusDays(10), invalidPrice),
          "Expected IllegalArgumentException for invalid price: " + invalidPrice
      );
      assertEquals("Price cannot be negative or NaN", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -100.0, 0.0, Double.NaN})
    @DisplayName("Should throw exception for invalid quantities")
    void shouldThrowExceptionForNegativeQuantity(double invalidQuantity) {
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> ingredient.setQuantity(invalidQuantity),
          "Expected IllegalArgumentException for invalid quantity: " + invalidQuantity
      );
      assertEquals("Quantity cannot be negative or NaN", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should throw exception for null unit")
    void shouldThrowExceptionForNullUnit() {
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> new Ingredient("Protein Bar", 2.5, null, LocalDate.now().plusDays(10), 75.5),
          "Expected IllegalArgumentException for null unit"
      );
      assertEquals("Unit cannot be empty or null: null", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should throw exception for invalid quantity in setter")
    void shouldThrowExceptionForInvalidQuantityInSetter() {
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> ingredient.setQuantity(-1.0),
          "Expected IllegalArgumentException for invalid quantity"
      );
      assertEquals("Quantity cannot be negative or NaN", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should throw exception for null expiration date")
    void shouldThrowExceptionForNullExpirationDate() {
      IllegalArgumentException exception = assertThrows(
          IllegalArgumentException.class,
          () -> new Ingredient("Creatine", 2.5, Unit.KILOGRAM, null, 15.0),
          "Expected IllegalArgumentException for null expiration date"
      );
      assertEquals("Expiration date cannot be null", exception.getMessage());
    }
  }
  
  
  @Test
  @DisplayName("Should throw exception when setting an invalid currency")
  void shouldThrowExceptionForInvalidCurrency() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> Ingredient.setUnitCurrency(Unit.GRAM),
        "Setting a non-currency unit as currency should throw IllegalArgumentException."
    );
    
    assertEquals("Invalid currency unit.", exception.getMessage());
  }
  
  @Test
  @DisplayName("Ingredient string representation should update based on currency")
  void ingredientStringRepresentationShouldUpdateCurrency() {
    Ingredient.setUnitCurrency(Unit.KR);
    Ingredient ingredient = new Ingredient(
        "Flour", 2.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 50.0);
    
    assertTrue(
        ingredient.toString().contains("Price: 50.0 " + Unit.KR.getSymbol()),
        "String representation should include the correct KR currency."
    );
    
    Ingredient.setUnitCurrency(Unit.USD);
    assertTrue(
        ingredient.toString().contains("Price: 50.0 " + Unit.USD.getSymbol()),
        "String representation should include the updated USD currency."
    );
  }
}
