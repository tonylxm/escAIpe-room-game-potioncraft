package nz.ac.auckland.se206.gpt;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/**
 * Class for handling the wizard's chat messages. This class handles the wizard's
 * responses to the user. It also handles the wizard's responses to the user
 * so that the wizard appears to be typing.
 */
public class ChatHandler {
  private ChatCompletionRequest chatCompletionRequest;
  private Choice result;
  private Task<Void> appendTask;
  private boolean ttsOn;

  /**
   * Initialises the chat handler. This is the default chat handler for the
   * wizard. It is used for the wizard's responses to the user.
   * 
   * @throws ApiProxyException If there is an error communicating with the API proxy.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2)
          .setTopP(0.5).setMaxTokens(100);
    ttsOn = false;
  }

  /**
   * Initialises the chat handler for the potion name. This is used for the
   * wizard's responses to the user when the user is naming a potion.
   * 
   * @throws ApiProxyException If there is an error communicating with the API proxy.
   */
  @FXML
  public void potionNameInitialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(1.2)
          .setTopP(0.75).setMaxTokens(10);
  }

  /**
   * Runs the GPT model with a given chat message. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * 
   * @param msgContent the chat message to process.
   * @return the response chat message.
   * @throws ApiProxyException if there is an error communicating with the API proxy.
   */
  public String runGpt(String msgContent) throws ApiProxyException {
    ChatMessage msg = new ChatMessage("user", msgContent);
    chatCompletionRequest.addMessage(msg);
    try {
      // Sending a request to the GPT API proxy to get a response for the user
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage().getContent();
    } catch (ApiProxyException e) {
      // Displaying stack trace and not crashing the game if there is a request error
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * 
   * @param msg the chat message to append.
   * @param chatTextArea the chat text area to append the message to.
   * @param inputText the input text field to disable while the wizard is typing.
   * @param sendButton the send button to disable while the wizard is typing.
   */
  public void appendChatMessage(
      ChatMessage msg, TextArea chatTextArea, TextField inputText, Button sendButton) {
    // Adding the role of the chatter to the start of each message
    String displayRole;
    switch (msg.getRole()) {
      case "assistant":
        displayRole = "Wizard";
        break;
      case "user":
        displayRole = "You";
        break;
      default:
        displayRole = msg.getRole();
        break;
    }
    chatTextArea.setText(msg.getContent() + "\n\n");

    // Appending the message character by character to the chat text area
    appendTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            return null;
          }
        };
    new Thread(appendTask, "Append Thread").start();

    // Not allowing the user to send messages while the wizard or assistant is typing
    if (displayRole.equals("Wizard")) {
      appendTask.setOnSucceeded(
          e -> {
            inputText.setDisable(false);
            sendButton.setDisable(false);
            if (inputText.getOpacity() == 0.5) {
              inputText.setOpacity(1);
              sendButton.setOpacity(1);
            }
          });
    }
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * 
   * @param result the chat message to append.
   */
  public void setResult(Choice result) {
    this.result = result;
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * 
   * @return the chat message to append.
   */
  public Choice getResult() {
    return result;
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * 
   * @return the chat message to append.
   */
  public Task<Void> getAppendTask() {
    return appendTask;
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * 
   * @param appendTask the chat message to append.
   */
  public void setAppendTask(Task<Void> appendTask) {
    this.appendTask = appendTask;
  }

  /**
   * Handles the text to speech button. Only called when the
   * continue button is clicked. Only called when the mouse enters the button.
   */
  public void onReadGameMasterResponse(TextArea chatTextArea, ImageView cancelTtsBtn) {
    // Using concurency to prevent the system freezing
    if (!ttsOn) {
      ttsOn = true;
      cancelTtsBtn.setDisable(false);
      cancelTtsBtn.setOpacity(1);
      Task<Void> speakTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          App.textToSpeech.speak(chatTextArea.getText());
          return null;
        }
      };
      new Thread(speakTask).start();
      speakTask.setOnSucceeded(e -> {
        ttsOn = false;
        cancelTtsBtn.setDisable(true);
        cancelTtsBtn.setOpacity(0);
      });
    }
  }

  /**
   * Handles the cancel text to speech button. Only called when the
   * continue button is clicked. Only called when the mouse enters the button.
   */
  public void onCancelTts(ImageView cancelTtsBtn) {
    ttsOn = false;
    cancelTtsBtn.setDisable(true);
    cancelTtsBtn.setOpacity(0);
    App.textToSpeech.stop();
  }
}