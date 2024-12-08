package edu.ntnu.idi.idatt.storage;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The FoodStorage class manages a collection of Ingredient instances, allowing these functions:
 * Adding, removing and searching ingredients.
 * operations like checking availability for recipes and sorting ingredients.
 *
 * @author TriLe
 */
public class FoodStorage {
  private final HashMap<String, ArrayList<Ingredient>> ingredients;
  
  /**
   * Constructor.
   */
  public FoodStorage() {
    this.ingredients = new HashMap<>();
  }
  
  /**
   * Retrieves the map of ingredients in the storage.
   *
   * @return A HashMap where the key is the ingredient name
   *      and the value is a list of Ingredient instances.
   */
  public HashMap<String, ArrayList<Ingredient>> getIngredients() {
    return ingredients;
  }
  
  /**
   * Adds an ingredient to the storage. If an ingredient with the same name, expiration date,
   * and price exists, its quantity will be updated. Otherwise, the ingredient is added.
   *
   * @param ingredient The Ingredient to be added to the storage.
   * @throws IllegalArgumentException if the ingredient is null.
   */
  public void addIngredientToFoodStorage(Ingredient ingredient) {
    ingredients.putIfAbsent(ingredient.getName().toLowerCase(), new ArrayList<>());
    ArrayList<Ingredient> ingredientArrayList = ingredients.get(ingredient.getName().toLowerCase());
    Optional<Ingredient> matchingIngredient = ingredientArrayList.stream()
        .filter(existingIngredient ->
            existingIngredient.getExpirationDate().equals(ingredient.getExpirationDate())
                && existingIngredient.getPrice() == ingredient.getPrice())
        .findFirst();
    
    if (matchingIngredient.isPresent()) {
      matchingIngredient.get().updateQuantity(ingredient.getQuantity());
    } else {
      ingredientArrayList.add(ingredient);
    }
  }
  
  /**
   * Checks if an ingredient exists in the storage by name.
   *
   * @param name The name of the ingredient.
   * @return true if the ingredient exists, false otherwise.
   */
  public boolean ingredientExist(String name) {
    return ingredients.containsKey(name);
  }
  
  /**
   * Searches for ingredients in the storage by name.
   *
   * @param nameIngredient The name of the ingredient to search for.
   * @return A list of Ingredient instances matching the given name.
   */
  public List<Ingredient> searchForIngredientsInFoodStorage(String nameIngredient) {
    return ingredients.entrySet().stream()
        .filter(entry -> entry.getKey().equalsIgnoreCase(nameIngredient))
        .flatMap(entry -> entry.getValue().stream())
        .collect(Collectors.toList());
  }
  
  /**
   * Removes a specified quantity of an ingredient from the storage.
   *
   * <p>If the Ingredient exists in the map and if the Ingredient's quantity is greater
   * than the amount to remove, then reduce the Ingredient's quantity
   * If the quantity is less than or equal to the amount to remove,
   * remove the Ingredient from the map.</p>
   *
   * @param name     The name of the ingredient to remove.
   * @param quantity The quantity to remove.
   * @return The actual quantity removed.
   * @throws IllegalArgumentException if the ingredient does not exist
   *                                  or if the quantity exceeds availability.
   */
  public double removeIngredientFromFoodStorage(String name, double quantity) {
    name = name.toLowerCase();
    ArrayList<Ingredient> ingredientArrayList = ingredients.get(name);
    
    if (ingredientArrayList == null || ingredientArrayList.isEmpty()) {
      throw new IllegalArgumentException("Ingredient " + name + " does not exist in storage.");
    }
    
    List<Ingredient> sortedList = getSorteIngredientsByExpirationsDate(ingredientArrayList);
    
    double totalQuantity = sortedList.stream()
        .mapToDouble(Ingredient::getQuantity)
        .sum();
    
    if (quantity > totalQuantity) {
      throw new IllegalArgumentException("Invalid quantity to remove: "
          + quantity + ". Available: " + totalQuantity);
    }
    final double initialQuantity = quantity;
    Iterator<Ingredient> iterator = sortedList.iterator();
    
    while (iterator.hasNext() && quantity > 0) {
      Ingredient ingredient = iterator.next();
      if (ingredient.getQuantity() > quantity) {
        ingredient.setQuantity(ingredient.getQuantity() - quantity);
        quantity = 0;
      } else {
        quantity -= ingredient.getQuantity();
        iterator.remove();
      }
    }
    ingredientArrayList.clear();
    ingredientArrayList.addAll(sortedList);
    
    if (ingredientArrayList.isEmpty()) {
      ingredients.remove(name);
    }
    return initialQuantity - quantity;
  }
  
  /**
   * Returns a list of ingredients sorted by expiration date.
   *
   * @param ingredientArrayList The list of ingredients to sort.
   * @return A sorted list of ingredients.
   */
  private List<Ingredient> getSorteIngredientsByExpirationsDate(
      List<Ingredient> ingredientArrayList) {
    return ingredientArrayList.stream()
        .filter(ingredient ->
            ingredient.getExpirationDate() != null) //Avoid nullpointer when using comparator
        .sorted(Comparator.comparing(Ingredient::getExpirationDate))
        .collect(Collectors.toList());
  }
  
