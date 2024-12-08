package edu.ntnu.idi.idatt.console;

import edu.ntnu.idi.idatt.utils.ConsoleInputManager;
import edu.ntnu.idi.idatt.data.DummyData;
import edu.ntnu.idi.idatt.service.CookbookService;
import edu.ntnu.idi.idatt.service.FoodStorageService;
import edu.ntnu.idi.idatt.storage.Cookbook;
import edu.ntnu.idi.idatt.storage.FoodStorage;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * <h1>UserInterface.</h1>
 *
 * <p>A text-based UI for the Food Conservation App. It handles user interactions
 * and integrates services to manage the food storage and recipes.</p>
 *
 * @author trile
 */
public class UserInterface {
  
  private FoodStorageService foodStorageService;
  private CookbookService cookbookService;
  private Scanner scanner;
  
  /**
   * Initializes the user interface by setting up services and loading dummy data.
   */
  public void init() {
    scanner = new Scanner(System.in);
    ConsoleInputManager inputManager = new ConsoleInputManager(scanner);
    
    FoodStorage foodStorage = new FoodStorage();
    Cookbook cookbook = new Cookbook();
    
    DummyData dummyData = new DummyData();
    dummyData.loadDummyData(foodStorage, cookbook);
    
    foodStorageService = new FoodStorageService(foodStorage, inputManager);
    cookbookService = new CookbookService(cookbook, foodStorage, inputManager);
  }
  
  /**
   * <h2>Main Application Loop.</h2>
   *
   * <p>Displays the main menu and handles user input to perform various actions.</p>
   */
  @SuppressWarnings("checkstyle:AvoidEscapedUnicodeCharacters")
  public void start() {
    System.out.println("\u001B[33m====================================================\u001B[0m");
    System.out.println("\u001B[34m    \uD83C\uDF31✨Welcome to the"
        + " \u001B[32mFood Conservation App!✨\uD83C\uDF31        \u001B[0m");
    System.out.println("\u001B[33m====================================================\u001B[0m");
    
    displayAsciiBanner();
    boolean running = true;
    
    while (running) {
      displayMenu();
      int choice = getUserChoice();
      
      switch (choice) {
        case 1 -> foodStorageService.handleAddIngredient();
        case 2 -> foodStorageService.handleSearchIngredient();
        case 3 -> foodStorageService.handleRemoveIngredient();
        case 4 -> foodStorageService.handleDisplayExpiredIngredients();
        case 5 -> foodStorageService.handleTotalValue();
        case 6 -> foodStorageService.handleViewIngredientsBeforeDate();
        case 7 -> foodStorageService.handleDisplayFoodStorageAlphabetically();
        case 8 -> cookbookService.handleAddRecipeToCookbook();
        case 9 -> cookbookService.handleSearchRecipe();
        case 10 -> cookbookService.handleViewHasEnoughIngredientsForRecipe();
        case 11 -> cookbookService.handleSuggestedRecipe();
        case 12 -> cookbookService.handleDisplayCookbook();
        case 13 -> cookbookService.handleRemoveRecipe();
        case 14 -> displayHelp();
        case 15 -> {
          displayGoodbyeMessage();
          running = false;
        }
        default -> System.out.println("\u001B[31mInvalid choice. "
            + "Please try again or type '12' for guidance.\u001B[0m");
      }
    }
    scanner.close();
  }
  
  /**
   * Displays the main menu to the user.
   */
  private void displayMenu() {
    System.out.println("\n\u001B[36m-----------------------------------------"
        + "\n               Main Menu                 "
        + "\n-----------------------------------------\u001B[0m");
    Arrays.stream(MenuOption.values())
        .map(option -> "\u001B[34m" + option.getDescription() + "\u001B[0m")
        .forEach(System.out::println);
    System.out.println("Enter a number (\u001B[32m1-13\u001B[0m). "
        + "Type '\u001B[33m12\u001B[0m' for help or '\u001B[31m13\u001B[0m' to quit.");
  }
  
  /**
   * Prompts the user to enter their menu choice and validates the input.
   *
   * @return The user's menu choice as an integer.
   */
  private int getUserChoice() {
    while (true) {
      System.out.print("\u001B[36mEnter your choice: \u001B[0m");
      try {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
      } catch (InputMismatchException e) {
        System.out.println("\u001B[31mInvalid input. "
            + "Please enter a number between 1 and 13.\u001B[0m");
        scanner.nextLine();
      }
    }
  }
  
