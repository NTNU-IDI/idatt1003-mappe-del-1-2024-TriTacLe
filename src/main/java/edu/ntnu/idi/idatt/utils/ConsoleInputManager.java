package edu.ntnu.idi.idatt.utils;

import edu.ntnu.idi.idatt.model.Unit;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * A utility class for handling the user input from the console and validates it
 * This class collects and validates user input, prompting until valid input is received..
 *
 * <p>Supports input of:</p>
 * <ul>
 *   <li>Strings</li>
 *   <li>Numbers (Double and Integer)</li>
 *   <li>Units</li>
 *   <li>Dates</li>
 * </ul>
 *
 * @author TriLe
 */
public class ConsoleInputManager {
  private final Scanner scanner;
  
  /**
   * Initializes a new ConsoleInputManager instance with the provided Scanner.
   *
   * @param scanner the Scanner instance to read user input from.
   */
  public ConsoleInputManager(Scanner scanner) {
    this.scanner = scanner;
  }
  
  /**
   * Prompts the user for a string input and validates that it is not null or empty.
   *
   * @param request      the message to display to the user.
   * @param errorMessage the error message to display in case of invalid input.
   * @param fieldName    the name of the field being validated, used for error reporting.
   * @return a valid non-null, non-empty string entered by the user.
   * @throws IllegalArgumentException if the user input is null or empty.
   */
  public String getValidatedString(String request, String errorMessage, String fieldName) {
    while (true) {
      System.out.println(request);
      try {
        String input = scanner.nextLine().trim();
        InputValidator.validateString(input, fieldName);
        return input;
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  /**
   * Prompts the user for a double input and validates that it is non-negative.
   *
   * <p>This method ensures the input is non-negative and optionally allows zero values.
   * If zero is allowed, the method prompts the user for confirmation. The input process
   * continues in a loop until a valid value is provided.</p>
   *
   * @param request      the message to display to the user.
   * @param errorMessage the error message to display in case of invalid input.
   * @param fieldName    the name of the field being validated, used for error reporting.
   * @return a valid non-negative double entered by the user.
   * @throws IllegalArgumentException if the user input is negative or invalid.
   */
  public double getValidatedDouble(
      String request, String errorMessage, String fieldName, boolean allowZero) {
    while (true) {
      System.out.println(request);
      try {
        double number = Double.parseDouble(scanner.next());
        InputValidator.validateDouble(number, fieldName);
        if (number == 0) {
          if (!allowZero) {
            System.out.println("Zero is not allowed for "
                + fieldName + ". Please enter a non-zero value.");
            continue;
          }
          if (!confirmZero(fieldName)) {
            System.out.println("Please enter a non-zero value.");
            continue;
          }
        }
        
        return number;
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  /**
   * Helper method that confirms if user wants to use zero as a value.
   *
   * @param fieldName the name of the field being validated, used for error messages.
   * @return true if user confirms zero, false otherwise
   */
  private static boolean confirmZero(String fieldName) {
    System.out.println(fieldName + " is zero. "
        + "Are you sure you want to continue? Yes to continue, anything else to retry:");
    Scanner scanner = new Scanner(System.in);
    String userInput = scanner.nextLine().trim().toLowerCase();
    return "yes".equals(userInput);
  }
  
  /**
   * Prompts the user for an integer input and validates that it is non-negative.
   *
   * @param request      the message to display to the user.
   * @param errorMessage the error message to display in case of invalid input.
   * @param fieldName    the name of the field being validated, used for error reporting.
   * @return a valid non-negative integer entered by the user.
   * @throws IllegalArgumentException if the user input is negative or invalid.
   */
  public int getValidatedInt(String request, String errorMessage, String fieldName) {
    while (true) {
      System.out.println(request);
      try {
        int number = Integer.parseInt(scanner.next());
        InputValidator.validateInt(number, fieldName);
        return number;
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  /**
   * Prompts the user for a unit input and validates that it matches a predefined {@link Unit}.
   *
   * @param request      the message to display to the user.
   * @param errorMessage the error message to display in case of invalid input.
   * @return a valid {@link Unit} entered by the user.
   * @throws IllegalArgumentException if the user input does not match a valid unit.
   */
  public Unit getValidatedUnit(String request, String errorMessage) {
    while (true) {
      System.out.println(request);
      try {
        Unit unit = Unit.fromSymbol(scanner.next());
        InputValidator.validationEnum(unit);
        return unit;
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  /**
   * Prompts the user for a date input and validates that it is not in the past.
   *
   * @param request      the message to display to the user.
   * @param errorMessage the error message to display in case of invalid input.
   * @return a valid LocalDate entered by the user.
   * @throws DateTimeParseException   if the user input is not in the expected format.
   * @throws IllegalArgumentException if the date is in the past or otherwise invalid.
   */
  public LocalDate getValidatedDate(String request, String errorMessage) {
    while (true) {
      System.out.println(request);
      try {
        LocalDate localDate = LocalDate.parse(scanner.next());
        InputValidator.validateDateInThePast(localDate, false);
        return localDate;
      } catch (DateTimeParseException e) {
        System.out.println(errorMessage
            + ": Invalid date format. Please enter in yyyy-mm-dd format.");
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
}
