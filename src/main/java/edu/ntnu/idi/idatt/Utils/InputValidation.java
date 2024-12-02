package edu.ntnu.idi.idatt.Utils;

import edu.ntnu.idi.idatt.model.Unit;

import java.time.LocalDate;
import java.util.Scanner;

public class InputValidation {
  
  /**
   * Validates that a string input is not null or empty.
   *
   * @param input     The string to validate.
   * @param fieldName the name of the field being validated.
   * @throws IllegalArgumentException if the field is null or empty.
   */
  
  public static void validateString(Object input, String fieldName) {
    String strInput = (String) input;
    if (strInput.isBlank()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty or blank");
    }
  }
  
  /**
   * Validates that a number is non-negative.
   *
   * @param number    The number to validate.
   * @param fieldName The name of the field being validated.
   * @throws IllegalArgumentException if the number is negative or null.
   */
  public static void validateDouble(Double number, String fieldName) {
    if (number == null || number < 0) {
      throw new IllegalArgumentException(fieldName + " cannot be negative/or another type than double");
    }
  }
  
  public static void validateInt(Integer number, String fieldName) {
    if (number == null || number < 0) {
      throw new IllegalArgumentException(fieldName + " cannot be negative or another type than int");
    }
  }
  
  /**
   * Validates that a date is not in the past
   * Optional null check, because ingredients in the recipe does not need expiration date
   *
   * @param date      The date to validate.
   * @param allowNull Whether null is allowed.
   * @throws IllegalArgumentException if the date is in the past.
   */
  public static void validateDateInThePast(LocalDate date, boolean allowNull) {
    if (date == null && !allowNull) {
      throw new IllegalArgumentException("Expiration date cannot be null");
    } else if (date.isBefore(LocalDate.now())) {
      System.out.println("This date is in the past. Are you sure you want to continue? Yes to continue, anything else to not continue");
      Scanner scanner = new Scanner(System.in);
      String userInput = scanner.nextLine().toUpperCase();
      if (!"YES".equalsIgnoreCase(userInput)) {
        throw new IllegalArgumentException("Expiration date cannot be in the past");
      }
    }
  }
  
  public static void validateDate(LocalDate date, boolean allowNull) {
    if (date == null && !allowNull) {
      throw new IllegalArgumentException("Expiration date cannot be null");
    }
  }
  
  /**
   * Validates the input of the unit String against Unit enum.
   *
   * @param unit The unit string to validate
   * @throws IllegalArgumentException if the unit is null, empty, or not a valid Unit enum value.
   */
  public static void validationEnum(Unit unit) {
    if (unit == null) {
      throw new IllegalArgumentException("Unit cannot be empty/null " + unit);
    }
  }
  
  public void validationEnumUnitType(Unit.UnitType unit, Unit.UnitType targetUnit) {
    if (unit != targetUnit) {
      throw new IllegalArgumentException("Incompatible units to convert " + this + " to " + targetUnit);
    }
  }
  
}
