package nz.ac.auckland.se206.gpt;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ChatHandler {
  private ChatCompletionRequest chatCompletionRequest;
  private Choice result;
  private Task<Void> appendTask;

  /**
   * Initialises the chat handler. This is the default chat handler for the
   * wizard. It is used for the wizard's responses to the user.
   * @throws ApiProxyException If there is an error communicating with the API proxy.
   */
  @FXML
  public void initialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2)
          .setTopP(0.5).setMaxTokens(100);
  }

  /**
   * Initialises the chat handler for the potion name. This is used for the
   * wizard's responses to the user when the user is naming a potion.
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
   * @param msg the chat message to process.
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
   * @param msg the chat message to append.
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
   * @param msg the chat message to append.
   */
  public void setResult(Choice result) {
    this.result = result;
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * @param msg the chat message to append.
   */
  public Choice getResult() {
    return result;
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * @param msg the chat message to append.
   */
  public Task<Void> getAppendTask() {
    return appendTask;
  }

  /**
   * Appends a chat message to the chat text area. This is the default chat
   * handler for the wizard. It is used for the wizard's responses to the user.
   * @param msg the chat message to append.
   */
  public void setAppendTask(Task<Void> appendTask) {
    this.appendTask = appendTask;
  }
}