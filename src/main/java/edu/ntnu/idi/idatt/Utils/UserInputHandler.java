package edu.ntnu.idi.idatt.Utils;

import edu.ntnu.idi.idatt.model.Unit;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserInputHandler {
  private final Scanner scanner;
  
  public UserInputHandler(Scanner scanner) {
    this.scanner = scanner;
  }
  
  public String getValidatedString(String request, String errorMessage, String fieldName) {
    while (true) {
      System.out.println(request);
      try {
        String input = scanner.next();
        InputValidation.validateString(input, fieldName);
        return input;
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  public double getValidatedDouble(String request, String errorMessage, String fieldName) {
    while (true) {
      System.out.println(request);
      try {
        double number = Double.parseDouble(scanner.next());
        InputValidation.validateDouble(number, fieldName);
        return number;
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  public int getValidatedInt(String request, String errorMessage, String fieldName) {
    while (true) {
      System.out.println(request);
      try {
        int number = Integer.parseInt(scanner.next());
        InputValidation.validateInt(number, fieldName);
        return number;
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  public Unit getValidatedUnit(String request, String errorMessage) {
    while (true) {
      System.out.println(request);
      try {
        Unit unit = Unit.fromSymbol(scanner.next());
        InputValidation.validationEnum(unit);
        return unit;
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
  
  public LocalDate getValidatedDate(String request, String errorMessage) {
    while (true) {
      System.out.println(request);
      try {
        LocalDate localDate = LocalDate.parse(scanner.next());
        InputValidation.validateDateInThePast(localDate, false);
        return localDate;
      } catch (DateTimeParseException e) {
        System.out.println(errorMessage + ": Invalid date format. Please enter in yyyy-mm-dd format.");
      } catch (IllegalArgumentException e) {
        System.out.println(errorMessage + ": " + e.getMessage());
      }
    }
  }
}