  /**
   * Displays the help menu with details about each option.
   */
  private void displayHelp() {
    System.out.println("\n\u001B[33mHELP MENU:\u001B[0m");
    System.out.println("1: Add Grocery - Add or update a grocery item in the food storage.");
    System.out.println("2: Search Grocery - Find a grocery item by name.");
    System.out.println("3: Remove Grocery - Remove or adjust the quantity of a grocery item.");
    System.out.println("4: View Expired Groceries - "
        + "Lists all expired items with their total cost.");
    System.out.println("5: Total Value - View the total value of groceries in storage.");
    System.out.println("6: View Groceries Before Date - "
        + "List items expiring before a specific date.");
    System.out.println("7: View All Groceries - Display all groceries alphabetically.");
    System.out.println("8: Add Recipe - Add a recipe to the cookbook.");
    System.out.println("9: Search Recipe - Find a recipe by its name.");
    System.out.println("10: Check Ingredients - "
        + "Verify if you have enough ingredients for a recipe.");
    System.out.println("11: Suggest Recipes - "
        + "Get recipe suggestions based on available ingredients.");
    System.out.println("12: View Cookbook - Display all recipes in the cookbook.");
    System.out.println("13. Remove Recipe - Remove a recipe from the cookbook by name");
    System.out.println("13: View this help menu.");
    System.out.println("14: Quit the application.");
  }
  
  /**
   * Displays ASCII art for the welcome banner.
   */
  private void displayAsciiBanner() {
    String blue = "\u001B[34m";
    System.out.print("\u001B[32m   🌳     🌲    🌳    \u001B[31m 🍎\u001B[0m");
    System.out.println("\u001B[33m       ☀️       \u001B[0m");
    System.out.print("\u001B[32m   🌳    \u001B[31m🍎\u001B[0m");
    System.out.print("\u001B[34m     🌍       🌏    \u001B[0m");
    System.out.print("\u001B[32m  🌲🌱  🌿   🌳🌿   \u001B[0m");
    System.out.println("\u001B[32m          \u001B[0m");
    System.out.print("\u001B[33m      🌾       🌾   \u001B[0m");
    System.out.print("\u001B[32m  🌳🌿   🌳    🌲🌿   🌳\u001B[0m");
    System.out.println("\u001B[32m    🌿🌿   \u001B[0m");
    System.out.println(blue + "    Please participate in saving the environment!   ");
  }
  
  /**
   * Displays the message when exiting the application.
   */
  private void displayGoodbyeMessage() {
    System.out.println("\u001B[35m-----------------------------------------\u001B[0m");
    System.out.println("\u001B[33mThank you for using the Food Conservation App!"
        + "\nTogether, we can reduce food waste and make a difference."
        + "\u001B[0m");
    System.out.println("\u001B[35m-----------------------------------------\u001B[0m");
  }
  
  /**
   * Enum representing the menu options with descriptions.
   */
  private enum MenuOption {
    ADD_GROCERY_TO_FOODSTORAGE("1. Add a grocery"),
    SEARCH_GROCERY("2. Search for a grocery"),
    REMOVE_GROCERY("3. Remove a grocery"),
    VIEW_EXPIRED_GROCERIES("4. View expired groceries and their cost"),
    TOTAL_VALUE("5. Get total value of all groceries"),
    VIEW_GROCERIES_BEFORE_DATE("6. View all groceries expiring before a date"),
    VIEW_ALL_GROCERIES("7. View all groceries"),
    ADD_RECIPE_TO_COOKBOOK("8. Add a recipe to the cookbook"),
    SEARCH_RECIPE("9. Search for a recipe"),
    CHECK_INGREDIENTS("10. Check if the fridge has enough ingredients for a recipe"),
    SUGGEST_RECIPES("11. View suggested recipes from the cookbook"),
    VIEW_COOKBOOK("12. View all recipes in the cookbook"),
    REMOVE_RECIPE_FROM_COOKBOOK("13. Remove a recipe from the cookbook"),
    HELP("14. Help"),
    EXIT("15. Exit");
    
    private final String description;
    
    MenuOption(String description) {
      this.description = description;
    }
    
    /**
     * Gets the description of the menu option.
     *
     * @return the description of the menu option
     */
    private String getDescription() {
      return description;
    }
  }
}
