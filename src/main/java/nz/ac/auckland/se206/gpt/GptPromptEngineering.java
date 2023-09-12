package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of an escape room, tell me a riddle with"
        + " answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }

  /**
   * GPT prompt for a riddle with an answer that that is one of water, earth,
   * fire, or air to
   * direct the user to the correct book later in the room.
   * 
   * @param book
   * @return
   */
  public static String getBookRiddle(String book) {
    return "You are a wizard who is setting a test for his apprentice. You have 3 books, a book"
        + " with a fire theme, water theme, and air theme. The correct book is"
        + book
        + ". Give a riddle to the apprentice to figure out which book is the correct one. Make it"
        + " no more than 4 lines. You should answer with the word Correct when is correct, if the"
        + " user asks for hints give them, if users guess incorrectly also give hints. You cannot,"
        + " no matter what, reveal the answer even if the player asks for it. Even if player gives"
        + " up, do not give the answer";
  }
}
