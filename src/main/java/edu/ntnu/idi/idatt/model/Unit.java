package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.Utils.InputValidation;

import java.util.Arrays;

public enum Unit {
  GRAM("g", UnitType.MASS, 0.001),
  KILOGRAM("kg", UnitType.MASS, 1.0),
  LITRE("L", UnitType.VOLUME, 1.0),
  DESILITRE("dL", UnitType.VOLUME, 0.1),
  MILLILITRE("mL", UnitType.VOLUME, 0.001),
  PIECES("pcs", UnitType.NUMBER, 1.0);
  
  private final String symbol;
  private final UnitType type;
  private final Double conversionNumber;
  private InputValidation inputValidation;
  
  public enum UnitType {
    MASS,
    VOLUME,
    NUMBER
  }
  
  /**
   * Construtor.
   *
   * @param symbol
   * @param type
   * @param conversionNumber
   */
  Unit(String symbol, UnitType type, Double conversionNumber) {
    this.symbol = symbol;
    this.type = type;
    this.conversionNumber = conversionNumber;
  }
  
  public String getSymbol() {
    return symbol;
  }
  
  public Double getConversionNumber() {
    return conversionNumber;
  }
  
  /**
   * Converts a given value from the current unit to a target unit.
   *
   * <p>This method validates the input to ensure that the target unit is a valid
   * enumeration and belongs to the same unit type as the source unit. It then
   * calculates the converted value using the conversion factors of both units.
   * </p>
   *
   * @param value      the value to be converted. This represents the quantity in the
   *                   current unit.
   * @param targetUnit the unit to which the value should be converted. This must
   *                   belong to the same unit type as the current unit.
   * @return the converted value in the target unit.
   * @throws IllegalArgumentException if the target unit is invalid or does not
   *                                  belong to the same unit type as the source unit.
   */
  public Double convertValue(Double value, Unit targetUnit) {
    inputValidation.validationEnum(targetUnit);
    inputValidation.validationEnumUnitType(this.type, targetUnit.type);
    return (value * this.conversionNumber) / targetUnit.conversionNumber;
  }
  
  /**
   * Finds the Unit enum based on a user input string.
   *
   * @param input The string input by the user.
   * @return The corresponding Unit enum.
   * @throws IllegalArgumentException if the input does not match any Unit.
   */
  public static Unit fromSymbol(String input) {
    return Arrays.stream(Unit.values())
        .filter(unit -> unit.symbol.equalsIgnoreCase(input)) //ALlow case in-sensitive
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid unit: " + input));
  }
  
}
