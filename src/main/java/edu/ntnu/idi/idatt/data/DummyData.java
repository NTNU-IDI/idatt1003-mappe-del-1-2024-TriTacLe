package edu.ntnu.idi.idatt.data;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Recipe;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.storage.Cookbook;
import edu.ntnu.idi.idatt.storage.FoodStorage;
import java.time.LocalDate;

/**
 * Provides dummy data for initializing the application with predefined
 * {@link Ingredient} and{@link Recipe} instances. The class
 * includes a variety of ingredients and recipe for testing purposes.
 */
public class DummyData {
  
  /**
   * Loads dummy data into the provided {@link FoodStorage} and {@link Cookbook} instances.
   *
   * <p>This method adds a collection of ingredients to the food storage,
   * including expired ingredients too. It also adds some recipes to the cookbook,
   * including recipes that cannot be made with the ingredients added to the food storage</p>
   *
   * @param foodStorage the {@link FoodStorage} instance to add ingredients to
   * @param cookbook    the {@link Cookbook} instance to add recipes to
   */
  public static void loadDummyData(FoodStorage foodStorage, Cookbook cookbook) {
    //Ingredients to FoodStorage
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Honey", 2000, Unit.GRAM, LocalDate.of(2028, 2, 19), 30));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Apple", 5, Unit.PIECES, LocalDate.of(2026, 6, 7), 10));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Apple", 2, Unit.PIECES, LocalDate.of(2029, 6, 7), 15));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Milk", 1, Unit.LITRE, LocalDate.of(2024, 12, 15), 20));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Eggs", 12, Unit.PIECES, LocalDate.of(2025, 12, 24), 5));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Sugar", 1000, Unit.GRAM, LocalDate.of(2025, 12, 24), 15));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Flour", 2000, Unit.GRAM, LocalDate.of(2026, 1, 15), 12));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Butter", 500, Unit.GRAM, LocalDate.of(2025, 5, 10), 25));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Butter", 200, Unit.GRAM, LocalDate.of(2029, 5, 10), 20));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Chocolate", 200, Unit.GRAM, LocalDate.of(2026, 3, 15), 40));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Banana", 6, Unit.PIECES, LocalDate.of(2024, 11, 30), 5));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Vanilla", 50, Unit.MILLILITRE, LocalDate.of(2025, 10, 15), 10));
    //expired Ingredients
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Honey", 2000, Unit.GRAM, LocalDate.of(1934, 2, 19), 30));
    foodStorage.addIngredientToFoodStorage(
        new Ingredient("Vanilla", 50, Unit.MILLILITRE, LocalDate.of(1925, 10, 15), 10));
    //Recipes
    Recipe pancakeRecipe = new Recipe(
        "Pancakes",
        "A simple pancake recipe.",
        "1. Mix ingredients.\n2. Cook on a skillet.",
        4
    );
    pancakeRecipe.addIngredientToRecipe(new Ingredient("Flour", 250, Unit.GRAM, 12));
    pancakeRecipe.addIngredientToRecipe(new Ingredient("Milk", 0.5, Unit.LITRE, 10));
    pancakeRecipe.addIngredientToRecipe(new Ingredient("Eggs", 2, Unit.PIECES, 5));
    pancakeRecipe.addIngredientToRecipe(new Ingredient("Sugar", 50, Unit.GRAM, 15));
    
    Recipe honeyCakeRecipe = new Recipe(
        "Honey Cake",
        "A delicious honey cake recipe.",
        "1. Mix ingredients.\n2. Bake in the oven.",
        6
    );
    honeyCakeRecipe.addIngredientToRecipe(new Ingredient("Honey", 500, Unit.GRAM, 30));
    honeyCakeRecipe.addIngredientToRecipe(new Ingredient("Flour", 300, Unit.GRAM, 12));
    honeyCakeRecipe.addIngredientToRecipe(new Ingredient("Eggs", 4, Unit.PIECES, 5));
    honeyCakeRecipe.addIngredientToRecipe(new Ingredient("Sugar", 200, Unit.GRAM, 15));
    
    Recipe bananaBreadRecipe = new Recipe(
        "Banana Bread",
        "A moist and delicious banana bread recipe.",
        "1. Mash bananas.\n2. Mix all ingredients.\n3. Bake in the oven.",
        8
    );
    bananaBreadRecipe.addIngredientToRecipe(new Ingredient("Banana", 4, Unit.PIECES, 5));
    bananaBreadRecipe.addIngredientToRecipe(new Ingredient("Flour", 300, Unit.GRAM, 12));
    bananaBreadRecipe.addIngredientToRecipe(new Ingredient("Eggs", 2, Unit.PIECES, 5));
    bananaBreadRecipe.addIngredientToRecipe(new Ingredient("Sugar", 100, Unit.GRAM, 15));
    bananaBreadRecipe.addIngredientToRecipe(new Ingredient("Butter", 100, Unit.GRAM, 25));
    
    Recipe chocolateCakeRecipe = new Recipe(
        "Chocolate Cake",
        "A rich chocolate cake recipe.",
        "1. Melt chocolate.\n2. Mix ingredients.\n3. Bake in the oven.",
        10
    );
    chocolateCakeRecipe.addIngredientToRecipe(new Ingredient("Chocolate", 200, Unit.GRAM, 40));
    chocolateCakeRecipe.addIngredientToRecipe(new Ingredient("Flour", 250, Unit.GRAM, 12));
    chocolateCakeRecipe.addIngredientToRecipe(new Ingredient("Eggs", 3, Unit.PIECES, 5));
    chocolateCakeRecipe.addIngredientToRecipe(new Ingredient("Sugar", 200, Unit.GRAM, 15));
    chocolateCakeRecipe.addIngredientToRecipe(new Ingredient("Butter", 150, Unit.GRAM, 25));
    
    //recipes that cant be made
    Recipe chickenAndRice = new Recipe(
        "Chicken and Rice",
        "Manly diet for gym",
        "1. Fry rice. \n2. Season and steak chicken \n3. Mix it up with veggies",
        1
    );
    chickenAndRice.addIngredientToRecipe(new Ingredient("Chicken", 1, Unit.KILOGRAM, 90));
    chickenAndRice.addIngredientToRecipe(new Ingredient("Rice", 500, Unit.GRAM, 30));
    chickenAndRice.addIngredientToRecipe(new Ingredient("Oil", 100, Unit.MILLILITRE, 45));
    
    cookbook.addRecipeToCookbook(pancakeRecipe);
    cookbook.addRecipeToCookbook(honeyCakeRecipe);
    cookbook.addRecipeToCookbook(bananaBreadRecipe);
    cookbook.addRecipeToCookbook(chocolateCakeRecipe);
    cookbook.addRecipeToCookbook(chickenAndRice);
    
  }
}
