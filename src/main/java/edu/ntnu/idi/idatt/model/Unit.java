package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.utils.InputValidator;
import java.util.Arrays;

/**
 * Represents different units of measurement used in the system.
 *
 * <p>Each unit is associated with a symbol, a type (e.g., mass, volume, or number) for readability,
 * and a conversion factor for unit conversion. </p>
 *
 * @author TriLe
 *
 */
public enum Unit {
  GRAM("g", UnitType.MASS, 0.001),
  KILOGRAM("kg", UnitType.MASS, 1.0),
  LITRE("L", UnitType.VOLUME, 1.0),
  DESILITRE("dL", UnitType.VOLUME, 0.1),
  MILLILITRE("mL", UnitType.VOLUME, 0.001),
  PIECES("pcs", UnitType.NUMBER, 1.0),
  
  /**
   * Currencies for countries known for high food waste.
   */
  KR("NOK", UnitType.CURRENCY, 1.0, "Norway"),
  USD("$", UnitType.CURRENCY, 0.090, "United States"),
  CNY("¥", UnitType.CURRENCY, 0.66, "China"),
  INR("₹", UnitType.CURRENCY, 7.5, "India"),
  JPY("¥", UnitType.CURRENCY, 12.0, "Japan"),
  GBP("£", UnitType.CURRENCY, 0.075, "United Kingdom"),
  EUR("€", UnitType.CURRENCY, 0.085, "European Union"),
  AUD("A$", UnitType.CURRENCY, 0.14, "Australia"),
  ZAR("R", UnitType.CURRENCY, 1.7, "South Africa"),
  BRL("R$", UnitType.CURRENCY, 0.46, "Brazil"),
  MXN("MX$", UnitType.CURRENCY, 0.054, "Mexico"),
  IDR("Rp", UnitType.CURRENCY, 140.0, "Indonesia"),
  RUS("₽", UnitType.CURRENCY, 9.0, "Russia"),
  ITL("₤", UnitType.CURRENCY, 0.08, "Italy");
  
  private final UnitType type;
  private final Double conversionNumber;
  private final InputValidator inputValidator = new InputValidator();
  private final String country;
  private String symbol;
  
  /**
   * Constructor to initialize a currency unit.
   *
   * @param symbol           the symbol representing the unit.
   * @param type             the type of the unit.
   * @param conversionNumber the conversion factor for the unit relative to its base unit.
   * @param country          the country associated with the currency.
   */
  Unit(String symbol, UnitType type, double conversionNumber, String country) {
    setSymbol(symbol);
    this.type = type;
    this.conversionNumber = conversionNumber;
    this.country = country;
  }
  
  /**
   * Constructor to initialize a unit of measurements.
   *
   * @param symbol           the symbol representing the unit (e.g., "kg", "g").
   * @param type             the type of the unit (e.g., mass, volume).
   * @param conversionNumber the conversion factor for the unit relative to its base unit.
   */
  Unit(String symbol, UnitType type, Double conversionNumber) {
    setSymbol(symbol);
    this.type = type;
    this.conversionNumber = conversionNumber;
    this.country = "N/A";
  }
  
  /**
   * Finds the corresponding unit based on a symbol.
   *
   * @param input the symbol representing the unit (case-insensitive).
   * @return the matching Unit.
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
   * Sets the symbol for this unit. Validates the input to ensure it is a non-null,
   * non-empty string before setting the symbol.
   *
   * @param symbol the new symbol to assign to the unit. Must be a valid string.
   * @throws IllegalArgumentException if the symbol is null or empty.
   */
  public void setSymbol(String symbol) {
    InputValidator.validateString(symbol, "Unit symbol");
    this.symbol = symbol;
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
    InputValidator.validationEnum(targetUnit);
    inputValidator.validationEnumUnitType(this.type, targetUnit.type);
    return (value * this.conversionNumber) / targetUnit.conversionNumber;
  }
  
  /**
   * Retrieves the country of the corresponding currency.
   *
   * @return the country of the currency.
   */
  public String getCountry() {
    return country;
  }
  
  public UnitType getType() {
    return type;
  }
  
  /**
   * Defines types of units used for categorizing measurement units.
   */
  public enum UnitType {
    MASS,
    VOLUME,
    NUMBER,
    CURRENCY
  }
}
