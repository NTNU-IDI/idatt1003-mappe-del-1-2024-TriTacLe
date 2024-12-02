package edu.ntnu.idi.idatt.storage;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.Iterator;
import java.util.Objects;
import java.util.Comparator;
import java.util.Map;

/**
 * The FoodStorage class manages a collection of Ingredients,
 * allowing operations such as....
 *
 * <p>p>
 * Name, getName, as key, and Ingredient object as the value.
 *
 * @author Tri Tac Le
 * @since
 */
public class FoodStorage {
  //private HashMap<String, Ingredient> Ingredients;
  private final HashMap<String, ArrayList<Ingredient>> ingredients;
  
  /**
   * Constructor.
   */
  public FoodStorage() {
    this.ingredients = new HashMap<>();
  }
  
  /**
   * getter
   *
   * @return Ingredients hastmap
   */
  public HashMap<String, ArrayList<Ingredient>> getIngredients() {
    return ingredients;
  }
  
  /**
   * A method that adds a Ingredient to the storage.
   * If the Ingredient already exist (check by name) the quantity increases with the quantity of the added Ingredient.
   * If not it is getting added to the map
   *
   * @param ingredient
   */
  public void addIngredientToFoodStorage(Ingredient ingredient) {
    // Update or make a new arrayList for Ingredient arguments name
    ingredients.putIfAbsent(ingredient.getName().toLowerCase(), new ArrayList<>());
    ArrayList<Ingredient> ingredientArrayList = ingredients.get(ingredient.getName().toLowerCase());
  
    /*check if Ingredient and its attributes already exist
    Checks the attributes pricePerUnit, expirationDate and unit
     */
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
  
  public boolean ingredientExist(String name) {
    return ingredients.containsKey(name);
  }
  
  /**
   * @param nameIngredient that represent the name attribute of the Ingredient. Can be called with getName
   *                       It also represents the key for the map
   */
  public List<Ingredient> searchForIngredientsInFoodStorage(String nameIngredient) {
    if (nameIngredient == null) {
      throw new NullPointerException("Ingredient name cannot be null");
    }
    return ingredients.entrySet().stream()
        //.filter(entry -> entry.getKey().toLowerCase().equals(nameIngredient.toLowerCase()))
        .filter(entry -> entry.getKey().equalsIgnoreCase(nameIngredient))
        .flatMap(entry -> entry.getValue().stream())
        .collect(Collectors.toList());
//        .findAny()
//        .orElse(null);
  }
  
  
  /**
   * If the Ingredient exists in the map and if the Ingredient's quantity is greater than the amount to remove
   * Reduce the Ingredient's quantity
   * If the quantity is less than or equal to the amount to remove, remove the Ingredient from the map.
   *
   * @param name     name of the Ingredient
   * @param quantity quantity of the Ingredient
   */
  public double removeIngredientFromFoodStorage(String name, double quantity) {
    name = name.toLowerCase();
    ArrayList<Ingredient> ingredientArrayList = ingredients.get(name);
    
    if (ingredientArrayList == null || ingredientArrayList.isEmpty()) {
      throw new IllegalArgumentException("Ingredient does not exist in storage.");
    }
    
    List<Ingredient> sortedList = getSorteIngredientsByExpirationsDate(ingredientArrayList);
    
    double totalQuantity = sortedList.stream()
        .mapToDouble(Ingredient::getQuantity)
        .sum();
    
    if (quantity > totalQuantity) {
      throw new IllegalArgumentException("Invalid quantity to remove: " + quantity + ". Available: " + totalQuantity);
    }
    
    double initialQuantity = quantity;
    Iterator<Ingredient> iterator = sortedList.iterator();
    
    while (iterator.hasNext() && quantity > 0) {
      Ingredient ingredient = iterator.next();
      if (ingredient.getQuantity() > quantity) {
        ingredient.setQuantity(ingredient.getQuantity() - quantity);
        quantity = 0;
      } else {
        quantity -= ingredient.getQuantity();
        iterator.remove();
        //IngredientArrayList.remove(Ingredient);
      }
    }
    ingredientArrayList.clear();
    ingredientArrayList.addAll(sortedList);
    
    if (ingredientArrayList.isEmpty()) {
      ingredients.remove(name);
    }
    return initialQuantity - quantity;
  }
  
  
  public List<Ingredient> getSorteIngredientsByExpirationsDate(List<Ingredient> ingredientArrayList) {
    return ingredientArrayList.stream()
        .filter(ingredient -> ingredient.getExpirationDate() != null) //Avoid nullpointer when using comparator
        .sorted(Comparator.comparing(Ingredient::getExpirationDate))
        .collect(Collectors.toList());
  }
  
  /**
   * prints out all expired Ingredients plus how much it costs
   *
   * @return
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
   * Calculates the total value of Ingredients provided in the stream.
   *
   * @param ingredientsStream Stream of Ingredients to calculate the total value for.
   * @return Total value of the Ingredients.
   */
  public double calculateTotalValue(Stream<Ingredient> ingredientsStream) {
    if (ingredientsStream == null) {
      throw new IllegalArgumentException("Ingredients stream cannot be null");
    }
    return ingredientsStream
        //.mapToDouble(Ingredient -> Ingredient.getQuantity() * Ingredient.getPerUnitPrice()) // Uncomment if quantity is relevant
        .mapToDouble(Ingredient::getPrice)
        .sum();
  }
  
  /**
   * Method that checks if Ingredients hashmap is empty.
   *
   * @return Ingredients.isEmpty a bolean (true/false) value after checking if Ingredients is empty
   */
  public boolean isEmpty() {
    return ingredients.isEmpty();
  }
  
  /**
   * Using streams to find every Ingredient with a expirationdate before input date-
   *
   * @param date Ingredients date
   */
  public List<Ingredient> getIngredientsExpiringBefore(LocalDate date) {
    return ingredients.values().stream()
        .flatMap(List::stream)
        .filter(ingredient -> ingredient.getExpirationDate().isBefore(date))
        .sorted(Comparator.comparing(Ingredient::getExpirationDate))
        .collect(Collectors.toList());
  }
  
  public List<Ingredient> getIngredientsExpiringAfter(LocalDate date) {
    return ingredients.values().stream()
        .flatMap(List::stream)
        .filter(ingredient -> ingredient.getExpirationDate().isAfter(date))
        .sorted(Comparator.comparing(Ingredient::getExpirationDate))
        .collect(Collectors.toList());
  }
  
  /**
   * Method that sorts out the Ingredients map alphabetically by name using streams sorted method
   */
  public void getFoodStorageAlphabetically() {
    ingredients.keySet().stream()
        .sorted(String.CASE_INSENSITIVE_ORDER) //sort key alphabetically case in-sensitive
        .forEach(key -> {
          double quantity = ingredients.get(key).stream()
              .mapToDouble(Ingredient::getQuantity)
              .sum();
          if (ingredients.get(key).size() > 1) {
            System.out.println(ingredients.get(key).getFirst().getName() + " (Quantity: " + quantity + "):");
            ingredients.get(key).forEach(ingredient -> System.out.println("- " + ingredient));
          } else {
            System.out.println(ingredients.get(key).getFirst());
          }
        });
  }
  
  public boolean hasEnoughIngredientsForRecipe(FoodStorage foodStorage, Recipe recipe) {
    return recipe.getIngredientsList().stream()
        .allMatch(recipeIngredient -> {
          double totalAvailable = foodStorage.getIngredients().values().stream()
              .flatMap(List::stream)
              .filter(storageIngredient -> storageIngredient.getName().equals(recipeIngredient.getName()))
              .mapToDouble(storageIngredient -> {
                if (!storageIngredient.getUnit().equals(recipeIngredient.getUnit())) {
                  return storageIngredient.getUnit().convertValue(storageIngredient.getQuantity(), recipeIngredient.getUnit());
                }
                return storageIngredient.getQuantity();
              })
              .sum();
          return totalAvailable >= recipeIngredient.getQuantity();
        });
  }
  
  public Map<String, Double> getIngredientAvailabilityForRecipe(FoodStorage foodStorage, Recipe recipe) {
    Map<String, Double> availability = new HashMap<>();
    recipe.getIngredientsList().forEach(recipeIngredient -> {
      double totalAvailable = foodStorage.getIngredients().values().stream()
          .flatMap(List::stream)
          .filter(storageIngredient -> storageIngredient.getName().equals(recipeIngredient.getName()))
          .mapToDouble(Ingredient::getQuantity)
          .sum();
      availability.put(recipeIngredient.getName(), totalAvailable);
    });
    return availability;
  }
  
  /**
   * returns every Ingredients in Ingredients map, food storage.
   *
   * @return toString
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(); //Saves all
    stringBuilder.append("Ingredients in storage:\n");
    
    for (Map.Entry<String, ArrayList<Ingredient>> entry : ingredients.entrySet()) {
      //stringBuilder.append(entry.getKey()).append(":\n").append(entry.getValue().toString()).append("\n");
      
      entry.getValue().forEach(ingredient -> stringBuilder.append("  - ").append(ingredient).append("\n"));
    }
    return stringBuilder.toString();
  }
}

