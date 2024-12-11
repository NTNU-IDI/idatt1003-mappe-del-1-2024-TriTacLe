package edu.ntnu.idi.idatt.console;

/**
 * The initializing of Food Conservation Application.
 *
 * <p>Run this class to start the Food Conservation Application.</p>
 *
 * @author TriLe
 */
public class App {
  
  /**
   * It initializes the {@link UserInterface}
   * which manages the user's interaction with the application.</p>
   *
   * @param args Command-line arguments passed to the application.
   */
  public static void main(String[] args) {
    UserInterface userInterface = new UserInterface();
    userInterface.init();
    userInterface.start();
  }
}
