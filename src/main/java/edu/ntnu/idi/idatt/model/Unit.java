package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.utils.InputValidator;

import java.util.Arrays;

/**
 * Represents different units of measurement used in the system.
 *
 * <p>Each unit is associated with a symbol, a type (e.g., mass, volume, or number) for readability,
 * and a conversion factor for unit conversion. </p>
 */
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
  private final InputValidator inputValidator = new InputValidator();
  
  /**
   * Constructor to initialize a unit with its attributes.
   *
   * @param symbol           the symbol representing the unit (e.g., "kg", "g").
   * @param type             the type of the unit (e.g., mass, volume).
   * @param conversionNumber the conversion factor for the unit relative to its base unit.
   */
  Unit(String symbol, UnitType type, Double conversionNumber) {
    this.symbol = symbol;
    this.type = type;
    this.conversionNumber = conversionNumber;
  }
  
  /**
   * Finds the corresponding unit based on a symbol.
   *
   * @param input the symbol representing the unit (case-insensitive).
   * @return the matching {@code Unit} enumeration.
   * @throws IllegalArgumentException if no matching unit is found.
   */
  public static Unit fromSymbol(String input) {
    return Arrays.stream(Unit.values())
        .filter(unit -> unit.symbol.equalsIgnoreCase(input))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid unit: " + input));
  }
  
  /**
   * Gets the symbol of the unit.
   *
   * @return the symbol representing the unit.
   */
  public String getSymbol() {
    return symbol;
  }
  
  /**
   * Gets the conversion factor of the unit relative to its base type.
   *
   * <p>This method is currently unused but may be useful for future extensions
   * or for accessing the conversion factor.</p>
   *
   * @return the conversion factor of the unit.
   */
  public Double getConversionNumber() {
    return conversionNumber;
  }
  
  /**
   * Converts a given value from the current unit to a target unit.
   *
   * <p>This method validates the target unit to ensure compatibility with the current unit's type.
   * </p>
   *
   * @param value      the value to be converted (in the current unit).
   * @param targetUnit the target unit for conversion.
   * @return the converted value in the target unit.
   * @throws IllegalArgumentException if the target unit is null, invalid, or incompatible.
   */
  public Double convertValue(Double value, Unit targetUnit) {
    inputValidator.validationEnum(targetUnit);
    inputValidator.validationEnumUnitType(this.type, targetUnit.type);
    return (value * this.conversionNumber) / targetUnit.conversionNumber;
  }
  
  /**
   * Defines types of units used for categorizing measurement units.
   */
  public enum UnitType {
    MASS,
    VOLUME,
    NUMBER
  }
  
}
