package edu.ntnu.idi.idatt.console;

import edu.ntnu.idi.idatt.storage.Cookbook;
import edu.ntnu.idi.idatt.storage.FoodStorage;
import edu.ntnu.idi.idatt.model.Item;
import edu.ntnu.idi.idatt.model.Recipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * attributtes are the outer layer classes: CookingBook and FoodStorage.
 */
public class UserInterface {
  private FoodStorage foodStorage;
  private Cookbook cookBook;
  private Scanner scanner;
  
  
  /**
   * Initalizes cookingbook and foodStorage.
   */
  public void init() {
    foodStorage = new FoodStorage();
    cookBook = new Cookbook();
    scanner = new Scanner(System.in);
    preLoadData();
  }
  
  private void preLoadData() {
    foodStorage.addItemToFoodStorage(new Item("Honey", 2000, "Grams", LocalDate.of(2028, 2, 19), 30));
    foodStorage.addItemToFoodStorage(new Item("Apple", 2, "Pieces", LocalDate.of(2000, 12, 15), 20));
    foodStorage.addItemToFoodStorage(new Item("Apple", 4, "Pieces", LocalDate.of(2026, 6, 7), 18));
    foodStorage.addItemToFoodStorage(new Item("Honey", 2000, "Grams", LocalDate.of(2028, 2, 19), 30));
    foodStorage.addItemToFoodStorage(new Item("Milk", 100, "Milliliters", LocalDate.of(2024, 12, 15), 30));
    foodStorage.addItemToFoodStorage(new Item("Eggs", 10, "Pieces", LocalDate.of(2025, 12, 24), 3));
    foodStorage.addItemToFoodStorage(new Item("Margarine", 1000, "Grams", LocalDate.of(2025, 12, 24), 30));
    foodStorage.addItemToFoodStorage(new Item("Sugar", 3000, "Grams", LocalDate.of(2025, 12, 24), 40));
    // Expired items
    foodStorage.addItemToFoodStorage(new Item("Apple", 2, "Pieces", LocalDate.of(2000, 12, 15), 20));
    foodStorage.addItemToFoodStorage(new Item("Orange", 5, "Pieces", LocalDate.of(1900, 12, 15), 20));
    
    Recipe recipeForChicken = new Recipe("Chicken Breast", "decription decription decription", "procedure procedure procedure", 4);
    Recipe recipeFiletMignon = new Recipe("Filet mignon", "decription decription decription", "procedure procedure procedure", 4);
    recipeForChicken.addItemToRecipe(new Item("Honey", 200, "Grams", LocalDate.of(2028, 2, 19), 30));
    recipeFiletMignon.addItemToRecipe(new Item("Margarine", 1000, "Grams", LocalDate.of(2025, 12, 24), 30));
    recipeFiletMignon.addItemToRecipe(new Item("Margarine", 1000, "Grams", LocalDate.of(2025, 12, 24), 30));
    
    recipeFiletMignon.addItemToRecipe(new Item("Filet mignon", 200, "Grams", LocalDate.of(2024, 12, 19), 100));
  }
  
  /**
   * .
   */
  public void start() {
    System.out.println("Welcome to the food conservation app");
    boolean running = true;
    while (running) {
      displayMenu();
      int choiceInput = getUserChoice();
      MenuOption selectOption = MenuOption.values()[choiceInput - 1];
      if (selectOption == MenuOption.EXIT) {
        running = false;
      } else {
        handleUserChoice(choiceInput);
      }
    }
    System.out.println("You are welcome back anytime");
  }
  
  public enum MenuOption {
    CREATE_GROCERY("1. Create a grocery"),
    ADD_GROCERY("2. Add a grocery (update quantity)"),
    SEARCH_GROCERY("3. Search for a grocery"),
    REMOVE_GROCERY("4. Remove grocery quantity"),
    VIEW_EXPIRED_GROCERIES("5. View expired groceries and get their cost"),
    TOTAL_VALUE("6. Get total value of all groceries"),
    VIEW_GROCERIES_BEFORE_DATE("7. View all groceries expiring before a date"),
    VIEW_ALL_GROCERIES("8. View all groceries (alphabetically)"),
    CREATE_RECIPE("9. Create a recipe"),
    CHECK_INGREDIENTS("10. Check if the fridge has enough ingredients for a recipe"),
    ADD_RECIPE_TO_COOKBOOK("11. Add a recipe to the cookbook"),
    SUGGEST_RECIPES("12. View suggested recipes from the cookbook"),
    VIEW_COOKBOOK("13. View all recipes in the cookbook"),
    EXIT("14. Exit");
    
    private final String description;
    
    MenuOption(String description) {
      this.description = description;
    }
    
    public String getDescription() {
      return description;
    }
  }
  
  private void displayMenu() {
    System.out.println("\nMenu:");
    Arrays.stream(MenuOption.values())
        .map(MenuOption::getDescription)
        .forEach(System.out::println);
  }
  
  private int getUserChoice() {
    System.out.println("Enter your choice: ");
    while (!scanner.hasNext()) {
      System.out.println("Please enter a valid choice number");
      scanner.next();
    }
    return scanner.nextInt();
  }
  
  private void handleUserChoice(int choice) {
    MenuOption[] options = MenuOption.values();
    if (choice < 1 || choice > options.length) {
      System.out.println("Invalid choice. Please try again.");
      return;
    }
    
    MenuOption selectedOption = options[choice - 1];
    
    switch (selectedOption) {
      case CREATE_GROCERY -> createItem();
      case ADD_GROCERY -> addItemToFoodStorage();
      case SEARCH_GROCERY -> addItemToFoodStorage();
      case REMOVE_GROCERY -> removeItem();
      case VIEW_EXPIRED_GROCERIES -> displayExpiredItems();
      case TOTAL_VALUE -> totalValueOfFoodStorage();
      case VIEW_GROCERIES_BEFORE_DATE -> viewItemsExpirationDateBefore();
      case VIEW_ALL_GROCERIES -> displayFoodStorageAlphabetically();
      case CREATE_RECIPE -> createRecipe();
      case CHECK_INGREDIENTS -> hasEnoughItemsForRecipe();
      case ADD_RECIPE_TO_COOKBOOK -> addRecipeToCookbook();
      case SUGGEST_RECIPES -> suggestionRecipe();
      case VIEW_COOKBOOK -> displayCookbook();
      case EXIT -> System.out.println("Thank you for taking the effort to save food!");
    }
  }
  
  private void addItem() {
    System.out.print("Enter item name: ");
    String name = scanner.next();
    System.out.print("Enter quantity: ");
    double quantity = scanner.nextDouble();
    System.out.print("Enter unit (e.g., kg, pcs): ");
    String unit = scanner.next();
    System.out.print("Enter expiration date (yyyy-mm-dd): ");
    String dateInput = scanner.next();
    LocalDate expirationDate = LocalDate.parse(dateInput);
    System.out.print("Enter price per unit: ");
    double pricePerUnit = scanner.nextDouble();
    Item item = new Item(name, quantity, unit, expirationDate, pricePerUnit);
    
    foodStorage.addItemToFoodStorage(item);
    
  }
  
  
}
