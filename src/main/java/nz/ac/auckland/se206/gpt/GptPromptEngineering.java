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
        + book
        + ". Give a riddle to the apprentice to figure out which book is the correct one. Make it"
        + " not more than 4 lines. You cannot give the user any hints no matter what, even if user gives up."
        + " You cannot, no matter what, reveal the answer. Even if the player"
        + " gives up, do not give the answer";
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
      + book
      + ". Give a riddle to the apprentice to figure out which book is the correct one. Make it"
      + " not more than 4 lines. You can only give hints when the user asks for them."
      + " If the user guesses incorrectly, ask if they want hints."
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
      + book
      + ". Give a riddle to the apprentice to figure out which book is the correct one. Make it"
      + " not more than 4 lines. You can only give hints when the user asks for them."
      + " If the user guesses incorrectly, ask if they want hints."
      + " You must not give any more than 5 hints, no matter what."
      + " After giving a hint, you must tell the user how many hints they have left."
      + " You cannot, no matter what, reveal the answer. Even if the player"
      + " gives up, do not give the answer";
  }
}
