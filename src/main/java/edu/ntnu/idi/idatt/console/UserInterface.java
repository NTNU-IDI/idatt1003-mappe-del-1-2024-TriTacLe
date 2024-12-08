package edu.ntnu.idi.idatt.console;

import edu.ntnu.idi.idatt.model.Ingredient;
import edu.ntnu.idi.idatt.model.Unit;
import edu.ntnu.idi.idatt.utils.ConsoleInputManager;
import edu.ntnu.idi.idatt.data.DummyData;
import edu.ntnu.idi.idatt.service.CookbookService;
import edu.ntnu.idi.idatt.service.FoodStorageService;
import edu.ntnu.idi.idatt.storage.Cookbook;
import edu.ntnu.idi.idatt.storage.FoodStorage;

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
  private Unit unit;
  
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
    
    unit = Unit.KR;
    unit.setSymbol("NOK");
    
    foodStorageService = new FoodStorageService(foodStorage, inputManager, unit); // Pass the selected currency
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
        case 16 -> chooseCurrency();
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
   * Displays a list of available currencies for selection and prompts the user to choose one.
   * Updates the symbol of the selected currency if the input is valid.
   *
   * @return the updated symbol of the selected currency.
   * @throws IllegalArgumentException if the entered currency is invalid or not found in the list.
   */
  public String chooseCurrency() {
    System.out.println("Currency to choose from:");
    for (Unit unit : Unit.values()) {
      if (unit.getType() == Unit.UnitType.CURRENCY) {
        System.out.println(" -" + unit.name()
            + " (" + unit.getSymbol() + ") - " + unit.getCountry());
      }
    }
    String choice;
    Unit selectedUnit;
    do {
      System.out.println("Please enter a currency by name  (e.g., USD and not $):");
      choice = scanner.nextLine().trim().toUpperCase();
      
      try {
        selectedUnit = Unit.valueOf(choice);
        if (selectedUnit.getType() == Unit.UnitType.CURRENCY) {
          break;
        } else {
          System.out.println("The selected unit is not a currency. Please try again.");
        }
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid currency entered. Valid options are:");
        for (Unit unit : Unit.values()) {
          if (unit.getType() == Unit.UnitType.CURRENCY) {
            System.out.println(" -" + unit.name() + " (" + unit.getSymbol() + ")");
          }
        }
      }
    } while (true);
    
    unit = selectedUnit;
    Ingredient.setUnitCurrency(selectedUnit);
    foodStorageService.setUnit(selectedUnit);
    System.out.println("Chosen unit: " + selectedUnit.getSymbol());
    return selectedUnit.getSymbol();
  }
  
  /**
   * Displays the main menu to the user.
   */
  private void displayMenu() {
    System.out.println("\n\u001B[33m=================== 🍎 FOOD CONSERVATION APP - MAIN MENU 🍎 ===================\u001B[0m");
    System.out.println("Select an option by entering the corresponding number:");
    
    System.out.println("\u001B[36m📦 Food Storage Management:\u001B[0m");
    System.out.println("  1  Add a grocery");
    System.out.println("  2  Search for a grocery");
    System.out.println("  3  Remove a grocery");
    System.out.println("  4  View expired groceries and their cost");
    System.out.println("  5  Get total value of all groceries");
    System.out.println("  6  View groceries expiring before a date");
    System.out.println("  7  View all groceries alphabetically");
    
    System.out.println("\u001B[36m📖 Recipe Management:\u001B[0m");
    System.out.println("  8  Add a recipe to the cookbook");
    System.out.println("  9  Search for a recipe");
    System.out.println(" 10  Check if the fridge has enough ingredients for a recipe");
    System.out.println(" 11  View suggested recipes from the cookbook");
    System.out.println(" 12  Display all recipes in the cookbook");
    System.out.println(" 13  Remove a recipe from the cookbook");
    
    System.out.println("\u001B[36m⚙️ General Options:\u001B[0m");
    System.out.println(" 14  Help");
    System.out.println(" 15  Exit");
    System.out.println(" 16  Choose currency");
    
    System.out.println("\u001B[36mYour current currency:\u001B[0m " + unit.getSymbol());
    System.out.println("\u001B[33m================================================================================\u001B[0m");
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
    System.out.println("\n\u001B[33m===================== 🍎 FOOD CONSERVATION APP - HELP MENU 🍎 =====================\u001B[0m");
    System.out.println("Here’s everything you need to know to navigate and use the app effectively!\n");
    
    System.out.println("\u001B[36m🍴 Food Storage Management:\u001B[0m");
    System.out.println("   \u001B[32m1: Add Grocery\u001B[0m         - Add a new item or update an existing one.");
    System.out.println("   \u001B[32m2: Search Grocery\u001B[0m      - Find a specific item in your storage.");
    System.out.println("   \u001B[32m3: Remove Grocery\u001B[0m      - Remove or decrease the quantity of an item.");
    System.out.println("   \u001B[32m4: View Expired Items\u001B[0m  - See expired items and their total value.");
    System.out.println("   \u001B[32m5: View Total Value\u001B[0m    - Get the combined value of all stored items.");
    System.out.println("   \u001B[32m6: View Items by Date\u001B[0m  - List items expiring before a specific date.");
    System.out.println("   \u001B[32m7: View All Items\u001B[0m       - Display all groceries sorted alphabetically.\n");
    
    System.out.println("\u001B[36m📖 Recipe Management:\u001B[0m");
    System.out.println("   \u001B[34m8: Add Recipe\u001B[0m          - Save a new recipe in your cookbook.");
    System.out.println("   \u001B[34m9: Search Recipe\u001B[0m       - Look up a recipe by its name.");
    System.out.println("  \u001B[34m10: Check Ingredients\u001B[0m   - Ensure you have the required items for a recipe.");
    System.out.println("  \u001B[34m11: Suggest Recipes\u001B[0m     - Get recipe ideas based on what’s in your storage.");
    System.out.println("  \u001B[34m12: View Cookbook\u001B[0m       - Browse all your saved recipes.");
    System.out.println("  \u001B[34m13: Remove Recipe\u001B[0m       - Delete a recipe you no longer need.\n");
    
    System.out.println("\u001B[36m⚙️ General Options:\u001B[0m");
    System.out.println("  \u001B[35m14: Help\u001B[0m                - Display this help menu anytime.");
    System.out.println("  \u001B[35m15: Exit\u001B[0m                - Quit the application.");
    System.out.println("  \u001B[35m16: Choose Currency\u001B[0m     - Set your preferred currency for displayed prices.\n");
    
    System.out.println("\u001B[33m=============================== TIPS & TRICKS ===============================\u001B[0m");
    System.out.println("\u001B[36m💡 Tips:\u001B[0m");
    System.out.println(" - 🌟 Use \u001B[32moption 14\u001B[0m anytime to come back to this menu.");
    System.out.println(" - 🍃 Plan meals around items nearing expiration to reduce waste.");
    System.out.println(" - 💰 Experiment with recipes based on the ingredients you already have.");
    System.out.println(" - 💱 Update your currency preference (option 16) for accurate price displays.");
    System.out.println("\n\u001B[36mReady to make the most of your food? Let’s get started! 🚀\u001B[0m\n");
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
}
