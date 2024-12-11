package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.model.Unit;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Utility class for validating various types of input values.
 * Provides methods for ensuring input values meet requirements like being
 * non-null, non-negative, or valid dates.
 *
 * <p>This class supports validation of:
 * <ul>
 *   <li>Strings</li>
 *   <li>Doubles and Integers</li>
 *   <li>Dates</li>
 *   <li>Enums</li>
 * </ul>
 * </p>
 *
 * @author TriLe
 */
public class InputValidator {
  
  /**
   * Validates that a string input is not null or empty.
   *
   * @param input     The string to validate.
   * @param fieldName The name of the field being validated, used for error messages.
   * @throws IllegalArgumentException if the input is null, empty, or blank.
   */
  public static void validateString(Object input, String fieldName) {
    String strInput = (String) input;
    if (strInput.isBlank()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty or blank");
    }
  }
  
  /**
   * Validates that an integer is non-negative.
   *
   * @param number    The integer to validate.
   * @param fieldName The name of the field being validated, used for error messages.
   * @throws IllegalArgumentException if the integer is null or less than zero,
   *                                  or if the value is zero and the user does not confirm.
   */
  public static void validateInt(Integer number, String fieldName) {
    if (number == null || number < 0) {
      throw new IllegalArgumentException(fieldName
          + " cannot be negative/or another type than double");
    }
  }
  
  /**
   * Validates that a number is non-negative.
   *
   * @param number    The number to validate.
   * @param fieldName The name of the field being validated, used for error messages.
   * @throws IllegalArgumentException if the number is null or less than zero.
   *                                  Or if field quantity is 0 (quantity cannot be 0)
   *                                  Though price can (free food exists).
   *                                  Or if field portions is 0 (Recipe cannot be made for 0 people)
   */
  public static void validateDouble(Double number, String fieldName) {
    if (fieldName == "Quantity" || fieldName == "Portions") {
      if (number == null || number <= 0 || Double.isNaN(number)) {
        throw new IllegalArgumentException(fieldName + " cannot be negative or NaN");
      }
    } else {
      if (number == null || number < 0 || Double.isNaN(number)) {
        throw new IllegalArgumentException(fieldName + " cannot be negative or NaN");
      }
    }
  }
  
  
  /**
   * Validates that a date is not in the past.
   * Optionally checks if null is allowed.
   *
   * @param date      The date to validate.
   * @param allowNull Whether null values are allowed.
   * @throws IllegalArgumentException if the date is null (when allowNull is false)
   *                                  or if the date is in the past and the user does not confirm.
   */
  public static void validateDateInThePast(LocalDate date, boolean allowNull) {
    if (date == null && !allowNull) {
      throw new IllegalArgumentException("Expiration date cannot be null");
    } else if (date.isBefore(LocalDate.now())) {
      System.out.println(
          "This date is in the past. Are you sure you want to continue? "
              + "Yes to continue, anything else to not continue");
      Scanner scanner = new Scanner(System.in);
      String userInput = scanner.nextLine().toUpperCase();
      if (!"YES".equalsIgnoreCase(userInput)) {
        throw new IllegalArgumentException("Expiration date cannot be in the past");
      }
    }
  }
  
  /**
   * Validates a date, checking for null values if specified.
   *
   * @param date      The date to validate.
   * @param allowNull Whether null values are allowed.
   * @throws IllegalArgumentException if the date is null and allowNull is false.
   */
  public static void validateDate(LocalDate date, boolean allowNull) {
    if (date == null && !allowNull) {
      throw new IllegalArgumentException("Expiration date cannot be null");
    }
  }
  
  /**
   * Validates that a unit is not null and is a valid enum value.
   *
   * @param unit The unit to validate.
   * @throws IllegalArgumentException if the unit is null or invalid.
   */
  public static void validationEnum(Unit unit) {
    if (unit == null) {
      throw new IllegalArgumentException("Unit cannot be empty or null: " + unit);
    }
  }
  
  /**
   * Validates that two units are compatible for conversion.
   *
   * @param unit       The current unit.
   * @param targetUnit The target unit for conversion.
   * @throws IllegalArgumentException if the units are not compatible for conversion.
   */
  public void validationEnumUnitType(Unit.UnitType unit, Unit.UnitType targetUnit) {
    if (unit != targetUnit) {
      throw new IllegalArgumentException(
          "Incompatible units to convert " + this + " to " + targetUnit);
    }
  }
  
}
