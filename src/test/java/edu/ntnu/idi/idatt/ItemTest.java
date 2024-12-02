package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Unit;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {
  
  @Test
  void shouldCreateIngredientWithValidAttributes() {
    Ingredient ingredient = new Ingredient("Apple", 2.5, Unit.PIECES, LocalDate.now().plusDays(10), 3.5);
    assertEquals("Apple", ingredient.getName());
    assertEquals(2.5, ingredient.getQuantity());
    assertEquals("Kg", ingredient.getUnit());
    assertEquals(LocalDate.now().plusDays(10), ingredient.getExpirationDate());
    assertEquals(3.5, ingredient.getPrice());
  }
  
  @Test
  void shouldThrowExceptionForEmptyName() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("", 2.5, Unit.GRAM, LocalDate.now().plusDays(10), 3.5);
    });
    assertEquals("Name cannot be empty", exception.getMessage());
  }
  
  @Test
  void shouldThrowExceptionForNegativeQuantity() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Apple", -1.0, Unit.KILOGRAM, LocalDate.now().plusDays(10), 3.5);
    });
    assertEquals("Quantity cannot be negative", exception.getMessage());
  }
  
  @Test
  void shouldThrowExceptionForEmptyUnit() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Apple", 2.5, Unit.KILOGRAM, LocalDate.now().plusDays(10), 3.5);
    });
    assertEquals("Unit cannot be empty", exception.getMessage());
  }
  
  @Test
  void shouldThrowExceptionForNegativePricePerUnit() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Apple", 2.5, Unit.KILOGRAM, LocalDate.now().plusDays(10), -3.5);
    });
    assertEquals("Price per unit cannot be negative", exception.getMessage());
  }
  
  @Test
  void shouldReturnCorrectQuantityAfterSetQuantity() {
    Ingredient ingredient = new Ingredient("Apple", 2.5, Unit.KILOGRAM, LocalDate.now().plusDays(10), 3.5);
    ingredient.setQuantity(5.0);
    assertEquals(5.0, ingredient.getQuantity());
  }
  
  @Test
  void getExpirationDateShouldReturnCorrectDate() {
    LocalDate expirationDate = LocalDate.now().plusDays(15);
    Ingredient ingredient = new Ingredient("Water", 1.0, Unit.LITRE, expirationDate, 1.5);
    assertEquals(expirationDate, ingredient.getExpirationDate());
  }
  
  @Test
  void getUnitShouldReturnCorrectUnit() {
    Ingredient ingredient = new Ingredient("Flour", 1.0, Unit.KILOGRAM, LocalDate.now().plusDays(20), 0.8);
    assertEquals("kg", ingredient.getUnit());
  }
  
  @Test
  void getPerUnitPriceShouldReturnCorrectPrice() {
    Ingredient ingredient = new Ingredient("Sugar", 3.0, Unit.KILOGRAM, LocalDate.now().plusDays(20), 2.2);
    assertEquals(2.2, ingredient.getPrice());
  }
  
  @Test
  void testToStringShouldReturnFormattedString() {
    LocalDate expirationDate = LocalDate.of(2023, 12, 25);
    Ingredient ingredient = new Ingredient("Butter", 0.5, Unit.KILOGRAM, expirationDate, 4.0);
    String expectedString = "Butter 0.5 Kg 2023-12-25 4.0";
    assertEquals(expectedString, ingredient.toString());
  }
}
