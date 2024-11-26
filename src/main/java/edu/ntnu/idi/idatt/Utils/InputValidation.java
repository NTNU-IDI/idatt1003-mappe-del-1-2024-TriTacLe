package edu.ntnu.idi.idatt.Utils;

import edu.ntnu.idi.idatt.model.Unit;

import java.time.LocalDate;
import java.util.Scanner;

public class InputValidation {
  
  private static Scanner scanner = new Scanner(System.in);
  
  /**
   * Validates that a string input is not null or empty.
   *
   * @param input     The string to validate.
   * @param fieldName the name of the field being validated.
   * @throws IllegalArgumentException if the field is null or empty.
   */
  
  public static void validateString(String input, String fieldName) {
    if (input.isEmpty() || input == null) {
      throw new IllegalArgumentException(fieldName + " cannot be empty/null");
    }
  }
  
  /**
   * Validates that a number is non-negative.
   *
   * @param number    The number to validate.
   * @param fieldName The name of the field being validated.
   * @throws IllegalArgumentException if the number is negative.
   */
  public static void validateNonNegative(double number, String fieldName) {
    if (number < 0) {
      throw new IllegalArgumentException(fieldName + " cannot be negative");
    }
  }
  
  /**
   * Validates that an object is not null.
   *
   * @param obj       The object to validate.
   * @param fieldName The name of the field being validated.
   * @throws IllegalArgumentException if the object is null.
   */
  public static void validateNotNull(Object obj, String fieldName) {
    if (obj == null) {
      throw new IllegalArgumentException(fieldName + " cannot be null");
    }
  }
  
  /**
   * Validates that a date is not in the past
   * Optional null check, because items in the recipe does not need expiration date
   *
   * @param date      The date to validate.
   * @param allowNull Whether null is allowed.
   * @param fieldName The name of the field being validated.
   * @throws IllegalArgumentException if the date is in the past.
   */
  public static void validateDateNotInPast(LocalDate date, boolean allowNull, String fieldName) {
    if (date == null && !allowNull) {
      throw new IllegalArgumentException(fieldName + " cannot be null");
    } else if (date.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException(fieldName + " cannot be in the past");
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
