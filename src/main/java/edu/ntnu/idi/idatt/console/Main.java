package edu.ntnu.idi.idatt.console;

/**
 * The entry point of the Food Conservation Application.
 *
 * <p>Run this class to start the Food Conservation Application.</p>
 */
public class Main {
  
  /**
   * The main method serves as the application's entry point.
   *
   * <p>It initializes the {@link UserInterface},
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
