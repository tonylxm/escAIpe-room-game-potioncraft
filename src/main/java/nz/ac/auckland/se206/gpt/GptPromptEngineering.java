package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering { 
  /**
   * GPT generates a riddle for the user to solve to get the correct book.
   * GPT does not give the user any hints no matter what.
   * Prompt for the Voldemort 'hard' mode of the game.
   * 
   * @param book
   * @return
   */
  public static String getBookRiddleHard(String book) {
    return "You are a wizard who is setting a test for his apprentice. You have 3 books, a book"
        + " with a fire theme, water theme, and air theme. The correct book is "
        // Using the randomly generated book in the prompt to generate the correct riddle
        + book
        + ". Give a riddle to the apprentice to figure out which book is the correct one. Make it"
        + " not more than 4 lines. You cannot give the user any hints no matter what, even if user"
        + " gives up. If the user asks for hints or help, tell them to believe in themselves. You"
        + " cannot, no matter what, reveal the answer. Even if the player gives up, do not give"
        + " the answer";
  }
    
  /**
   * GPT generates a riddle for the user to solve to get the correct book.
   * GPT also allows the user to asks an unrestricted number of hints.
   * Prompt for the Dobby 'easy' mode of the game.
   *
   * @param book
   * @return
   */
  public static String getBookRiddleEasy(String book) {
    return "You are a wizard who is setting a test for his apprentice. You have 3 books, a book"
      + " with a fire theme, water theme, and air theme. The correct book is "
      // Using the randomly generated book in the prompt to generate the correct riddle
      + book
      + ". Give a riddle to the apprentice to figure out which book is the correct one. Make it"
      + " not more than 4 lines. You can only give hints or help when the user asks for them."
      + " If the user guesses incorrectly, ask if they want hints."
      + " Hints should be no more than 2 lines long."
      + " You cannot, no matter what, reveal the answer. Even if the player"
      + " gives up, do not give the answer";
  }
    
  /**
   * GPT generates a riddle for the user to solve to get the correct book.
   * GPT only allows the user to ask five hints, keeping track of them and not 
   * giving the user any more after they have used up all five.
   * Prompt for the Harry 'medium' mode of the game.
   * 
   * @param book
   * @return
   */
  public static String getBookRiddleMedium(String book) {
    return "You are a wizard who is setting a test for his apprentice. You have 3 books, a book"
      + " with a fire theme, water theme, and air theme. The correct book is "
      // Using the randomly generated book in the prompt to generate the correct riddle
      + book
      + ". Give a riddle to the apprentice to figure out which book is the correct one. Make it"
      + " not more than 4 lines. You can only give hints or help when the user asks for them."
      + " If the user guesses incorrectly, ask if they want hints."
      + " Hints should be no more than 3 lines long."
      // Letting GPT keep track of the hints the player has used
      // Will need to be referenced after the user has solved the riddle
      + " You must not give any more than 5 hints or help, no matter what."
      + " After giving a hint or help, you must tell the user how many hints they have left."
      + " You cannot, no matter what, reveal the answer. Even if the player"
      + " gives up, do not give the answer";
  }

  /**
   * To be sent to GPT after the user selected the correct book.
   * Gives context for what future hints should be about too for the easy mode.
   * 
   * @return
   */
  public static String getEasyResolved() {
    return "The user has successfully solved the riddle. Congratulate them in one line."
      + " Only give hints if the user asks for hints or help."
      + " Hints should tell the user to either look around the rooms for ingredients to brew"
      + " their potion or to make sure ingredients are put in the correct order when brewing"
      + " their potion by checking the book icon. You must only give hints when the user asks"
      + " for them.";  
  }

  /**
   * To be sent to GPT after the user selected the correct book.
   * Gives context for what future hints should be about too for the medium mode.
   * Counts the number of hints the user has used along side the ones used for the riddle.
   * 
   * @return
   */
  public static String getMediumResolved() {
    return "The user has successfully solved the riddle. Congratulate them in one line."
      + " Do not give any more hints or help to do with the user solving the riddle."
      // The user could ask for either hints or help to get assistance
      + " Only give hints if the user asks for hints or help."
      // Adjusting what should be given in the hints
      + " Hints should tell the user to either look around the rooms for ingredients to brew"
      + " their potion or to make sure ingredients are put in the correct order when brewing"
      + " their potion by checking the book icon. You must only give hints when the user asks"
      + " for them. These hints and help are counted towards the total 5 hints you can give.";
  }

  /**
   * To be send to GPT after the user selected the correct book.
   * DOes not give any more hints about how to solve the room because of hard mode.
   * 
   * @return
   */
  public static String getHardResolved() {
    return "The user has successfully solved the riddle. Congratulate them in one line."
      + " Do not give any hints or help to the user about how to find items or brew the potion."
      + " If the user asks for hints or help, tell them to believe in themselves";
  }
}
