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
  private final String name;
  private final Unit unit;
  private final double price;
  private final InputValidator inputValidator = new InputValidator();
  private double quantity;
  private LocalDate expirationDate;
  
  /**
   * Initialize an Ingredient instance with the specified attributes and performs validation.
   *
   * @param name           the name of the ingredient
   * @param quantity       the quantity of the ingredient
   * @param unit           the unit of measurements (SI-unit) for the ingredient.
   * @param expirationDate expiration date for the Ingredient
   * @param price          the price of the ingredient(s)
   * @throws IllegalArgumentException if any input validation fails
   */
  public Ingredient(String name, double quantity, Unit unit,
                    LocalDate expirationDate, double price) {
    inputValidator.validateString(name, "Name");
    inputValidator.validationEnum(unit);
    inputValidator.validateDouble(price, "Price");
    setExpirationDate(expirationDate);
    setQuantity(quantity);
    this.name = name;
    this.unit = unit;
    this.price = price;
  }
  
  /**
   * Initialize an instance of Ingredient without an expiration date.
   *
   * @param name     the name of the ingredient
   * @param quantity the quantity of the ingredient
   * @param unit     the unit of measurement for the ingredient
   * @param price    the price per unit of the ingredient
   * @throws IllegalArgumentException if any input validation fails
   */
  public Ingredient(String name, double quantity, Unit unit, double price) {
    inputValidator.validateString(name, "Name");
    inputValidator.validationEnum(unit);
    inputValidator.validateDouble(price, "Price");
    setQuantity(quantity);
    this.name = name;
    this.unit = unit;
    this.price = price;
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
   * Sets the expiration date of the ingredient.
   *
   * @param expirationDate the new expiration date
   * @throws IllegalArgumentException if the expiration date is invalid
   */
  private void setExpirationDate(LocalDate expirationDate) {
    InputValidator.validateDate(expirationDate, false);
    this.expirationDate = expirationDate;
  }
  
  /**
   * Retrieves the unit of measurement of the ingredient.
   *
   * @return the unit of measurement of the ingredient
   */
  public Unit getUnit() {
    return unit;
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
    inputValidator.validateDouble(quantity, "Quantity");
    this.quantity += quantity;
  }
  
  /**
   * Returns a string representation of the ingredient, including information such as
   * name, quantity, unit, expiration date (if available), and price.
   *
   * @return a string representation of the ingredient
   */
  @Override
  public String toString() {
    // TODO Auto-generated method stub
    String expirationDateOutput = (expirationDate != null) ? " Expires: " + expirationDate : "";
    
    return name + " (" + quantity + " " + unit.getSymbol() + ")" + expirationDateOutput
        + " Price: " + price + " kr";
  }
}