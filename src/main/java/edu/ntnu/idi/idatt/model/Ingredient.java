package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.Utils.InputValidation;

import java.time.LocalDate;

/**
 * Class represent an ingredient/grocery
 * Consist of attributes name, quantity, unit for the quantity, expiration date and the price per unit
 */
public class Ingredient {
  // Attributter
  private final String name;
  private double quantity;
  private final Unit unit;
  private LocalDate expirationDate;
  private final double price;
  private InputValidation inputValidation;
  
  /**
   * konstruktør to make an Ingredient (object) with (attributes) name, quantity, unit, expirations date price per unit
   * I also implemented input control
   *
   * @param name           Ingredients name
   * @param quantity       quanity of Ingredient
   * @param unit           unit for the quantity (SI-unit) ex: Kg
   * @param expirationDate expiration date for the Ingredient
   * @param price          price per unit of Ingredient
   */
  public Ingredient(String name, double quantity, Unit unit, LocalDate expirationDate, double price) {
    //Input validering
    inputValidation.validateString(name, "Name");
    //inputValidation.validateDouble(quantity, "Quantity");
    inputValidation.validationEnum(unit);
    inputValidation.validateDouble(price, "Price");
    
    setExpirationDate(expirationDate);
    setQuantity(quantity);
    this.name = name;
    //this.quantity = quantity;
    this.unit = unit;
    //this.expirationDate = expirationDate;
    this.price = price;
  }
  
  public Ingredient(String name, double quantity, Unit unit, double price) {
    inputValidation.validateString(name, "Name");
    //inputValidation.validateDouble(quantity, "Quantity");
    inputValidation.validationEnum(unit);
    inputValidation.validateDouble(price, "Price");
    
    setQuantity(quantity);
    this.name = name;
    //this.quantity = quantity;
    this.unit = unit;
    this.price = price;
  }
  
  /***
   * Gets the name of the Ingredient
   * @return name of the Ingredient
   */
  public String getName() {
    return name;
  }
  
  /***
   * Gets the quantity of the Ingredient
   * @return quantity of the Ingredient
   */
  public double getQuantity() {
    return quantity;
  }
  
  /**
   * Gets the expiration date of the Ingredient
   *
   * @return new expiration date
   */
  public LocalDate getExpirationDate() {
    return expirationDate;
  }
  
  /***
   * gets the unit of quantity
   * @return unit of quantity
   */
  public Unit getUnit() {
    return unit;
  }
  
  /***
   * gets the price of the Ingredient
   * @return
   */
  public double getPrice() {
    return price;
  }
  
  /***
   * Sets the quantity of the Ingredient
   * @param quantity new quantity of the Ingredient
   */
  public void setQuantity(double quantity) {
    InputValidation.validateDouble(quantity, "quantity");
    this.quantity = quantity;
  }
  
  
  /**
   * Sets the expirations date of the Ingredient
   *
   * @param expirationDate
   */
  private void setExpirationDate(LocalDate expirationDate) {
    InputValidation.validateDate(expirationDate, false);
    this.expirationDate = expirationDate;
  }
  
  public void updateQuantity(double quantity) {
    inputValidation.validateDouble(quantity, "Quantity");
    this.quantity += quantity;
  }
  
  /**
   * Skriver ut detaljene fra en vare (fra hint)
   *
   * @return detaljene
   */
  @Override
  public String toString() {
    // TODO Auto-generated method stub
    //if else forenklet
    String expirationDateOutput = (expirationDate != null) ? " Expires: " + expirationDate : "";
    return name + " (" + quantity + " " + unit.getSymbol() + ")" + expirationDateOutput + " Price: " + price + " kr"; // + ". Todays date: " + LocalDate.now();
  }
}