  /**
   * Retrieves all expired ingredients from the storage.
   *
   * @return A list of expired ingredients.
   */
  public List<Ingredient> getExpiredIngredients() {
    return ingredients.values().stream()
        .filter(Objects::nonNull)
        .flatMap(List::stream) //Flatten nested ArrayList into stream
        .filter(ingredient -> ingredient != null && ingredient.getExpirationDate() != null)
        .filter(ingredient -> ingredient.getExpirationDate().isBefore(LocalDate.now()))
        .collect(Collectors.toList());
  }
  
  /**
   * Calculates the total value of a stream of ingredients.
   *
   * @param ingredientsStream A stream of ingredients to calculate.
   * @return The total value of the ingredients.
   * @throws IllegalArgumentException if the ingredientsStream is null.
   */
  public double calculateTotalValue(Stream<Ingredient> ingredientsStream) {
    if (ingredientsStream == null) {
      throw new IllegalArgumentException("Ingredients stream cannot be null");
    }
    return ingredientsStream
        .mapToDouble(Ingredient::getPrice)
        .sum();
  }
  
  /**
   * Checks if the storage is empty.
   *
   * @return true if the storage is empty, false otherwise.
   */
  public boolean isEmpty() {
    return ingredients.isEmpty();
  }
  
  /**
   * Retrieves ingredients expiring before a specific date.
   *
   * @param date The date to check against.
   * @return A list of ingredients expiring before the given date.
   */
  public List<Ingredient> getIngredientsExpiringBefore(LocalDate date) {
    return ingredients.values().stream()
        .flatMap(List::stream)
        .filter(ingredient -> ingredient.getExpirationDate().isBefore(date))
        .sorted(Comparator.comparing(Ingredient::getExpirationDate))
        .collect(Collectors.toList());
  }
  
  /**
   * Retrieves ingredients expiring after a specific date.
   *
   * @param date The date to check against.
   * @return A list of ingredients expiring before the given date.
   */
  public List<Ingredient> getIngredientsExpiringAfter(LocalDate date) {
    return ingredients.values().stream()
        .flatMap(List::stream)
        .filter(ingredient -> ingredient.getExpirationDate().isAfter(date))
        .sorted(Comparator.comparing(Ingredient::getExpirationDate))
        .collect(Collectors.toList());
  }
  
  /**
   * Displays the storage ingredients sorted alphabetically by their name.
   * Outputs ingredient details including their quantity.
   */
  public void getFoodStorageAlphabetically() {
    ingredients.keySet().stream()
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .forEach(key -> {
          double quantity = ingredients.get(key).stream()
              .mapToDouble(Ingredient::getQuantity)
              .sum();
          if (ingredients.get(key).size() > 1) {
            System.out.println(ingredients.get(key).getFirst().getName()
                + " (Quantity: " + quantity + "):");
            ingredients.get(key).forEach(ingredient -> System.out.println("- " + ingredient));
          } else {
            System.out.println(ingredients.get(key).getFirst());
          }
        });
  }
  
  /**
   * Checks if there are sufficient
   * ingredients in the storage to make the given recipe.
   *
   * @param foodStorage The FoodStorage instance to check against.
   * @param recipe      The Recipe to evaluate.
   * @return true if all required ingredients are
   *        available in sufficient quantities; otherwise false.
   */
  public boolean hasEnoughIngredientsForRecipe(
      FoodStorage foodStorage, Recipe recipe) {
    return recipe.getIngredientsList().stream()
        .allMatch(recipeIngredient -> {
          double totalAvailable
              = foodStorage.getIngredients().values().stream()
              .flatMap(List::stream)
              .filter(storageIngredient ->
                  storageIngredient.getName().equals(recipeIngredient.getName()))
              .mapToDouble(storageIngredient -> {
                if (!storageIngredient.getUnit().equals(recipeIngredient.getUnit())) {
                  return storageIngredient.getUnit()
                      .convertValue(storageIngredient.getQuantity(),
                          recipeIngredient.getUnit());
                }
                return storageIngredient.getQuantity();
              })
              .sum();
          return totalAvailable >= recipeIngredient.getQuantity();
        });
  }
  
  /**
   * Checks the availability of ingredients required for making the given recipe.
   *
   * @param foodStorage The FoodStorage instance to check against.
   * @param recipe      The Recipe to evaluate.
   * @return A map containing the ingredient names as
   *      keys and their total available quantities as values.
   */
  public Map<String, Double> getIngredientAvailabilityForRecipe(
      FoodStorage foodStorage, Recipe recipe) {
    Map<String, Double> availability = new HashMap<>();
    recipe.getIngredientsList().forEach(recipeIngredient -> {
      double totalAvailable = foodStorage.getIngredients().values().stream()
          .flatMap(List::stream)
          .filter(storageIngredient ->
              storageIngredient.getName().equals(recipeIngredient.getName()))
          .mapToDouble(Ingredient::getQuantity)
          .sum();
      availability.put(recipeIngredient.getName(), totalAvailable);
    });
    return availability;
  }
  
  /**
   * Retrieves a list of all ingredients in the storage, formatted as a string.
   *
   * @return A formatted string representation of all ingredients in the storage.
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Ingredients in storage:\n");
    for (Map.Entry<String, ArrayList<Ingredient>> entry : ingredients.entrySet()) {
      entry.getValue().forEach(ingredient ->
          stringBuilder.append("  - ").append(ingredient).append("\n"));
    }
    return stringBuilder.toString();
  }
}

