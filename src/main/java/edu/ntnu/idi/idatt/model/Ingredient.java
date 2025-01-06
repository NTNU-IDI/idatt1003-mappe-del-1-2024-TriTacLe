package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.utils.InputValidator;
import java.time.LocalDate;

/**
 * Class that represents an ingredient or a grocery item that consist of information such as name,
 * quantity, unit for the quantity, expiration date and the price.
 *
 * <p>Provides a constructor to create a train departure and a functionality for validation.
 * Also include mutator methods for expiration date and quantity</p>
 *
 * @author TriLe
 * @version 1.2.1
 */
public class Ingredient {
  private static Unit unitCurrency = Unit.KR;
  private final String name;
  private final Unit unitMeasurement;
  private final double price;
  private double quantity;
  private final LocalDate expirationDate;
  
  /**
   * Initialize an Ingredient instance with the specified attributes and performs validation.
   *
   * @param name            the name of the ingredient
   * @param quantity        the quantity of the ingredient
   * @param unitMeasurement the unit of measurements (SI-unit) for the ingredient.
   * @param expirationDate  expiration date for the Ingredient
   * @param price           the price of the ingredient(s)
   * @throws IllegalArgumentException if any input validation fails
   */
  public Ingredient(String name, double quantity, Unit unitMeasurement,
                    LocalDate expirationDate, double price) {
    InputValidator.validateString(name, "Name");
    InputValidator.validationEnum(unitMeasurement);
    InputValidator.validateDouble(price, "Price");
    InputValidator.validateDate(expirationDate, false);
    setQuantity(quantity);
    this.expirationDate = expirationDate;
    this.name = name;
    this.unitMeasurement = unitMeasurement;
    this.price = price;
  }
  
  /**
   * Initialize an instance of Ingredient without an expiration date.
   *
   * @param name            the name of the ingredient
   * @param quantity        the quantity of the ingredient
   * @param unitMeasurement the unit of measurement for the ingredient
   * @param price           the price per unit of the ingredient
   * @throws IllegalArgumentException if any input validation fails
   */
  public Ingredient(String name, double quantity, Unit unitMeasurement, double price) {
    InputValidator.validateString(name, "Name");
    InputValidator.validationEnum(unitMeasurement);
    InputValidator.validateDouble(price, "Price");
    setQuantity(quantity);
    this.name = name;
    this.unitMeasurement = unitMeasurement;
    this.price = price;
    this.expirationDate = null;
  }
  
  /**
   * Retrieves the name of the Ingredient.
   *
   * @return the name of the Ingredient
   */
  public String getName() {
    return name;
  }
  
  /**
   * Retrieves the quantity of the Ingredient.
   *
   * @return the quantity of the Ingredient
   */
  public double getQuantity() {
    return quantity;
  }
  
  /**
   * Sets the quantity of the ingredient.
   *
   * @param quantity the new quantity of the ingredient
   * @throws IllegalArgumentException if the quantity is invalid
   */
  public void setQuantity(double quantity) {
    InputValidator.validateDouble(quantity, "Quantity");
    this.quantity = quantity;
  }
  
  /**
   * Retrieves the expiration date of the ingredient.
   *
   * @return the expiration date of the ingredient or{@code null} if not set
   */
  public LocalDate getExpirationDate() {
    return expirationDate;
  }
  
  /**
   * Retrieves the unit of measurement of the ingredient.
   *
   * @return the unit of measurement of the ingredient
   */
  public Unit getUnitMeasurement() {
    return unitMeasurement;
  }
  
  /**
   * Retrieves the price of the ingredient.
   *
   * @return the price of the ingredient.
   */
  public double getPrice() {
    return price;
  }
  
  /**
   * Updates the quantity of the ingredient.
   *
   * @param quantity the total quantity to add to the current quantity
   * @throws IllegalArgumentException if the quantity is invalid
   */
  public void updateQuantity(double quantity) {
    InputValidator.validateDouble(quantity, "Quantity");
    this.quantity += quantity;
  }
  
  /**
   * Retrieves the current unit currency for price display.
   *
   * @return the current unit currency
   */
  public static Unit getUnitCurrency() {
    return unitCurrency;
  }
  
  /**
   * Sets the current currency for price conversion.
   *
   * @param newCurrency the selected currency
   */
  public static void setUnitCurrency(Unit newCurrency) {
    InputValidator.validationEnum(newCurrency);
    if (newCurrency.getType() == Unit.UnitType.CURRENCY) {
      unitCurrency = newCurrency;
    } else {
      throw new IllegalArgumentException("Invalid currency unit.");
    }
  }
  
  /**
   * Returns a string representation of the ingredient, including information such as
   * name, quantity, unitMeasurement, expiration date (if available), and price.
   *
   * @return a string representation of the ingredient
   */
  @Override
  public String toString() {
    // TODO Auto-generated method stub
    String expirationDateOutput = (expirationDate != null) ? " Expires: " + expirationDate : "";
    
    return name + " (" + quantity + " " + unitMeasurement.getSymbol() + ")" + expirationDateOutput
        + " Price: " + price + " " + unitCurrency.getSymbol();
  }
}